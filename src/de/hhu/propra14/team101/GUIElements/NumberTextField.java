package de.hhu.propra14.team101.GUIElements;

import javafx.scene.control.TextField;

/**
 * Took from
 * https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx#answer-18959399
 * with some slight modifications
 */

public class NumberTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        return text.matches("[0-9]") || text.equals("");
    }
}