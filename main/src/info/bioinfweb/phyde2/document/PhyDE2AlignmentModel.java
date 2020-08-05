package info.bioinfweb.phyde2.document;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.undo.UndoManager;

import info.bioinfweb.commons.swing.AccessibleUndoManager;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.AlignmentModelAdapter;
import info.bioinfweb.libralign.model.AlignmentModelListener;
import info.bioinfweb.libralign.model.events.SequenceChangeEvent;
import info.bioinfweb.libralign.model.events.SequenceRenamedEvent;
import info.bioinfweb.libralign.model.events.TokenChangeEvent;
import info.bioinfweb.libralign.model.events.TypedAlignmentModelChangeEvent;
import info.bioinfweb.libralign.model.implementations.AbstractAlignmentModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.implementations.swingundo.SwingEditFactory;
import info.bioinfweb.libralign.model.implementations.swingundo.SwingUndoAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.model.undo.EditRecorder;
import info.bioinfweb.libralign.model.undo.alignment.AlignmentModelUndoListener;
import info.bioinfweb.phyde2.document.undo.AlignmentModelEditFactory;
import info.bioinfweb.phyde2.document.undo.PhyDE2Edit;



/**
 * The main data model class of <i>PhyDE 2</i>.
 * 
 * @author Ben St&ouml;ver
 */
public abstract class PhyDE2AlignmentModel {
	public static final int UNDO_LIMIT = 50;

	
	private Document document;
	private AccessibleUndoManager undoManager;
	private boolean changed;
	private File file;  //TODO Remove, since file is now a property of Document.
	private SwingEditFactory<Character> alignmentModelEditFactory;
	private AlignmentModel<Character> alignmentModel;
	private CharSetDataModel charSetModel;
	protected Collection<PhyDE2AlignmentModelListener> listeners = new ArrayList<>();
	private EditRecorder<?, ?> editRecorder = null;
	
	
	public PhyDE2AlignmentModel(Document owner) {
		this(owner, new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)));
	}
	
	
	public PhyDE2AlignmentModel(Document owner, AlignmentModel<Character> alignmentModel) {
		this(owner, alignmentModel, new CharSetDataModel(alignmentModel));
	}
	
	
	public PhyDE2AlignmentModel(Document owner, AlignmentModel<Character> alignmentModel, CharSetDataModel charSetModel) {
		super();

		if (alignmentModel != charSetModel.getAlignmentModel()) {
			throw new IllegalArgumentException("The specified CharSetDataModel is not associated with the specified AlignmentModel.");
		}
		else {
			undoManager = new AccessibleUndoManager();
			undoManager.setLimit(UNDO_LIMIT);
			alignmentModelEditFactory = new AlignmentModelEditFactory(this);
			this.charSetModel = charSetModel;
			setAlignmentModel(alignmentModel);
			
			this.document = owner;
		}
	}
	
	
	public abstract AlignmentType getType();
	
	
	public Document getDocument() {
		return document;
	}
	

	public void executeEdit(PhyDE2Edit edit) {
		if (!getUndoManager().addEdit(edit)) {  // Must happen before execution, since undo switches not be activated otherwise.
			throw new RuntimeException("The edit could not be executed.");
		}
		edit.redo();  // actually execute
	}
	
	
	public AccessibleUndoManager getUndoManager() { 
		return editRecorder.getUndoManager();
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
	
	
	public AlignmentModel<Character> getAlignmentModel() {
		return alignmentModel;
	}
	
	
	private void setAlignmentModel(AlignmentModel<Character> alignmentModel) {
		this.alignmentModel = alignmentModel;
		editRecorder = new EditRecorder(alignmentModel);
		this.alignmentModel.addModelListener(new AlignmentModelUndoListener<Character>(editRecorder));
		//TODO: possibly register undo listeners with all data models already present now
		
		// Register changes listener to know when to ask for saving changes:
		alignmentModel.addModelListener(new AlignmentModelAdapter<Character>() {
			@Override
			public void afterSequenceChange(SequenceChangeEvent<Character> e) {
				setChanged(true);
			}

			@Override
			public void afterSequenceRenamed(SequenceRenamedEvent<Character> e) {
				setChanged(true);
			}

			@Override
			public void afterTokenChange(TokenChangeEvent<Character> e) {
				setChanged(true);
			}
		});
		//TODO The same needs to be done for the character set model as well!
	}
	
	
	public CharSetDataModel getCharSetModel() {
		return charSetModel;
	}
	
	
	public void setCharSetModel(CharSetDataModel charSetModel) {
		this.charSetModel = charSetModel;
	}


	@Override
	public String toString() {
		return getAlignmentModel().getLabel();
	}


	
	public void addAlignmentListener(PhyDE2AlignmentModelListener listener) {
		listeners.add(listener);
	}
	
	
	public void removeAlignmentListener(PhyDE2AlignmentModelListener listener) {
		listeners.remove(listener);
	}
	
	
	public EditRecorder<?, ?> getEditRecorder() {
		return editRecorder;
	}


	protected void fireAfterFileNameChanged() {
		PhyDE2AlignmentModelChangeEvent e = new PhyDE2AlignmentModelChangeEvent(this);
		for (PhyDE2AlignmentModelListener listener : new ArrayList<PhyDE2AlignmentModelListener>(listeners)) {  // Copy list to avoid possible ConcurrentModificationException
			listener.afterFileNameChanged(e);
		}
	}
	
	
	protected void fireAfterChangedFlagSet() {
		PhyDE2AlignmentModelChangeEvent e = new PhyDE2AlignmentModelChangeEvent(this);
		for (PhyDE2AlignmentModelListener listener : new ArrayList<PhyDE2AlignmentModelListener>(listeners)) {  // Copy list to avoid possible ConcurrentModificationException
			listener.afterChangedFlagSet(e);
		}
	}
}
