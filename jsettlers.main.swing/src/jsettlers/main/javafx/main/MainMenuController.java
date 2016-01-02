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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import jsettlers.graphics.localization.Labels;
import jsettlers.graphics.startscreen.interfaces.IStartingGame;
import jsettlers.logic.map.save.MapList;
import jsettlers.logic.map.save.loader.RemakeMapLoader;
import jsettlers.logic.map.save.loader.SavegameLoader;
import jsettlers.logic.player.PlayerSetting;
import jsettlers.main.JSettlersGame;
import jsettlers.main.javafx.SettlersApplicationController;
import jsettlers.main.javafx.UiUtils;

import java.net.URL;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class MainMenuController extends SettlersApplicationController implements Initializable {

	private final Callback<ListView<RemakeMapLoader>, ListCell<RemakeMapLoader>> savegameListViewCellFactory;
	@FXML private BorderPane startMenuPane;
	@FXML private Button settingsButton;
	@FXML private Button exitButton;
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
	@FXML private ListView<RemakeMapLoader> listView;
	@FXML private TextField listViewFilterField;

	private ButtonBase[] goButtons;
	private ButtonBase[] allButtons;

	public MainMenuController() {
		savegameListViewCellFactory = new Callback<ListView<RemakeMapLoader>, ListCell<RemakeMapLoader>>() {
			@Override
			public ListCell<RemakeMapLoader> call(ListView<RemakeMapLoader> listView) {
				return new ListCell<RemakeMapLoader>() {
					@Override
					protected void updateItem(RemakeMapLoader mapLoader, boolean isEmpty) {
						super.updateItem(mapLoader, isEmpty);
						if (mapLoader != null) {
							DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Labels.preferredLocale);
							String formattedDate = dateFormat.format(mapLoader.getCreationDate());
							String[] values = { formattedDate, mapLoader.getMapName() };
							setText(String.format("%s (%s)", values));
						}
					}
				};
			}
		};
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		goButtons = new ButtonBase[] { newGameGoButton, joinMultiPlayerGoButton, newMultiPlayerGoButton, loadGameGoButton };
		allButtons = new ButtonBase[] { loadGameButton, joinMultiPlayerButton, settingsButton, exitButton, newGameButton, newGameGoButton,
				joinMultiPlayerGoButton, newMultiPlayerGoButton, loadGameGoButton, newMultiPlayerButton };

		settingsButton.setOnAction(event -> {
			settlersApplication.showSettingsScene();
		});

		exitButton.setOnAction(e -> {
			settlersApplication.close();
		});

		loadGameButton.setOnAction(e -> {
			MapList mapList = MapList.getDefaultList();
			listView.getItems().clear();
			List<RemakeMapLoader> singlePlayerSaveGames = mapList.getSavedMaps().getItems();
			listView.getItems().addAll(singlePlayerSaveGames);
			listView.setCellFactory(savegameListViewCellFactory);
			selectFromListPane.setVisible(true);
			hideGoButtons();
			loadGameGoButton.setVisible(true);
		});

		loadGameGoButton.setOnAction(e -> {
			SavegameLoader savegameLoader = (SavegameLoader) listView.getSelectionModel().getSelectedItem();
			if (savegameLoader != null) {
				long randomSeed = 4711L;
				byte playerId = 0;
				PlayerSetting[] playerSettings = PlayerSetting.createDefaultSettings(playerId, (byte) savegameLoader.getMaxPlayers());
				JSettlersGame game = new JSettlersGame(savegameLoader, randomSeed, playerId, playerSettings);
				IStartingGame startingGame = game.start();
				settlersApplication.showStartingGameMenu(startingGame);
			}
		});

		setOriginalSettlersBackgroundImages();
		resetUiState();
	}

	private void setOriginalSettlersBackgroundImages() {
		UiUtils.setGuiBackground(startMenuPane, 2, 29);
		Arrays.stream(allButtons).forEach(UiUtils::registerMousePressedBackgroundChange);

		startMenuToggleButtons.selectedToggleProperty().addListener((observableValue, buttonToDisable, buttonToEnable) -> {
			if (buttonToDisable != null) {
				UiUtils.setGuiBackground((ToggleButton) buttonToDisable, 3, 326);
			}
		});
	}

	private void hideGoButtons() {
		Arrays.stream(goButtons).forEach(button -> {
			button.setVisible(false);
		});
	}

	@Override public void resetUiState() {
		Arrays.stream(allButtons).forEach(UiUtils::setInitialButtonBackground);

		listViewFilterField.setText("");
		listView.getItems().clear();
		selectFromListPane.setVisible(false);
	}

}