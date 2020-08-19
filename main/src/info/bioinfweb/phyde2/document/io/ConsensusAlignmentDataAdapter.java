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
import java.util.Iterator;

import info.bioinfweb.commons.io.W3CXSConstants;
import info.bioinfweb.jphyloio.ReadWriteConstants;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.utils.JPhyloIOWritingUtils;
import info.bioinfweb.libralign.model.io.AlignmentModelDataAdapter;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

public class ConsensusAlignmentDataAdapter extends AlignmentModelDataAdapter<Character> implements IOConstants {
	private String consensusSequenceID;
	private int sequenceStart;
	private SingleReadContigAlignmentModel model;
	
	public ConsensusAlignmentDataAdapter(SingleReadContigAlignmentModel model) {
		super(model.getConsensusModel().getID(), new LinkedLabeledIDEvent(EventContentType.ALIGNMENT, 
				model.getConsensusModel().getID(), model.getConsensusModel().getLabel(), null), model.getConsensusModel(), false);
		this.model = model;
		this.consensusSequenceID = model.getConsensusModel().getID() + model.getConsensusSequenceID();
		sequenceStart = getIDPrefix().length();
	}

	
	@Override
	protected void writeSequenceMetadata(JPhyloIOEventReceiver receiver, String jPhyloIOPrefixSequenceID)
			throws IOException, IllegalArgumentException {
		super.writeSequenceMetadata(receiver, jPhyloIOPrefixSequenceID);

				
		System.out.println("writeSequenceMetadata, Consensus "+ jPhyloIOPrefixSequenceID.toString());
	
		JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver,
				jPhyloIOPrefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "1", null,
				ReadWriteParameterConstants.PREDICATE_IS_CONSENSUS_SEQUENCE, W3CXSConstants.DATA_TYPE_BOOLEAN, 
				true); 
	}

	
	@Override
	public long getSequenceLength(ReadWriteParameterMap parameters, String sequenceID) throws IllegalArgumentException {
		return model.getConsensusModel().getSequenceLength(modelByJPhyloIOSequenceID(consensusSequenceID));
	}

	
	@Override
	public Iterator<String> getSequenceIDIterator(ReadWriteParameterMap parameters) {
		// TODO Auto-generated method stub
		return super.getSequenceIDIterator(parameters);
	}	
}
