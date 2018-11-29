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


import info.bioinfweb.commons.swing.OkCancelApplyHelpDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;



public class AddSingleReadDialog extends OkCancelApplyHelpDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField sequenceNameTextField;
	private JTextField filePathTextField;

	

	public AddSingleReadDialog(Frame owner) {
		super (owner, true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblSingleReadName = new JLabel("Single read name: ");
			GridBagConstraints gbc_lblSingleReadName = new GridBagConstraints();
			gbc_lblSingleReadName.anchor = GridBagConstraints.EAST;
			gbc_lblSingleReadName.insets = new Insets(0, 0, 5, 5);
			gbc_lblSingleReadName.gridx = 0;
			gbc_lblSingleReadName.gridy = 0;
			contentPanel.add(lblSingleReadName, gbc_lblSingleReadName);
		}
		{
			sequenceNameTextField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.gridwidth = 2;
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 0;
			contentPanel.add(sequenceNameTextField, gbc_textField);
			sequenceNameTextField.setColumns(10);
			
		}
		{
			JLabel lblPherogram = new JLabel("Pherogram:");
			GridBagConstraints gbc_lblPherogram = new GridBagConstraints();
			gbc_lblPherogram.insets = new Insets(0, 0, 0, 5);
			gbc_lblPherogram.anchor = GridBagConstraints.WEST;
			gbc_lblPherogram.gridx = 0;
			gbc_lblPherogram.gridy = 1;
			contentPanel.add(lblPherogram, gbc_lblPherogram);
		}
		{
			filePathTextField = new JTextField();
			GridBagConstraints gbc_textField_1 = new GridBagConstraints();
			gbc_textField_1.insets = new Insets(0, 0, 0, 5);
			gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_1.gridx = 1;
			gbc_textField_1.gridy = 1;
			contentPanel.add(filePathTextField, gbc_textField_1);
			filePathTextField.setColumns(10);
		}
		{
			JButton button = new JButton("...");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//TODO onclickButtonAction
				}
			});
			GridBagConstraints gbc_button = new GridBagConstraints();
			gbc_button.gridx = 2;
			gbc_button.gridy = 1;
			contentPanel.add(button, gbc_button);
		}
		{
			getContentPane().add(getButtonsPanel());
		}
	}


	@Override
	protected void help() {
		// TODO Auto-generated method stub
		
	}
	
	public String getSequenceName(){
		return sequenceNameTextField.getText();
	}
	
	public String getFilePath(){
		return filePathTextField.getText();
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
