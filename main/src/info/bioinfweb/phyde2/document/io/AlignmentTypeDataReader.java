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
import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.libralign.model.io.AbstractDataModelEventReader;
import info.bioinfweb.libralign.model.io.AlignmentDataReader;
import info.bioinfweb.libralign.model.io.DataModelKey;

public class AlignmentTypeDataReader extends AbstractDataModelEventReader<AlignmentTypeDataModel> implements ReadWriteParameterConstants {
	private boolean isPredicate;
	private String currentAlignmentID;
	
	
	public AlignmentTypeDataReader(AlignmentDataReader mainReader) {
		super(mainReader, null);
	}


	@Override
	public void processEvent(JPhyloIOEventReader source, JPhyloIOEvent event) throws IOException {
		switch (event.getType().getContentType()) {
			case ALIGNMENT:
				if (event.getType().getTopologyType().equals(EventTopologyType.START)) {
					currentAlignmentID = event.asLabeledIDEvent().getID();
				}
				else if (event.getType().getTopologyType().equals(EventTopologyType.END)) {
					currentAlignmentID = null;
				}
				break;
			case LITERAL_META:
				if (event.getType().getTopologyType().equals(EventTopologyType.START)) {
					if (PREDICATE_ALIGNMENT_TYPE.equals(event.asLiteralMetadataEvent().getPredicate().getURI())) {
						isPredicate = true;
					}
					else {
						isPredicate = false;
					}
				}
				break;
			case LITERAL_META_CONTENT:
				if (isPredicate && (currentAlignmentID != null)) {
					Object value = event.asLiteralMetadataContentEvent().getObjectValue();
					getCompletedModels().put(new DataModelKey(currentAlignmentID), new AlignmentTypeDataModel((String) value));
				}
				break;
			default:
				break;
		}
	}
}
