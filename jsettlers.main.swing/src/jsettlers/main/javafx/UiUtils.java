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

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import jsettlers.graphics.localization.Labels;
import jsettlers.graphics.map.draw.ImageProvider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PropertyResourceBundle;

/**
 * @author codingberlin
 */
public class UiUtils {

	public static FXMLLoader createFxmlLoader() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		InputStreamReader reader = null; // using reader ensures the usage of correct encoding
		reader = new InputStreamReader(Labels.getMostDominantLocaleStream());
		System.out.println("Encoding of locale file: " + reader.getEncoding());
		fxmlLoader.setResources(new PropertyResourceBundle(reader));
		return fxmlLoader;
	}

	public static void setGuiBackground(Region region, int guiFile, int sequenceNumber) {
		BufferedImage bufferedBackgroundImage = ImageProvider.getInstance().getGuiImage(guiFile, sequenceNumber).generateBufferedImage();
		WritableImage fxImage = new WritableImage(bufferedBackgroundImage.getWidth(), bufferedBackgroundImage.getHeight());
		SwingFXUtils.toFXImage(bufferedBackgroundImage, fxImage);

		region.setBackground(
				new Background(
						new BackgroundImage(
								fxImage,
								BackgroundRepeat.NO_REPEAT,
								BackgroundRepeat.NO_REPEAT,
								BackgroundPosition.DEFAULT,
								new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));

	}

	public static void registerMousePressedBackgroundChange(Region button) {
		button.setOnMousePressed(e -> {
			UiUtils.setGuiBackground(button, 3, 329);
		});

		button.setOnMouseClicked(e -> {
			UiUtils.setGuiBackground(button, 3, 329);
		});

		button.setOnMouseReleased(e -> {
			setInitialButtonBackground(button);
		});
	}

	public static void setInitialButtonBackground(Region button) {
		UiUtils.setGuiBackground(button, 3, 326);
	}



}