/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben St�ver, Jonas Bohn, Kai M�ller
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
package info.bioinfweb.phyde2.document;


import info.bioinfweb.commons.collections.ListChangeType;

import java.util.Map;
import java.util.TreeMap;

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.model.AlignmentModel;



public class DefaultPhyDE2AlignmentModel extends PhyDE2AlignmentModel {
	private Map<String, SingleReadContigAlignmentModel> contigModelMap = new TreeMap<String, SingleReadContigAlignmentModel>();
	
	
	public DefaultPhyDE2AlignmentModel (Document document){
		super(document);
	}
	
	
	public DefaultPhyDE2AlignmentModel(Document owner, AlignmentModel<Character> alignmentModel, CharSetDataModel charSetModel) {
		super(owner, alignmentModel, charSetModel);
	}
	
	
	@Override
	public AlignmentType getType() {
		return AlignmentType.DEFAULT;
	}
	
	
	public void addConsensus(SingleReadContigAlignmentModel contig, String sequenceID) {
		for (int i = 0; i < contig.getConsensusModel().getSequenceLength(contig.getConsensusSequenceID()); i++) {
			this.getAlignmentModel().getUnderlyingModel().appendToken(sequenceID, contig.getConsensusModel().getTokenAt(contig.getConsensusSequenceID(), i));
		}
		
		if (!sequenceHasContig(sequenceID)){
			contigModelMap.put(sequenceID, contig);
		}
		
		fireAfterContigReferenceAddedOrDeleted(this, ListChangeType.INSERTION, sequenceID, contigModelMap.get(sequenceID));
	}
	
	
	public SingleReadContigAlignmentModel getContig (String sequenceID){
		return contigModelMap.get(sequenceID);
	}
	
	
	public void removeConsensusReference(String sequenceID){
		if (contigModelMap.remove(sequenceID) != null) {
			fireAfterContigReferenceAddedOrDeleted(this, ListChangeType.DELETION, sequenceID, contigModelMap.get(sequenceID));
		}
	}
	
	protected void fireAfterContigReferenceAddedOrDeleted(PhyDE2AlignmentModel source, ListChangeType listChangeType, String sequenceID, 
			SingleReadContigAlignmentModel contigReference) {
		
		ContigReferenceChangeEvent e = new ContigReferenceChangeEvent(source, listChangeType, sequenceID, contigReference);
		for (PhyDE2AlignmentModelListener listener : listeners){
			listener.afterContigReferenceAddedOrDeleted(e);
		}		
	}
	
	
	public boolean sequenceHasContig (String sequenceID){
		if (contigModelMap.get(sequenceID) != null){
			return true;
		}
		
		return false;
	}
}
