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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import info.bioinfweb.phyde2.document.Document;



public class FileContentTreeView extends JFrame {
	
	private static JTree tree;
	//private Document document;
    
	public FileContentTreeView (/*Document document*/){
		//this.document = document;
		createTree();
	}
	
	public void createTree(){
	//create the root node
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Alignments");
    //create the child nodes
    DefaultMutableTreeNode defaultPhyDE2 = new DefaultMutableTreeNode("Multiple Sequence Alignments");
    //TODO irgendwas wie: for each MSA in der Liste im Document defaultPhyDE2.add(MSA) 
    DefaultMutableTreeNode contigs = new DefaultMutableTreeNode("Contig Alignments");
    //TODO irgendwas wie: for each contig in der Liste im Document contigs.add(contig)
    //=> geht das so überhaupt? ich hab ja ne Map und brauche irgendwo dann eigentlich die IDs her..
    
    root.add(defaultPhyDE2);
    root.add(contigs);
     
    //create the tree by passing in the root node
    tree = new JTree(root);
    //add(tree);
     
   //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setTitle("JTree Example");       
    this.pack();
    this.setVisible(true);
	}

	public JTree getTree ()
	{
		return tree;
	}
	
}
