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
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.ContigReferenceChangeEvent;
import info.bioinfweb.phyde2.document.PherogramReferenceChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelListener;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;



public class ContigTab extends Tab {
	private AlignmentArea consensusSequenceArea = null;
			
	
	public ContigTab(SingleReadContigAlignmentModel contigAlignment) {
		super(contigAlignment);
		getAlignmentArea().setDataAreaFactory(new PhyDE2DataAreaFactory(getAlignmentArea()));
		
		consensusSequenceArea = new AlignmentArea(getAlignmentsContainer());
		consensusSequenceArea.setAlignmentModel(contigAlignment.getConsensusModel());
		getAlignmentsContainer().getAlignmentAreas().add(consensusSequenceArea);
		consensusSequenceArea.setAllowVerticalScrolling(false);
		
		//TODO Add initial pherogram areas
		
//		contigAlignment.addAlignmentListener(new PhyDE2AlignmentModelListener() {
//			@Override
//			public void afterPherogramAddedOrDeleted(PherogramReferenceChangeEvent e) {
//				PherogramAreaModel pherogramModel = e.getPherogramReference();
//				AlignmentArea alignmentArea = MainFrame.getInstance().getActiveAlignmentArea();
//
//				switch (e.getListChangeType()) {
//					case INSERTION: 	
//						if (pherogramModel != null) {
//							PherogramArea pherogramDataArea = new PherogramArea(alignmentArea, pherogramModel, MainFrame.getInstance().getPherogramFormats());
//							pherogramDataArea.addMouseListener(new PherogramMouseListener(pherogramDataArea));
//							alignmentArea.getDataAreas().getSequenceList(e.getSequenceID()).add(pherogramDataArea);
//						}
//						break;
//						
//					case DELETION:
//						if (pherogramModel != null) {
//							contigAlignment.removeAlignmentListener(this);  //TODO If a ConcurrentModificationException occurs, make sure to copy list in firing code.
//							//TODO Why is the listener for the whole alignment removed when a single pherogram is removed?
//							alignmentArea.getDataAreas().getSequenceList(e.getSequenceID()).clear();  //TODO Should this be done in LibrAlign?
//						}
//						break;
//					
//					default:
//						break;
//				}
//			}
//			
//			@Override
//			public void afterFileNameChanged(PhyDE2AlignmentModelChangeEvent e) {}
//			
//			@Override
//			public void afterChangedFlagSet(PhyDE2AlignmentModelChangeEvent e) {}
//
//			@Override
//			public void afterContigReferenceAddedOrDeleted(ContigReferenceChangeEvent e) {}
//		});
	}

	
	@Override
	public SingleReadContigAlignmentModel getAlignmentModel() {
		return (SingleReadContigAlignmentModel)super.getAlignmentModel();
	}
}
