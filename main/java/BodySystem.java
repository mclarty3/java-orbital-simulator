package main.java;

import java.util.ArrayList;
import java.util.List;

public class BodySystem {

    List<Body> simulatedBodies;
    String name;


    public BodySystem(String sysName, Body... bodies) {
        name = sysName;

        simulatedBodies = new ArrayList<>();
        for (Body body : bodies) {
            System.out.println("Adding " + body.name + " to " + sysName);
            simulatedBodies.add(body);
        }
    }

    static BodySystem solarSystem = new BodySystem("Solar System",
            new Body("Sun", 1.989e30, 0, 0, 0, 0, 695.51e6, 0xFFECD67E),
            new Body("Mercury", 0.330e24, -57.9e9, 0, 0, -47.4e3, 2439, 0xFFFFFF00),
            new Body("Venus", 4.87e24, -108.2e9, 0, 0, -35e3, 6057, 0xFF8BB7AB),
            new Body("Earth", 5.97e24, 1 * Main.AU, 0, 0, 29.8e3, 6378, 0xFF8CB1DE),
            new Body("Mars", 0.642e24, -227.9e9, 0, 0, -24.1e3, 3396, 0xFFFF0000),
            new Body("Jupiter", 1898e24, 778.6e9, 0, 0, 13.1e3, 71492, 0xFFA79C86),
            new Body("Saturn", 568e24, -1433.5e9, 0, 0, -9.7e3, 60268, 0xFF343E47),
            new Body("Uranus", 86.8e24, 2872.5e9, 0, 0, 6.8e3, 25509, 0xFFD5FBFC),
            new Body("Neptune", 102e24, 0, 4495.1e9, -5.4e3, 0, 24764, 0xFF3e54E8));

    static BodySystem alphaCentauri = new BodySystem("Alpha Centauri",
            new Body("Alpha Centauri A", 1.1 * 1.989e30, -(10e11 / 2), 0, 0, -4.88e3, 1.223 * 695.51e6, 0xFFECD67E),
            new Body("Alpha Centauri B", .907 * 1.989e30, 10e11 / 2, 0, 0, 4.88e3, 0.863 * 695.51e6, 0xFFFFA500));
            //new Body ("Alpha Centauri C", 0, 0, 0, 0, 0, 0, 0xFFFFFFFF));*/
}
