package main.java;

class Vector {

    double x;
    double y;

    Vector() {
        x = 0;
        y = 0;
    }

    Vector(double initX, double initY) {
        x = initX;
        y = initY;
    }

    // Adds another vectors components to this vector's components
    void addEquals(Vector other) {
        x += other.x;
        y += other.y;
    }

    // Returns a scalar multiple of the calling vector and the scalar n
    Vector times(double n) {
        return new Vector(x * n, y * n);
    }

    // Normalizes calling vector (reduces to magnitude = 1)
    void normalize() {
        double mag = getMagnitude();
        x /= mag;
        y /= mag;
    }

    // Sets both components equal to 0
    void reset() {
        x = 0;
        y = 0;
    }

    // Returns a vector pointing from the calling vector towards other
    Vector getDifference(Vector other) {
        return new Vector(other.x - x, other.y - y);
    }

    // Returns the magnitude of the vector (the square root of the sum of its components squared)
    double getMagnitude() {
        return Math.sqrt((x * x) + (y * y));
    }

    // Returns true if this vector is equal to other, false otherwise
    boolean equals(Vector other) {
        return ((x == other.x) && (y == other.y));
    }

    // Converts a vector from screen coordinates to world coordinates
    void toWorldSpace() {
        x = (x - Window.WINDOW_WIDTH / 2.0) / Main.SCALE;
        y = ( Window.WINDOW_HEIGHT / 2.0 - y) / Main.SCALE;
    }

    // Converts a vector from world coordinates to screen coordinates
    void toScreenSpace() {
        x = (Window.WINDOW_WIDTH / 2.0) + (x * Main.SCALE);
        y = (Window.WINDOW_HEIGHT / 2.0) + (y * Main.SCALE);
    }

    // Prints the vectors components in <x, y> format
    void print() {
        System.out.println("<" + x + ", " + y + ">");
    }
}