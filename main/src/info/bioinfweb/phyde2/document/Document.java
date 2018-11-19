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

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import info.bioinfweb.phyde2.gui.MainFrame;



public class Document {
	private Collection<DocumentListener> listeners = new ArrayList<>();
	private File file;
	private Map<String, DefaultPhyDE2AlignmentModel> defaultPhyDE2AlignmentModelMap = new TreeMap <String, DefaultPhyDE2AlignmentModel>();
	private Map<String, SingleReadContigAlignmentModel> singleReadContigAlignmentModelMap = new TreeMap <String, SingleReadContigAlignmentModel>();
	//Wenn ich zu den Maps eine Instanz hinzufüge, muss ich das mit den Listenern verknüpfen. 
	//brauche ich noch eine Methode um die IDs der Einträge zu bekommen?
	public SingleReadContigAlignmentModel getSingleReadContigAlignmentModelFromMap(String id) {
		return singleReadContigAlignmentModelMap.get(id);
		//Gibt das Alignment mit der übergebenen ID zurück
	}


	public void setSingleReadContigAlignmentModelToMap(SingleReadContigAlignmentModel contig, String id) {
		this.singleReadContigAlignmentModelMap.put(id,contig);
		// fügt ein Alignment mit definierter ID hinzu.
	}


	public DefaultPhyDE2AlignmentModel getDefaultPhyDE2AlignmentModelFromMap(String id) {
		return defaultPhyDE2AlignmentModelMap.get(id);
		//gibt das Alignment mit der übergebenen ID zurück.
	}

	public void setDefaultPhyDE2AlignmentModelToMap(DefaultPhyDE2AlignmentModel phyDE2Alignment, String id) {
		this.defaultPhyDE2AlignmentModelMap.put(id, phyDE2Alignment);
		//fügt ein Alignment mit definierter ID hinzu.
	}

	
	public void addAlignmentModels (PhyDE2AlignmentModel model, String id)
	{
		// irgendwie muss hier noch hin, wie ich die verschiedenen models aus dem File bekomme..
		if (model instanceof DefaultPhyDE2AlignmentModel){
			//add to defaultPhyDE2AlignmentModelMap
			setDefaultPhyDE2AlignmentModelToMap((DefaultPhyDE2AlignmentModel)model, id);
		}
		
		if (model instanceof SingleReadContigAlignmentModel){
			//add to singleReadContigAlignmentModelMap
			setSingleReadContigAlignmentModelToMap((SingleReadContigAlignmentModel)model, id);
			//fireAfterContigAdded(contig) aufrufen, um Listener zu informieren, dass ein Contig hinzugefügt wurde:
			fireAfterContigAdded((SingleReadContigAlignmentModel) model);
		}
	}
	
	
	
	public void addDocumentListener(DocumentListener listener) {
		listeners.add(listener);
	}

	
	public boolean removeDocumentListener(DocumentListener listener) {
		return listeners.remove(listener);
	}
	
	
	protected void fireAfterContigAdded(SingleReadContigAlignmentModel contig) {
		DocumentChangeEvent e = new DocumentChangeEvent(this, contig);
		for (DocumentListener listener : listeners) {
			listener.afterContigAdded(e);
		}
	}
	
	protected void fireAfterDefaultPhyDE2Added(DefaultPhyDE2AlignmentModel defaultPhyDE2) {
		DocumentChangeEvent e = new DocumentChangeEvent(this, defaultPhyDE2);
		for (DocumentListener listener : listeners) {
			listener.afterDefaultPhyDE2Added(e);
		}
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		MainFrame.getInstance().refreshWindowTitle();  //TODO Replace this call by DocumentChangeEvent processing in the future.
	}

	
}
