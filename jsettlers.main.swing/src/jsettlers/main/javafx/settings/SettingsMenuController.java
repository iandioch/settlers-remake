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
package jsettlers.main.javafx.settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import jsettlers.graphics.startscreen.SettingsManager;
import jsettlers.main.javafx.SettlersApplicationController;
import jsettlers.main.javafx.UiUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class SettingsMenuController extends SettlersApplicationController implements Initializable {

	@FXML private Slider volumeSlider;
	@FXML private TextField nameField;
	@FXML private Button backButton;
	@FXML private Button okButton;
	@FXML private BorderPane settingsMenuPane;

	@Override public void initialize(URL location, ResourceBundle resources) {
		setOriginalSettlersBackgroundImages();

		resetUiState();

		backButton.setOnAction(event -> {
			settlersApplication.showMainScene();
		});

		okButton.setOnAction(event -> {
			SettingsManager sm = SettingsManager.getInstance();
			sm.set(SettingsManager.SETTING_USERNAME, nameField.getText());
			sm.set(SettingsManager.SETTING_VOLUME, (volumeSlider.getValue() / 100D) + "");

			settlersApplication.showMainScene();
		});
	}

	private void setOriginalSettlersBackgroundImages() {
		UiUtils.setGuiBackground(settingsMenuPane, 2, 29);
		UiUtils.registerMousePressedBackgroundChange(okButton);
		UiUtils.registerMousePressedBackgroundChange(backButton);
	}

	@Override public void resetUiState() {
		UiUtils.setInitialButtonBackground(okButton);
		UiUtils.setInitialButtonBackground(backButton);

		SettingsManager sm = SettingsManager.getInstance();
		nameField.setText(sm.getPlayer().getName());
		volumeSlider.setValue(sm.getVolume() * 100);
	}
}
