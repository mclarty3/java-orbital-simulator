package main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {

    public int bgColour;                                 // Background colour for the menu
    public List<Button> menuButtons = new ArrayList<>(); // List of buttons to be displayed in the menu
    public List<MenuText> menuTexts = new ArrayList<>(); // List of texts to be displayed in the menu
    public Menu menuToReturnTo;                          // Menu to display (if any) when this menu is exited

    public boolean isDisplayed = false; // This might be useless, but it holds whether the menu is on screen

    public Menu(int backgroundColour, Button... buttons) {
        bgColour = backgroundColour;
        menuButtons.addAll(Arrays.asList(buttons));
    }

    // Checks if the given (x,y) mouse coordinates are hovering over one of the buttons in the menu
    // If the mouse is on a button, it highlights it. Otherwise, it leaves it un-highlighted
    public void checkButtonHighlights(int x, int y) {
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
    public void addButtons(Button... buttons) {
        menuButtons.addAll(Arrays.asList(buttons));
    }

    // Adds a variable number of texts to the menu's list of texts
    public void addTexts(MenuText... texts) {
        menuTexts.addAll(Arrays.asList(texts));
    }

    // Changes the sizes and positions of all components to remain consistent with resolution change
    public void resizeComponents(int oldWidth, int oldHeight, int newWidth, int newHeight) {
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
