import processing.core.PApplet;
import processing.core.PImage;

public class Block extends PApplet {

    Main main;
    float x, y;
    PImage blockImage;

    public Block(Main main, float x, float y) {
        this.main = main;
        blockImage = main.loadImage("assets/block.png");
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw() {
        main.image(blockImage, x, y);
        super.draw();
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
