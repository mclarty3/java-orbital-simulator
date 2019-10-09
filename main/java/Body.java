package main.java;

import java.lang.Math;

public class Body {

    String name;                    // Body's name
    private double mass;                    // Body's mass in kilograms
    Vector velocity = new Vector(); // Body's velocity vector in meters per second
    Vector position = new Vector(); // Body's position vector in meters
    private Vector force = new Vector();
    double radius;                  // Body's radius in meters
    int colour;                     // Body's colour in hexadecimal: 0xFFFFFFFF (first two digits represent opacity)

    Body(String bodyName, double bodyMass, double initXPos, double initYPos,
         double initXVel, double initYVel, double bodyRadius, int bodyColour) {
        name = bodyName;
        mass = bodyMass;
        velocity.x = initXVel;
        velocity.y = initYVel;
        position.x = initXPos;
        position.y = initYPos;
        radius = bodyRadius;
        colour = bodyColour;
    }

    // Returns true if the calling body is the same as other
    private boolean isSameAs(Body other) {
        return ((name.equals(other.name)) &&
                (mass == other.mass) &&
                (velocity.equals(other.velocity)) &&
                (position.equals(other.position)));
    }

    // Updates the position of the body based on its velocity
    private void updatePosition(double seconds) {
        position.x += velocity.x * seconds;
        position.y += velocity.y * seconds;
    }

    // Updates the velocity of the body based on force acting on it
    private void updateVelocity(double seconds) {
        velocity.x += ((force.x / mass) * seconds);
        velocity.y += ((force.y / mass) * seconds);
    }

    // Returns the distance between this body and another body in vector form
    private Vector getDistance(Body other) {
        Vector temp = new Vector();
        temp.x = other.position.x - position.x;
        temp.y = other.position.y - position.y;
        return temp;
    }

    // Returns the force of gravity acting on this body by other body
    private Vector getForceG(Body other) {
        Vector temp = new Vector();
        Vector distance = getDistance(other);
        double force = ((Main.G * mass * other.mass) / Math.pow(distance.getMagnitude(), 2));
        double theta = Math.atan2(distance.y, distance.x);
        if ((theta <= 0.1) && (theta >= -0.1)) {
            temp.x = force;
            temp.y = 0;
        } else if ((theta <= 3.142) && (theta >= 3.141)) {
            temp.x = -force;
            temp.y = 0;
        } else if ((theta <= 1.59) && (theta >= 1.55)) {
            temp.x = 0;
            temp.y = force;
        } else if ((theta <= -1.55) && (theta >= -1.59)) {
            temp.x = 0;
            temp.y = -force;
        }
        else {
            temp.x = force * Math.cos(theta);
            temp.y = force * Math.sin(theta);
        }
        return temp;
    }

    // Prints the name, position, and velocity of a body to the console
    public void printInfo() {
        System.out.println(name + "\tPosition: (" + position.x + ", " + position.y + ")\tVelocity: (" + velocity.x + ", " + velocity.y + ")\tTOTAL VELOCITY: " + velocity.getMagnitude());
    }

    // Calculates the force of gravity acting on all planets by all other planets
    // and updates the velocities and positions accordingly
    static void runFramePhysics(double timeStep) {
        for (Body body: Main.currentBodies) {
            for (Body other: Main.currentBodies) {
                if (!body.isSameAs(other)) {
                    body.force.addEquals(body.getForceG(other));
                }
            }
            body.updateVelocity(timeStep);
            body.updatePosition(timeStep);
            body.force.reset();
        }
    }

    // Gets the index of a body in the simulated body array if it exists
    // Used for removing body from the simulation
    // Return value of -1 means body not found in array
    private static int findBodyInSim(Body body) {
        for (int i = 0; i < Main.currentBodies.size(); i++) {
            if (Main.currentBodies.get(i).isSameAs(body)) {
                return i;
            }
        }
        return -1;
    }

    // Adds the calling body to the simulated bodies array, spawning it in the simulation
    private void addToSimulation() {
        Main.currentBodies.add(this);
    }

    // Removes a body from the simulation
    public void removeFromSimulation() {
        int search = findBodyInSim(this);
        if (search != -1) {
            Main.currentBodies.remove(search);
        }
        else {
            System.out.println("Body not found in simulation!");
        }
    }

    // PROBLEM: Orbiting body's don't orbit circularly unless placed pretty much right on the y axis of the body
    // Adds a body orbiting bodyToOrbit at the correct velocity relative to its distance from bodyToOrbit
    static void addOrbitingBody(double x, double y, Body bodyToOrbit, double radius) {
        // Holds the distance vector between the new body and the body it's orbiting
        Vector distance = bodyToOrbit.position.getDifference(new Vector(x, y));

        double velocity = Math.sqrt((Main.G * bodyToOrbit.mass) / (distance.getMagnitude()));

        // Normalizes the distance vector to be multiplied by the magnitude of the velocity
        distance.normalize();

        // Holds the velocity vector of the orbiting planet
        Vector velocityVec = new Vector(distance.y * -1, distance.x * 1).times(velocity);
        // Adds the parent body's velocity to account for its movement
        velocityVec.x += bodyToOrbit.velocity.x;
        velocityVec.y += bodyToOrbit.velocity.y;

        // Adds the orbiting body to the simulation
        Body bodyToAdd = new Body("New body", 100, x, y, velocityVec.x, velocityVec.y, radius, 0xFFFFFFFF);
        bodyToAdd.addToSimulation();
    }

    // Pans the camera in the specified direction
    // Parameter must be "left", "right", "up", or "down"
    private static void pan(String direction) {
        if (direction.equals("left")) {
            for (Body body : Main.currentBodies) {
                body.position.x += 1000 * Main.AU_TO_WINDOW_SCALAR;
            }
        } else if (direction.equals("right")) {
            for (Body body: Main.currentBodies) {
                body.position.x -= 1000 * Main.AU_TO_WINDOW_SCALAR;
            }
        } else if (direction.equals("up")) {
            for (Body body: Main.currentBodies) {
                body.position.y -= 1000 * Main.AU_TO_WINDOW_SCALAR;
            }
        } else if (direction.equals("down")) {
            for (Body body: Main.currentBodies) {
                body.position.y += 1000 * Main.AU_TO_WINDOW_SCALAR;
            }
        } else {
            System.out.println("Function pan() must take parameter of form \"left\", \"right\", \"up\", or \"down\"");
        }
        Main.focusedOnBody = false;
    }

    // Takes a change in screen coordinates and converts and applies it to a simulation-scale pan
    // Screen should follow mouse movement
    static void panXY(float mouseX, float mouseY) {
        Vector movement = new Vector(mouseX, mouseY);

        for (Body body: Main.currentBodies) {
            body.position.x += (movement.x / Main.SCALE);
            body.position.y -= (movement.y / Main.SCALE);
        }
    }

    // Checks if one of the four booleans is true
    // If so: executes pan() in that direction
    static void checkPan(boolean up, boolean down, boolean left, boolean right) {
        if (up) {
            pan("up");
        }
        if (left) {
            pan("left");
        }
        if (right) {
            pan("right");
        }
        if (down) {
            pan("down");
        }
        Main.window.redraw();
    }

    // Returns a body if the cursor is hovering over that body, returns null otherwise
    static Body checkMouseOnBody(float x, float y) {
        Vector mousePos = new Vector((x - Window.WINDOW_WIDTH/2.0), (Window.WINDOW_HEIGHT/2.0 - y));
        for (Body body: Main.currentBodies) {
            if (body.position.times(Main.SCALE).getDifference(mousePos).getMagnitude() <= (body.radius * Main.SCALE)) {
                return body;
            }
        }
        return null;
    }

    // Changes the camera zoom by increasing/decreasing simulation scale
    // Enter 1 to increase zoom, -1 to decrease zoom, or 0 to reset zoom
    // Also note that I'm incrementing/decrementing the scalar by stupidly small values because of how quickly
    // the command gets run in the main game loop
    static void zoom(int zoomType) {
        // Zoom out
        if (zoomType == -1) {
            Main.AU_TO_WINDOW_SCALAR *= 1.0000001;
        }
        // Zoom in
        else if (zoomType == 1) {
            Main.AU_TO_WINDOW_SCALAR /= 1.0000001;
        }
        // Reset zoom to simulation default
        else if (zoomType == 0) {
            Main.AU_TO_WINDOW_SCALAR = 5;
        }
        Main.SCALE = Window.WINDOW_WIDTH / (Main.AU_TO_WINDOW_SCALAR * Main.AU);
    }

    static void newZoom(int zoomType) {
        // Zoom out
        if (zoomType == -1) {
            Main.AU_TO_WINDOW_SCALAR *= 1.1;
        }
        // Zoom in
        else if (zoomType == 1) {
            Main.AU_TO_WINDOW_SCALAR /= 1.1;
        }
        // Reset zoom to simulation default
        else if (zoomType == 0) {
            Main.AU_TO_WINDOW_SCALAR = 5;
        }
        Main.SCALE = Window.WINDOW_WIDTH / (Main.AU_TO_WINDOW_SCALAR * Main.AU);
    }

    // Similar to checkPan -- checks if the user is zooming in or out to allow for smooth zooming
    static void checkZoom(boolean in, boolean out) {
        if (in) {
            zoom(1);
        } else if (out) {
            zoom(-1);
        }
    }

    // Returns a string with the current time-scale in a respectable format
    public static String getTimeScaleString() {
        String timeWord;
        double modifiedTimeScale;
        if (Main.TIMESCALE > 365 * 100) {
            timeWord = " centuries";
            modifiedTimeScale = (Main.TIMESCALE / 365) / 100;
        } else if (Main.TIMESCALE > 365 * 10) {
            timeWord = " decades";
            modifiedTimeScale = (Main.TIMESCALE / 365) / 10;
        } else if (Main.TIMESCALE > 365) {
            timeWord = " years";
            modifiedTimeScale = (Main.TIMESCALE / 365);
        } else if (Main.TIMESCALE > 1) {
            timeWord = " days";
            modifiedTimeScale = Main.TIMESCALE;
        } else if (Main.TIMESCALE > (1.0 / 24.0)) {
            timeWord = " hours";
            modifiedTimeScale = Main.TIMESCALE * 24;
        } else if (Main.TIMESCALE > (1.0 / 24.0 / 60.0)) {
            timeWord = " minutes";
            modifiedTimeScale = Main.TIMESCALE * 24 * 60;
        } else {
            modifiedTimeScale = Main.TIMESCALE * 24 * 3600;
            timeWord = " seconds";
        }
        return(modifiedTimeScale + timeWord + " per second");
    }

    // Pans the camera to place the focused body in the center of the screen
    static void focusBody(Body focused) {
        double xOffset = focused.position.x;
        double yOffset = focused.position.y;
        for (Body body: Main.currentBodies) {
            body.position.x -= xOffset;
            body.position.y -= yOffset;
        }
    }
}


