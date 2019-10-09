package main.java;

import processing.core.PApplet;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Window extends PApplet {

    private static int[] RESOLUTION_WIDTHS = {640, 1280, 1920}; // List of supported resolution widths
    private static int[] RESOLUTION_HEIGHTS = {480, 720, 1080}; // List of supported resolution heights
    private static int viewedResolutionIndex = 1; // Holds which resolution is currently viewed in resolution menu

    static int WINDOW_WIDTH = 1280;  // Width of the sketch window in pixels
    static int WINDOW_HEIGHT = 720; // Height of the sketch window in pixels

    private static float infoBoxWidth = Window.WINDOW_WIDTH / 5;    // Width of body info menu in relation to window
    private static float infoBoxHeight = Window.WINDOW_HEIGHT / 10; // Height of body info menu in relation to window

    private static DecimalFormat velocityFormat = new DecimalFormat("0.000"); // Used for formatting velocity
    static DecimalFormat timeFormat = new DecimalFormat("00"); // Used for formatting time output

    private PFont font;       // Default font used for drawing text
    private PFont buttonFont; // Font used for drawing buttons

    private boolean stayPaused; // Tracks whether simulation is paused before menu is opened

    private boolean panning; // Tracks whether the user is panning with the mouse

    private static Menu currentlyDisplayedMenu; // Holds the menu currently on screen
    private static List<Menu> menus = new ArrayList<>(); // Holds all menus in the simulation
    private static Stack<Menu> previousMenus = new Stack<>(); // Holds the previous menu(s) to return to (aren't I clever!)
    private static Menu startMenu = new Menu(0xFF000000); // Start menu opened on simulation startup
    private static Menu escapeMenu = new Menu(0xFF000000); // Menu opened when user pushes escape
    private static Menu optionsMenu = new Menu(0xFF000000); // Options menu
    private static Menu videoOptionsMenu = new Menu(0xFF000000); // Menu for changing resolution
    private static Menu keyboardLayoutMenu = new Menu(0xFF000000); // Menu for changing keyboard layout

    // Start menu button declarations
    private static Button playButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT / 10, "Play",
                                                    255, 0xFF282828, 0x00000000,
                                                    Button.action.PLAY, null);

    private static Button chooseSystemButton = new Button(WINDOW_WIDTH / 5, WINDOW_HEIGHT / 5,
                                                            WINDOW_WIDTH * 21 / 30, WINDOW_HEIGHT / 10, "Choose system",
                                                            255, 0xFF282828, 0x00000000,
                                                            Button.action.OPEN_MENU, null);
    private static Button returnToStartButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                            WINDOW_WIDTH / 3, WINDOW_HEIGHT * 3 / 5, "Quit to main menu",
                                                            255, 0xFF282828, 0x00000000,
                                                            Button.action.OPEN_MENU, startMenu);

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
                                                        Button.action.OPEN_MENU, videoOptionsMenu);
    private static Button keyboardLayoutSetup = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                        WINDOW_WIDTH / 3, WINDOW_HEIGHT * 7 / 20, "Keyboard layout",
                                                        255, 0xFF282828, 0x00000000,
                                                        Button.action.OPEN_MENU, keyboardLayoutMenu);
    private static Button viewControls = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 5,
                                                        WINDOW_WIDTH / 3, WINDOW_HEIGHT * 3 / 5, "Controls (doesn't work)",
                                                        255, 0xFF282828, 0x00000000,
                                                        Button.action.OPEN_MENU, null);
    private static Button exitOptionsMenu = new Button(WINDOW_WIDTH / 10, WINDOW_HEIGHT / 10,
                                                          WINDOW_WIDTH / 20, WINDOW_HEIGHT * 17 / 20, "Back",
                                                        255, 0xFF282828, 0x00000000,
                                                        Button.action.PREVIOUS_MENU, escapeMenu);

    // Video settings menu text
    private static MenuText videoSettingsText = new MenuText(WINDOW_WIDTH  * 2 / 5, WINDOW_HEIGHT / 5,
                                                            WINDOW_WIDTH / 5, WINDOW_HEIGHT / 5,
                                                            "Resolution", 255);
    private static MenuText resolutionText = new MenuText(WINDOW_WIDTH * 3 / 7, WINDOW_HEIGHT / 3,
                                                        WINDOW_WIDTH / 7, WINDOW_HEIGHT / 6,
                                                        displayResolution(viewedResolutionIndex), 255);
    private static Button incrementResolutionList = new Button(WINDOW_WIDTH / 20, (WINDOW_WIDTH / 20) * (WINDOW_WIDTH / WINDOW_HEIGHT),
                                                                WINDOW_WIDTH * 33 / 56, WINDOW_HEIGHT * 3 / 8,
                                                                "+", 255, 0xFF282828,
                                                                0x00000000, Button.action.SCROLL_RIGHT, null);
    private static Button decrementResolutionList = new Button(WINDOW_WIDTH / 20, (WINDOW_WIDTH / 20) * (WINDOW_WIDTH / WINDOW_HEIGHT),
                                                            WINDOW_WIDTH * 5 / 14, WINDOW_HEIGHT * 3 / 8,
                                                            "-", 255, 0xFF282828,
                                                            0x00000000, Button.action.SCROLL_LEFT, null);
    private static Button applyResolutionChange = new Button(WINDOW_WIDTH / 10, WINDOW_HEIGHT / 10,
                                                            WINDOW_WIDTH * 17 / 20, WINDOW_HEIGHT * 17 / 20, "Apply",
                                                            255, 0xFF282828, 0x00000000,
                                                            Button.action.CHANGE_RESOLUTION, null);
    private static Button returnToOptionsMenu = new Button(WINDOW_WIDTH / 10, WINDOW_HEIGHT / 10,
                                                            WINDOW_WIDTH / 20, WINDOW_HEIGHT * 17 / 20, "Back",
                                                            255, 0xFF282828, 0x00000000,
                                                            Button.action.PREVIOUS_MENU, optionsMenu);

    // Keyboard layout settings menu
    private static Button qwertyButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 6,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT / 36,
                                                    "QWERTY", 255, 0xFF282828,
                                                    0x00000000, Button.action.CHANGE_LAYOUT, null);
    private static Button dvorakButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 6,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT * 8 / 36,
                                                    "Dvorak", 255, 0xFF282828,
                                                    0x00000000, Button.action.CHANGE_LAYOUT, null);
    private static Button azertyButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 6,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT * 15 / 36,
                                                    "AZERTY", 255, 0xFF282828,
                                                    0x00000000, Button.action.CHANGE_LAYOUT, null);
    private static Button qwertzButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 6,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT * 22 / 36,
                                                    "QWERTZ", 255, 0xFF282828,
                                                    0x00000000, Button.action.CHANGE_LAYOUT, null);
    private static Button colemakButton = new Button(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 6,
                                                    WINDOW_WIDTH / 3, WINDOW_HEIGHT * 29 / 36,
                                                    "Colemak", 255, 0xFF282828,
                                                    0x00000000, Button.action.CHANGE_LAYOUT, null);

    // Window settings
    public void setup() {
        surface.setResizable(true);
        surface.setTitle("Java Orbital Simulator");

        // Fonts
        font = createFont("Arial", 12, true);
        buttonFont = createFont("Arial", 24, true);

        // Add buttons and texts to simulation menus
        startMenu.addButtons(playButton, chooseSystemButton, optionsButton, quitButton);
        escapeMenu.addButtons(resumeButton, optionsButton, returnToStartButton);
        optionsMenu.addButtons(displaySizeSetup, keyboardLayoutSetup, viewControls, exitOptionsMenu);
        videoOptionsMenu.addTexts(videoSettingsText, resolutionText);
        videoOptionsMenu.addButtons(returnToOptionsMenu, incrementResolutionList, decrementResolutionList, applyResolutionChange);
        keyboardLayoutMenu.addButtons(returnToOptionsMenu, qwertyButton, dvorakButton, azertyButton, qwertzButton, colemakButton);

        // Append initialized menus to list of menus
        menus.add(escapeMenu);
        menus.add(optionsMenu);
        menus.add(videoOptionsMenu);
        menus.add(keyboardLayoutMenu);

        // Displays the start menu when the simulation starts
        currentlyDisplayedMenu = startMenu;
    }

    // Sets initial resolution and window title
    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    // Draws one frame on the screen
    public void draw() {
        // Window resized
        if (WINDOW_WIDTH != width || WINDOW_HEIGHT != height) {
            for (Menu menu: menus) {
                menu.resizeComponents(WINDOW_WIDTH, WINDOW_HEIGHT, width, height);
            }
            WINDOW_WIDTH = width;
            WINDOW_HEIGHT = height;
            redraw();
        }

        // Draws currently displayed menu
        if (currentlyDisplayedMenu != null) {
            // Highlights button if mouse hovering over
            currentlyDisplayedMenu.checkButtonHighlights(mouseX, mouseY);
            // Draws the menu on the screen
            displayMenu(currentlyDisplayedMenu);
        }
        else {
            if (panning) {
                Body.panXY(mouseX - pmouseX, mouseY - pmouseY);
            }
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
        fill(button.colour);
        stroke(button.borderColour);
        rect(button.xPos, button.yPos,  button.width, button.height);
        fill(button.textColour);
        stroke(0x00000000);
        textAlign(CENTER);
        textFont(buttonFont);
        text(button.text, button.xPos + (button.width / 2), button.yPos + (button.height / 2));
    }

    // Changes size of text to fit text in specified text box size
    private void resizeText(MenuText text) {
        textSize(text.size);
        while (textWidth(text.text) > text.width) {
            text.size--;
            textSize(text.size);
        }
    }

    // Draws text on the screen
    private void drawText(MenuText text) {
        resizeText(text);
        fill(text.colour);
        stroke(0x00000000);
        textAlign(CENTER);
        textFont(font);
        textSize(text.size);
        text(text.text, text.xPos + (text.width / 2), text.yPos + (text.height * 2 / 3)); // Dunno why this works
        //drawTextDebugBox(text);
    }

    // Draws the (typically invisible) text box around drawn text for debugging purposes
    private void drawTextDebugBox(MenuText text) {
        noFill();
        stroke(255);
        rect(text.xPos, text.yPos, text.width, text.height);
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
        currentlyDisplayedMenu = menu;
    }

    // Closes the specified menu, returning to a previous menu or the simulation
    private void closeMenu(Menu menu) {
        // Goes to previous menu if it exists
        if (menu.menuToReturnTo != null) {
            openMenu(menu.menuToReturnTo);
        }
        else {
            currentlyDisplayedMenu = null;
            Main.paused = stayPaused;
        }
    }

    // Draws a menu's components on the screen
    private void displayMenu(Menu menu) {
        background(menu.bgColour);
        for (Button button: menu.menuButtons) {
            drawButton(button);
        }
        for (MenuText text: menu.menuTexts) {
            drawText(text);
        }
    }

    // Returns a string with a resolution's width and height in width x height format
    private static String displayResolution(int index) {
        return(RESOLUTION_WIDTHS[index] + "x" + RESOLUTION_HEIGHTS[index]);
    }

    // Interprets a button's action and executes a function in the simulation accordingly
    private void buttonPress(Button button) {
        Button.action action = button.getAction();
        if (action == Button.action.PLAY) {
            //previousMenus.push(currentlyDisplayedMenu);
            closeMenu(currentlyDisplayedMenu);
            Main.paused = false;
        }
        else if (action == Button.action.RETURN) {
            closeMenu(currentlyDisplayedMenu);
        }
        else if (action == Button.action.QUIT) {
            exit();
        }
        else if (action == Button.action.OPEN_MENU && button.nextMenu != null) {
            previousMenus.push(currentlyDisplayedMenu);
            openMenu(button.nextMenu);
        }
        else if (action == Button.action.PREVIOUS_MENU && previousMenus.size() != 0) {
            closeMenu(currentlyDisplayedMenu);
            openMenu(previousMenus.pop());
        }
        else if (action == Button.action.SCROLL_RIGHT && viewedResolutionIndex != 2) {
            viewedResolutionIndex++;
            resolutionText.text = displayResolution(viewedResolutionIndex);
        }
        else if (action == Button.action.SCROLL_LEFT && viewedResolutionIndex != 0) {
            viewedResolutionIndex--;
            resolutionText.text = displayResolution(viewedResolutionIndex);
        }
        else if (action == Button.action.CHANGE_RESOLUTION && WINDOW_WIDTH != RESOLUTION_WIDTHS[viewedResolutionIndex]) {
            for (Menu menu: menus) {
                menu.resizeComponents(WINDOW_WIDTH, WINDOW_HEIGHT, RESOLUTION_WIDTHS[viewedResolutionIndex], RESOLUTION_HEIGHTS[viewedResolutionIndex]);
            }
            WINDOW_WIDTH = RESOLUTION_WIDTHS[viewedResolutionIndex];
            WINDOW_HEIGHT = RESOLUTION_HEIGHTS[viewedResolutionIndex];
            surface.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        else if (action == Button.action.CHANGE_LAYOUT) {
            if (button.text.equals("QWERTY")) {
                KBInput.setKeyboardLayout(KBInput.keyboardLayouts.QWERTY);
            }
            else if (button.text.equals("Dvorak")) {
                KBInput.setKeyboardLayout(KBInput.keyboardLayouts.DVORAK);
            }
            else if (button.text.equals("AZERTY")) {
                KBInput.setKeyboardLayout(KBInput.keyboardLayouts.AZERTY);
            }
            else if (button.text.equals("QWERTZ")) {
                KBInput.setKeyboardLayout(KBInput.keyboardLayouts.QWERTZ);
            }
            else if (button.text.equals("Colemak")) {
                KBInput.setKeyboardLayout(KBInput.keyboardLayouts.COLEMAK);
            }
        }
    }

    // Runs when the mouse is pressed
    public void mousePressed() {
        if (currentlyDisplayedMenu != null) {
            for (Button button: currentlyDisplayedMenu.menuButtons) {
                if (button.highlighted) {
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
                    Body.addOrbitingBody(planetPos.x, planetPos.y, Main.selectedBody, 3e3);
                }
                else {
                    panning = true;
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
    }

    // Runs when the mouse is released
    public void mouseReleased() {
        panning = false;
    }

    //TODO: Keep mouse in consistent place in relation to simulation-space when zooming
    public void mouseWheel(MouseEvent event) {
        float mouseWheelMovement = event.getCount();
        float cameraToMouseX = (mouseX - (WINDOW_WIDTH / 2.0f));
        float cameraToMouseY = (mouseY - (WINDOW_HEIGHT / 2.0f));
        if (mouseWheelMovement < 0) {
            Body.newZoom(1); // Zoom in
        }
        else {
            Body.newZoom(-1); // Zoom out
        }
    }

    // Runs when any keyboard key is pressed
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
            if (currentlyDisplayedMenu != startMenu) {
                // Opens escape menu
                if (currentlyDisplayedMenu != escapeMenu) {
                    stayPaused = Main.paused;
                    openMenu(escapeMenu);
                }
                // Closes escape menu
                else {
                    closeMenu(escapeMenu);
                }
            }
        }
    }

    // Runs when any keyboard key is released
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