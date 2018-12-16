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
import info.bioinfweb.commons.io.W3CXSConstants;
import info.bioinfweb.jphyloio.ReadWriteConstants;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.events.meta.URIOrStringIdentifier;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.utils.JPhyloIOWritingUtils;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataAdapter;
import info.bioinfweb.libralign.model.io.AlignmentModelDataAdapter;
import info.bioinfweb.libralign.pherogram.provider.ReverseComplementPherogramProvider;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;



/**
 * <i>JPhyloIO</i> document adapter for a <i>PhyDE</i> document.
 * 
 * @author Ben St&ouml;ver
 */
public class PhyDEAlignmentDataAdapter extends AlignmentModelDataAdapter<Character> implements IOConstants {
	private PhyDE2AlignmentModel model;
	private Integer sequenceStart;
	
	
	public PhyDEAlignmentDataAdapter(PhyDE2AlignmentModel model) {
		super(model.getAlignmentModel().getID(), new LinkedLabeledIDEvent(EventContentType.ALIGNMENT, model.getAlignmentModel().getID(), null, null), 
				model.getAlignmentModel(), false); 
		//TODO ensure that prefix is not added on each write operation again
		this.model = model;
		sequenceStart = getIDPrefix().length();
	}
	
	
	public PhyDEAlignmentDataAdapter(PhyDE2AlignmentModel model, String idPrefix) {
		super(idPrefix ,new LinkedLabeledIDEvent(EventContentType.ALIGNMENT, model.getAlignmentModel().getID(), null, null), 
				model.getAlignmentModel(), false); 
		//TODO ensure that prefix is not added on each write operation again
		this.model = model;
		sequenceStart = idPrefix.length();
	}

	
	@Override
	public ObjectListDataAdapter<LinkedLabeledIDEvent> getCharacterSets(ReadWriteParameterMap parameters) {
		return new CharSetDataAdapter(getIDPrefix(), model.getCharSetModel(), 
				PhyDEDocumentDataAdapter.ALIGNMENT_ID, new URIOrStringIdentifier(null, PREDICATE_COLOR), 
				new URIOrStringIdentifier(null, DATA_TYPE_COLOR));
	}


	@Override
	protected void writeSequenceMetadata(JPhyloIOEventReceiver receiver, String prefixSequenceID)
			throws IOException, IllegalArgumentException {
		super.writeSequenceMetadata(receiver, prefixSequenceID);
		
		if (model instanceof SingleReadContigAlignmentModel) {
			SingleReadContigAlignmentModel contigModel = (SingleReadContigAlignmentModel)model;
			String sequenceID = prefixSequenceID.substring(sequenceStart);
			
			JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, 
					prefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "1", null, ReadWriteParameterConstants.PREDICATE_IS_SINGLE_READ, W3CXSConstants.DATA_TYPE_BOOLEAN, true); 
			JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver,
					prefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "2", null,
					ReadWriteParameterConstants.PREDICATE_IS_REVERSE_COMPLEMENTED, W3CXSConstants.DATA_TYPE_BOOLEAN, 
					contigModel.getPherogramModel(sequenceID).getModel().getPherogramProvider() instanceof ReverseComplementPherogramProvider); // TODO use boolean method of PhyDE2 or LibrAlign in the future
			JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, 
					prefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "3", null, 
					ReadWriteParameterConstants.PREDICATE_HAS_LEFT_CUT_POSITION, W3CXSConstants.DATA_TYPE_INT, 
					contigModel.getPherogramModel(sequenceID).getModel().getLeftCutPosition());
			JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, 
					prefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "4", null, 
					ReadWriteParameterConstants.PREDICATE_HAS_RIGHT_CUT_POSITION, W3CXSConstants.DATA_TYPE_INT, 
					contigModel.getPherogramModel(sequenceID).getModel().getRightCutPosition());
			try {
				JPhyloIOWritingUtils.writeTerminalResourceMetadata(receiver, 
						prefixSequenceID + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "5", null, 
						ReadWriteParameterConstants.PREDICATE_HAS_PHEROGRAM, contigModel.getPherogramModel(sequenceID).getURL().toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
				throw new InternalError(e);
			}
		}
		
	}
	
}
