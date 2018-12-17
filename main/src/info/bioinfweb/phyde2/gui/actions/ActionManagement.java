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
package info.bioinfweb.phyde2.gui.actions;


import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.undo.UndoableEdit;

import info.bioinfweb.commons.swing.AbstractUndoActionManagement;
import info.bioinfweb.commons.swing.AccessibleUndoManager;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.edit.AddCharSetAction;
import info.bioinfweb.phyde2.gui.actions.edit.AddColumnsToCharSetAction;
import info.bioinfweb.phyde2.gui.actions.edit.AddContigAlignmentAction;
import info.bioinfweb.phyde2.gui.actions.edit.AddDefaultPhyDE2AlignmentAction;
import info.bioinfweb.phyde2.gui.actions.edit.AddSequenceAction;
import info.bioinfweb.phyde2.gui.actions.edit.ChangeCharSetColorAction;
import info.bioinfweb.phyde2.gui.actions.edit.DeleteAlignmentAction;
import info.bioinfweb.phyde2.gui.actions.edit.DeleteCharSetAction;
import info.bioinfweb.phyde2.gui.actions.edit.DeleteSequenceAction;
import info.bioinfweb.phyde2.gui.actions.edit.RedoAction;
import info.bioinfweb.phyde2.gui.actions.edit.RedoToAction;
import info.bioinfweb.phyde2.gui.actions.edit.RefreshConsensusSequenceAction;
import info.bioinfweb.phyde2.gui.actions.edit.RemoveColumnsFromCharSetAction;
import info.bioinfweb.phyde2.gui.actions.edit.RemoveGapsAction;
import info.bioinfweb.phyde2.gui.actions.edit.RenameCharSetAction;
import info.bioinfweb.phyde2.gui.actions.edit.RenameSequenceAction;
import info.bioinfweb.phyde2.gui.actions.edit.ReverseComplementAction;
import info.bioinfweb.phyde2.gui.actions.edit.ReverseComplementWithPherogramAction;
import info.bioinfweb.phyde2.gui.actions.edit.UndoAction;
import info.bioinfweb.phyde2.gui.actions.edit.UndoToAction;
import info.bioinfweb.phyde2.gui.actions.file.CloseTabAction;
import info.bioinfweb.phyde2.gui.actions.file.ExportAction;
import info.bioinfweb.phyde2.gui.actions.file.NewAction;
import info.bioinfweb.phyde2.gui.actions.file.OpenAction;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.phyde2.gui.actions.file.SaveAsAction;
import info.bioinfweb.phyde2.gui.actions.help.AboutAction;
import info.bioinfweb.phyde2.gui.actions.help.BioinfWebMainPageAction;
import info.bioinfweb.phyde2.gui.actions.help.PhyDEMainPageAction;
import info.bioinfweb.phyde2.gui.actions.help.TwitterAction;
import info.bioinfweb.phyde2.gui.actions.view.CutLeftAction;
import info.bioinfweb.phyde2.gui.actions.view.CutRightAction;
import info.bioinfweb.phyde2.gui.actions.view.DisplayQualityScoresAction;
import info.bioinfweb.phyde2.gui.actions.view.ShowHideBasecalllinesAction;
import info.bioinfweb.phyde2.gui.actions.view.ShowHideProbabilityValuesAction;
import info.bioinfweb.phyde2.gui.actions.view.SwitchInsertOverwriteAction;
import info.bioinfweb.phyde2.gui.actions.view.SwitchLeftRightInsertionAction;




public class ActionManagement extends AbstractUndoActionManagement {
	private MainFrame mainFrame;
//	private Vector<Action> popupActions = new Vector<Action>();
//	private JPopupMenu popupMenu = new JPopupMenu();
	
	
	public ActionManagement(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		fillMap();
	}
	
	
	protected void fillMap() {
		put("file.new", new NewAction(mainFrame));
		put("file.open", new OpenAction(mainFrame));
		put("file.closeTab", new CloseTabAction(mainFrame));
		put("file.save", new SaveAction(mainFrame));
		put("file.saveAs", new SaveAsAction(mainFrame));
		put("file.export",new ExportAction(mainFrame));
		
		put("edit.undo", new UndoAction(mainFrame));
		put("edit.redo", new RedoAction(mainFrame));
		put("edit.addContigAlignment", new AddContigAlignmentAction(mainFrame));
		put("edit.addDefaultPhyDE2Alignment", new AddDefaultPhyDE2AlignmentAction(mainFrame));
		put("edit.deleteAlignment", new DeleteAlignmentAction(mainFrame));
		put("edit.addSequence", new AddSequenceAction(mainFrame));
		put("edit.refreshConsensusSequence", new RefreshConsensusSequenceAction(mainFrame));
		put("edit.deleteSequence", new DeleteSequenceAction(mainFrame));
		put("edit.renameSequence", new RenameSequenceAction(mainFrame));
		put("edit.removeGaps", new RemoveGapsAction(mainFrame));
		put("edit.addCharSet", new AddCharSetAction(mainFrame));
		put("edit.deleteCharSet", new DeleteCharSetAction(mainFrame));
		put("edit.addcurrendCharSet", new AddColumnsToCharSetAction(mainFrame));
		put("edit.removecurrendCharSet", new RemoveColumnsFromCharSetAction(mainFrame));
		put("edit.changecolorCharSet", new ChangeCharSetColorAction(mainFrame));
		put("edit.renameCharSet", new RenameCharSetAction(mainFrame));
		put("edit.reverseComplement", new ReverseComplementAction(mainFrame));
		put("edit.reverseComplementWithPherogram", new ReverseComplementWithPherogramAction(mainFrame));
		
		put("view.switchLeftRightInsertion", new SwitchLeftRightInsertionAction(mainFrame));
		put("view.switchInsertOverwriteAction", new SwitchInsertOverwriteAction (mainFrame));
		put("view.showHideBasecalllinesAction", new ShowHideBasecalllinesAction(mainFrame));
		put("view.displayQualityScoresAction", new DisplayQualityScoresAction(mainFrame));
		put("view.showHideProbabilityValuesAction", new ShowHideProbabilityValuesAction(mainFrame));
		put("view.cutRightAction", new CutRightAction(mainFrame));
		put("view.cutLeftAction", new CutLeftAction(mainFrame));
		
		// TODO add Links
		put("help.about", new AboutAction(mainFrame));
		put("help.contents", new BioinfWebMainPageAction());
		put("help.index", new PhyDEMainPageAction());
		put("help.twitter", new TwitterAction());
	}
	
	
	@Override
	protected AccessibleUndoManager getUndoManager() {
		if (mainFrame.getActiveAlignment() != null) {
			return mainFrame.getActiveAlignment().getUndoManager();
		}
		else {
			return null;
		}
	}
	

	@Override
	protected JMenu getUndoMenu() {
		return mainFrame.getUndoMenu();
	}

	
	@Override
	protected JMenu getRedoMenu() {
		return mainFrame.getRedoMenu();
	}
	

	@Override
	protected Action createUndoAction(UndoableEdit edit) {
		return new UndoToAction(mainFrame, edit);
	}

	
	@Override
	protected Action createRedoAction(UndoableEdit edit) {
		return new RedoToAction(mainFrame, edit);
	}
	
	
	private void setActionStatusBySelection(PhyDE2AlignmentModel document, MainFrame mainframe) {
		
		Iterator<Action> iterator = getMap().values().iterator();
		while (iterator.hasNext()) {
			Action action = iterator.next();
			if (action instanceof AbstractPhyDEAction) {
				((AbstractPhyDEAction)action).setEnabled(document, mainframe);
			}
		}
	}
	
	
	public void refreshActionStatus() {
		PhyDE2AlignmentModel model = mainFrame.getActiveAlignment();
		setActionStatusBySelection(model, mainFrame);
		
		if (model != null) {
			editUndoRedoMenus();
			get("edit.undo").setEnabled(mainFrame.getActiveAlignment().getUndoManager().canUndo());
			get("edit.redo").setEnabled(mainFrame.getActiveAlignment().getUndoManager().canRedo());
		}
		
//			Test:
//		get("edit.undo").setEnabled(false);
//		get("edit.redo").setEnabled(false);
//		getUndoMenu().setEnabled(false);
//		getRedoMenu().setEnabled(false);
	}
	
}
