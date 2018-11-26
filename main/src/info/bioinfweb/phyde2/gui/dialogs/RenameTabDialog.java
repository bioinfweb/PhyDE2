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

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import info.bioinfweb.commons.swing.OkCancelApplyHelpDialog;
import info.bioinfweb.phyde2.Main;

@SuppressWarnings("serial")
public class RenameTabDialog extends OkCancelApplyHelpDialog {
	private JPanel jContentPane = null;
	private JTextField nameTextField;
	
	
	public RenameTabDialog(Frame owner) {
		super(owner, true);
		initialize();
		this.pack();
	}
	
	
	private void initialize() {
		this.setTitle("New character set");
        this.setIconImage(new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource(Main.ICON_PATH)).getImage());
		this.setContentPane(getJContentPane());
	}
	
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			
			JPanel namePanel = new JPanel();
			jContentPane.add(namePanel);
			GridBagLayout gbl_namePanel = new GridBagLayout();
			gbl_namePanel.columnWidths = new int[]{0, 0, 0};
			gbl_namePanel.rowHeights = new int[]{0, 0};
			gbl_namePanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_namePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			namePanel.setLayout(gbl_namePanel);
			
			JLabel nameLabel = new JLabel("Name:");
			GridBagConstraints gbc_nameLabel = new GridBagConstraints();
			gbc_nameLabel.insets = new Insets(0, 3, 0, 5);
			gbc_nameLabel.anchor = GridBagConstraints.EAST;
			gbc_nameLabel.gridx = 0;
			gbc_nameLabel.gridy = 0;
			namePanel.add(nameLabel, gbc_nameLabel);
			
			nameTextField = new JTextField();
			GridBagConstraints gbc_nameTextField = new GridBagConstraints();
			gbc_nameTextField.insets = new Insets(3, 0, 3, 3);
			gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_nameTextField.gridx = 1;
			gbc_nameTextField.gridy = 0;
			namePanel.add(nameTextField, gbc_nameTextField);
			nameTextField.setColumns(10);
			jContentPane.add(getButtonsPanel(), null);
			getApplyButton().setVisible(false);
		}
		return jContentPane;
	}
	
	
	@Override
	protected void help() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean apply() {
		if (getName().equals("")) {
			switch(JOptionPane.showConfirmDialog(this, "There is no name for the tab. Do you want to change it anyway?", "Missing Name!", JOptionPane.YES_NO_OPTION)) {
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
	
	
	public String getName() {
		return nameTextField.getText();
	}
	
	
	public void setName(String name) {
		nameTextField.setText(name);
	}

}
