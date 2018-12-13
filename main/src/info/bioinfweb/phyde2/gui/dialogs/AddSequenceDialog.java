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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import info.bioinfweb.commons.swing.OkCancelApplyHelpDialog;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.SelectContigView;

public class AddSequenceDialog extends OkCancelApplyHelpDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField sequenceNameTextField;
	private SelectContigView selectContigView = null;
	private MainFrame owner = null;


	public AddSequenceDialog(Frame owner) {
		super(owner,true);
		this.owner = (MainFrame) owner;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] {0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblEnterASequence = new JLabel("Enter a sequence name:");
			GridBagConstraints gbc_lblEnterASequence = new GridBagConstraints();
			gbc_lblEnterASequence.insets = new Insets(0, 0, 5, 5);
			gbc_lblEnterASequence.anchor = GridBagConstraints.WEST;
			gbc_lblEnterASequence.gridx = 0;
			gbc_lblEnterASequence.gridy = 0;
			contentPanel.add(lblEnterASequence, gbc_lblEnterASequence);
		}
		{
			sequenceNameTextField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 0;
			contentPanel.add(sequenceNameTextField, gbc_textField);
			sequenceNameTextField.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("Choose a contig (optional):");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JScrollPane scrollPane = new JScrollPane(getSelectContigView());
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 1;
			contentPanel.add(scrollPane, gbc_scrollPane);
		}
		getContentPane().add(getButtonsPanel());
	}
	
	public SelectContigView getSelectContigView() {
		if (selectContigView == null) {
			selectContigView = new SelectContigView(owner);
		}
		return selectContigView;
	}

	
	
	public String getSequenceName() {
		return sequenceNameTextField.getText();
	}
	
	
	public SingleReadContigAlignmentModel getSelectedConitgModel (){

		if (getSelectContigView().getSelectionModel().getLeadSelectionPath() != null) {
			Object selectedNode= getSelectContigView().getSelectionModel().getLeadSelectionPath().getLastPathComponent();
			if(selectedNode instanceof DefaultMutableTreeNode){
				Object userObject = (((DefaultMutableTreeNode) selectedNode).getUserObject());
				if(userObject instanceof SingleReadContigAlignmentModel){
					return (SingleReadContigAlignmentModel) userObject;
				}
			}
		}
		return null;
	}

	@Override
	protected void help() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean apply() {
		if (getSequenceName().equals("")) {
			switch(JOptionPane.showConfirmDialog(this, "There is no name for the new sequence. Do you want to add it anyway?", "Missing Name!", JOptionPane.YES_NO_OPTION)) {
			case JOptionPane.YES_OPTION:
				return true;
			case JOptionPane.NO_OPTION:
				return false;
			case JOptionPane.CLOSED_OPTION:
				return false;
			}
		}
		return true;
	}
	

}
