package main.java;

import java.awt.event.KeyEvent;

public class KBInput {

    public static int panUpKey;
    public static int panDownKey;
    public static int panLeftKey;
    public static int panRightKey;
    public static int increaseSimSpeedKey;
    public static int decreaseSimSpeedKey;
    public static int increaseZoomKey;
    public static int decreaseZoomKey;
    public static int centerOnBodyKey;
    public static int addOrbitingBodyKey;

    // Supported layouts
    enum keyboardLayouts {
        QWERTY,
        DVORAK,
        AZERTY,
        QWERTZ,
        COLEMAK
    }

    // Sets the simulation's inputs according to the provided keyboard layout
    // Currently supported layouts: QWERTY, Dvorak, AZERTY, QWERTZ, and Colemak
    public static void setKeyboardLayout(keyboardLayouts layout) {
        switch (layout) {
            case QWERTY:
                panUpKey = KeyEvent.VK_W;
                panDownKey = KeyEvent.VK_S;
                panLeftKey = KeyEvent.VK_A;
                panRightKey = KeyEvent.VK_D;
                increaseSimSpeedKey = KeyEvent.VK_X;
                decreaseSimSpeedKey = KeyEvent.VK_Z;
                increaseZoomKey = KeyEvent.VK_E;
                decreaseZoomKey = KeyEvent.VK_Q;
                centerOnBodyKey = KeyEvent.VK_C;
                addOrbitingBodyKey = KeyEvent.VK_1;
                break;
            case DVORAK:
                panUpKey = KeyEvent.VK_COMMA;
                panDownKey = KeyEvent.VK_O;
                panLeftKey = KeyEvent.VK_A;
                panRightKey = KeyEvent.VK_E;
                increaseSimSpeedKey = KeyEvent.VK_Q;
                decreaseSimSpeedKey = KeyEvent.VK_SEMICOLON;
                increaseZoomKey = KeyEvent.VK_PERIOD;
                decreaseZoomKey = KeyEvent.VK_QUOTE;
                centerOnBodyKey = KeyEvent.VK_J;
                addOrbitingBodyKey = KeyEvent.VK_1;
                break;
            case AZERTY:
                panUpKey = KeyEvent.VK_Z;
                panDownKey = KeyEvent.VK_S;
                panLeftKey = KeyEvent.VK_Q;
                panRightKey = KeyEvent.VK_D;
                increaseSimSpeedKey = KeyEvent.VK_X;
                decreaseSimSpeedKey = KeyEvent.VK_W;
                increaseZoomKey = KeyEvent.VK_E;
                decreaseZoomKey = KeyEvent.VK_A;
                centerOnBodyKey = KeyEvent.VK_C;
                addOrbitingBodyKey = KeyEvent.VK_1;
                break;
            case QWERTZ:
                panUpKey = KeyEvent.VK_W;
                panDownKey = KeyEvent.VK_S;
                panLeftKey = KeyEvent.VK_A;
                panRightKey = KeyEvent.VK_D;
                increaseSimSpeedKey = KeyEvent.VK_X;
                decreaseSimSpeedKey = KeyEvent.VK_Y;
                increaseZoomKey = KeyEvent.VK_E;
                decreaseZoomKey = KeyEvent.VK_Q;
                centerOnBodyKey = KeyEvent.VK_C;
                addOrbitingBodyKey = KeyEvent.VK_1;
                break;
            case COLEMAK:
                panUpKey = KeyEvent.VK_W;
                panDownKey = KeyEvent.VK_R;
                panLeftKey = KeyEvent.VK_A;
                panRightKey = KeyEvent.VK_S;
                increaseSimSpeedKey = KeyEvent.VK_X;
                decreaseSimSpeedKey = KeyEvent.VK_Z;
                increaseZoomKey = KeyEvent.VK_F;
                decreaseZoomKey = KeyEvent.VK_Q;
                centerOnBodyKey = KeyEvent.VK_C;
                addOrbitingBodyKey = KeyEvent.VK_1;
                break;
        }
    }

    // Returns the name of the key assigned to a certain action in the simulation
    // Used to show user controls consistent with their chosen keyboard layout
    public static String getKeyName(int keyAction) {
        return getKeyName(keyAction);
    }
}
