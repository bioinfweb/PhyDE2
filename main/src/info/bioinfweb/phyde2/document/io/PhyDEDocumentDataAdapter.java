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


import info.bioinfweb.commons.io.W3CXSConstants;
import info.bioinfweb.jphyloio.ReadWriteConstants;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.JPhyloIOEventReceiver;
import info.bioinfweb.jphyloio.dataadapters.implementations.ListBasedDocumentDataAdapter;
import info.bioinfweb.jphyloio.events.CommentEvent;
import info.bioinfweb.jphyloio.utils.JPhyloIOWritingUtils;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;

import java.io.IOException;
import java.util.Set;



public class PhyDEDocumentDataAdapter extends ListBasedDocumentDataAdapter implements IOConstants {
	public static final String ALIGNMENT_ID = "Alignment0";
	
	
	private Document document;
	
	
	public PhyDEDocumentDataAdapter(Document document) {
		super();
		this.document = document;
		Set<String> ids = document.idSet();
		for (String id : ids) {
			getMatrices().add(new PhyDEAlignmentDataAdapter(document.getAlignmentModel(id)));
		}
	}
	
	
	public PhyDEDocumentDataAdapter(Set<PhyDE2AlignmentModel> models) {
		super();
		String idPrefix;
		Integer i = 0;
		for (PhyDE2AlignmentModel model : models) {
			idPrefix = ReadWriteConstants.DEFAULT_SEQUENCE_SET_ID_PREFIX + i;
			getMatrices().add(new PhyDEAlignmentDataAdapter(model, idPrefix));
			i++;
		}
	}

	
	public Document getDocument() {
		return document;
	}


	public void writeMetadata(ReadWriteParameterMap parameters, JPhyloIOEventReceiver receiver) throws IOException {
		receiver.add(new CommentEvent(" This document contains information specific for " + Main.APPLICATION_NAME + 
				" and should not be edited by hand or with other software. If unsupported data is added, it may get lost when "
				+ "the file is processed by " + Main.APPLICATION_NAME + " the next time."));
		JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, ReadWriteConstants.DEFAULT_META_ID_PREFIX + "1", null, 
				PREDICATE_FORMAT_VERSION, W3CXSConstants.DATA_TYPE_TOKEN, FORMAT_VERSION);
		JPhyloIOWritingUtils.writeSimpleLiteralMetadata(receiver, ReadWriteConstants.DEFAULT_META_ID_PREFIX + "2", null, 
				PREDICATE_APPLICATION_VERSION, W3CXSConstants.DATA_TYPE_TOKEN, Main.getInstance().getVersion());
	}
}
