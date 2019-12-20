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


import java.io.IOException;
import java.net.URL;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;

import info.bioinfweb.libralign.pherogram.provider.PherogramProvider;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PherogramProviderByURL;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;



public class AddSequenceEdit extends AlignmentEdit {
	private String sequenceName;
	private String sequenceID = null;
	private URL url;
	private PherogramProvider pherogramProvider;
	private PherogramReference pherogramReference = null;
	private SingleReadContigAlignmentModel contigReference;
	
	
	public AddSequenceEdit(PhyDE2AlignmentModel alignmentModel, String sequenceName, URL url, SingleReadContigAlignmentModel contigReference) 
			throws UnsupportedChromatogramFormatException, IOException {
		
		super(alignmentModel);
	
		this.sequenceName = sequenceName;
		this.url = url;
		if (url == null) {
			pherogramProvider = null;
		}
		else {
			//TODO The sequence ID is not known yet
			pherogramProvider = PherogramProviderByURL.getInstance().getPherogramProvider(url);
		}
		this.contigReference = contigReference;
	}


	@Override
	public void redo() throws CannotRedoException {
		if (sequenceID == null) {
			sequenceID = getAlignment().getAlignmentModel().getUnderlyingModel().addSequence(sequenceName);
			pherogramReference = new PherogramReference(getAlignment().getAlignmentModel().getUnderlyingModel(), pherogramProvider, url, sequenceID);  // PherogramReference cannot be created before, since the sequenceID is not known. The provider cannot be created here, since that might throw an exception that cannot be caught.
		}
		else {
			getAlignment().getAlignmentModel().getUnderlyingModel().addSequence(sequenceName, sequenceID);
		}
		
		if ((getAlignment() instanceof SingleReadContigAlignmentModel) && (pherogramReference != null)) {
			for (int j = 0; j < pherogramReference.getPherogramProvider().getSequenceLength(); j++) {
				getAlignment().getAlignmentModel().getUnderlyingModel().appendToken(sequenceID, pherogramReference.getPherogramProvider().getBaseCall(j));
			}
			((SingleReadContigAlignmentModel) getAlignment()).addPherogram(sequenceID, pherogramReference);
		}
		
		else if (getAlignment() instanceof DefaultPhyDE2AlignmentModel && contigReference != null){
			((DefaultPhyDE2AlignmentModel) getAlignment()).addConsensus(contigReference, sequenceID);
		}
		
		super.redo();
	}


	@Override
	public void undo() throws CannotUndoException {
		getAlignment().getAlignmentModel().getUnderlyingModel().removeSequence(sequenceID);
		if (getAlignment() instanceof SingleReadContigAlignmentModel) {
			((SingleReadContigAlignmentModel) getAlignment()).removePherogramModel(sequenceID);	
		}
		
		if (getAlignment() instanceof DefaultPhyDE2AlignmentModel){
			((DefaultPhyDE2AlignmentModel)getAlignment()).removeConsensusReference(sequenceID);
		}
	
		super.undo();
	}


	@Override
	public String getPresentationName() {
		StringBuilder result = new StringBuilder();
		result.append("Sequence ");
		result.append(sequenceName);
		result.append(" added in alignment ");
		result.append(getAlignment());
		result.append(".");
		
		return result.toString();
	}
}
