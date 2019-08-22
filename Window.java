import processing.core.PApplet;
import processing.core.PFont;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Window extends PApplet {

    static int WINDOW_WIDTH = 1500;  // Width of the sketch window in pixels
    static int WINDOW_HEIGHT = 1000; // Height of the sketch window in pixels

    private static float infoBoxWidth = Window.WINDOW_WIDTH / 5;    // Width of body info menu in relation to window
    private static float infoBoxHeight = Window.WINDOW_HEIGHT / 20; // Height of body info menu in relation to window

    private static DecimalFormat velocityFormat = new DecimalFormat("0.000"); // Used for formatting velocity
    static DecimalFormat timeFormat = new DecimalFormat("00"); // Used for formatting time output

    private PFont font;
    private PFont buttonFont;

    /*private static List<Button> visibleButtons = new ArrayList<>(); // List of buttons on screen (for checking if user clicks on a button)
    private static List<Button> escapeMenuButtons = new ArrayList<>();

    private static boolean escapeMenuOpen = false; // Holds whether user is currently in the escape menu
    private static boolean displayingMenu = false;*/

    private static Menu currentlyDisplayedMenu;
    private static Menu escapeMenu = new Menu(0xFF000000);
    private static Menu optionsMenu = new Menu(0xFF000000);

    // Escape menu button declarations
    private static Button resumeButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT / 10, "Resume",
                                                    255, 0xFF282828, 0x00000000,
                                                    Button.action.RETURN, null);
    private static Button optionsButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT * 7 / 20, "Options",
                                                    255, 0xFF282828, 0x00000000,
                                                    Button.action.OPEN_MENU, optionsMenu);
    private static Button quitButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                WINDOW_WIDTH / 3, WINDOW_HEIGHT * 3 / 5, "Quit",
                                                255, 0xFF282828, 0x00000000,
                                                Button.action.QUIT, null);

    // Options menu button declarations
    private static Button displaySizeSetup = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                        WINDOW_WIDTH / 3, WINDOW_HEIGHT / 10, "Display size",
                                                        255, 0xFF282828, 0x00000000,
                                                        Button.action.OPEN_MENU, null);
    private static Button keyboardLayoutSetup = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                        WINDOW_WIDTH / 3, WINDOW_HEIGHT * 7 / 20, "Keyboard layout",
                                                        255, 0xFF282828, 0x00000000,
                                                        Button.action.OPEN_MENU, null);
    private static Button viewControls = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                        WINDOW_WIDTH / 3, WINDOW_HEIGHT * 3 / 5, "Controls",
                                                        255, 0xFF282828, 0x00000000,
                                                        Button.action.OPEN_MENU, null);


    public void setup() {
        // Fonts
        font = createFont("Arial", 12, true);
        buttonFont = createFont("Arial", 24, true);

        escapeMenu.addButtons(resumeButton, optionsButton, quitButton);
        optionsMenu.addButtons(displaySizeSetup, keyboardLayoutSetup, viewControls);
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void draw() {

        // Draws currently displayed menu
        if (currentlyDisplayedMenu != null) {
            // Highlights button if mouse hovering over
            currentlyDisplayedMenu.checkButtonHighlights(mouseX, mouseY);
            // Draws the menu on the screen
            displayMenu(currentlyDisplayedMenu);
        }
        else {
            background(0);
            List<Body> bodiesToDraw = Main.currentBodies;
            stroke(0x00000000);

            // Draw celestial bodies
            drawBodies(bodiesToDraw);

            // Draw selected body menu (if there IS a selected body)
            if (Main.selectedBody != null) {
                drawSelectedBodyMenu(Main.selectedBody);
            }

            if (Main.selectedBody != null && Main.addingOrbitingBody) {
                drawOrbitalPlacementCircle(Main.selectedBody);
            }
        }
    }

    // Draws each body in list of bodies on the screen, relative to their position
    private void drawBodies(List<Body> simulatedBodies) {
        float radiusDrawSize;

        for (Body body: simulatedBodies) {
            radiusDrawSize = (float) (body.radius * Main.SCALE);
            if (radiusDrawSize < 2) {
                radiusDrawSize = 2;
            }
            fill(body.colour);
            ellipse((float) ((WINDOW_WIDTH / 2) + body.position.x * Main.SCALE), (float) ((WINDOW_HEIGHT / 2) - body.position.y * Main.SCALE),
                    radiusDrawSize, radiusDrawSize);
        }
    }

    // Draws a single button on the screen
    private void drawButton(Button button) {
        fill(button.getColour());
        stroke(button.getBorderColour());
        rect(button.getXPos(), button.getYPos(),  button.getWidth(), button.getHeight());
        fill(button.getTextColour());
        stroke(0x00000000);
        textAlign(CENTER);
        textFont(buttonFont);
        text(button.getText(), button.getXPos() + (button.getWidth() / 2), button.getYPos() + (button.getHeight() / 2));
    }

    // Draws a box with information about the currently selected body in the bottom right of the screen
    private void drawSelectedBodyMenu(Body body) {
        fill(0x99808080);
        rect(WINDOW_WIDTH - infoBoxWidth, WINDOW_HEIGHT - infoBoxHeight, infoBoxWidth, infoBoxHeight);
        fill(0);
        textAlign(CENTER);
        textFont(font);
        text(body.name, Window.WINDOW_WIDTH - (infoBoxWidth / 2), WINDOW_HEIGHT - infoBoxHeight + 20);
        text("Velocity: " + velocityFormat.format(body.velocity.getMagnitude()) + " m/s", WINDOW_WIDTH - (infoBoxWidth / 2), WINDOW_HEIGHT - infoBoxHeight + 40);
    }

    // Draws a circle around selected body to determine where an added orbiting object will be placed
    private void drawOrbitalPlacementCircle(Body orbitedBody) {
        Vector mousePos = new Vector(mouseX, mouseY);
        mousePos.toWorldSpace();
        Vector planetPos = new Vector(orbitedBody.position.x, orbitedBody.position.y);
        planetPos.toScreenSpace();
        double radius = orbitedBody.position.getDifference(mousePos).getMagnitude() * Main.SCALE * 2;
        noFill();
        stroke(0xFFFFFFFF);
        ellipse((float) ((WINDOW_WIDTH / 2) + orbitedBody.position.x * Main.SCALE),
                (float) ((WINDOW_HEIGHT / 2) - orbitedBody.position.y * Main.SCALE),
                (float) radius, (float) radius);
    }

    // Opens the specified menu
    private void openMenu(Menu menu) {
        Main.paused = true;
        menu.isDisplayed = true;
        currentlyDisplayedMenu = menu;
    }

    // Closes the specified menu, returning to a previous menu or the simulation
    private void closeMenu(Menu menu) {
        menu.isDisplayed = false;
        // Goes to previous menu if it exists
        if (menu.menuToReturnTo != null) {
            openMenu(menu.menuToReturnTo);
        }
        else {
            currentlyDisplayedMenu = null;
            Main.paused = false;
        }
    }

    private void displayMenu(Menu menu) {
        background(menu.bgColour);
        for (Button button: menu.menuButtons) {
            drawButton(button);
        }
    }

    private void buttonPress(Button button) {
        Button.action action = button.getAction();
        if (action == Button.action.RETURN) {
            closeMenu(currentlyDisplayedMenu);
        }
        else if (action == Button.action.QUIT) {
            exit();
        }
        else if (action == Button.action.OPEN_MENU && button.nextMenu != null) {
            openMenu(button.nextMenu);
        }
    }

    public void mousePressed() {
        if (currentlyDisplayedMenu != null) {
            for (Button button: currentlyDisplayedMenu.menuButtons) {
                if (button.isHighlighted()) {
                    buttonPress(button);
                }
            }
        }
        else {
            if (mouseButton == RIGHT) {
                Main.addingOrbitingBody = false;
                Main.selectingOrbitedBody = false;
            } else {
                // Mouse action if adding orbiting body
                if (Main.addingOrbitingBody) {
                    Main.addingOrbitingBody = false;
                    Vector planetPos = new Vector(mouseX, mouseY);
                    planetPos.toWorldSpace();
                    Body.addOrbitingBody(planetPos.x, planetPos.y, Main.selectedBody, 10e6);
                }
                // Selects the clicked on body in the simulation
                Main.selectedBody = Body.checkMouseOnBody(mouseX, mouseY);
                // Checks if player is selecting a body to add an orbiting body around
                if (Main.selectingOrbitedBody) {
                    if (Main.selectedBody != null) {
                        Main.selectingOrbitedBody = false;
                        Main.addingOrbitingBody = true;
                    } else {
                        Main.selectingOrbitedBody = false;
                    }
                }
            }
        }
    }

    public void mouseReleased() {
        Main.panning = false;
    }

    public void keyPressed() {
        if (keyCode == KBInput.panUpKey) {
            Main.panningUp = true;
        }
        else if (keyCode == KBInput.panDownKey) {
            Main.panningDown = true;
        }
        else if (keyCode == KBInput.panLeftKey) {
            Main.panningLeft = true;
        }
        else if (keyCode == KBInput.panRightKey) {
            Main.panningRight = true;
        }
        else if (keyCode == KeyEvent.VK_SPACE) {
            Main.paused = !Main.paused;
        }
        else if (keyCode == KBInput.increaseSimSpeedKey) {
            if (!Main.changingTime) {
                Main.TIMESCALE *= 1.5;
                Main.changingTime = true;
                //System.out.println(Main.getTimeScaleString());
            }
        }
        else if (keyCode == KBInput.decreaseSimSpeedKey) {
            if (!Main.changingTime) {
                Main.TIMESCALE /= 1.5;
                Main.changingTime = true;
                //System.out.println(Main.getTimeScaleString());
            }
        }
        else if (keyCode == KBInput.increaseZoomKey) {
            Main.zoomingIn = true;
        }
        else if (keyCode == KBInput.decreaseZoomKey) {
            Main.zoomingOut = true;
        }
        else if (keyCode == KeyEvent.VK_U) {
            Body.zoom(0);
        }
        else if (keyCode == KBInput.centerOnBodyKey) {
            if (Main.selectedBody != null) {
                Main.focusedOnBody = true;
                Main.focusedBody = Main.selectedBody;
                Body.focusBody(Main.focusedBody);
            }
        }
        else if (keyCode == KBInput.addOrbitingBodyKey) {
            Main.selectingOrbitedBody = true;
        }
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            key = 0; // Prevents Processing from closing the sketch (as it typically does when ESC is pushed)
            // Opens escape menu
            if (!escapeMenu.isDisplayed) {
                openMenu(escapeMenu);
            }
            // Closes escape menu
            else {
                closeMenu(escapeMenu);
            }
        }
    }

    public void keyReleased() {
        if (keyCode == KBInput.panUpKey) {
            Main.panningUp = false;
        }
        else if (keyCode == KBInput.panDownKey) {
            Main.panningDown = false;
        }
        else if (keyCode == KBInput.panLeftKey) {
            Main.panningLeft = false;
        }
        else if (keyCode == KBInput.panRightKey) {
            Main.panningRight = false;
        }
        else if (keyCode == KBInput.increaseSimSpeedKey || keyCode == KBInput.decreaseSimSpeedKey) {
            Main.changingTime = false;
        }
        else if (keyCode == KBInput.increaseZoomKey) {
            Main.zoomingIn = false;
        }
        else if (keyCode == KBInput.decreaseZoomKey) {
            Main.zoomingOut = false;
        }
    }
}