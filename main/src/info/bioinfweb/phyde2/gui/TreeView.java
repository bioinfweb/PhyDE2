/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben St�ver, Jonas Bohn, Kai M�ller
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
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.gui.TreeView.CheckChangeEvent;



@SuppressWarnings("serial")
public class TreeView extends JTree {
	private JTree selfPointer = this;
	
	private HashMap<TreePath, CheckedNode> nodesCheckingState = new HashMap<>();
	private HashSet<TreePath> checkedPaths = new HashSet<TreePath>();
	
	
	//public FileContentTreeView(Document document, MainFrame mainframe) {
	public TreeView(Document document, MainFrame mainframe) {
		super(new DefaultTreeModel(new DefaultMutableTreeNode()));
        
        //tree content
		setRootVisible(false);
		this.setModel(mainframe.getFileContentTreeView().getModel());
		this.expandRow(0);
		this.setToggleClickCount(0);
		setEverythingNotSelected();
		
		//get checkboxes
		CheckBoxCellRenderer cellRenderer = new CheckBoxCellRenderer();
        this.setCellRenderer(cellRenderer);
        this.setSelectionModel(null);
	}
	
	
	public boolean isAlreadySelected(TreePath tp) {
		return nodesCheckingState.get(tp).isSelected;
	}
	
	
	public void fireMouseClicked(TreePath tp) {
		boolean checkMode = !nodesCheckingState.get(tp).isSelected;
		checkSubTree(tp, checkMode);
		fireCheckChangeEvent(new CheckChangeEvent(new Object()));
		selfPointer.repaint();
	}
	
	
	public void setEverythingNotSelected() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) selfPointer.getModel().getRoot();
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			CheckedNode cn = new CheckedNode(false, node.getChildCount() > 0, false);
			nodesCheckingState.put(new TreePath(node.getPath()), cn);
		}
	}
	
	
	public class CheckedNode {
		public boolean isSelected;
		public boolean hasChildren;
		public boolean allChildrenSelected;

		public CheckedNode(boolean isSelected, boolean hasChildren, boolean allChildrenSelected) {
			this.isSelected = isSelected;
			this.hasChildren = hasChildren;
			this.allChildrenSelected = allChildrenSelected;
		}
	}
	
	
	private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {    
		JCheckBox checkBox;     
		public CheckBoxCellRenderer() {
			super();
			this.setLayout(new BorderLayout());
			checkBox = new JCheckBox();
			add(checkBox, BorderLayout.CENTER);
			setOpaque(false);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			TreePath tp = new TreePath(node.getPath());
			CheckedNode cn = nodesCheckingState.get(tp);
			checkBox.setText(((DefaultMutableTreeNode)value).toString());
			if (cn == null) {
				return this;
			}
			checkBox.setSelected(cn.isSelected);
			checkBox.setOpaque(cn.isSelected && cn.hasChildren && ! cn.allChildrenSelected);
			return this;
		}     
	}
	
	
	// Defining a new event type for the checking mechanism and preparing event-handling mechanism
	protected EventListenerList listenerList = new EventListenerList();

	public class CheckChangeEvent extends EventObject {
		public CheckChangeEvent(Object source) {
			super(source);          
		}       
	}
  
  
	void fireCheckChangeEvent(CheckChangeEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CheckChangeEventListener.class) {
				((CheckChangeEventListener) listeners[i + 1]).checkStateChanged(evt);
			}
		}
	}
  
  
	public interface CheckChangeEventListener extends EventListener {
		public void checkStateChanged(CheckChangeEvent event);
	}
	
	
	//Recursively checks/unchecks a subtree
	protected void checkSubTree(TreePath tp, boolean check) {
		CheckedNode cn = nodesCheckingState.get(tp);
		cn.isSelected = check;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
		for (int i = 0 ; i < node.getChildCount() ; i++) {              
			checkSubTree(tp.pathByAddingChild(node.getChildAt(i)), check);
		}
		cn.allChildrenSelected = check;
		if (check) {
			checkedPaths.add(tp);
		} 
		else {
			checkedPaths.remove(tp);
		}
	}
	
	
	public int getSelectedAlignmentsCount() {
		int count = 0;
		for (TreePath path : nodesCheckingState.keySet()) {
			if (nodesCheckingState.get(path).isSelected && path.getPathCount() == 4) {
				count++;
			}
		}
		return count;
	}
	
	
	public int childrenToCheck(TreePath path) {
		if (path.getPathCount() < 4) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			return node.getChildCount();
		};
		return 0;
	}
}