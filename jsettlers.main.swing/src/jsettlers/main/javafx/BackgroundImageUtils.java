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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import jsettlers.graphics.map.draw.ImageProvider;

import java.awt.image.BufferedImage;

/**
 * @author codingberlin
 */
public class BackgroundImageUtils {


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
}
