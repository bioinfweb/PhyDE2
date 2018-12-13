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

import java.util.Map;
import java.util.TreeMap;

public class DefaultPhyDE2AlignmentModel extends PhyDE2AlignmentModel {
	private Map<String, SingleReadContigAlignmentModel> contigModelMap = new TreeMap<String, SingleReadContigAlignmentModel>();
	
	public DefaultPhyDE2AlignmentModel (Document document){
		super(document);
	}
	
	
	public void addConsensus(SingleReadContigAlignmentModel contig, String sequenceID) {
		for (int i = 0; i < contig.getConsensusModel().getSequenceLength(contig.getConsensusSequenceID()); i++) {
			this.getAlignmentModel().appendToken(sequenceID, contig.getConsensusModel().getTokenAt(contig.getConsensusSequenceID(), i));
		}
		
		if(!sequenceHasContig(sequenceID)){
		contigModelMap.put(sequenceID, contig);
		}
	}
	
	public SingleReadContigAlignmentModel getContig (String sequenceID){
		return contigModelMap.get(sequenceID);
	}
	
	public boolean sequenceHasContig (String sequenceID){
		if (contigModelMap.get(sequenceID) != null){
			return true;
		}
		
	return false;
	}
}
