/*******************************************************************************
 * Copyright (c) 2013-2016 LAAS-CNRS (www.laas.fr)
 * 7 Colonel Roche 31077 Toulouse - France
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial Contributors:
 *     Thierry Monteil : Project manager, technical co-manager
 *     Mahdi Ben Alaya : Technical co-manager
 *     Samir Medjiah : Technical co-manager
 *     Khalil Drira : Strategy expert
 *     Guillaume Garzone : Developer
 *     François Aïssaoui : Developer
 *
 * New contributors :
 *******************************************************************************/
package org.eclipse.om2m.ipe.sample.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.ipe.sample.model.SampleModel;
import org.eclipse.om2m.ipe.sample.monitor.SampleMonitor;
import org.osgi.framework.FrameworkUtil;

/**
 * The Graphical User Interface of the IPE sample.
 */
public class GUI extends JFrame {
    /** Logger */
    static Log LOGGER = LogFactory.getLog(GUI.class);
    /** Serial Version UID */
    private static final long serialVersionUID = 1L;
    /** GUI Content Panel */
    private JPanel contentPanel;
    /** LAMP_ON Icon */
    static ImageIcon iconLampON = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Lamp_ON.png"));
    /** LAMP_OFF Icon */
    static ImageIcon iconLampOFF = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Lamp_OFF.png"));
    /** BUTTON_ON Icon */
    static ImageIcon iconButtonON = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Btn_ON.png"));
    /** BUTTON_OFF Icon */
    static ImageIcon iconButtonOFF = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Btn_OFF.png"));
    /** GUI Frame */
    static GUI frame;
    /** LAMP_0 LABEL */
    static JLabel [][] LABEL_LAMP;
    /** LAMP_1 LABEL */
    //static JLabel LABEL_LAMP_1 = new JLabel("");
    /** LAMP_0 ID */
    static String [][] LAMP;
    /** LAMP_1 ID */
    //static String LAMP_1 = "LAMP_1";
    static SampleModel.LampObserver lampObserver;
    /**
     * Initiate The GUI.
     */

    public static void init() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame = new GUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    LOGGER.error("GUI init Error", e);
                }
            }
        });
    }

    /**
     * Stop the GUI.
     */
    public static void stop() {
    	SampleModel.deleteObserver(lampObserver);
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Creates the frame.
     */
    public GUI() {
        setLocationByPlatform(true);
        setVisible(false);
        setResizable(false);
        setTitle("Sample Simulated IPE");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(10, 10, 1000, 780);

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        LABEL_LAMP = new JLabel[3][3];

        // Lamp0 Switcher0
        for(int i=0; i<3; i++){
            for(int j=0; j<3; ++j)
            {
                LABEL_LAMP[i][j] = new JLabel("LAMP_"+i+"_"+j);
                JPanel panel_Lamp = new JPanel();
                panel_Lamp.setBounds(i*319, j*260, 319, 260);
                contentPanel.add(panel_Lamp);
                panel_Lamp.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
                panel_Lamp.setLayout(null);
                LABEL_LAMP[i][j].setIcon(iconLampOFF);
                LABEL_LAMP[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
                LABEL_LAMP[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                LABEL_LAMP[i][j].setBounds(10, 9, 149, 240);
                panel_Lamp.add(LABEL_LAMP[i][j]);

                // Lamp0 Switch Button
                JButton button_Lamp = new JButton();
                button_Lamp.setOpaque(false);
                button_Lamp.setPressedIcon(iconButtonON);
                button_Lamp.setIcon(iconButtonOFF);
                button_Lamp.setBounds(187, 44, 122, 155);
                panel_Lamp.add(button_Lamp);
                button_Lamp.setMinimumSize(new Dimension(30, 23));
                button_Lamp.setMaximumSize(new Dimension(30, 23));
                button_Lamp.setPreferredSize(new Dimension(30, 23));

                JLabel labelSwitcher = new JLabel("LAMP_"+i+"_"+j);
                labelSwitcher.setFont(new Font("Vani", Font.BOLD | Font.ITALIC, 14));
                labelSwitcher.setFocusCycleRoot(true);
                labelSwitcher.setBorder(null);
                labelSwitcher.setAutoscrolls(true);
                labelSwitcher.setBounds(187, 199, 118, 29);
                panel_Lamp.add(labelSwitcher);

                button_Lamp.putClientProperty("i", i);
                button_Lamp.putClientProperty("j", j);

                // Listener for Lamp0 Switch Button
                button_Lamp.addActionListener(new java.awt.event.ActionListener() {
                    // Button Clicked
                    public void actionPerformed(final java.awt.event.ActionEvent evt) {
                        new Thread(){
                            public void run() {
                                // Send switch request to switch lamp i,j state
                                int i = (Integer)((JButton)(evt.getSource())).getClientProperty("i");
                                int j = (Integer)((JButton)(evt.getSource())).getClientProperty("j");
                                SampleMonitor.switchLamp("LAMP_"+i+"_"+j);
                            }
                        }.start();
                    }
                });
            }
        }
        
        lampObserver = new SampleModel.LampObserver() {

			@Override
			public void onLampStateChange(String lampId, boolean state) {
				setLabel(lampId, state);
			}
		};
        SampleModel.addObserver(lampObserver);
    }

    /**
     * Sets the LampIcon to ON or OFF depending on the newState
     * @param appId - The LAMP AppId
     * @param newState - The new LAMP State
     */
    public static void setLabel(String appId, boolean newState) {
        JLabel label = new JLabel("");

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++){
                if(("LAMP_"+i+"_"+j).equals(appId)) {
                    label = LABEL_LAMP[i][j];
                    break;
                }
            }
        }

        if(newState) {
            label.setIcon(iconLampON);
        } else {
            label.setIcon(iconLampOFF);
        }
    }

}
