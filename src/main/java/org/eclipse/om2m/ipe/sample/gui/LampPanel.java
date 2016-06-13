package org.eclipse.om2m.ipe.sample.gui;

import org.eclipse.om2m.ipe.sample.monitor.SampleMonitor;
import org.osgi.framework.FrameworkUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

class LampPanel extends JPanel {
    private static ImageIcon iconLampON = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Lamp_ON.png"));
    private static ImageIcon iconLampOFF = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Lamp_OFF.png"));
    private static ImageIcon iconButtonON = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Btn_ON.png"));
    private static ImageIcon iconButtonOFF = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Btn_OFF.png"));

    private final JLabel bulbImage;
    private final JLabel descriptionLabel;
    private final int horizontalIndex;
    private final int verticalIndex;

    LampPanel(final int horizontalIndex, final int verticalIndex) {
        this.horizontalIndex = horizontalIndex;
        this.verticalIndex= verticalIndex;

        configurePanel();

        bulbImage = createBulbImage();
        setOff();

        createMoveDetectedButton();

        descriptionLabel = createDescriptionLabel();
        setClickCounter(0);
    }

    private void configurePanel() {
        setBounds(horizontalIndex * 319, verticalIndex * 260, 319, 260);
        setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        setLayout(null);
    }

    private JLabel createBulbImage() {
        JLabel label;
        label = new JLabel();
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(10, 9, 149, 240);
        add(label);
        return label;
    }

    private void createMoveDetectedButton() {
        JButton moveDetectedButton = new JButton();
        moveDetectedButton.setOpaque(false);
        moveDetectedButton.setPressedIcon(iconButtonON);
        moveDetectedButton.setIcon(iconButtonOFF);
        moveDetectedButton.setBounds(187, 44, 122, 155);
        moveDetectedButton.setMinimumSize(new Dimension(30, 23));
        moveDetectedButton.setMaximumSize(new Dimension(30, 23));
        moveDetectedButton.setPreferredSize(new Dimension(30, 23));
        moveDetectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(final java.awt.event.ActionEvent evt) {
                new Thread() {
                    public void run() {
                        SampleMonitor.moveDetectedAtLamp("LAMP_" + horizontalIndex + "_" + verticalIndex);
                    }
                }.start();
            }
        });
        add(moveDetectedButton);
    }

    private JLabel createDescriptionLabel() {
        JLabel description = new JLabel();
        description.setFont(new Font("Arial", Font.BOLD, 14));
        description.setFocusCycleRoot(true);
        description.setBorder(null);
        description.setAutoscrolls(true);
        description.setBounds(187, 210, 118, 29);
        add(description);
        return description;
    }

    void setOn() {
        bulbImage.setIcon(iconLampON);
    }

    void setOff() {
        bulbImage.setIcon(iconLampOFF);
    }

    void setClickCounter(int count) {
        descriptionLabel.setText(
                String.format("<html>pos(%d, %d)<br>%d clicks</html>", horizontalIndex, verticalIndex, count));
    }
}
