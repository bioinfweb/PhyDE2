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

import info.bioinfweb.commons.events.GenericEventObject;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionListener;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.alignmentarea.tokenpainter.NucleotideTokenPainter;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetArea;
import info.bioinfweb.libralign.dataarea.implementations.sequenceindex.SequenceIndexArea;
import info.bioinfweb.libralign.multiplealignments.MultipleAlignmentsContainer;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.gui.actions.ActionManagement;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.tic.SwingComponentFactory;



//TODO: change class header to ... extends Main for remove getInstance method

@SuppressWarnings("serial")
public class MainFrame extends JFrame { 
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	
	private static MainFrame firstInstance = null;
	
	private Document document = new Document();
	
	private ActionManagement actionManagement = new ActionManagement(this);
	
	private JPanel jContentPane = null;
	
	private JMenuBar mainMenu = null;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private JMenu helpMenu = null;
	private JMenu undoMenu = null;
	private JMenu redoMenu = null;
	private JPanel toolBarPanel = null;
	
	// Alignment views:
	private AlignmentArea mainArea = null;
	private MultipleAlignmentsContainer container = null;
	private AlignmentArea sequenceIndexAlignmentArea = null;
	private AlignmentArea characterSetAlignmentArea = null;
	private CharSetArea charSetArea = null;
	

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
	

	public void setDocument(Document document) {
		this.document = document;
		mainArea.setAlignmentModel(document.getAlignmentModel(), true);
		charSetArea.setModel(document.getCharSetModel(), true);
		getActionManagement().refreshActionStatus();
	}


	public Document getDocument() {
		return document;
	}


	public CharSetArea getCharSetArea() {
		return charSetArea;
	}
	
	
	public AlignmentArea getAlignmentArea() {
		return mainArea;
	}

	
	public ActionManagement getActionManagement() {
		return actionManagement;
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
	
	
	public void refreshWindowTitle() {
		StringBuilder title = new StringBuilder();
		title.append(Main.APPLICATION_NAME);
		title.append(" - ");
		if (getDocument().isChanged()) {
			title.append("*");
		}
		if (getDocument().getFile() != null) {
			title.append(getDocument().getFile().getAbsolutePath());
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
		setExtendedState(JFrame.NORMAL);//MAXIMIZE_BOTH

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
		
		// Create main container instance (TIC component):
		container = new MultipleAlignmentsContainer();
		
		// out head and main AlignmentArea in container:
		sequenceIndexAlignmentArea = new AlignmentArea(container);
		characterSetAlignmentArea = new AlignmentArea(container);
		mainArea = new AlignmentArea(container);
		charSetArea = new CharSetArea(characterSetAlignmentArea.getContentArea(), mainArea, getDocument().getCharSetModel());
		charSetArea.getSelectionListeners().add(new SelectionListener<GenericEventObject<CharSetArea>>() {
			@Override
			public void selectionChanged(GenericEventObject<CharSetArea> event) {
				getActionManagement().refreshActionStatus();
			}
		});
		
		// Prepare heading area:
		sequenceIndexAlignmentArea.getDataAreas().getTopAreas().add(new SequenceIndexArea(sequenceIndexAlignmentArea.getContentArea(), mainArea));
		characterSetAlignmentArea.getDataAreas().getTopAreas().add(charSetArea);
		
		container.getAlignmentAreas().add(sequenceIndexAlignmentArea);
		container.getAlignmentAreas().add(characterSetAlignmentArea);
//		container.getAlignmentAreas().add(mainArea);  //TODO Why have sequence index and character set areas no width if the main area is added here already? 
		
		// Prepare main area:
		mainArea.setAlignmentModel(getDocument().getAlignmentModel(), false);  //TODO The underlying model should not be passed here anymore, as soon as the problem of displying its contents is solved.
		mainArea.getPaintSettings().getTokenPainterList().set(0, new NucleotideTokenPainter());  // Define how sequences shall be painted
		mainArea.getSelection().addSelectionListener(new SelectionListener<GenericEventObject<SelectionModel>>() {
			@Override
			public void selectionChanged(GenericEventObject<SelectionModel> event) {
				getActionManagement().refreshActionStatus();
			}
		});
		
		container.getAlignmentAreas().add(mainArea);
		
		// Create Swing-specific component from TIC component:
		JComponent swingContainer = SwingComponentFactory.getInstance().getSwingComponent(container);
			
		setJMenuBar(getMainMenu());
		
		// Create Swing-specific component from TIC component:
		swingContainer = SwingComponentFactory.getInstance().getSwingComponent(container);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getToolBarPanel(), BorderLayout.PAGE_START);
		// Add Swing component to GUI:
		getContentPane().add(swingContainer, BorderLayout.CENTER);
		
		refreshMenue();
	}
	
	
	public void refreshMenue() {
		getActionManagement().refreshActionStatus();
	}
	
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
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
		}
		return editMenu;
	}
	
	
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
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
