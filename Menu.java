import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {

    public int bgColour;                                 // Background colour for the menu
    public List<Button> menuButtons = new ArrayList<>(); // List of buttons to be displayed in the menu
    public Menu menuToReturnTo;                          // Menu to display (if any) when this menu is exited

    public boolean isDisplayed = false;

    public Menu(int backgroundColour, Button... buttons) {
        bgColour = backgroundColour;
        menuButtons.addAll(Arrays.asList(buttons));
    }

    // Checks if the given (x,y) mouse coordinates are hovering over one of the buttons in the menu
    // If the mouse is on a button, it highlights it. Otherwise, it leaves it un-highlighted
    public void checkButtonHighlights(int x, int y) {
        for (Button button: menuButtons) {
            if (button.isHighlighted() && !button.mouseOnButton(x, y)) {
                button.unHighlightColour();
            }
            else if (!button.isHighlighted() && button.mouseOnButton(x, y)) {
                button.highlightColour();
            }
        }
    }

    public void addButtons(Button... buttons) {
        menuButtons.addAll(Arrays.asList(buttons));
    }


}
