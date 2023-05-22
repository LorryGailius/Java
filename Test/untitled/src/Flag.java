import processing.core.PApplet;
import processing.core.PImage;

public class Flag extends PApplet {

    Main main;
    float x, y;
    PImage flagImage;
    boolean reached = false;

    public Flag(Main main, float x, float y) {
        this.main = main;
        flagImage = main.loadImage("assets/flag.png");
        this.x = x;
        this.y = y;
    }

    public void update() {
        // Check if player is on flag
        if (main.player.getX() == x && main.player.getY() == y && !reached) {
            main.nextLevel();
            reached = true;
        }
        if(!main.loadingLevel) main.image(flagImage, x, y);
    }


}
