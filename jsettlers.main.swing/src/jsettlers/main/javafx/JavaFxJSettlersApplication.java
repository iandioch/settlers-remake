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
package jsettlers.main.javafx;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PropertyResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jsettlers.common.utils.MainUtils;
import jsettlers.common.utils.OptionableProperties;
import jsettlers.graphics.localization.Labels;
import jsettlers.main.SceneAndController;
import jsettlers.main.swing.SwingManagedJSettlers;

/**
 * @author codingberlin
 */
public class JavaFxJSettlersApplication extends Application {

	private SceneAndController mainSceneAndController;
	private SceneAndController settingsSceneAndController;
	private Stage stage;

	private SceneAndController createScene(String fxmlUrl) throws IOException {
		FXMLLoader fxmlLoader = createFxmlLoader();
		Scene scene = new Scene(fxmlLoader.load(getClass().getResource(fxmlUrl).openStream()));
		scene.getStylesheets().add("/jsettlers/main/javafx/base.css");

		SettlersApplicationController controller = fxmlLoader.<SettlersApplicationController>getController();
		controller.setSettlersApplication(this);

		return new SceneAndController(scene, controller);
	}

	public FXMLLoader createFxmlLoader() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		InputStreamReader reader = null; // using reader ensures the usage of correct encoding
		reader = new InputStreamReader(Labels.getMostDominantLocaleStream());
		System.out.println("Encoding of locale file: " + reader.getEncoding());
		fxmlLoader.setResources(new PropertyResourceBundle(reader));
		return fxmlLoader;
	}

	public void showMainScene() {
		resetControllerAndShowScene(mainSceneAndController);
	}

	public void showSettingsScene() {
		resetControllerAndShowScene(settingsSceneAndController);
	}

	private void resetControllerAndShowScene(SceneAndController sceneAndController) {
		sceneAndController.getController().resetUiState();
		stage.setScene(sceneAndController.getScene());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		mainSceneAndController = createScene("/jsettlers/main/javafx/main/mainMenu.fxml");
		settingsSceneAndController = createScene("/jsettlers/main/javafx/settings/SettingsMenu.fxml");
		showMainScene();

		stage.show();
	}

	public static void main(String args[]) throws IOException {
		OptionableProperties optionableProperties = MainUtils.loadOptions(args);

		SwingManagedJSettlers.setupResourceManagers(optionableProperties, "config.prp");

		launch(args);
	}
}
