package de.hhu.propra14.team101.GUIElements;

import javafx.scene.control.TextField;

/**
 * Based on NumberTextField
 *
 * TextField that disallows a specified char
 * <pre>
 * {@code
 * DisallowedCharTextField noSpaceField = new DisallowedChatTextField(' ');
 * String text = noSpaceField.getText();
 * }
 * </pre>
 */
public class DisallowedCharTextField extends TextField {
    public char disallowedChar;

    public DisallowedCharTextField(char disallowedChar) {
        this.disallowedChar = disallowedChar;
    }

    public DisallowedCharTextField(char disallowedChar, String prefill) {
        this.disallowedChar = disallowedChar;
        this.setText(prefill);
    }

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
        return !text.contains(String.valueOf(this.disallowedChar));
    }
}