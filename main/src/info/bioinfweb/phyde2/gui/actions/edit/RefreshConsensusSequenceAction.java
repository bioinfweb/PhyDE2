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

import java.awt.List;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.RefreshConsensusSequenceEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class RefreshConsensusSequenceAction extends AbstractPhyDEAction implements Action{

	public RefreshConsensusSequenceAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Refresh consensus sequence"); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
		ArrayList<String> sequenceIDs = new ArrayList<>();
		//String sequenceID = getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(selection.getFirstRow());
		DefaultPhyDE2AlignmentModel model = (DefaultPhyDE2AlignmentModel) getMainFrame().getActiveAlignment();
		for (int row = selection.getFirstRow(); row <= selection.getLastRow(); row++) {
			sequenceIDs.add(getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(row));
		}
		getMainFrame().getActiveAlignment().executeEdit(new RefreshConsensusSequenceEdit(model, sequenceIDs));
		
	}

	@Override
	public void setEnabled(PhyDE2AlignmentModel model, MainFrame mainframe) {
		boolean enabled = false;
		if (mainframe.getActiveAlignmentArea() != null){
			if (mainframe.getActiveAlignmentArea().getSelection() != null){
				if (model instanceof DefaultPhyDE2AlignmentModel){
					SelectionModel selection = mainframe.getActiveAlignmentArea().getSelection();
					boolean hasContig = false; 
					if (model.getAlignmentModel().getSequenceCount() > 0) {
						
						for (int row = selection.getFirstRow(); row <= selection.getLastRow(); row++){
							if (((DefaultPhyDE2AlignmentModel) model).sequenceHasContig(getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(row))) {
								hasContig = true;
							}
						}
					}
					
					enabled = ((model != null) && (model.getAlignmentModel().getSequenceCount() != 0) && hasContig);		
				}
				
			}
			
		}
		setEnabled(enabled);
		
	}

}
