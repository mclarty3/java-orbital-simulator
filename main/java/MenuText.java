package main.java;

class MenuText {

    int xPos;    // X position of the top left corner of the text box
    int yPos;    // Y position of the top left corner of the text box
    int width;   // Width of the text box in pixels
    int height;  // Height of the text box in pixels
    String text; // Text to be displayed in the text box
    int colour;  // Colour of the text
    int size;    // Size of the text font

    MenuText(int xPosition, int yPosition, int textBoxWidth, int textBoxHeight, String displayedText, int textColour) {
        xPos = xPosition;
        yPos = yPosition;
        width = textBoxWidth;
        height = textBoxHeight;
        text = displayedText;
        colour = textColour;
        size = height;
    }
}
