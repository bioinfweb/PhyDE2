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
package info.bioinfweb.phyde2.gui.dialogs;


import java.awt.Frame;
import java.io.IOException;

import info.bioinfweb.commons.io.TextReader;
import info.bioinfweb.phyde2.Main;



@SuppressWarnings("serial")
public class AboutDialog extends info.bioinfweb.commons.swing.AboutDialog {
	public static final String RESOURCES_PATH = "/";
	
	
	/**
	 * @param owner
	 */
	public AboutDialog(Frame owner) {
		super(owner, true);
		addTabs();
		setSize(700, 600);
		setTitle("About PhyDE 2");
		setLocationRelativeTo(owner);
	}
	
	
	private void addContentFromFile(String title, String path, String altURL) {
		String text;
		try {
			text = TextReader.readText(Object.class.getResource(path));
		}
		catch (IOException e) {
			text = "<html><body>Unable to read licence file. Licence is available at <a href=\"" + altURL + "\">\" + altURL + \"</a>.</body></html>";
		}
		
		addTab(title, null, "text/html", text, null);
	}
	
	
	private void addTabs() {
		addTab("General", null, "text/html", getGeneralContent(), null);
		addContentFromFile("PhyDE 2 License", "/resources/about/GPL.html", "http://bioinfweb.info/PhyDE2/License");  //TODO Add link label to panel or include link in HTML.
		addContentFromFile("Apache License", "/resources/about/ApacheLicense.html", "http://www.apache.org/licenses/LICENSE-2.0.html");
	}
	
	
//	private static String getResourcePath(String file) {
//		return AboutDialog.class.getResource(RESOURCES_PATH + file).toString();
//	}
	
	
	private String getGeneralContent() {
		return 
			"<head><link rel='stylesheet' type='text/css' href='" + //getResourcePath("Style.css") + 
				"'></head>" +
				"<body>" +
				"<h1><i>PhyDE " + Main.getInstance().getVersion().toString() + "</i></h1>" +
				"<p>Development: <a href='http://bioinfweb.info/People/Stoever'>Ben St&ouml;ver</a>, " +
						"Jonas Bohn, Sarah van Groen, Lara K&ouml;sters, " + 
				    "<a href='http://bioinfweb.info/People/Mueller'>Kai M&uuml;ller</a><br />" +
				"Website: <a href='http://bioinfweb.info/PhyDE2/'>http://bioinfweb.info/PhyDE2</a><br  />" +
				"Copyright 2017-2018 Ben St&ouml;ver, Jonas Bohn, Sarah van Groen, Lara K&ouml;sters, Kai M&uuml;ller. All rights reserved.</p>" +
				
				"<p>This program is free software: you can redistribute it and/or modify it " +
				    "under the terms of the GNU General Public License (see General Public " +
				    "License tab) as published by the Free Software Foundation, either version 3 " +
				    "of the License, or (at your option) any later version.</p>" +
				
				"<p>This program is distributed in the hope that it will be useful, but " +
				    "WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY " +
				    "or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for " +
				    "more details.</p>" +
				
				"<p>The included Apache Commons, Batic and l2fprod-common Libraries are distributed under " +
				    "Apache Public Licence (see Apache Public Licence tab).</p>" +
				
				"<p><b>The following libraries are used by <i>PhyDE 2</i>:</b></p>" +
				"<ul>" +
				  "<li><i>bioinfweb.commons.java</i> (<a href='http://commons.bioinfweb.info/Java/'>http://commons.bioinfweb.info/Java/</a>)</li>" +
				  "<li><i>JPhyloIO</i> (<a href='http://bioinfweb.info/JPhyloIO/'>http://bioinfweb.info/JPhyloIO/</a>)</li>" +
				  "<li><i>TIC</i> (<a href='http://bioinfweb.info/TIC/'>http://bioinfweb.info/TIC/</a>)</li>" +
				  "<li><i>LibrAlign</i> (<a href='http://bioinfweb.info/LibrAlign/'>http://bioinfweb.info/LibrAlign/</a>)</li>" +
				  "<li><i>Apache Commons</i> (<a href='http://commons.apache.org/'>http://commons.apache.org/</a>)</li>" +
				  "<li><i>Tango Desktop Project</i> (<a href='http://tango.freedesktop.org/'>http://tango.freedesktop.org/</a>)</li>" +
				"</ul>" +
				"</body></html>";			
	}
}
