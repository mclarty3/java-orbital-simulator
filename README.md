# java-orbital-simulator
A very informal README

This is a pretty basic 2-D physics-based orbital simulator (in-progress). Each frame, every body inflicts a force of gravity on 
every other body, then the bodies update their positions and velocities appropriately.

For graphics, I used Java processing, and put together some primitive GUI elements (buttons, menus, etc.). It's in
need of some polishing, but it's a start.

Currently, it's just the solar system in the simulation. But, it's built to support a bunch more bodies, and I intend to add 
the ability to have multiple systems that can be switched between. There's a lot of potential for content to be added.

# Running the simulation
If you want to *run* this, you can clone/download the repo and navigate to orbitalsim/target and execute the .jar file there.


# Controls
In the options menu, there's a sub-menu for choosing your keyboard layout. This will change the simulator input such that the 
controls match the shape of the QWERTY layout (e.g: pan up/left/down/right are binded to WASD). Or, you could just switch your
system layout to QWERTY and set the game to QWERTY, either way.

Here are the QWERTY controls:  
Pan up               - W  
Pan left             - A  
Pan down             - S  
Pan right            - D  
  
Zoom out             - Q  
Zoom in              - E  
Default zoom         - F  
  
(Un)Pause simulation - Space  
Slow down time       - Z  
Speed up time        - X  
  
Select body          - Mouse 1  
  
Center body          - C  
Place orbiting body  - 1 [NOTE: Press 1, then select a body, then click again to place the new body]

