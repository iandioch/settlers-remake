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
import jsettlers.logic.map.MapLoader;
import jsettlers.logic.map.save.loader.RemakeMapLoader;

/**
 * @author codingberlin
 */
public abstract class SettlersListViewCellFactory implements Callback<ListView<MapLoader>, ListCell<MapLoader>> {
	@Override
	public ListCell<MapLoader> call(ListView<MapLoader> listView) {
		return new ListCell<MapLoader>() {
			@Override
			protected void updateItem(MapLoader map, boolean isEmpty) {
				super.updateItem(map, isEmpty);
				if (map != null && !isEmpty) {
					setText(getLabelOf(map));
				} else {
					setGraphic(null);
					setText(null);
				}
			}
		};
	}

	public abstract String getLabelOf(MapLoader mapLoader);
}
