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
package info.bioinfweb.phyde2.gui.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.dataarea.DataAreaList;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class CutRightAction extends AbstractPhyDEAction implements Action{
	private PherogramArea pherogramArea = null;
	public CutRightAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Set right cut position");	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
			for (int row = selection.getFirstRow(); row <= selection.getLastRow(); row++) {
				String id = getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(selection.getFirstRow());
				DataAreaList sequenceDataAreaList = getMainFrame().getActiveAlignmentArea().getDataAreas().getSequenceAreas(id);
				
				if (sequenceDataAreaList.get(sequenceDataAreaList.size()-1) instanceof PherogramArea){// da als letztes hinzugefügt
				pherogramArea = (PherogramArea) sequenceDataAreaList.get(sequenceDataAreaList.size()-1); 
				pherogramArea.setRightCutPositionBySelection();
				}
				
			}
		}
		catch (IndexOutOfBoundsException e1){
			
		}
		
	}
	

	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		//setEnabled (pherogramArea != null);
		//Das Funktioniert nicht, da die pherogramArea noch gar nicht bekannt ist. 
		setEnabled(mainframe.getActiveAlignment() instanceof SingleReadContigAlignmentModel);
		
	}

}
