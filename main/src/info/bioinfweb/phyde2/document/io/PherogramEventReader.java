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
import java.net.URL;

import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;

import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;
import info.bioinfweb.jphyloio.events.meta.URIOrStringIdentifier;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.libralign.model.io.AbstractDataElementEventReader;
import info.bioinfweb.libralign.model.io.AlignmentDataReader;
import info.bioinfweb.libralign.model.io.DataElementKey;
import info.bioinfweb.phyde2.document.PherogramReference;



public class PherogramEventReader extends AbstractDataElementEventReader<PherogramReference> {
	private URIOrStringIdentifier predicate;
	private String currentSequenceID = null;
	private String currentAlignmentID = null;
	private int leftCutPosition;
	private int rightCutPosition;
	private URL pherogramURL;
	private boolean isSingleRead;
	
	
	public PherogramEventReader(AlignmentDataReader mainReader) {
		super(mainReader, null);
	}
	
	
	private void getPherogramAreaModel() {
		if (pherogramURL != null) {
			DataElementKey key = new DataElementKey(currentAlignmentID, currentSequenceID);
			try {
				PherogramReference model = new PherogramReference(
						getMainReader().getAlignmentModelReader().getModelByJPhyloIOID(key.getAlignmentID()), 
						pherogramURL, currentSequenceID);  //TODO Reference to the parent alignment model is required here.

				if (leftCutPosition != -1) {
					model.setLeftCutPosition(leftCutPosition);
				}
				if (rightCutPosition != -1) {
					model.setRightCutPosition(rightCutPosition);				
				}
				
				getCompletedElements().put(key, model);
			} 
			catch (UnsupportedChromatogramFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public void processEvent(JPhyloIOEventReader source, JPhyloIOEvent event) throws IOException {
		switch (event.getType().getContentType()) {
			case ALIGNMENT:
				if (event.getType().getTopologyType().equals(EventTopologyType.START)) {
					currentAlignmentID = event.asLabeledIDEvent().getID();
				}
				break;
			case SEQUENCE:
				if (event.getType().getTopologyType().equals(EventTopologyType.START)) {
					currentSequenceID = event.asLabeledIDEvent().getID();
				}
				else if (event.getType().getTopologyType().equals(EventTopologyType.END)) {
					getPherogramAreaModel();
					pherogramURL = null;
					leftCutPosition = -1;
					rightCutPosition = -1;
				}
				break;
			case RESOURCE_META:
				if (event.getType().getTopologyType().equals(EventTopologyType.START)) {
					if (ReadWriteParameterConstants.PREDICATE_HAS_PHEROGRAM.equals(event.asResourceMetadataEvent().getRel().getURI())) {
						pherogramURL = new URL((String) event.asResourceMetadataEvent().getHRef().toString());
					}
				}
				break;
			case LITERAL_META:
				if (event.getType().getTopologyType().equals(EventTopologyType.START)) {
					predicate = event.asLiteralMetadataEvent().getPredicate();
				}
				break;
			case LITERAL_META_CONTENT:
				Object value = event.asLiteralMetadataContentEvent().getObjectValue();
				if (ReadWriteParameterConstants.PREDICATE_IS_SINGLE_READ.equals(predicate.getURI())) {
					isSingleRead = (boolean) value;
				}
				else if (ReadWriteParameterConstants.PREDICATE_IS_REVERSE_COMPLEMENTED.equals(predicate.getURI())) {
					
				}
				else if (ReadWriteParameterConstants.PREDICATE_HAS_LEFT_CUT_POSITION.equals(predicate.getURI())) {
					leftCutPosition = (int) value;
				}
				else if (ReadWriteParameterConstants.PREDICATE_HAS_RIGHT_CUT_POSITION.equals(predicate.getURI())) {
					rightCutPosition = (int) value;
				}
			default:
				break;
		}
	}
}
