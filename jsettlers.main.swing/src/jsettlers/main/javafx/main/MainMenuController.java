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

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class MainMenuController extends SettlersApplicationController implements Initializable {

	private final SaveGameListViewCellFactory savegameListViewCellFactory;

	@FXML private BorderPane startMenuPane;
	@FXML private Button settingsButton;
	@FXML private Button exitButton;
	@FXML private Button triggerActionWithSelectedItemButton;
	@FXML private ToggleButton newGameButton;
	@FXML private ToggleButton joinMultiPlayerButton;
	@FXML private ToggleButton newMultiPlayerButton;
	@FXML private ToggleButton loadGameButton;
	@FXML private ToggleGroup startMenuToggleButtons;
	@FXML private BorderPane selectFromListPane;
	@FXML private ListView<RemakeMapLoader> listView;
	@FXML private TextField listViewFilterField;


	/*
	%start-joinmultiplayer-go
	%start-newmultiplayer-go
	%start-loadgame-go
	%newgame-go
	 */

	private ButtonBase[] allButtons;

	public MainMenuController() {
		savegameListViewCellFactory = new SaveGameListViewCellFactory();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allButtons = new ButtonBase[] { loadGameButton, joinMultiPlayerButton, settingsButton, exitButton, newGameButton,
				triggerActionWithSelectedItemButton, newMultiPlayerButton };

		settingsButton.setOnAction(event -> {
			settlersApplication.showSettingsScene();
		});

		exitButton.setOnAction(e -> {
			settlersApplication.close();
		});

		loadGameButton.setOnAction(e -> {
			VoidCallback actionWithSelectedItemHandler = () -> {
				SavegameLoader savegameLoader = (SavegameLoader) listView.getSelectionModel().getSelectedItem();
				if (savegameLoader != null) {
					long randomSeed = 4711L;
					byte playerId = 0;
					PlayerSetting[] playerSettings = PlayerSetting.createDefaultSettings(playerId, (byte) savegameLoader.getMaxPlayers());
					JSettlersGame game = new JSettlersGame(savegameLoader, randomSeed, playerId, playerSettings);
					IStartingGame startingGame = game.start();
					settlersApplication.showStartingGameMenu(startingGame);
				}
			};

			MapList mapList = MapList.getDefaultList();
			FilteredList<RemakeMapLoader> singlePlayerSaveGames =
					new FilteredList<>(FXCollections.observableList(mapList.getSavedMaps().getItems()), map -> true);
			listView.setCellFactory(savegameListViewCellFactory);
			listView.setItems(singlePlayerSaveGames);
			listView.getSelectionModel().selectFirst();
			listViewFilterField.textProperty().addListener((observable, oldText, newText) -> {
				if (newText == null || newText.length() == 0) {
					singlePlayerSaveGames.setPredicate(map -> true);
				} else {
					singlePlayerSaveGames.setPredicate(map ->
							savegameListViewCellFactory.getLabelOf(map).toLowerCase().contains(newText.toLowerCase()));
				}
			});
			listViewFilterField.setOnKeyReleased(keyEvent -> {
				switch (keyEvent.getCode()) {
				case ENTER:
					actionWithSelectedItemHandler.call();
					break;
				case DOWN:
					int nextIndex = listView.getSelectionModel().getSelectedIndex() + 1;
					if (nextIndex < listView.getItems().size()) {
						listView.getSelectionModel().clearSelection();
						listView.getSelectionModel().select(nextIndex);
					}
					break;
				case UP:
					int previousIndex = listView.getSelectionModel().getSelectedIndex() - 1;
					if (previousIndex >= 0) {
						listView.getSelectionModel().clearSelection();
						listView.getSelectionModel().select(previousIndex);
					}
					break;
				default:
					if (listView.getSelectionModel().getSelectedItem() == null) {
						listView.getSelectionModel().selectFirst();
					}
				}
			});
			triggerActionWithSelectedItemButton.setDisable(listView.getSelectionModel().getSelectedItem() == null);
			listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				triggerActionWithSelectedItemButton.setDisable(newValue == null);
			});
			triggerActionWithSelectedItemButton.setText(Labels.getString("start-loadgame-go"));
			triggerActionWithSelectedItemButton.setOnAction(event -> actionWithSelectedItemHandler.call());
			selectFromListPane.setVisible(true);
			listViewFilterField.requestFocus();
		}
		);

		listView.getSelectionModel().

				setSelectionMode(SelectionMode.SINGLE);

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

	@Override public void resetUiState() {
		Arrays.stream(allButtons).forEach(UiUtils::setInitialButtonBackground);

		listViewFilterField.setText("");
		selectFromListPane.setVisible(false);
	}

}