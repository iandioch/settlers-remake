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
package jsettlers.ai.construction;

import java.util.ArrayList;
import java.util.List;

import jsettlers.ai.highlevel.AiStatistics;
import jsettlers.algorithms.construction.AbstractConstructionMarkableMap;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.position.ShortPoint2D;

/**
 * Assumptions: stones are placed as groups at the map, never alone without other stones
 * 
 * Algorithm: find all possible construction points within the borders of the player - calculates a score based on the distance from the most near
 * stone of the possible construction position - takes the position with the best score (lowest distance to the most near stone)
 * 
 * @author codingberlin
 */
public class BestStoneCutterConstructionPositionFinder implements IBestConstructionPositionFinder {

	EBuildingType buildingType;

	public BestStoneCutterConstructionPositionFinder(EBuildingType buildingType) {
		this.buildingType = buildingType;
	}

	@Override
	public ShortPoint2D findBestConstructionPosition(AiStatistics aiStatistics, AbstractConstructionMarkableMap constructionMap, byte playerId) {
		List<ShortPoint2D> stones = aiStatistics.getStonesForPlayer(playerId);

		List<ScoredConstructionPosition> scoredConstructionPositions = new ArrayList<ScoredConstructionPosition>();
		for (ShortPoint2D point : aiStatistics.getLandForPlayer(playerId)) {
			if (constructionMap.canConstructAt(point.x, point.y, buildingType, playerId)) {
				double stoneDistance = Double.MAX_VALUE;
				for (ShortPoint2D stone : stones) {
					double currentStoneDistance = Math.sqrt((stone.x - point.x) * (stone.x - point.x) + (stone.y - point.y) * (stone.y - point.y));
					if (currentStoneDistance < stoneDistance) {
						stoneDistance = currentStoneDistance;
					}
				}
				scoredConstructionPositions.add(new ScoredConstructionPosition(new ShortPoint2D(point.x, point.y), stoneDistance));
			}
		}

		ScoredConstructionPosition winnerPosition = null;
		for (ScoredConstructionPosition scoredConstructionPosition : scoredConstructionPositions) {
			if (winnerPosition == null) {
				winnerPosition = scoredConstructionPosition;
			} else if (winnerPosition.score > scoredConstructionPosition.score) {
				winnerPosition = scoredConstructionPosition;
			}
		}

		if (winnerPosition == null) {
			return null;
		}

		return winnerPosition.point;
	}

}