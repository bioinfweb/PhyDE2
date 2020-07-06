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


import info.bioinfweb.commons.collections.observable.ObservableList;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetArea;
import info.bioinfweb.libralign.editsettings.EditSettings;
import info.bioinfweb.libralign.pherogram.PherogramFormats;
import info.bioinfweb.libralign.pherogram.view.PherogramView;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.document.ContigReferenceChangeEvent;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.PherogramReferenceChangeEvent;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelListener;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.actions.ActionManagement;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.tic.SwingComponentFactory;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;



//TODO: change class header to ... extends Main for remove getInstance method

@SuppressWarnings("serial")
public class MainFrame extends JFrame { 
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	
	
	private static MainFrame firstInstance = null;
	
	private ObservableList<Document> documentList = new ObservableList<>(new ArrayList<>());
	private ActionManagement actionManagement = new ActionManagement(this);
	
	private JSplitPane splitPane = null;
	private JSplitPane contentSplitPane = null;
	private PherogramView pherogramView = null;
	public ClosableJTabbedPane tabbedPane = null;
	private JPanel jContentPane = null;  // ?
	
	private JMenuBar mainMenu = null;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private JMenu helpMenu = null;
	private JMenu viewMenu = null;
	private JMenu undoMenu = null;
	private JMenu redoMenu = null;
	private JPanel toolBarPanel = null;
	private FileContentTreeView treeView = null;
	
	private EditSettings editSettings = new EditSettings();
	private PherogramFormats pherogramFortmats = new PherogramFormats();

	
	/**
	 * Create the application.
	 */
	private MainFrame() {
		super();
		initialize();
	}
	
	
	public EditSettings getEditSettings (){
		return editSettings;
	}
	
	
	public PherogramFormats getPherogramFormats(){
		return pherogramFortmats;
	}
	
	
	public static MainFrame getInstance() {
		if (firstInstance == null) {
			firstInstance = new MainFrame();
		}
		return firstInstance;
	}
	
	
	public ActionManagement getActionManagement() {
		return actionManagement;
	}
	

	public ObservableList<Document> getDocumentList() {
		return documentList;
	}
	
	
	public void hideAlignment (PhyDE2AlignmentModel alignmentModel){
		if (tabbedPane.tabByAlignment(alignmentModel) != null){
			getTabbedPane().remove(tabbedPane.tabByAlignment(alignmentModel));
		}
	}
	
	
	public void showAlignment(PhyDE2AlignmentModel phyDE2model) {
		Tab newTab = null;
		String tabTitle;
		
		// Check if document is already present in a tab

		if (tabbedPane.tabByAlignment(phyDE2model) != null) {
			getTabbedPane().setSelectedComponent(tabbedPane.tabByAlignment(phyDE2model));
		}
		else {
			phyDE2model.addAlignmentListener(new PhyDE2AlignmentModelListener() {
				@Override
				public void afterFileNameChanged(PhyDE2AlignmentModelChangeEvent e) {
					refreshWindowTitle();
					refreshTabTitle();
				}

				@Override
				public void afterChangedFlagSet(PhyDE2AlignmentModelChangeEvent e) {
					refreshWindowTitle();
					refreshTabTitle();
				}

				@Override
				public void afterPherogramAddedOrDeleted(PherogramReferenceChangeEvent e) {}

				@Override
				public void afterContigReferenceAddedOrDeleted(ContigReferenceChangeEvent e) {}
			});
			
			
			if (phyDE2model instanceof SingleReadContigAlignmentModel) {
				newTab = new ContigTab((SingleReadContigAlignmentModel)phyDE2model);	
			}
			else {
				newTab = new Tab(phyDE2model);
			}
			
			tabTitle = phyDE2model.getAlignmentModel().getLabel();
			
			tabbedPane.addTab(tabTitle, null, newTab, null);
		}
	}


	private Tab getActiveTab() {
		if (tabbedPane.getSelectedComponent() instanceof Tab) {
			return ((Tab)tabbedPane.getSelectedComponent());
		}
		else {
			return null;
		}
	}
	
	
	public void removeTab() {
		int i = tabbedPane.getSelectedIndex();
		tabbedPane.removeTabAt(i);
	}
	
	
	public void setActiveTabTitleTip(String title, String tooltip) {
		tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), title);
		tabbedPane.setToolTipTextAt(tabbedPane.getSelectedIndex(), tooltip);
		tabbedPane.addCloseButton(title);
	}
	
	
	public String getActiveTabTitle() {
		return tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
	}
	
	
	public PhyDE2AlignmentModel getActiveAlignment() {
		if (getActiveTab() != null) {
			return getActiveTab().getAlignmentModel();
		}
		else {
			return null;
		}
	}
	
	
	public CharSetArea getActiveCharSetArea() {
		if (getActiveTab() != null) {
			return getActiveTab().getCharSetArea();
		}
		else {
			return null;
		}
	}
	
	
	public AlignmentArea getActiveAlignmentArea() {
		if (getActiveTab() != null) {
			return getActiveTab().getAlignmentArea();
		}
		else {
			return null;
		}
	}
	
	
	public void refreshWindowTitle() {
		StringBuilder title = new StringBuilder();
		title.append(Main.APPLICATION_NAME);
		if (!documentList.isEmpty()) {
			if (getActiveAlignment() != null) {
				title.append(" - ");
			}
			if ((getActiveAlignment() != null) && getActiveAlignment().isChanged() && (!getActiveTabTitle().contains("*") || getActiveAlignment().getFile() != null)) {
				title.append("*");
			}
			if ((getActiveAlignment() != null) && getActiveAlignment().getFile() != null) {
				title.append(getActiveAlignment().getFile().getAbsolutePath());
			}
			else if (getActiveAlignment() != null) {
					title.append(getActiveTabTitle());
			}
		}
		setTitle(title.toString());
	}
	
	
	public void refreshTabTitle() {
		StringBuilder title = new StringBuilder();
		StringBuilder tip = new StringBuilder();
		if ((getActiveAlignment() != null) && getActiveAlignment().isChanged()) {
			title.append("*");
		}
		if ((getActiveAlignment() != null) && getActiveAlignment().getFile() != null) {
			title.append(getActiveAlignment().getFile().getName());
			tip.append(getActiveAlignment().getFile().getAbsolutePath());
		}
		else {
			title.append(getActiveTabTitle());
		}
		setActiveTabTitleTip(title.toString(), tip.toString());
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(200, 200, 550, 500);
		setExtendedState(JFrame.NORMAL);  //MAXIMIZE_BOTH

		setJMenuBar(getMainMenu());
		setContentPane(getJContentPane());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i;
				int closeOption = 0;
				boolean close = false;
				for(i = 0; i < tabbedPane.getTabCount(); i = 0) {
					if (closeOption == 1) {
//						close = ((SaveAction)getActionManagement().get("file.save")).save();
					}
					else if (closeOption == 3) {
						removeTab();
						close = true;
					}
					else if (closeOption != 4) {
						tabbedPane.setSelectedIndex(i);
						closeOption = ((SaveAction)getActionManagement().get("file.save")).handleUnsavedChanges();
						if (closeOption == 2) {
							removeTab();
							close = true;
						}
					}
					else {
						break;
					}
				}
				if (tabbedPane.getSelectedComponent() == null) {
					close = true;
				}
				if (close) {
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);  // Workaround since AboutDialog hinders application termination for a currently unknown reason.
			}
		});
		refreshWindowTitle();
		
		refreshMenue();
	}
	
	
	/**
	 * This method initializes toolBarPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getToolBarPanel() {
		if (toolBarPanel == null) {
			toolBarPanel = new ToolBarPanel(this);
		}
		return toolBarPanel;
	}
	
	
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new ClosableJTabbedPane();
			tabbedPane.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent changeEvent) {
					refreshWindowTitle();
					refreshMenue();
				}
			});
		}
		return tabbedPane;
	}
	
	
	@Override
	public void setVisible(boolean flag) {
		super.setVisible(flag);
		getSplitPane().setDividerLocation(0.3);
		getContentSplitPane().setDividerLocation(0.7);
	}


	public void refreshMenue() {
		getActionManagement().refreshActionStatus();
	}
	
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			
			jContentPane.add(getToolBarPanel(), BorderLayout.NORTH);
			jContentPane.add(getSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	
	public FileContentTreeView getFileContentTreeView () {
		if (treeView == null) {
			treeView = new FileContentTreeView(this);
			treeView.setLayout(new BorderLayout());
			treeView.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					refreshMenue();
				}
			});
		}
		return treeView;
	}
	
	
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setLeftComponent(new JScrollPane(getFileContentTreeView()));
			splitPane.setRightComponent(getContentSplitPane());
			splitPane.setResizeWeight(0.1);
		}
		return splitPane;
	}
	
	
	private JSplitPane getContentSplitPane(){
		if (contentSplitPane == null){
			contentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			contentSplitPane.setTopComponent(getTabbedPane());
			contentSplitPane.setBottomComponent(SwingComponentFactory.getInstance().getSwingComponent(getPherogramView()));
			contentSplitPane.setResizeWeight(0.9);
		}
		return contentSplitPane;
	}
	
	
	public PherogramView getPherogramView(){
		if (pherogramView == null){
			pherogramView = new PherogramView();
			pherogramView.assignSize();
		}
		return pherogramView;
	}
	
	
	public PhyDE2AlignmentModel getSelectedAlignment() {
		if (getFileContentTreeView().getSelectionModel().getLeadSelectionPath() != null) {
			Object selectedNode = getFileContentTreeView().getSelectionModel().getLeadSelectionPath().getLastPathComponent();
			while (selectedNode != null) {
				if (selectedNode instanceof DefaultMutableTreeNode) {
					Object userObject = (((DefaultMutableTreeNode) selectedNode).getUserObject());
					if (userObject instanceof PhyDE2AlignmentModel) {
						return (PhyDE2AlignmentModel) userObject;
					}
				}
				selectedNode = ((TreeNode) selectedNode).getParent();				
			}
		}
		return null;
	}
	
	
	public PherogramReference getSelectedPherogram() {
		if (getFileContentTreeView().getSelectionModel().getLeadSelectionPath() != null) {
			Object selectedNode= getFileContentTreeView().getSelectionModel().getLeadSelectionPath().getLastPathComponent();
			if(selectedNode instanceof DefaultMutableTreeNode){
				Object userObject = (((DefaultMutableTreeNode) selectedNode).getUserObject());
				if(userObject instanceof PherogramReference){
					return (PherogramReference) userObject;
				}
			}
		}
		return null;
	}
	
	
	public Document getSelectedDocument() {
		if (getFileContentTreeView().getSelectionModel().getLeadSelectionPath() != null) {
			Object selectedNode = getFileContentTreeView().getSelectionModel().getLeadSelectionPath().getLastPathComponent();
			while (selectedNode != null) {
				if (selectedNode instanceof DefaultMutableTreeNode) {
					Object userObject = (((DefaultMutableTreeNode) selectedNode).getUserObject());
					if (userObject instanceof Document) {
						return (Document) userObject;
					}
				}
				selectedNode = ((TreeNode) selectedNode).getParent();				
			}
		}
		return null;
	}
	
	
	private JMenuBar getMainMenu() {
		if (mainMenu == null) {
			mainMenu = new JMenuBar();
			mainMenu.add(getFileMenu());
			mainMenu.add(getEditMenu());
			mainMenu.add(getViewMenu());
			mainMenu.add(getHelpMenu());
	
		}
		return mainMenu;
	}
	
	
	private JMenu getViewMenu (){
		if (viewMenu == null){
			viewMenu = new JMenu();
			viewMenu.setText("View");
			viewMenu.setMnemonic('V');
			viewMenu.add(getActionManagement().get("view.displayQualityScoresAction"));
			viewMenu.addSeparator();
			viewMenu.add(getActionManagement().get("view.switchLeftRightInsertion"));
			viewMenu.add(getActionManagement().get("view.switchInsertOverwriteAction"));
			viewMenu.add(getActionManagement().get("view.showHideBasecalllinesAction"));
			viewMenu.add(getActionManagement().get("view.showHideProbabilityValuesAction"));
		}
		return viewMenu;
	}
	
	
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.setMnemonic('E');
			editMenu.add(getUndoMenu());
			editMenu.add(getRedoMenu());
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addContigAlignment"));
			editMenu.add(getActionManagement().get("edit.addDefaultPhyDE2Alignment"));
			editMenu.add(getActionManagement().get("edit.deleteAlignment"));
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addSequence"));
			editMenu.add(getActionManagement().get("edit.deleteSequence"));
			editMenu.add(getActionManagement().get("edit.renameSequence"));
			editMenu.add(getActionManagement().get("edit.refreshConsensusSequence"));
			editMenu.add(getActionManagement().get("edit.removeGaps"));
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addCharSet"));
			editMenu.add(getActionManagement().get("edit.deleteCharSet"));
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addcurrendCharSet"));
			editMenu.add(getActionManagement().get("edit.removecurrendCharSet"));
			editMenu.add(getActionManagement().get("edit.changecolorCharSet"));
			editMenu.add(getActionManagement().get("edit.renameCharSet"));
			editMenu.add(getActionManagement().get("edit.changeCharactersStateSet"));
			editMenu.add(getActionManagement().get("edit.reverseComplement"));
			editMenu.add(getActionManagement().get("edit.reverseComplementWithPherogram"));
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.cutRightAction"));
			editMenu.add(getActionManagement().get("edit.cutLeftAction"));
		}
		return editMenu;
	}
	
	
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic('F');
			fileMenu.add(getActionManagement().get("file.new"));
			fileMenu.add(getActionManagement().get("file.open"));
			fileMenu.add(getActionManagement().get("file.closeTab"));
			fileMenu.addSeparator();
			fileMenu.add(getActionManagement().get("file.save"));
			fileMenu.add(getActionManagement().get("file.saveAs"));
			fileMenu.add(getActionManagement().get("file.export"));
		}
		return fileMenu;
	}
	
	
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.setMnemonic('H');
			helpMenu.add(getActionManagement().get("help.about"));
			helpMenu.addSeparator();
			helpMenu.add(getActionManagement().get("help.index"));
			helpMenu.add(getActionManagement().get("help.contents"));
			helpMenu.add(getActionManagement().get("help.twitter"));
			//helpMenu.setIcon(new ImageIcon(Object.class.getResource("/resources/symbols/Help16.png")));
			}
		return helpMenu;
	}

	
	/**
	 * This method initializes undoMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	public JMenu getUndoMenu() {
		if (undoMenu == null) {
			undoMenu = new JMenu();
			undoMenu.setText("Undo");
			undoMenu.setMnemonic(KeyEvent.VK_U);
			undoMenu.setIcon(new ImageIcon(Object.class.getResource("/resources/symbols/Undo16.png")));
		}
		return undoMenu;
	}


	/**
	 * This method initializes redoMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	public JMenu getRedoMenu() {
		if (redoMenu == null) {
			redoMenu = new JMenu();
			redoMenu.setText("Redo");
			redoMenu.setMnemonic(KeyEvent.VK_R);
			redoMenu.setIcon(new ImageIcon(Object.class.getResource("/resources/symbols/Redo16.png")));
		}
		return redoMenu;
	}
}
