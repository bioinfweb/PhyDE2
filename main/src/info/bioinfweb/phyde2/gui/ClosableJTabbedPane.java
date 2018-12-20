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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;



@SuppressWarnings("serial")
public class ClosableJTabbedPane extends JTabbedPane {
	private ClosableJTabbedPane tabbedPane;
	
	
	public ClosableJTabbedPane() {
		super();
		tabbedPane = this;
	}
	
	
	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		super.addTab(title, icon, component, tip);
		setSelectedComponent(component);
		addCloseButton(title);
	}
	
	
	public void addCloseButton(String title) {
		
		JPanel pnlTab = new JPanel(new BorderLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(title);
		JButton btnClose = new JButton("x");
		btnClose.setPreferredSize(new Dimension(20, 20));
		btnClose.setContentAreaFilled(false);
		btnClose.setOpaque(false);
		btnClose.setBorder(null);
		
		pnlTab.add(lblTitle, BorderLayout.WEST);
		pnlTab.add(btnClose, BorderLayout.EAST);

		setTabComponentAt(getSelectedIndex(), pnlTab);
		
		btnClose.addMouseListener(new MouseListener() {

			private JButton button;
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setContentAreaFilled(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setContentAreaFilled(false);
			}
			
			private MouseListener init(JButton button) {
				this.button = button;
				return this;
			}
			
		}.init(btnClose));
		
		
		btnClose.addActionListener(new ActionListener() {
			
			private String title;
		    
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.remove(tabbedPane.indexOfTab(title));						
			}
			
			private ActionListener init(String title) {
				this.title = title;
				return this;
			}
			
		}.init(title));
	}
	
	
	public Tab tabByAlignment(PhyDE2AlignmentModel alignment) {
		for (int i = 0; i < getTabCount(); i++) {
			Component component = getComponentAt(i);
			if (((Tab)component).getAlignmentModel() == alignment) {
				return (Tab)component;
			}
		}
		return null;
	}
}
