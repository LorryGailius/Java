import processing.core.PApplet;
import processing.core.PImage;

public class Block extends PApplet {

    Main main;
    float x, y;
    PImage blockImage;
    boolean walkable = false;
    boolean water = false;

    public Block(Main main, float x, float y) {
        this.main = main;
        blockImage = main.loadImage("assets/block.png");
        this.x = x;
        this.y = y;
    }

    public Block(Main main, float x, float y, boolean isWater) {
        this.main = main;
        blockImage = main.loadImage("assets/block.png");
        this.x = x;
        this.y = y;
        this.water = isWater;
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

    public boolean isWalkable() {
        return walkable;
    }

    public PImage getBlockImage() {
        return blockImage;
    }

    public void setBlockImage(PImage blockImage) {
        this.blockImage = blockImage;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void setWalkable(boolean walkable, PImage newImage) {
        this.walkable = walkable;
        this.blockImage = newImage;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }
}
