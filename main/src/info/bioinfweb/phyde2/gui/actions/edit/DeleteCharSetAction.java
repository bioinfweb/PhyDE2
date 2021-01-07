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
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSet;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.DeleteCharSetEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;



@SuppressWarnings("serial")
public class DeleteCharSetAction extends AbstractPhyDEAction implements Action{
	public DeleteCharSetAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Delete character set"); 
		putValue(Action.SHORT_DESCRIPTION, "Delete char. set"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
		loadSymbols("DeleteCH");
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		String id = getSelectedCharSetID();
		
		if (id == null) {
			JOptionPane.showMessageDialog(getMainFrame(), "Please select the Char-Set which you want to delete.","Char-Set not found.", JOptionPane.ERROR_MESSAGE);
		}
		CharSet charSet = getMainFrame().getActiveAlignment().getCharSetModel().get(id);
		CharSetDataModel dataModel = getMainFrame().getActiveAlignment().getCharSetModel();
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
		dataModel.remove(id, charSet);
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit(getPresentationName(charSet.getName()));
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
		//getMainFrame().getActiveAlignment().executeEdit(new DeleteCharSetEdit(getMainFrame().getActiveAlignment(), charSet, id));
	}
	
	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(getSelectedCharSetID() != null);
	}
	
	
	private String getPresentationName(String name) {
		return "Delete character set \"" + name + "\"";
	}
}
