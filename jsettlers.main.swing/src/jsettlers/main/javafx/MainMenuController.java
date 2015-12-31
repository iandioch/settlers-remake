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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author codingberlin
 */
public class MainMenuController implements Initializable {

    @FXML
    private BorderPane startMenuPane;
    @FXML
    private Button settingsButton;
    @FXML
    private Button newGameGoButton;
    @FXML
    private Button joinMultiPlayerGoButton;
    @FXML
    private Button newMultiPlayerGoButton;
    @FXML
    private Button loadGameGoButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button joinMultiPlayerButton;
    @FXML
    private Button newMultiPlayerButton;
    @FXML
    private Button loadGameButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // overall background
        BackgroundImageUtils.setGuiBackground(startMenuPane, 2, 29);

        // buttons not pressed
        BackgroundImageUtils.setGuiBackground(settingsButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(newGameGoButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(joinMultiPlayerGoButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(newMultiPlayerGoButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(loadGameGoButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(newGameButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(joinMultiPlayerButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(newMultiPlayerButton, 3, 326);
        BackgroundImageUtils.setGuiBackground(loadGameButton, 3, 326);

        //TODO buttons pressed - 3, 329
    }
}