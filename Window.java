import processing.core.PApplet;
import processing.core.PFont;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;

public class Window extends PApplet {

    public static int WINDOW_WIDTH = 1500;
    public static int WINDOW_HEIGHT = 1000;

    public static float infoBoxWidth = Window.WINDOW_WIDTH / 5;
    public static float infoBoxHeight = Window.WINDOW_HEIGHT / 20;

    public static DecimalFormat velocityFormat = new DecimalFormat("0.000"); // Used for formatting velocity
    public static DecimalFormat timeFormat = new DecimalFormat("00"); // Used for formatting time output

    public void setup() {
        noLoop();
        PFont font = createFont("Arial", 12, true);
    }

    public void settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void draw() {
        float radiusDrawSize;
        background(0);
        List<Body> bodiesToDraw = Main.currentBodies;
        stroke(0x00000000);
        // Draw celestial bodies
        for (Body body: bodiesToDraw) {
            radiusDrawSize = (float) (body.radius * Main.SCALE);
            if (radiusDrawSize < 2) {
                radiusDrawSize = 2;
            }
            fill(body.colour);
            ellipse((float) ((WINDOW_WIDTH / 2) + body.position.x * Main.SCALE), (float) ((WINDOW_HEIGHT / 2) - body.position.y * Main.SCALE),
                    radiusDrawSize, radiusDrawSize);
        }
        // Draw selected body menu (if there IS a selected body)
        if (Main.selectedBody != null) {
            fill(0x99808080);
            rect(WINDOW_WIDTH - infoBoxWidth, WINDOW_HEIGHT - infoBoxHeight, infoBoxWidth, infoBoxHeight);
            fill(0);
            textAlign(CENTER);
            text(Main.selectedBody.name, Window.WINDOW_WIDTH - (infoBoxWidth / 2), WINDOW_HEIGHT - infoBoxHeight + 20);
            text("Velocity: " + velocityFormat.format(Main.selectedBody.velocity.getMagnitude()) + " m/s", WINDOW_WIDTH - (infoBoxWidth / 2), WINDOW_HEIGHT - infoBoxHeight + 40);
        }
        if (Main.addingOrbitingBody ) {
            Vector mousePos = new Vector(mouseX, mouseY);
            mousePos.toWorldSpace();
            Vector planetPos = new Vector(Main.selectedBody.position.x, Main.selectedBody.position.y);
            planetPos.toScreenSpace();
            double radius = Main.selectedBody.position.getDifference(mousePos).getMagnitude() * Main.SCALE * 2;
            noFill();
            stroke(0xFFFFFFFF);
            ellipse((float) ((WINDOW_WIDTH / 2) + Main.selectedBody.position.x * Main.SCALE),
                    (float) ((WINDOW_HEIGHT / 2) - Main.selectedBody.position.y * Main.SCALE),
                    (float) radius, (float) radius);
        }
    }

    public void mousePressed() {
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
                System.out.println(Main.selectedBody.name + " selected, now choose where to add orbiting body");
            } else {
                Main.selectingOrbitedBody = false;
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
            System.out.println("Selecting orbited body");
            Main.selectingOrbitedBody = true;
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