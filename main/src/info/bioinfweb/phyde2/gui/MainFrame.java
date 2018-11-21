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


import info.bioinfweb.commons.events.GenericEventObject;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionListener;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetArea;
import info.bioinfweb.libralign.dataarea.implementations.sequenceindex.SequenceIndexArea;
import info.bioinfweb.libralign.multiplealignments.MultipleAlignmentsContainer;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.gui.actions.ActionManagement;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.tic.SwingComponentFactory;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;



//TODO: change class header to ... extends Main for remove getInstance method

@SuppressWarnings("serial")
public class MainFrame extends JFrame { 
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	
	
	private static MainFrame firstInstance = null;
	
	
	private ActionManagement actionManagement = new ActionManagement(this);
	
	private JTabbedPane tabbedPane = null;
	
	private JPanel jContentPane = null;
	
	private JMenuBar mainMenu = null;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private JMenu helpMenu = null;
	private JMenu undoMenu = null;
	private JMenu redoMenu = null;
	private JPanel toolBarPanel = null;
	

	/**
	 * Create the application.
	 */
	private MainFrame() {
		super();
		initialize();
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
	

	public void addDocument(Document document) {
		if (document.getAlignmentModel().getLabel() != null) {
			tabbedPane.addTab(document.getAlignmentModel().getLabel(), null, new Tab(document), null);
		}
		else {
			tabbedPane.addTab("<unnamed alignment>", null, new Tab(document), null);
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
	
	
	public Document getActiveDocument() {
		if (getActiveTab() != null) {
			return getActiveTab().getDocument();
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
			tabbedPane = new JTabbedPane();
		}
		return tabbedPane;
	}
	
	
	public void refreshWindowTitle() {
		StringBuilder title = new StringBuilder();
		title.append(Main.APPLICATION_NAME);
		title.append(" - ");
		if ((getActiveDocument() != null) && getActiveDocument().isChanged()) {
			title.append("*");
		}
		if ((getActiveDocument() != null) && getActiveDocument().getFile() != null) {
			title.append(getActiveDocument().getFile().getAbsolutePath());
		}
		else {
			title.append("Unsaved");
		}
		setTitle(title.toString());
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
				if (((SaveAction)getActionManagement().get("file.save")).handleUnsavedChanges()) {
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
	
	
	public void refreshMenue() {
		getActionManagement().refreshActionStatus();
	}
	
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());			
			
			jContentPane.add(getToolBarPanel(), BorderLayout.PAGE_START);
			// Add Swing component to GUI:
			jContentPane.add(getTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	
	private JMenuBar getMainMenu() {
		if (mainMenu == null) {
			mainMenu = new JMenuBar();
			mainMenu.add(getFileMenu());
			mainMenu.add(getEditMenu());
			mainMenu.add(getHelpMenu());
		}
		return mainMenu;
	}
	
	
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.setMnemonic('E');
			editMenu.add(getUndoMenu());
			editMenu.add(getRedoMenu());
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addSequence"));
			editMenu.add(getActionManagement().get("edit.deleteSequence"));
			editMenu.add(getActionManagement().get("edit.renameSequence"));
			editMenu.add(getActionManagement().get("edit.removeGaps"));
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addCharSet"));
			editMenu.add(getActionManagement().get("edit.deleteCharSet"));
			editMenu.addSeparator();
			editMenu.add(getActionManagement().get("edit.addcurrendCharSet"));
			editMenu.add(getActionManagement().get("edit.removecurrendCharSet"));
			editMenu.add(getActionManagement().get("edit.changecolorCharSet"));
			editMenu.add(getActionManagement().get("edit.renameCharSet"));
			editMenu.add(getActionManagement().get("edit.reverseComplement"));
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
