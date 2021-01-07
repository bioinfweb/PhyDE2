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
package info.bioinfweb.phyde2.document.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import info.bioinfweb.commons.io.W3CXSConstants;
import info.bioinfweb.jphyloio.ReadWriteConstants;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.utils.JPhyloIOWritingUtils;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramIOUtils;
import info.bioinfweb.libralign.pherogram.provider.PherogramReference;
import info.bioinfweb.libralign.pherogram.provider.ReverseComplementPherogramProvider;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

public class SingleReadContigAlignmentDataAdapter extends AbstractPhyDE2AlignmentDataAdapter<SingleReadContigAlignmentModel> {
	private String consensusSequenceID;
	private ConsensusAlignmentDataAdapter consensusAlignmentDataAdapter; 
	
	
	public SingleReadContigAlignmentDataAdapter(SingleReadContigAlignmentModel model) {
		super(model);
		consensusAlignmentDataAdapter = new ConsensusAlignmentDataAdapter(model); 
		this.consensusSequenceID = model.getConsensusModel().getID() + model.getConsensusSequenceID();
	}
	
	
	public SingleReadContigAlignmentDataAdapter(SingleReadContigAlignmentModel model, String idPrefix) {
		super(model, idPrefix);
		consensusAlignmentDataAdapter = new ConsensusAlignmentDataAdapter(model); 
		this.consensusSequenceID = model.getConsensusModel().getID() + model.getConsensusSequenceID();
	}
	

	@Override
	public Iterator<String> getSequenceIDIterator(ReadWriteParameterMap parameters) {
		final Iterator<String> singleReadIterator = super.getSequenceIDIterator(parameters);
		final Iterator<String> consensusIterator = consensusAlignmentDataAdapter.getSequenceIDIterator(parameters);

		return new Iterator<String>() {

			@Override
			public boolean hasNext() {
				if (singleReadIterator.hasNext()) {
					return true;
				}	
				else { 
					return consensusIterator.hasNext();
				}
			}

			
			@Override
			public String next() {
				if (singleReadIterator.hasNext()) {
					return singleReadIterator.next();	
				}
				else {
					return consensusIterator.next();
				}
			}
		}; 
	}
	
	
	@Override
	protected void writeSequenceMetadata(JPhyloIOEventReceiver receiver, String jPhyloIOPrefixSequenceID)
			throws IOException, IllegalArgumentException {
		super.writeSequenceMetadata(receiver, jPhyloIOPrefixSequenceID);
		
		String sequenceID = jPhyloIOPrefixSequenceID.substring(getSequenceStart());
		SingleReadContigAlignmentModel contigModel = (SingleReadContigAlignmentModel)getModel();
		PherogramReference reference = contigModel.getPherogramReference(sequenceID);
	
		if (jPhyloIOPrefixSequenceID.equals(consensusSequenceID)) {
			consensusAlignmentDataAdapter.writeSequenceMetadata(receiver, jPhyloIOPrefixSequenceID);
		}
		else {
			JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, 
					jPhyloIOPrefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "1", null, 
					ReadWriteParameterConstants.PREDICATE_IS_SINGLE_READ, W3CXSConstants.DATA_TYPE_BOOLEAN, true); 
			
			if (reference != null) {
				try {
					PherogramIOUtils.writePherogramMetadata(receiver, jPhyloIOPrefixSequenceID, reference, contigModel.getPherogramReference(sequenceID).getURL().toURI());
				}
				catch (URISyntaxException e) {
					e.printStackTrace();
					throw new InternalError(e);
				}
			}
		}
	}

	
	@Override
	public long getSequenceCount(ReadWriteParameterMap parameters) {
		return super.getSequenceCount(parameters) + 1;
	}


	@Override
	public LinkedLabeledIDEvent getSequenceStartEvent(ReadWriteParameterMap parameters, String jPhyloIOPrefixSequenceID) {
		System.out.println("GetSequenceStartEvent: "+jPhyloIOPrefixSequenceID.toString());
		if (jPhyloIOPrefixSequenceID.equals(consensusSequenceID)) {
			return consensusAlignmentDataAdapter.getSequenceStartEvent(parameters, jPhyloIOPrefixSequenceID);
			
		}
		else {
			return super.getSequenceStartEvent(parameters, jPhyloIOPrefixSequenceID);
		}
	}


	@Override
	public long getSequenceLength(ReadWriteParameterMap parameters, String jPhyloIOPrefixSequenceID) throws IllegalArgumentException {
		if(jPhyloIOPrefixSequenceID.equals(consensusSequenceID)) { 
			return consensusAlignmentDataAdapter.getSequenceLength(parameters, consensusSequenceID);
		}
		else {
			return super.getSequenceLength(parameters, jPhyloIOPrefixSequenceID);
		}
	}


	@Override
	public void writeSequencePartContentData(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver,
			String jPhyloIOPrefixSequenceID, long startColumn, long endColumn) throws IOException, IllegalArgumentException {
		if(jPhyloIOPrefixSequenceID.equals(consensusSequenceID)) { 
			consensusAlignmentDataAdapter.writeSequencePartContentData(parameters, receiver, jPhyloIOPrefixSequenceID, startColumn, endColumn);
		}
		else {
			super.writeSequencePartContentData(parameters, receiver, jPhyloIOPrefixSequenceID, startColumn, endColumn);
		}
	}
}
