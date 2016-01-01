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
package jsettlers.main.javafx.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import jsettlers.main.javafx.SettlersApplicationController;
import jsettlers.main.javafx.UiUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class MainMenuController extends SettlersApplicationController implements Initializable {

	@FXML private BorderPane startMenuPane;
	@FXML private Button settingsButton;
	@FXML private Button newGameGoButton;
	@FXML private Button joinMultiPlayerGoButton;
	@FXML private Button newMultiPlayerGoButton;
	@FXML private Button loadGameGoButton;
	@FXML private ToggleButton newGameButton;
	@FXML private ToggleButton joinMultiPlayerButton;
	@FXML private ToggleButton newMultiPlayerButton;
	@FXML private ToggleButton loadGameButton;
	@FXML private ToggleGroup startMenuToggleButtons;
	@FXML private BorderPane selectFromListPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectFromListPane.setVisible(false);

		settingsButton.setOnAction(event -> {
			settlersApplication.showSettingsScene();
		});

		setOriginalSettlersBackgroundImages();
	}

	private void setOriginalSettlersBackgroundImages() {
		UiUtils.setGuiBackground(startMenuPane, 2, 29);
		UiUtils.setButtonBackgrounds(settingsButton);
		UiUtils.setButtonBackgrounds(newGameGoButton);
		UiUtils.setButtonBackgrounds(joinMultiPlayerGoButton);
		UiUtils.setButtonBackgrounds(newMultiPlayerGoButton);
		UiUtils.setButtonBackgrounds(loadGameGoButton);
		UiUtils.setButtonBackgrounds(newGameButton);
		UiUtils.setButtonBackgrounds(joinMultiPlayerButton);
		UiUtils.setButtonBackgrounds(newMultiPlayerButton);
		UiUtils.setButtonBackgrounds(loadGameButton);

		startMenuToggleButtons.selectedToggleProperty().addListener((observableValue, buttonToDisable, buttonToEnable) -> {
			if (buttonToDisable != null) {
				UiUtils.setGuiBackground((ToggleButton) buttonToDisable, 3, 326);
			}
		});
	}
}