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


import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.dataadapters.ObjectListDataAdapter;
import info.bioinfweb.jphyloio.events.LinkedLabeledIDEvent;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataAdapter;
import info.bioinfweb.libralign.model.io.AlignmentModelDataAdapter;



/**
 * <i>JPhyloIO</i> document adapter for a <i>PhyDE</i> document.
 * 
 * @author Ben St&ouml;ver
 */
public class PhyDEAlignmentDataAdapter extends AlignmentModelDataAdapter<Character> {
	private PhyDEDocumentDataAdapter parent;
	
	
	public PhyDEAlignmentDataAdapter(PhyDEDocumentDataAdapter parent) {
		super("", new LinkedLabeledIDEvent(EventContentType.ALIGNMENT, PhyDEDocumentDataAdapter.ALIGNMENT_ID, null, null), 
				parent.getDocument().getAlignmentModel(), false);
		this.parent = parent;
	}

	
	@Override
	public ObjectListDataAdapter<LinkedLabeledIDEvent> getCharacterSets(ReadWriteParameterMap parameters) {
		return new CharSetDataAdapter(getIDPrefix(), parent.getDocument().getCharSetModel(), 
				PhyDEDocumentDataAdapter.ALIGNMENT_ID);
	}
}
