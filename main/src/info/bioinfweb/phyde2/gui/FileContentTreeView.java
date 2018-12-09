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


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.DocumentChangeEvent;
import info.bioinfweb.phyde2.document.DocumentListener;
import info.bioinfweb.phyde2.document.PherogramChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelListener;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;



public class FileContentTreeView extends JTree {
	//private PhyDE2AlignmentModel model = new PhyDE2AlignmentModel();
	
	
	//public FileContentTreeView(Document document, MainFrame mainframe) {
	public FileContentTreeView(Document document, MainFrame mainframe) {
		super(new DefaultTreeModel(new DefaultMutableTreeNode()));
		setRootVisible(false);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
		DefaultMutableTreeNode file = new DefaultMutableTreeNode("Some file");
		DefaultMutableTreeNode defaultPhyDE2 = new DefaultMutableTreeNode("Multiple Sequence Alignments");
		DefaultMutableTreeNode contigs = new DefaultMutableTreeNode("Contig Alignments");
		file.add(defaultPhyDE2);
		file.add(contigs);
		root.add(file);
		getModel().reload();
		
		document.addDocumentListener(new DocumentListener() {
			@Override
			public void afterAlignmentModelAdded(DocumentChangeEvent e) {
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
				DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
			
				if (e.getModel() instanceof DefaultPhyDE2AlignmentModel){
					((DefaultMutableTreeNode)file.getChildAt(0)).add(new DefaultMutableTreeNode(e.getModel()));
					getModel().reload(file.getChildAt(0));
				}
				else if (e.getModel() instanceof SingleReadContigAlignmentModel)	{
					((DefaultMutableTreeNode)file.getChildAt(1)).add(new DefaultMutableTreeNode(e.getModel()));
					getModel().reload(file.getChildAt(1));
				}
				
				e.getModel().addDocumentListener(new PhyDE2AlignmentModelListener() {
					@Override
					public void afterFileNameChanged(PhyDE2AlignmentModelChangeEvent e) {
						//TODO Implement
					}

					@Override
					public void afterChangedFlagSet(PhyDE2AlignmentModelChangeEvent e) {}

					@Override
					public void afterPherogramAddedOrDeleted(PherogramChangeEvent e) {
					DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
					DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
					
					PhyDE2AlignmentModel phyDE2Model = e.getSource();
					PherogramAreaModel pherogramModel = e.getPherogramModel();
					String sequneceID = e.getSequenceID();
						
					switch (e.getListChangeType()) {
					case INSERTION: 	
						if (phyDE2Model instanceof SingleReadContigAlignmentModel){
							for (int i = 0; i < file.getChildAt(1).getChildCount(); i++) {
							if (phyDE2Model.getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
										((DefaultMutableTreeNode)file.getChildAt(1).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
										((DefaultMutableTreeNode) file.getChildAt(1).getChildAt(i)).add(new DefaultMutableTreeNode(pherogramModel));
										// TODO add setLabel() to PherogramAreaModel so that the file name could be set as label when loading the Pherogram
										// and use the label using pherogramModel.getLabel() to display only the file name-
										getModel().reload(file.getChildAt(1).getChildAt(i));
							}
							}
					}
					else if (phyDE2Model instanceof DefaultPhyDE2AlignmentModel)	{
					
					}
						break;
					case DELETION:
						break;
					default:
						break;
						
					}
				}});
			
				
				
			}

			@Override
			public void afterAlignmentModelDeleted(DocumentChangeEvent e) {
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
				DefaultMutableTreeNode file = (DefaultMutableTreeNode)root.getChildAt(0);
				
				if (e.getModel() instanceof DefaultPhyDE2AlignmentModel){
					for (int i = 0; i < file.getChildAt(0).getChildCount(); i++) {
						if (e.getModel().getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
								((DefaultMutableTreeNode)file.getChildAt(0).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
							((DefaultMutableTreeNode) file.getChildAt(0)).remove(i);
						}
					}
					getModel().reload(file.getChildAt(0));
				}
				else if (e.getModel() instanceof SingleReadContigAlignmentModel)	{
					for (int i = 0; i < file.getChildAt(1).getChildCount(); i++) {
						if (e.getModel().getAlignmentModel().getID().equals(((PhyDE2AlignmentModel) 
								((DefaultMutableTreeNode)file.getChildAt(1).getChildAt(i)).getUserObject()).getAlignmentModel().getID())){
								((DefaultMutableTreeNode) file.getChildAt(1)).remove(i);
						}
					}		
					getModel().reload(file.getChildAt(1));
				}
		}});
		
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//checks, if selected alignment is PhyDE2AlignmentModel
				if (mainframe.getSelectedAlignment() != null){
				if (e.getClickCount() == 2) {
					PhyDE2AlignmentModel model = mainframe.getSelectedAlignment();
					mainframe.showAlignment(model);
				   // System.out.println("double clicked");
				}
				}
				
				if (mainframe.getSelectedPherogram() != null){
					if (e.getClickCount()== 2){
						PherogramAreaModel pherogramModel = mainframe.getSelectedPherogram();
						mainframe.getPherogramView().getTraceCurveView().setModel(pherogramModel.getOwner().getModel());
					}
				}
			}
		});
	}

	
	@Override
	public DefaultTreeModel getModel() {
		return (DefaultTreeModel) super.getModel();
	}
	
	
	
}
