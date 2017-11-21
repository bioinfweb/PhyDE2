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
package info.bioinfweb.phyde2.gui.actions.edit;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSet;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

@SuppressWarnings("serial")
public class DeleteCharSetAction extends AbstractPhyDEAction implements Action{

	public DeleteCharSetAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Delete character set"); 
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		CharSetDataModel model = new CharSetDataModel();
		int id = getMainFrame().getCharSetArea().getSelectedIndex();
		
		if (id == -1) {
			JOptionPane.showMessageDialog(getMainFrame(), "Please select the Char-Set which you want to delete.","Char-Set not found.", JOptionPane.ERROR_MESSAGE);
		}
		
		model = getMainFrame().getCharSetArea().getModel();
		CharSet charSet = model.get(model.get(id));
		charSet.setName(null);
		model.remove(id);
	}

}
