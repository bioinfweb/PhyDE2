/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben St�ver, Jonas Bohn, Kai M�ller
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


import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;



@SuppressWarnings("serial")
public class ReverseComplementAction extends AbstractPhyDEAction implements Action {
	public ReverseComplementAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Reverse Complement"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
		putValue(Action.SHORT_DESCRIPTION, "Reverse Complement");
		//loadSymbols("Remove");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
		Collection<String> sequenceIDs = new ArrayList<>();
		
		for (int i = selection.getFirstRow(); i <= selection.getLastRow(); i++) {
			sequenceIDs.add(getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(i));
		}
		
		getMainFrame().getActiveDocument().executeEdit(new ReverseComplementEdit(getMainFrame().getActiveDocument(), 
				selection.getFirstColumn(), selection.getLastColumn(), sequenceIDs));
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && !mainframe.getActiveAlignmentArea().getSelection().isEmpty() && 
				mainframe.getActiveAlignmentArea().getAlignmentModel().getTokenSet().getType().isNucleotide());
	}
}
