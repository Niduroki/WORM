package de.hhu.propra14.team101.GUIElements;

import javax.swing.*;

public class Popup {

    public static void popup (String message, String title) {
        JOptionPane.showMessageDialog( null, message, title, JOptionPane.OK_CANCEL_OPTION);
    }
}