import processing.core.PApplet;
import processing.core.PFont;


public class MenuText {

    Window testWindow = new Window();

    public int xPos;    // X position of the top left corner of the text box
    public int yPos;    // Y position of the top left corner of the text box
    public int width;   // Width of the text box in pixels
    public int height;  // Height of the text box in pixels
    public String text; // Text to be displayed in the text box
    public int colour;  // Colour of the text
    public int size;    // Size of the text font

    public MenuText(int xPosition, int yPosition, int textBoxWidth, int textBoxHeight, String displayedText, int textColour) {
        xPos = xPosition;
        yPos = yPosition;
        width = textBoxWidth;
        height = textBoxHeight;
        text = displayedText;
        colour = textColour;
        size = height;
    }
}
