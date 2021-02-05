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


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSet;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;



/**
 * Allows to edit the color of a character set.
 * 
 * @author Ben St&ouml;ver
 * @author Jonas Bohn
 */
@SuppressWarnings("serial")
public class ChangeCharSetColorAction extends AbstractPhyDEAction implements Action {

	public ChangeCharSetColorAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Change character set color"); 
		putValue(Action.SHORT_DESCRIPTION, "Change char. set color");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		String id = getSelectedCharSetID();
		
		if (id == null) {
			JOptionPane.showMessageDialog(getMainFrame(), "A character set needs to be selected in order to edit its color.", "No character set selected", 
					JOptionPane.ERROR_MESSAGE);
		}
		else {
			CharSet charSet = getMainFrame().getActiveCharSetArea().getModel().get(id);
			Color color = JColorChooser.showDialog(getMainFrame(), "Edit character set color", charSet.getColor());
			if (color != null) {
				getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
				getMainFrame().getActiveCharSetArea().getModel().get(id).setColor(color);
				getMainFrame().getActiveAlignment().getEditRecorder().endEdit(getPresentationName(id));
				getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
				//getMainFrame().getActiveAlignment().executeEdit(new ChangeCharSetColorEdit(getMainFrame().getActiveAlignment(), getMainFrame().getActiveCharSetArea().getModel(), id, color));
			}
		}
	}
	
	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(getSelectedCharSetID() != null);
	}
	
	
	private String getPresentationName(String id) {
		return "Change color of character set \"" + getMainFrame().getActiveAlignment().getCharSetModel().get(id).getName() + "\"";
	}
}
