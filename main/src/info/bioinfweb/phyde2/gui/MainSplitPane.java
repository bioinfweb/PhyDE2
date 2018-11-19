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

import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class MainSplitPane extends JFrame{
	private JSplitPane jSplitPane;
	private MainFrame frame;
	private FileContentTreeView tree;
	
	public MainSplitPane(MainFrame frame, FileContentTreeView tree ) {
		super();
		this.frame = frame;
		this.tree = tree;
		initialize();
	}
	
	private void initialize (){
		getJSplitPane().setRightComponent(frame.getInstance().getContentPane());
		getJSplitPane().setLeftComponent(tree.getTree());
	}
	
	private JSplitPane getJSplitPane ()
	{
		if (jSplitPane == null)
		{
			jSplitPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT);
		}
		return jSplitPane;
	}
	
}
