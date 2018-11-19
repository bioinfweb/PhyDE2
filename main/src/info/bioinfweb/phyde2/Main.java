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
package info.bioinfweb.phyde2;


import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import info.bioinfweb.commons.ProgramMainClass;
import info.bioinfweb.commons.appversion.ApplicationType;
import info.bioinfweb.commons.appversion.ApplicationVersion;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.phyde2.gui.FileContentTreeView;
import info.bioinfweb.phyde2.gui.MainFrame;



/**
 * The main class of the <i>PhyDE 2</i> application.
 * 
 * @author Ben St&ouml;ver
 */
public class Main extends ProgramMainClass {
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	public static final String APPLICATION_NAME = "PhyDE 2"; 
	public static final String APPLICATION_URL = "http://bioinfweb.info/PhyDE2";
	public static final String ICON_PATH = "/resources/symbols/phyde_2.png";
	
	private static Main firstInstance = null;
	
	
	/**
	 * Launch the application.
	 */
	public Main(){
		super(new ApplicationVersion(0, 1, 0, 49, ApplicationType.ALPHA));
	}
	
	
	public static Main getInstance() {
		if (firstInstance == null) {
			firstInstance = new Main();
		}
		return firstInstance;
	}
	
	
	public static ImageIcon createImageIcon(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource(path)); 
	}
	
	
	private void startApplication() {
		// Set default look and feel of the current operating system:
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = MainFrame.getInstance();
					window.setVisible(true);
					window.setIconImage(createImageIcon(ICON_PATH).getImage());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void main(String[] args) {
		getInstance().startApplication();	
	}
}
