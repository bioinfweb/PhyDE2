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

import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.Sequence;

import info.bioinfweb.libralign.model.tokenset.TokenSet;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;

public class SingleReadContigAlignmentModel extends PhyDE2AlignmentModel {
	private Map<String, PherogramAreaModel> pherogramModelMap = new TreeMap <String, PherogramAreaModel>();
	private TokenSet<Character> consensus;
	public SingleReadContigAlignmentModel() {
		super();
		}
	
	public void addSingleRead (String id, PherogramAreaModel model){
		if ((id != null) && (model != null)){
			pherogramModelMap.put(id, model);
			getAlignmentModel().addSequence(id,id); //wo krieg ich den Sequenznamen her?
			//das AlignmentModel gehört ja zum SingleReadContigModel, deshalb muss da die SingleRead Sequenz
			//hinzugefügt werden (?). Außerdem das dazu gehörige Pherogram in die Map hier gespeichert werden.
		}
	}
	
	public PherogramAreaModel getPherogramModel (String id)
	{
		return pherogramModelMap.get(id);
	}

	public void setConsensus (TokenSet<Character> consensus){
		if (consensus != null)
		{
			this.consensus = consensus;
		}
		//soll später von Klassen aufgerufen werden, die fürs einlesen zuständig sind, da die consensus
		//in der Datei enthalten ist (?)
	}
	
	public TokenSet<Character> getConsensus (){
		return consensus;
		//soll von DefaultPhyDE2AlignmentModel benutzt werden, da das Alignment aus
		//verschiedenen Consensussequenzen bestehen kann.
	}
	}
