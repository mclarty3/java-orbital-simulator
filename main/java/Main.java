package main.java;

import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;


public class Main {

    // Constants
    static double G = 6.67e-11;  // Gravitational constant
    static double AU = 1.496e11; // Astronomical unit in meters

    // Window variables
    static Window window = new Window();
    static double AU_TO_WINDOW_SCALAR = 5; // Determines how many AU's are visible in the width of the window
    static double SCALE = Window.WINDOW_WIDTH / (AU_TO_WINDOW_SCALAR * AU); // Used to convert real-life scale to window scale for drawing

    // Simulation timing variables
    @SuppressWarnings("FieldCanBeLocal")
    private static double fps = 1000; // Turns out if I make this ridiculously high, the simulation runs much better!
    static boolean paused = false; // Holds if the simulation is paused
    static double TIMESCALE = 10; // Time scale at simulation start in days per second
    static boolean changingTime = false; // Prevents the time scale from changing more than once per key press

    // Selected body
    static Body selectedBody = null;      // The currently selected body (displays info in bottom right)
    static boolean focusedOnBody = false; // Holds whether a body is currently centered
    static Body focusedBody = null;       // Body currently being centered on the screen

    // Add orbiting body
    static boolean selectingOrbitedBody = false; // Holds whether player is selecting body to place orbiting body around
    static boolean addingOrbitingBody = false;   // Holds whether player is positioning orbiting body

    // These variables keep track of if the screen is panning, to allow for simultaneous multi-directional panning
    static boolean panningUp = false;
    static boolean panningDown = false;
    static boolean panningLeft = false;
    static boolean panningRight = false;
    //public static boolean panning; // To be used for future mouse panning, stay tuned!

    // These variables keep track of if the camera is zooming, for smooth zooming in/out
    static boolean zoomingIn = false;
    static boolean zoomingOut = false;

    // Stores all bodies currently active in the simulation
    static List<Body> currentBodies = new ArrayList<>();

    public static void main(String[] args) {
        DateTime startDate = new DateTime(0, 0, 0, 0, 0, 0, 2000);

        KBInput.setKeyboardLayout(KBInput.keyboardLayouts.DVORAK); // Dvorak is the superior layout, so it's the default
        // Opens window for simulation
        String[] windowName = {""};
        PApplet.runSketch(windowName, window);

        // Creates body in simulation and adds them to list of bodies
        /*Body sun = new Body("Sun", 1.989e30, 0, 0, 0, 0, 695.51e6, 0xFFECD67E);
        Body mercury = new Body("Mercury", 0.330e24, -57.9e9, 0, 0, -47.4e3, 2439, 0xFFFFFF00);
        Body venus = new Body("Venus", 4.87e24, -108.2e9, 0, 0, -35e3, 6057, 0xFF8BB7AB);
        Body earth = new Body("Earth", 5.97e24, 1 * AU, 0, 0, 29.8e3, 6378, 0xFF8CB1DE);
        //Body moon = new Body();
        Body mars = new Body("Mars", 0.642e24, -227.9e9, 0, 0, -24.1e3, 3396, 0xFFFF0000);
        Body jupiter = new Body("Jupiter", 1898e24, 778.6e9, 0, 0, 13.1e3, 71492, 0xFFA79C86);
        Body saturn = new Body("Saturn", 568e24, -1433.5e9, 0, 0, -9.7e3, 60268, 0xFF343E47);
        Body uranus = new Body("Uranus", 86.8e24, 2872.5e9, 0, 0, 6.8e3, 25509, 0xFFD5FBFC);
        Body neptune = new Body("Neptune", 102e24, 0, 4495.1e9, -5.4e3, 0, 24764, 0xFF3e54E8);
        currentBodies.add(sun);
        currentBodies.add(mercury);
        currentBodies.add(venus);
        currentBodies.add(earth);
        currentBodies.add(mars);
        currentBodies.add(jupiter);
        currentBodies.add(saturn);
        currentBodies.add(uranus);
        currentBodies.add(neptune);*/
        currentBodies = BodySystem.alphaCentauri.simulatedBodies;


        // Starts timer to keep track of frame timing
        long currentTime = System.currentTimeMillis();
        long frameTime = System.currentTimeMillis();

        // Keep tracks of how many frames run per second
        int frames = 0;

        // Pauses simulation when the game starts, until started by the user
        paused = true;

        // Main loop
        while (!window.finished) {
            // This if statement runs a certain number of times a second, dependent on the value of fps
            if (System.currentTimeMillis() - currentTime >= (1000 / fps)) {
                // Runs simulation physics if not paused
                if (!paused) {
                    Body.runFramePhysics(((24 * 3600) / fps) * TIMESCALE);
                    // Checks if simulation is centered on body
                    if (focusedOnBody && (focusedBody != null)) {
                        Body.focusBody(focusedBody);
                    }
                    startDate.addSeconds(((24 *  3600) / fps) * TIMESCALE );
                }
                window.redraw();
                // Increments frame count to keep track of how many frames are actually running per second
                frames++;
                // Resets the one second timer
                currentTime = System.currentTimeMillis();
            }

            // Applies camera pan/zoom if applicable
            Body.checkPan(panningUp, panningDown,  panningLeft, panningRight);
            Body.checkZoom(zoomingIn, zoomingOut);

            // Resets frame count
            if (System.currentTimeMillis() - frameTime >= 1000) {
                //System.out.println(frames); // Uncomment if you want to display how many frames are running per second
                frames = 0;
                frameTime = System.currentTimeMillis();
                //startDate.print();
            }
        }
    }
}
