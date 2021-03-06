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


import info.bioinfweb.commons.IntegerIDManager;
import info.bioinfweb.jphyloio.ReadWriteConstants;
import info.bioinfweb.phyde2.document.undo.PhyDE2Edit;
import info.bioinfweb.phyde2.gui.MainFrame;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;



public class Document {
	private File file;
	private Map<String, PhyDE2AlignmentModel> alignmentModelMap = new TreeMap <String, PhyDE2AlignmentModel>();
	private IntegerIDManager idManager = new IntegerIDManager();
	private Collection<DocumentListener> listeners = new ArrayList<>();
	
	
//	public Document() {
//		addDocumentListener(new DocumentListener() {
//			@Override
//			public void afterAlignmentModelAdded(DocumentChangeEvent e) {
//				DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
//				DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
//			
//				if (e.getModel() instanceof DefaultPhyDE2AlignmentModel){
//					((DefaultMutableTreeNode)file.getChildAt(0)).add(new DefaultMutableTreeNode(e.getModel()));
//					getModel().reload(file.getChildAt(0));
//				}
//				else if (e.getModel() instanceof SingleReadContigAlignmentModel)	{
//					((DefaultMutableTreeNode)file.getChildAt(1)).add(new DefaultMutableTreeNode(e.getModel()));
//					getModel().reload(file.getChildAt(1));
//				}
//			}
//
//			@Override
//			public void afterAlignmentModelDeleted(DocumentChangeEvent e) {
//				DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
//				DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
//				
//				if (e.getModel() instanceof DefaultPhyDE2AlignmentModel){
//					for (int i = 0; i < file.getChildAt(0).getChildCount(); i++) {
//						if (e.getModel().getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
//								((DefaultMutableTreeNode)file.getChildAt(0).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
//							((DefaultMutableTreeNode) file.getChildAt(0)).remove(i);
//						}
//					}
//					getModel().reload(file.getChildAt(0));
//				}
//				else if (e.getModel() instanceof SingleReadContigAlignmentModel)	{
//					for (int i = 0; i < file.getChildAt(1).getChildCount(); i++) {
//						if (e.getModel().getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
//								((DefaultMutableTreeNode)file.getChildAt(1).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
//								((DefaultMutableTreeNode) file.getChildAt(1)).remove(i);
//						}
//					}		
//					getModel().reload(file.getChildAt(1));
//				}
//		}});
//		
//		model.addDocumentListener(new PhyDE2AlignmentModelListener() {
//			@Override
//			public void afterFileNameChanged(PhyDE2AlignmentModelChangeEvent e) {
//				//TODO Implement
//			}
//
//			@Override
//			public void afterChangedFlagSet(PhyDE2AlignmentModelChangeEvent e) {}
//		});
//	}
	
	
	public void executeEdit(PhyDE2Edit edit) {
		//if (!getUndoManager().addEdit(edit)) {  // Must happen before execution, since undo switches not be activated otherwise.
		//	throw new RuntimeException("The edit could not be executed.");
		//}
		edit.redo();  // actually execute
	}
	
	
	public File getFile() {
		return file;
	}
	

	public void setFile(File file) {
		this.file = file;
		MainFrame.getInstance().refreshWindowTitle();  //TODO Replace this call by DocumentChangeEvent processing in the future.
	}

	
	public PhyDE2AlignmentModel getAlignmentModel(String id) {
		return alignmentModelMap.get(id);
	}
	

	/**
	 * Returns a set of all alignment IDs in this document. The set is not backed by the model and changes to the
	 * returned set will not be reflected in the document.
	 * 
	 * @return a new set containing all alignment IDs that are currently in the document
	 */
	public Set<String> idSet() {
		return new TreeSet<>(alignmentModelMap.keySet());
	}
	
	
	/**
	 * Returns a set of all IDs of alignments of the specified type in this document. The set is not backed by the 
	 * model and changes to the returned set will not be reflected in the document.
	 * 
	 * @param the alignment type
	 * @return a new set containing all alignment IDs that are currently in the document
	 */
	public Set<String> idSet(Class<? extends PhyDE2AlignmentModel> modelClass) {
		Set<String> result = idSet();
		
		Iterator<String> iterator = result.iterator();
		while (iterator.hasNext()) {
			if (!modelClass.isInstance(getAlignmentModel(iterator.next()))) {
				iterator.remove();
			}
		}
		return result;
	}

	
	private String generateID() {
		return ReadWriteConstants.DEFAULT_MATRIX_ID_PREFIX + Integer.toString(idManager.createNewID());
	}
	
	
	public String generateUniqueID() {
		String result = generateID();
		while (alignmentModelMap.containsKey(result)) {
			result = generateID();
		}
		return result;
	}
	

	public void addAlignmentModel(PhyDE2AlignmentModel model) {
		String id = model.getAlignmentModel().getID();
		if (id == null) {
			throw new IllegalArgumentException("The ID of the specified alignment model must not be null");
		}
		else if (alignmentModelMap.get(id) != null) {
			throw new IllegalArgumentException("The specified alignment model ID (\"" + id + "\") is already present in the document.");
		}
		else {
			alignmentModelMap.put(id, model);
			fireAfterAlignmentModelAdded(model);
		}
	}
	
	
	public PhyDE2AlignmentModel deleteAlignmentModel(String id) {
		PhyDE2AlignmentModel model = alignmentModelMap.remove(id);
		if (model != null) {
			fireAfterAlignmentModelDeleted(model);	
		}
		return model;
	}
	
	
	public void addDocumentListener(DocumentListener listener) {
		listeners.add(listener);
	}

	
	public boolean removeDocumentListener(DocumentListener listener) {
		return listeners.remove(listener);
	}
	
	
	protected void fireAfterAlignmentModelAdded(PhyDE2AlignmentModel model) {
		DocumentChangeEvent e = new DocumentChangeEvent(this, model);
		for (DocumentListener listener : listeners) {
			listener.afterAlignmentModelAdded(e);
		}
	}
	
	protected void fireAfterAlignmentModelDeleted(PhyDE2AlignmentModel model) {
		DocumentChangeEvent e = new DocumentChangeEvent(this, model);
		for (DocumentListener listener : listeners) {
			listener.afterAlignmentModelDeleted(e);
		}
	}


	@Override
	public String toString() {
		if (getFile() != null) {
			return getFile().getName();
		}
		else {
			return "Unsaved document";
		}
	}
}
