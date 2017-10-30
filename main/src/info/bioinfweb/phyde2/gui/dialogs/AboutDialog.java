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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import info.bioinfweb.commons.io.TextReader;
import info.bioinfweb.phyde2.Main;



@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
	public static final String RESOURCES_PATH = "/";
	private JPanel jContentPane = null;
	private JEditorPane generalEditorPane = null;
	private JScrollPane gplScrollPane = null;
	private JScrollPane apacheScrollPane = null;
	private JEditorPane gplEditorPane = null;
	private JEditorPane apacheTextArea = null;
	private JScrollPane generalScrollPane = null;
	private JTabbedPane contentsTabbedPane = null;
	private JPanel buttonPanel = null;
	private JButton closeButton = null;

	private JPanel generalPanel = null;
	private JPanel gplPanel = null;
	private JPanel apachePanel = null;
	
	
	/**
	 * @param owner
	 */
	public AboutDialog(Frame owner) {
		super(owner, true);
		initialize();
		setLocationRelativeTo(owner);
	}
	
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setSize(700, 600);
		setContentPane(getJContentPane());
		setTitle("About PhDE 2");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			jContentPane.add(getContentsTabbedPane(), null);
			jContentPane.add(getButtonPanel(), null);
		}
		return jContentPane;
	}
	
	
	private JTabbedPane getContentsTabbedPane() {
		if (contentsTabbedPane == null) {
			contentsTabbedPane = new JTabbedPane();
			contentsTabbedPane.addTab("Info", getGeneralPanel());
			contentsTabbedPane.addTab("General public lincence", getGPLPanel());
			contentsTabbedPane.addTab("Apache licence", getApachePanel());
		}
		return contentsTabbedPane;
	}
	
	
	/**
	 * This method initializes apachePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getApachePanel() {
		if (apachePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			apachePanel = new JPanel();
			apachePanel.setLayout(new GridBagLayout());
			apachePanel.add(getApacheScrollPane(), gridBagConstraints1);
		}
		return apachePanel;
	}

	
	/**
	 * This method initializes apacheScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getApacheScrollPane() {
		if (apacheScrollPane == null) {
			apacheScrollPane = new JScrollPane();
			apacheScrollPane.setViewportView(getApacheTextArea());
		}
		return apacheScrollPane;
	}
	
	
	/**
	 * This method initializes apacheTextArea	
	 * @param apacheTextArea 
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JEditorPane getApacheTextArea() {
		if (apacheTextArea == null) {
			String text;
			try {
				text = TextReader.readText(Object.class.getResource("/APACHE-LICENSE.txt"));
			}
			catch (IOException e) {
				text = "<html><body>Unable to read licence file. Licence is available at <a href=\"http://xmlgraphics.apache.org/batik/license.html\">http://xmlgraphics.apache.org/batik/license.html</a>.</body></html>";
			}
			
			apacheTextArea = new JEditorPane("text/text", text);
			apacheTextArea.setCaretPosition(0);
			apacheTextArea.setEnabled(false);
			apacheTextArea.setBackground(SystemColor.text);
			apacheTextArea.setDisabledTextColor(SystemColor.textText);
		}
		return apacheTextArea;
	}
	
	
	private JPanel getGPLPanel() {
		if (gplPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.weightx = 1.0;
			gplPanel = new JPanel();
			gplPanel.setLayout(new GridBagLayout());
			gplPanel.add(getGplScrollPane(), gridBagConstraints2);
		}
		return gplPanel;
	}

	
	private JScrollPane getGplScrollPane() {
		if (gplScrollPane == null) {
			gplScrollPane = new JScrollPane();
			gplScrollPane.setViewportView(getGplEditorPane());
		}
		return gplScrollPane;
	}


	/**
	 * This method initializes gplTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JEditorPane getGplEditorPane() {
		if (gplEditorPane == null) {
			String text;
			try {
				text = TextReader.readText(Object.class.getResource("/GPL.html"));
			}
			catch (IOException e) {
				text = "<html>Unable to read license file. Licence is available at " +
						"<a href=\"http://phyde2.web-insel.info/License\">" +
						"http://phyde2.bioinfweb.info/License</a>.</html>";
			}
			gplEditorPane = new JEditorPane();
			gplEditorPane.setContentType("text/html");
			gplEditorPane.setText(text);
			gplEditorPane.setCaretPosition(0);
			gplEditorPane.setEditable(false);
			//gplEditorPane.addHyperlinkListener(HYPERLINK_LISTENER);
		}
		return gplEditorPane;
	}

	
	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.weightx = 1.0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getCloseButton(), gridBagConstraints3);
		}
		return buttonPanel;
	}
	
	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return closeButton;
	}
	
	/**
	 * This method initializes generalPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getGeneralPanel() {
		if (generalPanel == null) {
			generalPanel = new JPanel();
			generalPanel.setLayout(new BoxLayout(getGeneralPanel(), BoxLayout.Y_AXIS));
			generalPanel.add(getGeneralScrollPane(), null);
		}
		return generalPanel;
	}
	
	private JScrollPane getGeneralScrollPane() {
		if (generalScrollPane == null) {
			generalScrollPane = new JScrollPane();
			generalScrollPane.setViewportView(getGeneralEditorPane());
		}
		return generalScrollPane;
	}
	
	
//	private static String getResourcePath(String file) {
//		return AboutDialog.class.getResource(RESOURCES_PATH + file).toString();
//	}
	
	private JEditorPane getGeneralEditorPane() {
		if (generalEditorPane == null) {
			generalEditorPane = new JEditorPane();
			generalEditorPane.setContentType("text/html");
			generalEditorPane.setText("<html>" +
					"<head><link rel='stylesheet' type='text/css' href='" + //getResourcePath("Style.css") + 
					"'></head>" +
					"<body>" +
					"<h1><i>PhyDE " + Main.getInstance().getVersion().toString() + "</i></h1>" +
					"<p>Development: <a href='http://bioinfweb.info/People/Stoever'>Ben St&ouml;ver</a>, " +
							"<a href='http://bioinfweb.info/People/Bohn'>Jonas Bohn</a>, " + 
					    "<a href='http://bioinfweb.info/People/Mueller'>Kai M&uuml;ller</a><br />" +
					"Website: <a href='http://phyde2.bioinfweb.info/'>Currently not available.</a><br  />" +
					"Copyright 2017 Ben St&ouml;ver, Jonas Bohn, Kai M&uuml;ller. All rights reserved.</p>" +
					
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
					  "<li><i>Apache Commons</i> (<a href='http://commons.apache.org/'>http://commons.apache.org/</a>)</li>" +
					  "<li><i>Tango Desktop Project</i> (<a href='http://tango.freedesktop.org/'>http://tango.freedesktop.org/</a>)</li>" +
					"</ul>" +
					"</body></html>");			
			generalEditorPane.setCaretPosition(0);
			generalEditorPane.setEditable(false);
			//generalEditorPane.addHyperlinkListener(HYPERLINK_LISTENER);
		}
		return generalEditorPane;
	}
}
