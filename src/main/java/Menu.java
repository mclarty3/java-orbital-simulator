package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Menu {

    int bgColour;                                 // Background colour for the menu
    List<Button> menuButtons = new ArrayList<>(); // List of buttons to be displayed in the menu
    List<MenuText> menuTexts = new ArrayList<>(); // List of texts to be displayed in the menu
    Menu menuToReturnTo;                          // Menu to display (if any) when this menu is exited

    Menu(int backgroundColour, Button... buttons) {
        bgColour = backgroundColour;
        menuButtons.addAll(Arrays.asList(buttons));
    }

    // Checks if the given (x,y) mouse coordinates are hovering over one of the buttons in the menu
    // If the mouse is on a button, it highlights it. Otherwise, it leaves it un-highlighted
    void checkButtonHighlights(int x, int y) {
        for (Button button: menuButtons) {
            if (button.highlighted && !button.mouseOnButton(x, y)) {
                button.unHighlightColour();
            }
            else if (!button.highlighted && button.mouseOnButton(x, y)) {
                button.highlightColour();
            }
        }
    }

    // Adds a variable number of buttons to the menu's list of buttons
    void addButtons(Button... buttons) {
        menuButtons.addAll(Arrays.asList(buttons));
    }

    // Adds a variable number of texts to the menu's list of texts
    void addTexts(MenuText... texts) {
        menuTexts.addAll(Arrays.asList(texts));
    }

    // Changes the sizes and positions of all components to remain consistent with resolution change
    void resizeComponents(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        float widthRatio = (float) newWidth / (float) oldWidth;
        float heightRatio = (float) newHeight / (float) oldHeight;
        for (Button button: menuButtons) {
            button.xPos *= widthRatio;
            button.width *= widthRatio;
            button.yPos *= heightRatio;
            button.height *= heightRatio;
        }
        for (MenuText text: menuTexts) {
            text.xPos *= widthRatio;
            text.width *= widthRatio;
            text.yPos *= heightRatio;
            text.height *= heightRatio;
            text.size *= widthRatio;
        }
    }
}
