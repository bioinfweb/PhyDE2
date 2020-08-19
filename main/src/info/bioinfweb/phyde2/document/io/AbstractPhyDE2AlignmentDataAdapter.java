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
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.events.meta.URIOrStringIdentifier;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.utils.JPhyloIOWritingUtils;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataAdapter;
import info.bioinfweb.libralign.model.io.AlignmentModelDataAdapter;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;



/**
 * <i>JPhyloIO</i> document adapter for a <i>PhyDE</i> document.
 * 
 * @author Ben St&ouml;ver
 */
public class AbstractPhyDE2AlignmentDataAdapter <M extends PhyDE2AlignmentModel> extends AlignmentModelDataAdapter<Character> implements IOConstants {
	private M model;
	private int sequenceStart;
	
	
	public AbstractPhyDE2AlignmentDataAdapter(M model) {
		super(model.getAlignmentModel().getID(), new LinkedLabeledIDEvent(EventContentType.ALIGNMENT, model.getAlignmentModel().getID(), model.getAlignmentModel().getLabel(), null), 
				model.getAlignmentModel(), false); 
		//TODO ensure that prefix is not added on each write operation again
			this.model = model;
			sequenceStart = getIDPrefix().length();
	}
	
	
	public AbstractPhyDE2AlignmentDataAdapter(M model, String idPrefix) {
		super(idPrefix ,new LinkedLabeledIDEvent(EventContentType.ALIGNMENT, model.getAlignmentModel().getID(), model.getAlignmentModel().getLabel(), null), 
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
	public void writeMetadata(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver) throws IOException {
		super.writeMetadata(parameters, receiver);
		JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, 
				model.getAlignmentModel().getID() + ReadWriteConstants.DEFAULT_META_ID_PREFIX + "1", null, ReadWriteParameterConstants.PREDICATE_ALIGNMENT_TYPE, W3CXSConstants.DATA_TYPE_STRING, model.getType());
	}


	protected M getModel() {
		return model;
	}


	protected int getSequenceStart() {
		return sequenceStart;
	}

	
}
