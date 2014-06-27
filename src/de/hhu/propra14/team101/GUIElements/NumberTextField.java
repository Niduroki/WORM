package de.hhu.propra14.team101.GUIElements;

import javafx.scene.control.TextField;

/**
 * Took from
 * https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx#answer-18959399
 * with some slight modifications
 *
 * TextField that only accepts positive integers
 * @code
 * <p>
 * NumberTextField numberField = new NumberTextField();
 * int number = Integer.parseInt(numberField.getText());
 * </p>
 */
public class NumberTextField extends TextField {

    /**
     * Replaces text
     * @param start Starting index
     * @param end Ending index
     * @param text Text
     */
    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    /**
     * Replaces selection
     * @param text Text to replace with
     */
    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    /**
     * Validates input text
     * @param text Text to validate
     * @return Validated text
     */
    private boolean validate(String text) {
        return text.matches("[0-9]") || text.equals("");
    }
}