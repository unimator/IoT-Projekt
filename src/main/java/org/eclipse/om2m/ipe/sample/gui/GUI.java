/*******************************************************************************
 * Copyright (c) 2013-2016 LAAS-CNRS (www.laas.fr)
 * 7 Colonel Roche 31077 Toulouse - France
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Initial Contributors:
 * Thierry Monteil : Project manager, technical co-manager
 * Mahdi Ben Alaya : Technical co-manager
 * Samir Medjiah : Technical co-manager
 * Khalil Drira : Strategy expert
 * Guillaume Garzone : Developer
 * François Aïssaoui : Developer
 * <p>
 * New contributors :
 *******************************************************************************/
package org.eclipse.om2m.ipe.sample.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.ipe.sample.model.SampleModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The Graphical User Interface of the IPE sample.
 */
public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int LAMPS_GRID_SIZE = 3;
    private static Log LOGGER = LogFactory.getLog(GUI.class);
    private static GUI frame;
    private static Map<String, LampPanel> lamps;
    private static SampleModel.LampObserver lampObserver;

    private GUI() {
        setLocationByPlatform(true);
        setVisible(false);
        setResizable(false);
        setTitle("Street lamps");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(10, 10, 960, 780);

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        lamps = new HashMap<String, LampPanel>();
        for (int i = 0; i < LAMPS_GRID_SIZE; i++) {
            for (int j = 0; j < LAMPS_GRID_SIZE; j++) {
                LampPanel lampPanel = new LampPanel(i, j);
                lamps.put("LAMP_" + i + "_" + j, lampPanel);
                contentPanel.add(lampPanel);
            }
        }

        lampObserver = new SampleModel.LampObserver() {
            public void onLampStateChange(String lampId, boolean state, int counter) {
                updateLamp(lampId, state, counter);
            }
        };
        SampleModel.addObserver(lampObserver);
    }

    private static void updateLamp(String appId, boolean newState, int counter) {
        LampPanel lamp = lamps.get(appId);
        if (lamp != null) {
            if (newState) {
                lamp.setOn();
            } else {
                lamp.setOff();
            }
            lamp.setClickCounter(counter);
        }
    }

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

    public static void stop() {
        SampleModel.deleteObserver(lampObserver);
        frame.setVisible(false);
        frame.dispose();
    }

}
