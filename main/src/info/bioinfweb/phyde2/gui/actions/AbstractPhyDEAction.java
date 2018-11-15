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
package info.bioinfweb.phyde2.gui.actions;


import info.bioinfweb.commons.swing.ExtendedAbstractAction;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public abstract class AbstractPhyDEAction extends ExtendedAbstractAction {
	private MainFrame mainframe;


	public AbstractPhyDEAction(MainFrame mainframe) {
		super();
		this.mainframe = mainframe;
	}	
	
	
	protected MainFrame getMainFrame() {
		return mainframe;
	}
	
	
	public abstract void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe);
	
	
	protected String getSelectedCharSetID() {
		int index = getMainFrame().getCharSetArea().getSelectedIndex();
		if (index == -1) {
			return null;
		}
		else {
			return getMainFrame().getDocument().getCharSetModel().get(index);
		}
	}
}