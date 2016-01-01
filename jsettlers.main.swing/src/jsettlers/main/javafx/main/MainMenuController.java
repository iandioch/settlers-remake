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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jsettlers.main.javafx.UiUtils;
import jsettlers.main.javafx.JavaFxJSettlersApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class MainMenuController implements Initializable {

	private Scene settingsScene;

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

	public MainMenuController() throws IOException {
		FXMLLoader fxmlLoader = UiUtils.createFxmlLoader();
		Parent root = fxmlLoader.load(getClass().getResource("/jsettlers/main/javafx/settings/settingsMenu.fxml").openStream());
		settingsScene = new Scene(root);
		settingsScene.getStylesheets().add("/jsettlers/main/javafx/base.css");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectFromListPane.setVisible(false);

		// overall background
		UiUtils.setGuiBackground(startMenuPane, 2, 29);

		// buttons not pressed
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

		settingsButton.setOnAction(event -> {
			((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(settingsScene);
		});
		//TODO buttons pressed - 3, 329
	}
}