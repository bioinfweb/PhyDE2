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

import java.awt.Container;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class SelectContigView extends JTree{
	//private DefaultTreeModel model = new DefaultTreeModel(null);
	public SelectContigView (MainFrame mainframe) {
		super(new DefaultTreeModel(new DefaultMutableTreeNode()));
		
		DefaultTreeModel treeViewModel = mainframe.getFileContentTreeView().getModel();
		setRootVisible(false);
		
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
		int numberOfDocuments = treeViewModel.getChildCount(treeViewModel.getRoot());
			for (int i = 0; i <numberOfDocuments; i++) {
				DefaultMutableTreeNode treeViewDocumentNode = (DefaultMutableTreeNode) treeViewModel.getChild(treeViewModel.getRoot(), i);
				if (treeViewDocumentNode.getChildAt(1).getChildCount() != 0){ 
					DefaultMutableTreeNode contigViewDocumentNode = new DefaultMutableTreeNode(treeViewDocumentNode.getUserObject());
					contigViewDocumentNode.add(new DefaultMutableTreeNode(((DefaultMutableTreeNode) treeViewDocumentNode.getChildAt(1)).getUserObject()));
						for (int j = 0; j <treeViewDocumentNode.getChildAt(1).getChildCount(); j++) {
							((DefaultMutableTreeNode) contigViewDocumentNode.getChildAt(0)).add(new DefaultMutableTreeNode(((DefaultMutableTreeNode) treeViewDocumentNode.getChildAt(1).getChildAt(j)).getUserObject()));
						}		
				root.add(contigViewDocumentNode);	
				}
			} 
		model.setRoot(root);	
		setModel(model);
	}
	
}
