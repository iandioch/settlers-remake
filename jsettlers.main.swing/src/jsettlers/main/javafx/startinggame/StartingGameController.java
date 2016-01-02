/*******************************************************************************
 * Copyright (c) 2015
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.main.javafx.startinggame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import jsettlers.graphics.localization.Labels;
import jsettlers.graphics.map.IMapInterfaceConnector;
import jsettlers.graphics.progress.EProgressState;
import jsettlers.graphics.startscreen.interfaces.*;
import jsettlers.main.javafx.SettlersApplicationController;
import jsettlers.main.javafx.UiUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class StartingGameController extends SettlersApplicationController implements Initializable, IStartingGameListener {

	@FXML private Label messageLabel;
	@FXML private StackPane stackPane;

	@Override public void initialize(URL location, ResourceBundle resources) {
		UiUtils.setGuiBackground(stackPane, 2, 29);

	}

	@Override public void resetUiState() {
		messageLabel.setText(Labels.getString("progress_LOADING"));
	}

	public void setStartingGame(IStartingGame startingGame) {
		startingGame.setListener(this);
	}

	@Override public void startProgressChanged(EProgressState state, float progress) {
		Platform.runLater(() -> messageLabel.setText(Labels.getProgress(state)));
	}

	@Override public IMapInterfaceConnector preLoadFinished(IStartedGame game) {
		game.setGameExitListener(game1 -> settlersApplication.showMainScene());
		return null;
	}

	@Override public void startFailed(EGameError errorType, Exception exception) {

	}

	@Override public void startFinished() {

	}
}
