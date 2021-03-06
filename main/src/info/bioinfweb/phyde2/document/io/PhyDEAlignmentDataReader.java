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


import info.bioinfweb.commons.appversion.ApplicationVersion;
import info.bioinfweb.commons.io.FormatVersion;
import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.events.JPhyloIOEvent;
import info.bioinfweb.jphyloio.events.meta.URIOrStringIdentifier;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.events.type.EventTopologyType;
import info.bioinfweb.jphyloio.events.type.EventType;
import info.bioinfweb.jphyloio.utils.JPhyloIOReadingUtils;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetEventReader;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramEventReaderIO;
import info.bioinfweb.libralign.model.factory.BioPolymerCharAlignmentModelFactory;
import info.bioinfweb.libralign.model.io.AlignmentDataReader;
import info.bioinfweb.libralign.pherogram.provider.BioJavaPherogramProviderByURL;



public class PhyDEAlignmentDataReader extends AlignmentDataReader implements IOConstants {
	private JPhyloIOEventReader reader;
	private FormatVersion formatVersion = null;;
	private ApplicationVersion applicationVersion = null;
	private CharSetEventReader charSetReader;
	private PherogramEventReaderIO pherogramReader;
	private AlignmentTypeDataReader alignmentTypeReader;
	private ContigReferenceDataReader contigReferenceReader;
	private ConsensusSequenceDataReader consensusSequenceReader;
	
	
	public PhyDEAlignmentDataReader(JPhyloIOEventReader reader) {
		super(reader, new BioPolymerCharAlignmentModelFactory('?', new PhyDE2CharacterStateSetChooser(),true));
		this.reader = reader;
		charSetReader = new CharSetEventReader(this, new URIOrStringIdentifier(null, PREDICATE_COLOR));
		addDataElementReader(charSetReader);
		pherogramReader = new PherogramEventReaderIO(this, BioJavaPherogramProviderByURL.getInstance());
		addDataElementReader(pherogramReader);
		alignmentTypeReader = new AlignmentTypeDataReader(this);
		addDataElementReader(alignmentTypeReader);
		contigReferenceReader = new ContigReferenceDataReader(this);
		addDataElementReader(contigReferenceReader);
		consensusSequenceReader = new ConsensusSequenceDataReader(this);
		addDataElementReader(consensusSequenceReader);
	}

	
	public FormatVersion getFormatVersion() {
		return formatVersion;
	}


	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}


	public CharSetEventReader getCharSetReader() {
		return charSetReader;
	}
	
	
	public PherogramEventReaderIO getPherogramEventReader() {
		return pherogramReader;
	}
	
	
	public AlignmentTypeDataReader getAlignmentTypeReader() {
		return alignmentTypeReader;
	}
	
	
	public ContigReferenceDataReader getContigReferenceReader() {
		return contigReferenceReader;
	}


	public ConsensusSequenceDataReader getConsensusSequenceReader() {
		return consensusSequenceReader;
	}


	@Override
	protected JPhyloIOEvent processNextEvent() throws Exception {
		if (reader.hasNextEvent()) {
			JPhyloIOEvent event = reader.peek(); 
			if (event.getType().equals(new EventType(EventContentType.LITERAL_META, EventTopologyType.START))) {
				if (PREDICATE_FORMAT_VERSION.equals(event.asLiteralMetadataEvent().getPredicate().getURI())) {
					reader.next();
					formatVersion = FormatVersion.parseFormatVersion(JPhyloIOReadingUtils.readLiteralMetadataContentAsString(reader));  //TODO Possibly catch exceptions.
				}
//				else if (PREDICATE_APPLICATION_VERSION.equals(event.asLiteralMetadataEvent().getPredicate().getURI())) {
//					reader.next();
//					applicationVersion = ApplicationVersion.parseApplicationVersion(JPhyloIOReadingUtils.readLiteralMetadataContentAsString(reader));  //TODO Possibly catch exceptions.
//					//TODO Add method to ApplicationVersion.
//				}
			}
		}
		return super.processNextEvent();
	}
}
