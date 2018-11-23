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
package info.bioinfweb.phyde2.document;

public class SingleReadContigAlignmentModel extends PhyDE2AlignmentModel {

//	TODO hier weiß ich nicht so wirklich, wie die Klasse mit dem Document kommunizieren soll. Bzw. wie 
//	ich die Info über die Anzahl an gespeicherten Alignments im File bekomme und ob das hier
//	überhaupt wichtig ist, oder im Document verarbeitet wird.
	
	public SingleReadContigAlignmentModel() {
		super();
		//TODO außerdem zusätzliches AlignemntModel für consensus
		
		//getAlignmentModel().addSequence(sequenceName)
	}

	
	//TODO Eine Funktion "getConsensus" muss hier hin, damit DefaultPhyDE2 sich das holen kann.
}
