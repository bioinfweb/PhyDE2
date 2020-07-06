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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import info.bioinfweb.commons.bio.CharacterStateSetType;
import info.bioinfweb.commons.swing.OkCancelApplyHelpDialog;
import info.bioinfweb.libralign.model.factory.CharacterStateSetChooser;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class CharacterStateSetChooserDialog extends OkCancelApplyHelpDialog implements CharacterStateSetChooser{
	private static CharacterStateSetChooserDialog firstInstance = null;
	private final JPanel contentPanel = new JPanel();
	
	private JRadioButton nucButton = new JRadioButton("Nucleotide");
	private JRadioButton aminoButton = new JRadioButton("Amino acid");
	private JRadioButton dnaButton = new JRadioButton("DNA");
	private JRadioButton rnaButton = new JRadioButton("RNA");

	
	@Override
	public CharacterStateSetType chooseCharacterStateSet() {
		return null;
	}

	
	public static CharacterStateSetChooserDialog getInstance(MainFrame owner) {
		if (firstInstance == null) {
			firstInstance = new CharacterStateSetChooserDialog(owner);
		}
		return firstInstance;
	}
	

	public CharacterStateSetChooserDialog(MainFrame owner) {
		super(owner, true);
		setTitle("Token set");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);

		contentPanel.setLayout(new GridBagLayout());
		{
			JLabel lblEnterASequence = new JLabel("The currently open file does not specify a character state set.");
			GridBagConstraints gbc_lblEnterASequence = new GridBagConstraints();
			gbc_lblEnterASequence.insets = new Insets(0, 0, 5, 0);
			gbc_lblEnterASequence.anchor = GridBagConstraints.WEST;
			gbc_lblEnterASequence.gridx = 0;
			gbc_lblEnterASequence.gridy = 0;
			contentPanel.add(lblEnterASequence, gbc_lblEnterASequence);
		}
		{
			JLabel lblNewLabel = new JLabel("Which character state set do you want to use?");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);

			JPanel radioPanel = new JPanel();
			GridBagConstraints gbc_radioPanel = new GridBagConstraints();
			gbc_radioPanel.insets = new Insets(5, 0, 0, 0);
			gbc_radioPanel.fill = GridBagConstraints.VERTICAL;
			gbc_radioPanel.gridx = 0;
			gbc_radioPanel.gridy = 2;
			contentPanel.add(radioPanel, gbc_radioPanel);
			radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

			radioPanel.add(nucButton);
			nucButton.setMnemonic(KeyEvent.VK_N);
			nucButton.setActionCommand("Nucleotide");  
			nucButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) { 
					
				}           
			});
			ButtonGroup group = new ButtonGroup();
			group.add(nucButton);

			
			radioPanel.add(aminoButton);
			aminoButton.setMnemonic(KeyEvent.VK_A);
			aminoButton.setActionCommand("Aminoacid");
			aminoButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {

				}
			});
			group.add(aminoButton);

			
			radioPanel.add(dnaButton);
			dnaButton.setMnemonic(KeyEvent.VK_D);
			dnaButton.setActionCommand("DNA");
			dnaButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {

				}
			});
			group.add(dnaButton);
			
			
			radioPanel.add(rnaButton);
			rnaButton.setMnemonic(KeyEvent.VK_R);
			rnaButton.setActionCommand("RNA");
			rnaButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {

				}
			});
			group.add(rnaButton);
			aminoButton.setSelected(true);

		}
		getApplyButton().setVisible(false);
		getContentPane().add(getButtonsPanel());
		pack();
	}


	public CharacterStateSetType getChangedType() {
		if (aminoButton.isSelected()) {
			return CharacterStateSetType.AMINO_ACID;
		}
		else if (nucButton.isSelected()) {
			return CharacterStateSetType.NUCLEOTIDE;
		}
		else if (dnaButton.isSelected()) {
			return CharacterStateSetType.DNA;
		}
		else if (rnaButton.isSelected()) {
			return CharacterStateSetType.RNA;
		}
		else {
			return CharacterStateSetType.UNKNOWN;
		}
	}


	public void setChangedType(CharacterStateSetType changedType) {
		switch (changedType) {
		case AMINO_ACID:
			aminoButton.setSelected(true);
			break;
		case NUCLEOTIDE:
			nucButton.setSelected(true);
			break;
		case DNA:
			dnaButton.setSelected(true);
			break;
		case RNA:
			rnaButton.setSelected(true);
			break;
		default:
			aminoButton.setSelected(true);
			break;	
		}
	}


	@Override
	protected void help() {
		// TODO Auto-generated method stub

	}


	@Override
	protected boolean apply() {
		return true;
	}
}
