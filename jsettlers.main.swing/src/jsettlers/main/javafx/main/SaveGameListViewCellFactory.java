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

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import jsettlers.graphics.localization.Labels;
import jsettlers.logic.map.save.loader.RemakeMapLoader;

import java.text.DateFormat;

/**
 * @author codingberlin
 */
public class SaveGameListViewCellFactory implements Callback<ListView<RemakeMapLoader>, ListCell<RemakeMapLoader>> {

	@Override
	public ListCell<RemakeMapLoader> call(ListView<RemakeMapLoader> listView) {
		return new ListCell<RemakeMapLoader>() {
			@Override
			protected void updateItem(RemakeMapLoader mapLoader, boolean isEmpty) {
				super.updateItem(mapLoader, isEmpty);
				if (mapLoader != null && !isEmpty) {
					setText(getLabelOf(mapLoader));
				} else {
					setGraphic(null);
					setText(null);
				}
			}
		};
	}

	public String getLabelOf(RemakeMapLoader mapLoader) {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Labels.preferredLocale);
		String formattedDate = dateFormat.format(mapLoader.getCreationDate());
		String[] values = { formattedDate, mapLoader.getMapName() };
		return String.format("%s (%s)", values);
	}

}
