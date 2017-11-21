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

import info.bioinfweb.commons.ProgramMainClass;
import info.bioinfweb.commons.appversion.ApplicationType;
import info.bioinfweb.commons.appversion.ApplicationVersion;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.phyde2.gui.MainFrame;



public class Main extends ProgramMainClass {
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	public static final String APPLICATION_NAME = "PhyDE 2"; 
	public static final String APPLICATION_VERSION = "0.0.0";
	public static final String APPLICATION_URL = "http://bioinfweb.info/PhyDE2";
	//public static final String ICON_PATH = "/resources/symbols/PHyDE216.png";
	public static final String ICON_PATH = "/resources/symbols/phyde16.png";
	
	private static Main firstInstance = null;
	
	//TODO Error report, problem with import of and create URL
	//private SwingErrorReporter errorReporter;
	//public static final String PHYDE2_URL = "http://phyde2.bioinfweb.info/";
	//public static final String ERROR_URL = PHYDE2_URL + "errorreport/ApplicationReport.jsp";
	
	
	/**
	 * Launch the application.
	 */
	public Main(){
		super(new ApplicationVersion(0, 0, 0, 0, ApplicationType.ALPHA));
		//errorReporter = new SwingErrorReporter(ERROR_URL, getVersion());
	}
	
	
	public static Main getInstance() {
		if (firstInstance == null) {
			firstInstance = new Main();
		}
		return firstInstance;
	}
	
	public ImageIcon createImageIcon(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource(path)); 
	}
	
	private void startApplication() {
		//Thread.setDefaultUncaughtExceptionHandler(getErrorReporter());
		//System.setProperty("apple.laf.useScreenMenuBar", "true");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					//show frame
					window.setVisible(true);
					window.setIconImage(createImageIcon(ICON_PATH).getImage());
					//window.swingContainer.assignSize();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//customizeSpashScreen();
		//cmdProcessor.process(new CommandLineReader(args));
	}
	
	
	public static void main(String[] args) {
		getInstance().startApplication();	
	}
}
