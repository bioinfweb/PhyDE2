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
package info.bioinfweb.phyde2.gui;

import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

public class ContigTab extends Tab {

	private AlignmentArea consensusSequenceArea = null;
			
	public ContigTab(SingleReadContigAlignmentModel document) {
		super(document);
		consensusSequenceArea = new AlignmentArea(getAlignmentsContainer());
		consensusSequenceArea.setAlignmentModel(document.getConsensusModel(), false);
		getAlignmentsContainer().getAlignmentAreas().add(consensusSequenceArea);
		consensusSequenceArea.setAllowVerticalScrolling(false);
	}

	
	@Override
	public SingleReadContigAlignmentModel getDocument() {
		return (SingleReadContigAlignmentModel)super.getDocument();
	}
	
	

}
