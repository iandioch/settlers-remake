/*******************************************************************************
 * Copyright (c) 2015 - 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.mapcreator.main.window.search;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import jsettlers.mapcreator.localization.EditorLabels;

/**
 * Search Text field
 * 
 * @author Andreas Butti
 */
public class SearchTextField extends JTextField {
	private static final long serialVersionUID = 1L;

	/**
	 * Search icon
	 */
	private SearchIcon searchIcon = new SearchIcon();

	/**
	 * Icon to clear search
	 */
	private ClearIcon clearIcon = new ClearIcon();

	/**
	 * Clear "Button"
	 */
	private JLabel lbClear = new JLabel(clearIcon);

	/**
	 * Search text
	 */
	private final String SEARCH = EditorLabels.getLabel("general.search");

	/**
	 * Constructor
	 */
	public SearchTextField() {
		setMargin(new Insets(2, searchIcon.getIconWidth() + 4, 2, clearIcon.getIconWidth() + 4));

		lbClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setText("");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				clearIcon.setHover(true);
				lbClear.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				clearIcon.setHover(false);
				lbClear.repaint();
			}

		});
		add(lbClear);
		lbClear.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void layout() {
		int x = getWidth() - 4 - clearIcon.getIconWidth();
		int y = (this.getHeight() - searchIcon.getIconHeight()) / 2;
		lbClear.setBounds(x, y, clearIcon.getIconWidth(), clearIcon.getIconHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int x = 4;
		searchIcon.paintIcon(this, g, x, (this.getHeight() - searchIcon.getIconHeight()) / 2);

		// color already set by search icon...
		if (getText().isEmpty()) {
			x += searchIcon.getIconWidth() + 6;
			int y = this.getHeight() - (this.getHeight() - g.getFontMetrics().getHeight()) / 2 - g.getFontMetrics().getDescent();
			g.drawString(SEARCH, x, y);
		}
	}

}
