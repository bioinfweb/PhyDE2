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


import java.awt.event.ActionEvent;

import javax.swing.Action;

import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;
import info.bioinfweb.phyde2.gui.dialogs.AboutDialog;



@SuppressWarnings("serial")
public class AboutAction extends AbstractPhyDEAction {
	private AboutDialog dialog = null;
	
	
	public AboutAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "About..."); 
		putValue(Action.SHORT_DESCRIPTION, "About PhyDE2"); 
		loadSymbols("Help");
	}


	private AboutDialog getDialog() {
		if (dialog == null) {
			dialog = new AboutDialog(MainFrame.getInstance());
		}
		return dialog;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		getDialog().setVisible(true);
	}
}
