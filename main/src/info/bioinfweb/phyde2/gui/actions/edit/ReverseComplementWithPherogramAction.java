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
import java.util.ArrayList;
import java.util.Collection;

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementEdit;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementWithPherogramEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import javax.swing.Action;

public class ReverseComplementWithPherogramAction extends AbstractPhyDEAction implements Action{

	public ReverseComplementWithPherogramAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Reverse complement with Pherogram"); 
		putValue(Action.SHORT_DESCRIPTION, "Reverse complement with pherogram");
	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
		Collection<String> sequenceIDs = new ArrayList<>();
		
		for (int i = selection.getFirstRow(); i <= selection.getLastRow(); i++) {
			sequenceIDs.add(getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(i));
		}
		
		getMainFrame().getActiveAlignment().executeEdit(new ReverseComplementWithPherogramEdit(getMainFrame().getActiveAlignment(), sequenceIDs));
		
	}

	@Override
	public void setEnabled(PhyDE2AlignmentModel model, MainFrame mainframe) {
		setEnabled((model != null) && (model instanceof SingleReadContigAlignmentModel) && 
				mainframe.getActiveAlignmentArea().getAlignmentModel().getTokenSet().getType().isNucleotide());
		
	}

}