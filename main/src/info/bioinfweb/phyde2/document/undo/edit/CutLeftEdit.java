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
package info.bioinfweb.phyde2.document.undo.edit;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.dataarea.DataAreaList;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;
import info.bioinfweb.phyde2.gui.MainFrame;

public class CutLeftEdit extends AlignmentEdit {
	private String sequenceID;
	private int newBaseCallCutPos;
	private int oldBaseCallCutPosition;
	private PherogramAreaModel pherogramModel= null;

	
	public CutLeftEdit(PhyDE2AlignmentModel alignment, String sequenceIDs, int newBaseCallCutPos, int oldBaseCallCutPosition) {
		super(alignment);
		this.sequenceID = sequenceIDs;
		this.newBaseCallCutPos = newBaseCallCutPos;
		this.oldBaseCallCutPosition = oldBaseCallCutPosition;
		pherogramModel = ((SingleReadContigAlignmentModel) alignment).getPherogramReference (sequenceID).getModel();
	}
	
	//Info: at the moment two undo steps have to be executed in PhyDE to undo the set of a new cut-position, because
	//the rebuilding of the sequence with the old cut position is an additional edit that happens in LibrAlign, so that
	//only a second click on "undo" undoes the set of a cut-position in the PherogramAreaModel. (see also: CutRightEdit)
	//Redo does not work properly as well, because a new edit is generated during redo which leads to truncation of the
	//current redo list.
	

	@Override
	public void redo() throws CannotRedoException {
		pherogramModel.setLeftCutPosition(newBaseCallCutPos);
		super.redo();	
	}


	@Override
	public void undo() throws CannotUndoException {	
		pherogramModel.setLeftCutPosition(oldBaseCallCutPosition);
		super.undo();
	}


	@Override
	public String getPresentationName() {
		StringBuilder result = new StringBuilder();
	
		result.append("Left cut position set to index ");
		result.append(newBaseCallCutPos);
		result.append(" in sequence ");
		result.append(getAlignment().getAlignmentModel().sequenceNameByID(sequenceID));
		result.append(".");
		
		return result.toString();

	}

}
