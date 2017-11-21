/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben Stöver, Jonas Bohn, Kai Müller
 * <http://bioinfweb.info/PhyDE2>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.phyde2.gui.actions.help;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.phyde2.Main;


@SuppressWarnings("serial")
public class PhyDEMainPageAction extends WebsiteAction {
	public PhyDEMainPageAction() {
		super(Main.APPLICATION_URL);
		
		putValue(Action.NAME, "PhyDE 2 website"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
		putValue(Action.SHORT_DESCRIPTION, "PhyDE Main Page comming soon");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, 
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		loadSymbols("PhyDE2");
	}

}
