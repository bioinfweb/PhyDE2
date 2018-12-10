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


import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.model.implementations.swingundo.edits.LibrAlignSwingAlignmentEdit;
import info.bioinfweb.libralign.model.implementations.swingundo.edits.sequence.SwingSequenceEdit;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;
import info.bioinfweb.phyde2.document.undo.AlignmentModelEdit;



public abstract class AbstractAddDeleteSequenceEdit extends AlignmentEdit {
	private String sequenceName;
	private String sequenceID;
	private PherogramReference pherogramReference;
	
	
	public AbstractAddDeleteSequenceEdit(PhyDE2AlignmentModel alignmentModel, String sequenceID, String sequenceName, 
			PherogramReference pherogramReference) {
		
		super(alignmentModel);
		this.sequenceID = sequenceID;
		this.sequenceName = sequenceName;
		this.pherogramReference = pherogramReference;
	}
	
	
	public void addSequence() throws CannotRedoException {
		if (sequenceID == null) {
			sequenceID = getAlignment().getAlignmentModel().getUnderlyingModel().addSequence(sequenceName);
			System.out.println(sequenceID);
		}
		else {
			getAlignment().getAlignmentModel().getUnderlyingModel().addSequence(sequenceName, sequenceID);
		}
		
		if (getAlignment() instanceof SingleReadContigAlignmentModel && pherogramReference != null) {
			System.out.println(pherogramReference);
			
			for (int j = 0; j < pherogramReference.getModel().getPherogramProvider().getSequenceLength(); j++) {
				getAlignment().getAlignmentModel().getUnderlyingModel().appendToken(sequenceID, pherogramReference.getModel().getPherogramProvider().getBaseCall(j));
			}
			((SingleReadContigAlignmentModel) getAlignment()).addPherogram(sequenceID, pherogramReference);
		}
		super.redo();
	}


	public void deleteSequence() throws CannotUndoException {
		getAlignment().getAlignmentModel().getUnderlyingModel().removeSequence(sequenceID);
		//TODO vielleicht umgekehrt wegen Exception
		if (getAlignment() instanceof SingleReadContigAlignmentModel){
			((SingleReadContigAlignmentModel) getAlignment()).removePherogramModel(sequenceID);	
		}
		super.undo();
	}
}
