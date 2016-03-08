/*******************************************************************************
 * Copyright (c) 2015
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.tests.ai;

import jsettlers.TestUtils;
import jsettlers.ai.highlevel.AiStatistics;
import jsettlers.ai.neurons.BuildingIndividual;
import jsettlers.common.CommonConstants;
import jsettlers.common.ai.EPlayerType;
import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.logging.StatisticsStopWatch;
import jsettlers.common.player.ECivilisation;
import jsettlers.graphics.startscreen.interfaces.IStartedGame;
import jsettlers.input.PlayerState;
import jsettlers.logic.constants.MatchConstants;
import jsettlers.logic.map.MapLoader;
import jsettlers.logic.map.save.MapList;
import jsettlers.logic.player.PlayerSetting;
import jsettlers.main.JSettlersGame;
import jsettlers.main.replay.ReplayUtils;
import jsettlers.network.client.OfflineNetworkConnector;
import jsettlers.tests.utils.MapUtils;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author codingberlin
 */
public class BrainTrainer {
	public static final int MINUTES = 1000 * 60;
	public static final int JUMP_FORWARD = 10 * MINUTES;

	static {
		CommonConstants.ENABLE_CONSOLE_LOGGING = true;
		TestUtils.setupResourcesManager();
	}

	@Test
	public void trainBrain() {
		BuildingIndividual individual = new BuildingIndividual();
		individual.generateRandomWeights();
		PlayerSetting[] playerSettings = new PlayerSetting[4];
		playerSettings[0] = new PlayerSetting(true, EPlayerType.AI_VERY_HARD, ECivilisation.ROMAN, (byte) 0, individual);
		playerSettings[1] = new PlayerSetting(false, (byte) -1);
		playerSettings[2] = new PlayerSetting(false, (byte) -1);
		playerSettings[3] = new PlayerSetting(false, (byte) -1);
		JSettlersGame.GameRunner startingGame = createStartingGame(playerSettings);
		IStartedGame startedGame = ReplayUtils.waitForGameStartup(startingGame);

		int score = 0;
		AiStatistics aiStatistics = new AiStatistics(startingGame.getMainGrid());
		MatchConstants.clock().fastForwardTo(10 * MINUTES);
		score += calculateScore(aiStatistics);
		MatchConstants.clock().fastForwardTo(20 * MINUTES);
		score += calculateScore(aiStatistics);
		System.out.println("Score " + score + " --> " + individual);

		ReplayUtils.awaitShutdown(startedGame);
	}

	private int calculateScore(AiStatistics aiStatistics) {
		aiStatistics.updateStatistics();
		return aiStatistics.getNumberOfTotalBuildingsForPlayer((byte) 0) - aiStatistics.getNumberOfNotFinishedBuildingsForPlayer((byte) 0);
	}

	private JSettlersGame.GameRunner createStartingGame(PlayerSetting[] playerSettings) {
		MapLoader mapCreator = MapList.getDefaultList().getMapById("066d3c28-8f37-41cf-96c1-270109f00b9f");
		JSettlersGame game = new JSettlersGame(mapCreator, 2L, new OfflineNetworkConnector(), (byte) 0, playerSettings);
		return (JSettlersGame.GameRunner) game.start();
	}

}
