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
import java.util.Timer;
import java.util.TimerTask;

import go.graphics.area.Area;
import go.graphics.region.Region;
import go.graphics.region.RegionContent;
import go.graphics.sound.SoundPlayer;
import go.graphics.swing.AreaContainer;
import go.graphics.swing.sound.SwingSoundPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jsettlers.common.utils.MainUtils;
import jsettlers.common.utils.OptionableProperties;
import jsettlers.graphics.localization.Labels;
import jsettlers.graphics.startscreen.IContentSetable;
import jsettlers.graphics.startscreen.interfaces.IStartingGame;
import jsettlers.graphics.ui.UIPanel;
import jsettlers.logic.map.save.MapList;
import jsettlers.main.SceneAndController;
import jsettlers.main.javafx.ingame.InGameController;
import jsettlers.main.javafx.startinggame.StartingGameController;
import jsettlers.main.swing.SwingManagedJSettlers;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author codingberlin
 */
public class JavaFxJSettlersApplication extends Application implements IContentSetable {

	private SceneAndController mainSceneAndController;
	private SceneAndController settingsSceneAndController;
	private SceneAndController startingGameSceneAndController;
	private SceneAndController inGameSceneAndController;
	private Stage stage;
	private SoundPlayer soundPlayer = new SwingSoundPlayer();

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

	public void close() {
		stage.close();
		Platform.exit();
	}

	public void showMainScene() {
		resetControllerAndShowScene(mainSceneAndController);
	}

	public void showSettingsScene() {
		resetControllerAndShowScene(settingsSceneAndController);
	}


	public void showStartingGameMenu(IStartingGame startingGame) {
		((StartingGameController) startingGameSceneAndController.getController()).setStartingGame(startingGame);
		resetControllerAndShowScene(startingGameSceneAndController);
	}
	private void resetControllerAndShowScene(SceneAndController sceneAndController) {
		sceneAndController.getController().resetUiState();
		stage.setScene(sceneAndController.getScene());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// invoke to shorten loading time later
		Platform.runLater(() -> {
			MapList.getDefaultList().getFreshMaps();
		});

		stage = primaryStage;
		mainSceneAndController = createScene("/jsettlers/main/javafx/main/mainMenu.fxml");
		settingsSceneAndController = createScene("/jsettlers/main/javafx/settings/SettingsMenu.fxml");
		startingGameSceneAndController = createScene("/jsettlers/main/javafx/startinggame/startingGame.fxml");
		inGameSceneAndController = createScene("/jsettlers/main/javafx/ingame/inGame.fxml");
		showMainScene();

		stage.show();
	}

	public static void main(String args[]) throws IOException {
		OptionableProperties optionableProperties = MainUtils.loadOptions(args);
		SwingManagedJSettlers.setupResourceManagers(optionableProperties, "config.prp");
		launch(args);
	}

	@Override public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}

	@Override public void setContent(UIPanel panel) {
		System.out.println("setContent UiPanel");
		throw new NotImplementedException();
	}

	@Override public void setContent(RegionContent panel) {
		System.out.println("setContent RegionContent");
		Region region = new Region(500, 500);
		region.setContent(panel);


		Area area = new Area();
		area.add(region);
		((InGameController) inGameSceneAndController.getController()).setSwingNodeContent(new AreaContainer(area));

		new Timer("opengl-redraw").schedule(new TimerTask() {
			@Override
			public void run() {
				region.requestRedraw();
			}
		}, 100, 25);

		Platform.runLater(() -> {
			stage.setScene(inGameSceneAndController.getScene());
		});
	}


	@Override public void goToStartScreen(String message) {
		System.out.println("goToStartScreen");
		throw new NotImplementedException();
	}
}
