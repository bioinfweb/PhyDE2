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


import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.DocumentChangeEvent;
import info.bioinfweb.phyde2.document.DocumentListener;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;



public class FileContentTreeView extends JTree {
	public FileContentTreeView(Document document) {
		super(new DefaultMutableTreeNode());
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
		DefaultMutableTreeNode file = new DefaultMutableTreeNode("Some file");
		DefaultMutableTreeNode defaultPhyDE2 = new DefaultMutableTreeNode("Multiple Sequence Alignments");
		DefaultMutableTreeNode contigs = new DefaultMutableTreeNode("Contig Alignments");
		file.add(defaultPhyDE2);
		file.add(contigs);
		root.add(file);
		
		
		document.addDocumentListener(new DocumentListener() {
			@Override
			public void afterAlignmentModelAdded(DocumentChangeEvent e) {
				// TODO Add new node and set e.getModel() as user object
				//file.setUserObject(e.getModel());//PhyDE2Alignmentmodel To String methode die label zurückgibt
				//das userObject muss zum jeweiligen Alignemntmodel gehören.
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
				DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
			
				if (e.getModel() instanceof DefaultPhyDE2AlignmentModel){
					((DefaultMutableTreeNode)file.getChildAt(0)).add(new DefaultMutableTreeNode(e.getModel()));
				}
				else if (e.getModel() instanceof SingleReadContigAlignmentModel)	{
					((DefaultMutableTreeNode)file.getChildAt(1)).add(new DefaultMutableTreeNode(e.getModel()));
					//TODO für getModel() eine neue toString()-Methode schreiben.
				}
			}

			@Override
			public void afterAlignmentModelDeleted(DocumentChangeEvent e) {
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
				DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
				
				if (e.getModel() instanceof DefaultPhyDE2AlignmentModel){
					for (int i = 0; i < file.getChildAt(0).getChildCount(); i++) {
						if (e.getModel().getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
								((DefaultMutableTreeNode)file.getChildAt(0).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
								file.remove((DefaultMutableTreeNode)file.getChildAt(0).getChildAt(i));
						}
					}
					
				}
				else if (e.getModel() instanceof SingleReadContigAlignmentModel)	{
				// TODO Search node with user object that has the same alignment model ID and remove it
					for (int i = 0; i < file.getChildAt(1).getChildCount(); i++) {
						if (e.getModel().getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
								((DefaultMutableTreeNode)file.getChildAt(1).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
								file.remove((DefaultMutableTreeNode)file.getChildAt(1).getChildAt(i));
						}
					}	
					
			}
				//e.getModel().getAlignmentModel().getID();
				//((PhyDE2AlignmentModel) ((DefaultMutableTreeNode)file.getChildAt(0).getChildAt(0)).getUserObject()).getAlignmentModel().getID();
		}});
	}
}
