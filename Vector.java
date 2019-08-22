public class Vector {

    double x;
    double y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(double initX, double initY) {
        x = initX;
        y = initY;
    }

    // Adds another vectors components to this vector's components
    public void addEquals(Vector other) {
        x += other.x;
        y += other.y;
    }

    public Vector times(double n) {
        Vector temp = new Vector(x * n, y * n);
        return temp;
    }

    public void normalize() {
        double mag = getMagnitude();
        x /= mag;
        y /= mag;
    }

    // Sets both components equal to 0
    public void reset() {
        x = 0;
        y = 0;
    }

    public Vector getDifference(Vector other) {
        Vector temp = new Vector(other.x - x, other.y - y);
        return temp;
    }

    // Returns the magnitude of the vector (the square root of the sum of its components squared)
    public double getMagnitude() {
        return Math.sqrt((x * x) + (y * y));
    }

    // Returns true if this vector is equal to other, false otherwise
    public boolean equals(Vector other) {
        return ((x == other.x) && (y == other.y));
    }

    // Converts a vector from screen coordinates to world coordinates
    public void toWorldSpace() {
        x = (x - Window.WINDOW_WIDTH / 2.0) / Main.SCALE;
        y = ( Window.WINDOW_HEIGHT / 2.0 - y) / Main.SCALE;
    }

    // Converts a vector from world coordinates to screen coordinates
    public void toScreenSpace() {
        x = (Window.WINDOW_WIDTH / 2.0) + (x * Main.SCALE);
        y = (Window.WINDOW_HEIGHT / 2.0) + (y * Main.SCALE);
    }

    // Prints the vectors components in <x, y> format
    public void print() {
        System.out.println("<" + x + ", " + y + ">");
    }
}