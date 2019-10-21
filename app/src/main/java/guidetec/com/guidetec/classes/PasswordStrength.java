package guidetec.com.guidetec.classes;

import android.content.Context;
import android.graphics.Color;

import guidetec.com.guidetec.R;

public enum PasswordStrength {
    WEAK(R.string.password_strength_weak, -65536),
    MEDIUM(R.string.password_strength_medium, Color.argb(255, 220, 185, 0)),
    STRONG(R.string.password_strength_strong, -16711936),
    VERY_STRONG(R.string.password_strength_very_strong, -16776961);

    static int REQUIRED_LENGTH = 8;
    static int MAXIMUM_LENGTH = 15;
    static boolean REQUIRE_SPECIAL_CHARACTERS = true;
    static boolean REQUIRE_DIGITS = true;
    static boolean REQUIRE_LOWER_CASE = true;
    static boolean REQUIRE_UPPER_CASE = false;
    int resId;
    int color;

    private PasswordStrength(int resId, int color) {
        this.resId = resId;
        this.color = color;
    }

    public CharSequence getText(Context ctx) {
        return ctx.getText(this.resId);
    }

    public int getColor() {
        return this.color;
    }

    public static PasswordStrength calculateStrength(String password) {
        int currentScore = 0;
        boolean sawUpper = false;
        boolean sawLower = false;
        boolean sawDigit = false;
        boolean sawSpecial = false;

        for(int i = 0; i < password.length(); ++i) {
            char c = password.charAt(i);
            if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                ++currentScore;
                sawSpecial = true;
            } else if (!sawDigit && Character.isDigit(c)) {
                ++currentScore;
                sawDigit = true;
            } else if (!sawUpper || !sawLower) {
                if (Character.isUpperCase(c)) {
                    sawUpper = true;
                } else {
                    sawLower = true;
                }

                if (sawUpper && sawLower) {
                    ++currentScore;
                }
            }
        }

        if (password.length() > REQUIRED_LENGTH) {
            if (REQUIRE_SPECIAL_CHARACTERS && !sawSpecial || REQUIRE_UPPER_CASE && !sawUpper || REQUIRE_LOWER_CASE && !sawLower || REQUIRE_DIGITS && !sawDigit) {
                currentScore = 1;
            } else {
                currentScore = 2;
                if (password.length() > MAXIMUM_LENGTH) {
                    currentScore = 3;
                }
            }
        } else {
            currentScore = 0;
        }

        switch(currentScore) {
            case 0:
                return WEAK;
            case 1:
                return MEDIUM;
            case 2:
                return STRONG;
            case 3:
                return VERY_STRONG;
            default:
                return VERY_STRONG;
        }
    }
}
