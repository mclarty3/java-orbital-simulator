import org.jetbrains.annotations.Nullable;

public class Button {

    enum action {
        RETURN,
        QUIT,
        OPEN_MENU
    }

    private float width;
    private float  height;
    private float xPos;
    private float yPos;
    private int colour;
    private String text;
    private int textColour;
    private int borderColour;
    private boolean highlighted;
    private action onButtonPress;

    public Menu nextMenu;


    // If this button does not open up another menu, pass null to goToMenu. Simple as that!
    public Button(float buttonWidth, float buttonHeight, float xPosition, float yPosition,
                  String buttonText, int buttonTextColour, int buttonColour, int buttonBorderColour,
                  action buttonPressAction, @Nullable Menu goToMenu) {
        width = buttonWidth;
        height = buttonHeight;
        xPos = xPosition;
        yPos = yPosition;
        colour = buttonColour;
        text = buttonText;
        textColour = buttonTextColour;
        borderColour = buttonBorderColour;
        onButtonPress = buttonPressAction;
        nextMenu = goToMenu;
    }

    // Getters and setters

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public int getColour() {
        return colour;
    }

    public String getText() {
        return text;
    }

    public int getTextColour() {
        return textColour;
    }

    public int getBorderColour() {
        return borderColour;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColour(int textColour) {
        this.textColour = textColour;
    }

    public void setBorderColour(int borderColour) {
        this.borderColour = borderColour;
    }

    public boolean mouseOnButton(int x, int y) {
        return((x >= xPos) && (x <= xPos + width) && (y >= yPos) && (y <= yPos + height));
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void highlightColour() {
        highlighted = true;
        String hexString = Integer.toHexString(colour);
        String convert;
        int hexToDec;
        String finalHex = "FF";

        for (int i = 2; i < 7; i += 2) {
            convert = hexString.charAt(i) + "" + hexString.charAt(i + 1);
            hexToDec = Integer.parseInt(convert, 16);
            hexToDec *= 2;
            finalHex = finalHex.concat(Integer.toHexString(hexToDec));
        }
        long longHex = Long.parseLong(finalHex, 16);
        colour = (int)longHex;
    }

    public void unHighlightColour() {
        highlighted = false;
        String hexString = Integer.toHexString(colour);
        String convert;
        int hexToDec;
        String finalHex = "FF";

        for (int i = 2; i < 7; i += 2) {
            convert = hexString.charAt(i) + "" + hexString.charAt(i + 1);
            hexToDec = Integer.parseInt(convert, 16);
            hexToDec /= 2;
            finalHex = finalHex.concat(Integer.toHexString(hexToDec));
        }
        long longHex = Long.parseLong(finalHex, 16);
        colour = (int)longHex;
    }

    public action getAction() {
        return onButtonPress;
    }
}
