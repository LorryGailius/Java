import processing.core.PApplet;
import processing.core.PImage;

public class Player extends PApplet {
    private float x;
    private float y;
    private Main main;
    private PImage playerImage;

    private boolean isUp = false, isDown = false, isLeft = false, isRight = true;

    public Player(Main main) {
        this.main = main;
        playerImage = main.loadImage("assets/player_right.png");
        x = 0;
        y = 0;
    }

    public void update() {
    }

    public void draw() {
        main.image(playerImage, x, y);
    }

    public void rotateQ()
    {
        // Rotate left
        if (isUp) {
            isUp = false;
            isLeft = true;
            playerImage = main.loadImage("assets/player_left.png");
        } else if (isDown) {
            isDown = false;
            isRight = true;
            playerImage = main.loadImage("assets/player_right.png");
        } else if (isLeft) {
            isLeft = false;
            isDown = true;
            playerImage = main.loadImage("assets/player_down.png");
        } else if (isRight) {
            isRight = false;
            isUp = true;
            playerImage = main.loadImage("assets/player_up.png");
        }
    }

    public void rotateE()
    {
        // Rotate right
if (isUp) {
            isUp = false;
            isRight = true;
            playerImage = main.loadImage("assets/player_right.png");
        } else if (isDown) {
            isDown = false;
            isLeft = true;
            playerImage = main.loadImage("assets/player_left.png");
        } else if (isLeft) {
            isLeft = false;
            isUp = true;
            playerImage = main.loadImage("assets/player_up.png");
        } else if (isRight) {
            isRight = false;
            isDown = true;
            playerImage = main.loadImage("assets/player_down.png");
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
