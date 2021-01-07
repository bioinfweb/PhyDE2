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

import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.dataarea.DataArea;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.dataelement.DataList;
import info.bioinfweb.libralign.pherogram.model.PherogramAlignmentRelation;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.CutLeftEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;



public class CutLeftAction extends AbstractPhyDEAction implements Action{
	public CutLeftAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Set left cut position");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
		String sequenceID = getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(selection.getFirstRow());
		DataList<AlignmentArea, DataArea> sequenceDataAreaList = MainFrame.getInstance().getActiveAlignmentArea().getDataAreas().getSequenceList(sequenceID);
		PherogramArea pherogramArea = null;
		
		if (sequenceDataAreaList.get(sequenceDataAreaList.size()-1) instanceof PherogramArea){
			pherogramArea = (PherogramArea) sequenceDataAreaList.get(sequenceDataAreaList.size()-1);
		}
		
		if (pherogramArea != null){
			PherogramAlignmentRelation relation = pherogramArea.getModel().baseCallIndexByEditableIndex(selection.getFirstColumn());
			int oldPos = pherogramArea.getModel().getLeftCutPosition();
			int newPos = relation.getAfter();
			if (newPos == PherogramAlignmentRelation.OUT_OF_RANGE) {
				newPos = relation.getBefore() + 1;  // Set cut position behind the end of the pherogram.
			}
			PherogramAreaModel pherogramModel = ((SingleReadContigAlignmentModel) getMainFrame().getActiveAlignment()).getPherogramReference(sequenceID);
			getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
			pherogramModel.setLeftCutPosition(newPos);
			getMainFrame().getActiveAlignment().getEditRecorder().endEdit(getPresentationName(newPos,sequenceID));
			getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
			//getMainFrame().getActiveAlignment().executeEdit(new CutLeftEdit(getMainFrame().getActiveAlignment(), sequenceID, newPos, oldPos));
		}	
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((mainframe.getActiveAlignment() instanceof SingleReadContigAlignmentModel) && 
				(mainframe.getActiveAlignmentArea().getSelection().getCursorHeight() == 1));		
	}
	
	
	private String getPresentationName(int newBaseCallCutPos, String id) {
		StringBuilder result = new StringBuilder();
	
		result.append("Left cut position set to index ");
		result.append(newBaseCallCutPos);
		result.append(" in sequence ");
		result.append(getMainFrame().getActiveAlignment().getAlignmentModel().sequenceNameByID(id));
		result.append(".");
		
		return result.toString();

	}
}
