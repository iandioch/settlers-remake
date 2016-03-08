/*******************************************************************************
 * Copyright (c) 2015
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.ai.highlevel;

import jsettlers.ai.army.ArmyGeneral;
import jsettlers.ai.construction.BestConstructionPositionFinderFactory;
import jsettlers.ai.construction.BuildingCount;
import jsettlers.ai.economy.EconomyMinister;
import jsettlers.ai.neurons.BuildingIndividual;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.material.EMaterialType;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.movable.IMovable;
import jsettlers.common.position.ShortPoint2D;
import jsettlers.input.tasks.*;
import jsettlers.logic.buildings.military.OccupyingBuilding;
import jsettlers.logic.map.grid.MainGrid;
import jsettlers.network.client.interfaces.ITaskScheduler;

import java.util.*;

import static jsettlers.common.buildings.EBuildingType.*;
import static jsettlers.common.material.EMaterialType.*;
import static jsettlers.logic.constants.Constants.TOWER_SEARCH_RADIUS;

/**
 * @author codingberling
 */
public class BrainWhatToDoAi implements IWhatToDoAi {

	public static final int NUMBER_OF_SMALL_LIVINGHOUSE_BEDS = 10;
	public static final int NUMBER_OF_MEDIUM_LIVINGHOUSE_BEDS = 30;
	public static final int NUMBER_OF_BIG_LIVINGHOUSE_BEDS = 100;
	public static final int MINIMUM_NUMBER_OF_BEARERS = 10;
	public static final int NUMBER_OF_BEARERSS_PER_HOUSE = 5;
	public static final int MAXIMUM_STONECUTTER_WORK_RADIUS_FACTOR = 2;
	private final MainGrid mainGrid;
	private final byte playerId;
	private final ITaskScheduler taskScheduler;
	private final AiStatistics aiStatistics;
	private final ArmyGeneral armyGeneral;
	private final BestConstructionPositionFinderFactory bestConstructionPositionFinderFactory;
	private final EconomyMinister economyMinister;
	private BuildingIndividual buildingIndividual;

	public BrainWhatToDoAi(byte playerId, AiStatistics aiStatistics, EconomyMinister economyMinister, ArmyGeneral armyGeneral, MainGrid mainGrid,
			ITaskScheduler taskScheduler, BuildingIndividual buildingIndividual) {
		this.playerId = playerId;
		this.mainGrid = mainGrid;
		this.taskScheduler = taskScheduler;
		this.aiStatistics = aiStatistics;
		this.armyGeneral = armyGeneral;
		this.economyMinister = economyMinister;
		this.buildingIndividual = buildingIndividual;
		bestConstructionPositionFinderFactory = new BestConstructionPositionFinderFactory();
		System.out.println(this);
	}

	@Override
	public void applyRules() {
		if (aiStatistics.isAlive(playerId)) {
			destroyBuildings();
			buildBuildings();
			armyGeneral.levyUnits();
			armyGeneral.commandTroops();
			occupyMilitaryBuildings();
		}
	}

	private void occupyMilitaryBuildings() {
		for (ShortPoint2D militaryBuildingPosition : aiStatistics.getBuildingPositionsOfTypesForPlayer(
				EBuildingType.getMilitaryBuildings(), playerId)) {

			OccupyingBuilding militaryBuilding = (OccupyingBuilding) aiStatistics.getBuildingAt(militaryBuildingPosition);
			if (militaryBuilding.getStateProgress() == 1 && !militaryBuilding.isOccupied()) {
				ShortPoint2D door = militaryBuilding.getDoor();
				IMovable soldier = aiStatistics.getNearestSwordsmanOf(door, playerId);
				if (soldier != null && militaryBuilding.getPos().getOnGridDistTo(soldier.getPos()) > TOWER_SEARCH_RADIUS) {
					sendMovableTo(soldier, door);
				}
			}
		}
	}

	private void sendMovableTo(IMovable movable, ShortPoint2D target) {
		if (movable != null) {
			taskScheduler.scheduleTask(new MoveToGuiTask(playerId, target, Collections.singletonList(movable.getID())));
		}
	}

	private void destroyBuildings() {
		// destroy stonecutters or set their work areas
		for (ShortPoint2D stoneCutterPosition : aiStatistics.getBuildingPositionsOfTypeForPlayer(STONECUTTER, playerId)) {
			if (aiStatistics.getBuildingAt(stoneCutterPosition).cannotWork()) {
				int numberOfStoneCutters = aiStatistics.getNumberOfBuildingTypeForPlayer(STONECUTTER, playerId);
				if (numberOfStoneCutters == 1) {
					ShortPoint2D nearestStone = aiStatistics.getStonesForPlayer(playerId).getNearestPoint(stoneCutterPosition);
					if (nearestStone != null) {
						taskScheduler.scheduleTask(new WorkAreaGuiTask(EGuiAction.SET_WORK_AREA, playerId, nearestStone, stoneCutterPosition));
					} // else wait and check again (next interval maybe there is a new or occupied tower)
				} else {
					ShortPoint2D nearestStone = aiStatistics.getStonesForPlayer(playerId)
							.getNearestPoint(stoneCutterPosition, STONECUTTER.getWorkradius() * MAXIMUM_STONECUTTER_WORK_RADIUS_FACTOR, null);
					if (nearestStone != null && numberOfStoneCutters < economyMinister.getMidGameNumberOfStoneCutters()) {
						taskScheduler.scheduleTask(new WorkAreaGuiTask(EGuiAction.SET_WORK_AREA, playerId, nearestStone, stoneCutterPosition));
					} else {
						taskScheduler.scheduleTask(new DestroyBuildingGuiTask(playerId, stoneCutterPosition));
						break; // destroy only one stone cutter
					}
				}
			}
		}

		// destroy livinghouses
		int numberOfFreeBeds = aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.SMALL_LIVINGHOUSE, playerId)
				* NUMBER_OF_SMALL_LIVINGHOUSE_BEDS
				+ aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.MEDIUM_LIVINGHOUSE, playerId) * NUMBER_OF_MEDIUM_LIVINGHOUSE_BEDS
				+ aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.BIG_LIVINGHOUSE, playerId) * NUMBER_OF_BIG_LIVINGHOUSE_BEDS
				- aiStatistics.getMovablePositionsByTypeForPlayer(EMovableType.BEARER, playerId).size();
		if (numberOfFreeBeds >= NUMBER_OF_SMALL_LIVINGHOUSE_BEDS + 1
				&& aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.SMALL_LIVINGHOUSE, playerId) > 0) {
			taskScheduler.scheduleTask(new DestroyBuildingGuiTask(playerId,
					aiStatistics.getBuildingPositionsOfTypeForPlayer(EBuildingType.SMALL_LIVINGHOUSE, playerId).get(0)));
		} else if (numberOfFreeBeds >= NUMBER_OF_MEDIUM_LIVINGHOUSE_BEDS + 1
				&& aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.MEDIUM_LIVINGHOUSE, playerId) > 0) {
			taskScheduler.scheduleTask(new DestroyBuildingGuiTask(playerId,
					aiStatistics.getBuildingPositionsOfTypeForPlayer(EBuildingType.MEDIUM_LIVINGHOUSE, playerId).get(0)));
		} else if (numberOfFreeBeds >= NUMBER_OF_BIG_LIVINGHOUSE_BEDS + 1
				&& aiStatistics.getNumberOfBuildingTypeForPlayer(EBuildingType.BIG_LIVINGHOUSE, playerId) > 0) {
			taskScheduler.scheduleTask(new DestroyBuildingGuiTask(playerId,
					aiStatistics.getBuildingPositionsOfTypeForPlayer(EBuildingType.BIG_LIVINGHOUSE, playerId).get(0)));
		}

		// destroy mines
		for (ShortPoint2D mine : aiStatistics.getDeadMinesOf(playerId)) {
			taskScheduler.scheduleTask(new DestroyBuildingGuiTask(playerId, mine));
		}
	}

	private void destroyHinterlandMilitaryBuildings() {
		for (ShortPoint2D militaryBuildingPositions : aiStatistics.getHinterlandMilitaryBuildingPositionsOfPlayer(playerId)) {
			taskScheduler.scheduleTask(new DestroyBuildingGuiTask(playerId, militaryBuildingPositions));
		}
	}

	private void buildBuildings() {
		if (aiStatistics.getNumberOfNotFinishedBuildingsForPlayer(playerId) < economyMinister.getNumberOfParallelConstructionSides()) {
			if (buildLivingHouse())
				return;
			if (buildTower())
				return;
			if (buildStock())
				return;
			buildEconomy();
		}
	}

	private boolean buildStock() {
		if (aiStatistics.getTotalNumberOfBuildingTypeForPlayer(GOLDMELT, playerId) < 1) {
			return false;
		}
		int stockCount = aiStatistics.getTotalNumberOfBuildingTypeForPlayer(STOCK, playerId);
		int goldCount = aiStatistics.getNumberOfMaterialTypeForPlayer(GOLD, playerId);
		if (stockCount * 6 * 8 - 32 < goldCount) {
			return construct(STOCK);
		}
		return false;
	}

	private void buildEconomy() {
		EBuildingType nextBuilding = buildingIndividual.determineNextBuildingToBuild(aiStatistics, playerId);
		if (nextBuilding != null) {
			construct(nextBuilding);
		}
	}

	private boolean buildTower() {
		if (aiStatistics.getTotalNumberOfBuildingTypeForPlayer(STONECUTTER, playerId) >= 1
				&& aiStatistics.getNumberOfNotOccupiedMilitaryBuildings(playerId) == 0) {
			destroyHinterlandMilitaryBuildings();
			return construct(TOWER);
		}
		return false;
	}

	private boolean buildLivingHouse() {
		if (aiStatistics.getNumberOfNotFinishedBuildingTypesForPlayer(SMALL_LIVINGHOUSE, playerId) > 0
				|| aiStatistics.getNumberOfNotFinishedBuildingTypesForPlayer(MEDIUM_LIVINGHOUSE, playerId) > 0) {
			return false;
		}

		int futureNumberOfBearers = aiStatistics.getMovablePositionsByTypeForPlayer(EMovableType.BEARER, playerId).size()
				+ aiStatistics.getNumberOfNotFinishedBuildingTypesForPlayer(BIG_LIVINGHOUSE, playerId) * NUMBER_OF_BIG_LIVINGHOUSE_BEDS;
		if (futureNumberOfBearers < MINIMUM_NUMBER_OF_BEARERS
				|| aiStatistics.getNumberOfTotalBuildingsForPlayer(playerId) * NUMBER_OF_BEARERSS_PER_HOUSE > futureNumberOfBearers) {
			if (aiStatistics.getTotalNumberOfBuildingTypeForPlayer(STONECUTTER, playerId) < 1
					|| aiStatistics.getTotalNumberOfBuildingTypeForPlayer(LUMBERJACK, playerId) < 3) {
				return construct(SMALL_LIVINGHOUSE);
			} else if (aiStatistics.getTotalNumberOfBuildingTypeForPlayer(WEAPONSMITH, playerId) < 2) {
				return construct(MEDIUM_LIVINGHOUSE);
			} else {
				return construct(BIG_LIVINGHOUSE);
			}
		}
		return false;
	}

	private boolean construct(EBuildingType type) {
		ShortPoint2D position = bestConstructionPositionFinderFactory
				.getBestConstructionPositionFinderFor(type)
				.findBestConstructionPosition(aiStatistics, mainGrid.getConstructionMarksGrid(), playerId);
		if (position != null) {
			taskScheduler.scheduleTask(new ConstructBuildingTask(EGuiAction.BUILD, playerId, position, type));
			if (type.isMilitaryBuilding()) {
				IMovable soldier = aiStatistics.getNearestSwordsmanOf(position, playerId);
				if (soldier != null) {
					sendMovableTo(soldier, position);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Player " + playerId + " with " + economyMinister.toString() + " and " + armyGeneral.toString();
	}

}
