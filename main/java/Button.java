package main.java;

import org.jetbrains.annotations.Nullable;

public class Button {

    // Holds all possible actions that a button can do (interpreted in Window.java)
    enum action {
        RETURN,
        QUIT,
        OPEN_MENU,
        CHANGE_RESOLUTION,
        SCROLL_LEFT,
        SCROLL_RIGHT,
        CHANGE_LAYOUT
    }

    float width;          // Width of the button in pixels
    float height;         // Height of the button in pixels
    float xPos;           // X position of the top left corner of the button
    float yPos;           // Y position of the top left corner of the button
    int colour;           // Colour of the button background
    String text;          // Text to be displayed in the button
    int textColour;       // Colour of the button text
    int borderColour;     // Border colour of the button
    boolean highlighted;  // Holds whether button is currently highlighted by cursor

    private action onButtonPress; // Action to be done in simulation/menu when button is pressed

    Menu nextMenu;


    // If this button does not open up another menu, pass null to goToMenu. Simple as that!
    public Button(float buttonWidth, float buttonHeight, float xPosition, float yPosition,
                  String buttonText, int buttonTextColour, int buttonColour, int buttonBorderColour,
                  action buttonPressAction, @Nullable Menu goToMenu) {
        width = buttonWidth;
        height = buttonHeight;
        xPos = xPosition;
        yPos = yPosition;
        colour = buttonColour;
        text = buttonText;
        textColour = buttonTextColour;
        borderColour = buttonBorderColour;
        onButtonPress = buttonPressAction;
        nextMenu = goToMenu;
    }

    // Returns true if the provided (x,y) coordinates are located within the calling button
    boolean mouseOnButton(int x, int y) {
        return((x >= xPos) && (x <= xPos + width) && (y >= yPos) && (y <= yPos + height));
    }

    // Changes the colour of the button to indicate the mouse is hovering over it
    void highlightColour() {
        highlighted = true;
        String hexString = Integer.toHexString(colour);
        String convert;
        int hexToDec;
        String finalHex = "FF";

        for (int i = 2; i < 7; i += 2) {
            convert = hexString.charAt(i) + "" + hexString.charAt(i + 1);
            hexToDec = Integer.parseInt(convert, 16);
            hexToDec *= 2;
            finalHex = finalHex.concat(Integer.toHexString(hexToDec));
        }
        long longHex = Long.parseLong(finalHex, 16);
        colour = (int)longHex;
    }

    // Returns the colour of a button to its normal colour after being highlighted
    void unHighlightColour() {
        highlighted = false;
        String hexString = Integer.toHexString(colour);
        String convert;
        int hexToDec;
        String finalHex = "FF";

        for (int i = 2; i < 7; i += 2) {
            convert = hexString.charAt(i) + "" + hexString.charAt(i + 1);
            hexToDec = Integer.parseInt(convert, 16);
            hexToDec /= 2;
            finalHex = finalHex.concat(Integer.toHexString(hexToDec));
        }
        long longHex = Long.parseLong(finalHex, 16);
        colour = (int)longHex;
    }

    // Returns a button's respective action
    action getAction() {
        return onButtonPress;
    }
}
