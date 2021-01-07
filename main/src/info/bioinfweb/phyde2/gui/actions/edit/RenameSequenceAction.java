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

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.model.events.SequenceRenamedEvent;
import info.bioinfweb.libralign.model.undo.alignment.sequence.AlignmentModelRenameSequenceEdit;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

@SuppressWarnings("serial")
public class RenameSequenceAction extends AbstractPhyDEAction implements Action {

	public RenameSequenceAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Rename sequence"); 
		putValue(Action.SHORT_DESCRIPTION, "Rename sequence");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		loadSymbols("Replace");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
		String name = JOptionPane.showInputDialog("New sequence name");
		if (name != null) {
			getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
			for (int row = selection.getFirstRow(); row <= selection.getLastRow(); row++) {
				String id = getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(row);
				getMainFrame().getActiveAlignment().getAlignmentModel().renameSequence(id, name);
			}
			getMainFrame().getActiveAlignment().getEditRecorder().endEdit("Sequence name changed to " + name);
			getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
		}
	}
	//TODO: undo option can not get the sequence name back
	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && document.getAlignmentModel().getSequenceCount() != 0);
	}
	
	
}
