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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import info.bioinfweb.phyde2.gui.actions.ActionManagement;



@SuppressWarnings("serial")
public class ToolBarPanel extends JPanel{
	
//	public enum ToolBarOrientation {
//		HORIZONTAL, VERTICAL;
//	}
	
	
	private MainFrame mainFrame;
//	private ToolBarOrientation orientation = ToolBarOrientation.VERTICAL;
	private JToolBar upperToolBar = null;
	private GridBagConstraints fileViewWindowGBC = new GridBagConstraints();

	public ToolBarPanel(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		setLayout(new GridBagLayout());
		
		fileViewWindowGBC.fill = GridBagConstraints.HORIZONTAL;
		fileViewWindowGBC.gridx = 0;
		fileViewWindowGBC.gridy = 1;
		fileViewWindowGBC.weightx = 1.0;
		fileViewWindowGBC.anchor = GridBagConstraints.WEST;
		add(getToolBar(), fileViewWindowGBC);
	}
	
	
	private ActionManagement getActionManagement() {
		return mainFrame.getActionManagement();
	}
	
	
	/**
	 * This method initializes fileviewWindowToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getToolBar() {
		if (upperToolBar == null) {
			upperToolBar = new JToolBar();
			upperToolBar.setFloatable(false);
			upperToolBar.setRollover(true);
			upperToolBar.add(getActionManagement().get("file.new"));
			upperToolBar.add(getActionManagement().get("file.open"));
			upperToolBar.add(getActionManagement().get("file.save"));
			upperToolBar.addSeparator();
			upperToolBar.add(getActionManagement().get("edit.undo"));
			upperToolBar.add(getActionManagement().get("edit.redo"));
			upperToolBar.addSeparator();
			upperToolBar.add(getActionManagement().get("edit.addContigAlignment"));
			upperToolBar.add(getActionManagement().get("edit.addDefaultPhyDE2Alignment"));
			upperToolBar.add(getActionManagement().get("edit.deleteAlignment"));
			upperToolBar.addSeparator();
			upperToolBar.add(getActionManagement().get("edit.addSequence"));
			upperToolBar.add(getActionManagement().get("edit.deleteSequence"));
			upperToolBar.addSeparator();
			upperToolBar.add(getActionManagement().get("edit.addCharSet"));
			upperToolBar.add(getActionManagement().get("edit.deleteCharSet"));
			upperToolBar.add(getActionManagement().get("edit.addcurrendCharSet"));
			upperToolBar.add(getActionManagement().get("edit.removecurrendCharSet"));
			
		}
		return upperToolBar;
	}
	
}
