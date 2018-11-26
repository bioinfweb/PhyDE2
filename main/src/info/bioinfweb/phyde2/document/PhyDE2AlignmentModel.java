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
import java.util.ArrayList;
import java.util.Collection;

import info.bioinfweb.commons.swing.AccessibleUndoManager;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.AlignmentModelChangeListener;
import info.bioinfweb.libralign.model.events.SequenceChangeEvent;
import info.bioinfweb.libralign.model.events.SequenceRenamedEvent;
import info.bioinfweb.libralign.model.events.TokenChangeEvent;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.implementations.swingundo.SwingEditFactory;
import info.bioinfweb.libralign.model.implementations.swingundo.SwingUndoAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.phyde2.document.undo.AlignmentModelEditFactory;
import info.bioinfweb.phyde2.document.undo.DocumentEdit;
import info.bioinfweb.phyde2.gui.MainFrame;



/**
 * The main data model class of <i>PhyDE 2</i>.
 * 
 * @author Ben St&ouml;ver
 */
public class PhyDE2AlignmentModel {
	public static final int UNDO_LIMIT = 50;
	
	
	private AccessibleUndoManager undoManager;
	
	private File file;
	private boolean changed;
	
	private SwingEditFactory<Character> alignmentModelEditFactory;
	private SwingUndoAlignmentModel<Character> undoAlignmentModel;
	private CharSetDataModel charSetModel;
	
	private Collection<PhyDE2AlignmentModelListener> listeners = new ArrayList<>();
	
	
	public PhyDE2AlignmentModel() {
		this(new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)), new CharSetDataModel());
	}
	
	
	public PhyDE2AlignmentModel(AlignmentModel<Character> alignmentModel, CharSetDataModel charSetModel) {
		super();
		
		undoManager = new AccessibleUndoManager();
		undoManager.setLimit(UNDO_LIMIT);
		alignmentModelEditFactory = new AlignmentModelEditFactory(this);
		this.charSetModel = charSetModel;
		setAlignmentModel(alignmentModel);
	}
	
	
	public void executeEdit(DocumentEdit edit) {
		if (!getUndoManager().addEdit(edit)) {  // Must happen before execution, since undo switches not be activated otherwise.
			throw new RuntimeException("The edit could not be executed.");
		}
		edit.redo();  // actually execute
	}
	
	
	public AccessibleUndoManager getUndoManager() {
		return undoManager;
	}
	
	
	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
		fireAfterFileNameChanged();
	}


	public boolean isChanged() {
		return changed;
	}


	public void setChanged(boolean changed) {
		if (this.changed != changed) {
			this.changed = changed;
			fireAfterChangedFlagSet();
		}
	}
	
	
	public SwingUndoAlignmentModel<Character> getAlignmentModel() {
		return undoAlignmentModel;
	}
	
	
	private void setAlignmentModel(AlignmentModel<Character> alignmentModel) {
		if (alignmentModel instanceof SwingUndoAlignmentModel) {
			this.undoAlignmentModel = (SwingUndoAlignmentModel<Character>)alignmentModel;
		}
		else {
			this.undoAlignmentModel = new SwingUndoAlignmentModel<Character>(alignmentModel, undoManager, alignmentModelEditFactory);
		}
		
		// Register changes listener to know when to ask for saving changes:
		undoAlignmentModel.getChangeListeners().add(new AlignmentModelChangeListener() {
			@Override
			public <T> void afterTokenChange(TokenChangeEvent<T> e) {
				setChanged(true);
			}
			
			@Override
			public <T> void afterSequenceRenamed(SequenceRenamedEvent<T> e) {
				setChanged(true);
			}
			
			@Override
			public <T> void afterSequenceChange(SequenceChangeEvent<T> e) {
				setChanged(true);
			}
			
			@Override
			public <T, U> void afterModelChanged(AlignmentModel<T> previous, AlignmentModel<U> current) {}
		});
		//TODO The same needs to be done for the character set model as well!
	}
	
	
	public CharSetDataModel getCharSetModel() {
		return charSetModel;
	}
	
	
	public void setCharSetModel(CharSetDataModel charSetModel) {
		this.charSetModel = charSetModel;
	}
	
	
	public void addDocumentListener(PhyDE2AlignmentModelListener listener) {
		listeners.add(listener);
	}
	
	
	public void removeDocumentListener(PhyDE2AlignmentModelListener listener) {
		listeners.remove(listener);
	}
	
	
	protected void fireAfterFileNameChanged() {
		PhyDE2AlignmentModelChangeEvent e = new PhyDE2AlignmentModelChangeEvent(this);
		for (PhyDE2AlignmentModelListener listener : listeners) {
			listener.afterFileNameChanged(e);
		}
	}
	
	
	protected void fireAfterChangedFlagSet() {
		PhyDE2AlignmentModelChangeEvent e = new PhyDE2AlignmentModelChangeEvent(this);
		for (PhyDE2AlignmentModelListener listener : listeners) {
			listener.afterChangedFlagSet(e);
		}
	}
}
