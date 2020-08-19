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

import info.bioinfweb.commons.io.W3CXSConstants;
import info.bioinfweb.jphyloio.ReadWriteConstants;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.utils.JPhyloIOWritingUtils;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

public class DefaultAlignmentDataAdapter extends AbstractPhyDE2AlignmentDataAdapter<DefaultPhyDE2AlignmentModel> {	
	public DefaultAlignmentDataAdapter(DefaultPhyDE2AlignmentModel model) {
		super(model);
	}
	
	public DefaultAlignmentDataAdapter(DefaultPhyDE2AlignmentModel model, String idPrefix) {
		super(model, idPrefix);	
	}
	

//	@Override
//	public void writeMetadata(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver) throws IOException {
//		// TODO Auto-generated method stub
//		super.writeMetadata(parameters, receiver);
//	}

	@Override
	protected void writeSequenceMetadata(JPhyloIOEventReceiver receiver, String jPhyloIOPrefixSequenceID)
			throws IOException, IllegalArgumentException {
		super.writeSequenceMetadata(receiver, jPhyloIOPrefixSequenceID);
		String sequenceID = jPhyloIOPrefixSequenceID.substring(getSequenceStart());
		DefaultPhyDE2AlignmentModel alignmentModel = (DefaultPhyDE2AlignmentModel)getModel();
		SingleReadContigAlignmentModel contigReference = ((DefaultPhyDE2AlignmentModel) getModel()).getContigReference(sequenceID);
		
		if (contigReference != null) {
			JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver,
					jPhyloIOPrefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "1", null,
					ReadWriteParameterConstants.PREDICATE_HAS_CONTIG_REFERENCE, W3CXSConstants.DATA_TYPE_NC_NAME, 
					alignmentModel.getContigReference(sequenceID).getAlignmentModel().getID()); 				
		}
	}
}
