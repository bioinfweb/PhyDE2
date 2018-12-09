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

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.commons.swing.OkCancelApplyHelpDialog;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.TreeView;

@SuppressWarnings("serial")
public class ExportDialog extends OkCancelApplyHelpDialog {
	private JPanel jContentPane = null;
	private TreeView treeView = null;
	private MainFrame owner = null;
	private JFileChooser exportFileChooser = null;
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private JTextField nameTextField = null;
	private JComboBox<String> cb = null;
	private HashMap<String, String> jComboBoxTranslator = new HashMap<>();
	private HashMap<String, Integer> allowedSelectedAlignments = new HashMap<>();
	
	
	public ExportDialog(Frame owner) {
		super(owner, true);
		this.owner = (MainFrame)owner;
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
			
			JPanel formatPanel = new JPanel();
			jContentPane.add(formatPanel);
			GridBagLayout gbl_formatPanel = new GridBagLayout();
			gbl_formatPanel.columnWidths = new int[]{0, 0, 0};
			gbl_formatPanel.rowHeights = new int[]{0, 0};
			gbl_formatPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_formatPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			formatPanel.setLayout(gbl_formatPanel);
			
			JLabel formatLabel = new JLabel("Format:");
			GridBagConstraints gbc_formatLabel = new GridBagConstraints();
			gbc_formatLabel.insets = new Insets(0, 3, 0, 5);
			gbc_formatLabel.anchor = GridBagConstraints.EAST;
			gbc_formatLabel.gridx = 0;
			gbc_formatLabel.gridy = 0;
			formatPanel.add(formatLabel, gbc_formatLabel);
			
			ArrayList<String> formats = new ArrayList<String>();
			
	        for (String formatID : factory.getFormatIDsSet()) {
				JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
				if (info.isElementModeled(EventContentType.ALIGNMENT, false)) {
					String choice = info.getFormatName();
					formats.add(choice);
					jComboBoxTranslator.put(choice, formatID);
					allowedSelectedAlignments.put(choice, Integer.MAX_VALUE);
				}
			}
	        
			getAllowedSelectedAlignments();
	        
	        String[] choices = new String[formats.size()];
	        choices = formats.toArray(choices);
	        cb = new JComboBox<String>(choices) {
	        	@Override
	        	public void setSelectedItem(Object anObject) {
	        		if (jComboBoxSelectionAllowedToChange(anObject)) {
	        			super.setSelectedItem(anObject);
	        		}
	        	}
	        };
	        GridBagConstraints gbc_formatBox = new GridBagConstraints();
			gbc_formatBox.insets = new Insets(0, 3, 0, 5);
			gbc_formatBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_formatBox.gridx = 1;
			gbc_formatBox.gridy = 0;
	        formatPanel.add(cb, gbc_formatBox);
	        
	        
			jContentPane.add(getTreeView(), null);
			
			// Calling checking mechanism on mouse click
	        treeView.addMouseListener(new MouseListener() {
	        	@Override
	        	public void mouseClicked(MouseEvent e) {
	        		TreePath tp = treeView.getPathForLocation(e.getX(), e.getY());
	        		if (tp == null) {
	        			return;
	        		}
	        		else if (treeView.isAlreadySelected(tp)
        					|| (allowedSelectedAlignments.get(cb.getSelectedItem()) >= treeView.childrenToCheck(tp)
        					&& changeCompatibleWithFormat((String)cb.getSelectedItem(), treeView.getSelectedAlignmentsCount()+1))) {
        				treeView.fireMouseClicked(tp);
        			}
	        		else {
        				JOptionPane.showMessageDialog(jContentPane, "Number of selected alignments not compatible with chosen format.", "Warning",
        						JOptionPane.WARNING_MESSAGE);
	        		}
	        	}

				@Override
				public void mousePressed(MouseEvent e) {	
				}

				@Override
				public void mouseReleased(MouseEvent e) {	
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}         
	        });
			
			JPanel namePanel = new JPanel();
			jContentPane.add(namePanel);
			GridBagLayout gbl_namePanel = new GridBagLayout();
			gbl_namePanel.columnWidths = new int[]{0, 0, 0};
			gbl_namePanel.rowHeights = new int[]{0, 0};
			gbl_namePanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_namePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			namePanel.setLayout(gbl_namePanel);
			
			JLabel nameLabel = new JLabel("Save to:");
			GridBagConstraints gbc_nameLabel = new GridBagConstraints();
			gbc_nameLabel.insets = new Insets(0, 3, 0, 5);
			gbc_nameLabel.anchor = GridBagConstraints.EAST;
			gbc_nameLabel.gridx = 0;
			gbc_nameLabel.gridy = 1;
			namePanel.add(nameLabel, gbc_nameLabel);
			
			nameTextField = new JTextField();
			GridBagConstraints gbc_nameTextField = new GridBagConstraints();
			gbc_nameTextField.insets = new Insets(3, 0, 3, 3);
			gbc_nameTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_nameTextField.gridx = 1;
			gbc_nameTextField.gridy = 1;
			namePanel.add(nameTextField, gbc_nameTextField);
			
			JButton button = new JButton("...");
			GridBagConstraints gbc_button = new GridBagConstraints();
			gbc_nameTextField.insets = new Insets(3, 0, 3, 3);
			gbc_button.fill = GridBagConstraints.HORIZONTAL;
			gbc_button.gridx = 2;
			gbc_button.gridy = 1;
			namePanel.add(button, gbc_button);
			
			button.addActionListener(new ActionListener() {
				@Override
		        public void actionPerformed(ActionEvent e) {
		            jButtonActionPerformed(e);
				}
			});
			
			jContentPane.add(getButtonsPanel(), null);
			getApplyButton().setVisible(false);
		}
		return jContentPane;
	}
	
	
	private void getAllowedSelectedAlignments() {
		allowedSelectedAlignments.put("FASTA", 1);
		allowedSelectedAlignments.put("Nexus", 1);		
	}
	
	
	private boolean changeCompatibleWithFormat(String selectedFormat, int selectedAlignments) {
		if ((allowedSelectedAlignments.get(selectedFormat) != null) && selectedAlignments > allowedSelectedAlignments.get(selectedFormat)) {
			return false;
		}
		return true;
	}
	
	
	private boolean jComboBoxSelectionAllowedToChange(Object newValue) {
		if (!changeCompatibleWithFormat((String)newValue, treeView.getSelectedAlignmentsCount())) {
			switch (JOptionPane.showConfirmDialog(this, "Exporting more than one alignment is not possible with the format " + newValue + ". Your selection will be deleted upon continuing. Do you still want to continue?", 
					"Warning", JOptionPane.YES_NO_OPTION)) {
			case JOptionPane.YES_OPTION:
				treeView.setEverythingNotSelected();
				treeView.repaint();
				return true;
			case JOptionPane.NO_OPTION:
				return false;
			case JOptionPane.CLOSED_OPTION:
				return false;
			};
		}
		return true;
	}
	
	
	public TreeView getTreeView() {
		if (treeView == null) {
			treeView = new TreeView(owner.getNewDocument(), owner);
		}
		return treeView;
	}
	
	
	private void jButtonActionPerformed(ActionEvent evt) {
		if (promptExportFileName()) {
			nameTextField.setText(getExportFileChooser().getSelectedFile().toString());
		}
	}
	
	
	//show save Dialog, set File, set File format
	private boolean promptExportFileName() {
		if (exportFileChooser != null) {
			exportFileChooser.resetChoosableFileFilters();
		}
		boolean result = (getExportFileChooser().showDialog(owner, "OK") == JFileChooser.APPROVE_OPTION);
		if (result) {
	    	owner.getNewDocument().setFile(getExportFileChooser().getSelectedFile());
		}
		return result;
	}
	
	
	private JFileChooser getExportFileChooser() {
		if (exportFileChooser == null) {
			exportFileChooser = new JFileChooser() {
				@Override
				   protected JDialog createDialog(Component parent) throws HeadlessException {
				       // intercept the dialog created by JFileChooser
				       JDialog dialog = super.createDialog(parent);
				       dialog.setModal(true);  // set modality (or setModalityType)
				       return dialog;
				   }
			};			
		}
		
		exportFileChooser.setAcceptAllFileFilterUsed(false);
		JPhyloIOFormatInfo info = factory.getFormatInfo(jComboBoxTranslator.get(cb.getSelectedItem()));
		if (info.isElementModeled(EventContentType.ALIGNMENT, false)) {
			JPhyloIOContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
			exportFileChooser.addChoosableFileFilter(filter);
		}
		
		return exportFileChooser;
	}
	
	
	public File getSelectedFile() {
		return exportFileChooser.getSelectedFile();
	}
	
	
	public String getFormatID() {
		return jComboBoxTranslator.get(cb.getSelectedItem());
	}
	
	
	@Override
	protected void help() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected boolean apply() {
		if (nameTextField.getText().equals("")) {
			JOptionPane.showMessageDialog(jContentPane, "You did not choose a file to export to. Please chose one first.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
}
