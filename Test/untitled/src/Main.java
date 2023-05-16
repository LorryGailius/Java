import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;

public class Main extends PApplet {
    private Player player;
    private static int height = 640, width = 480;

    ArrayList<Block> blocks = new ArrayList<Block>();

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        size(480, 640);
        noSmooth();
    }

    @Override
    public void setup() {
        player = new Player(this);
        blocks.add(new Block(this, 160, 160));
    }

    @Override
    public void draw() {
        // White background
        background(255);

        // Draw 32x32 pixel grid
        stroke(0, 0, 0);
        for (int i = 0; i < width; i += 32) {
            line(i, 0, i, height);
        }
        for (int i = 0; i < height; i += 32) {
            line(0, i, width, i);
        }

        player.update(); // Call the update() method of the player instance
        player.draw(); // Call the draw() method of the player instance
        blocks.forEach(Block::draw); // Call the draw() method of each block instance
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case UP:
                // Check if the player is not at the top of the screen and if no block is above the player
                if (player.getY() > 0 && blocks.stream().noneMatch(block -> block.getX() == player.getX() && block.getY() == player.getY() - 32)) {

                    player.setY(player.getY() - 32);
                }
                break;
            case DOWN:
                // Check if the player is not at the bottom of the screen
                if (player.getY() < height - 32 && blocks.stream().noneMatch(block -> block.getX() == player.getX() && block.getY() == player.getY() + 32)) {
                    player.setY(player.getY() + 32);
                }
                break;
            case LEFT:
                // Check if the player is not at the left of the screen
                if (player.getX() > 0 && blocks.stream().noneMatch(block -> block.getX() == player.getX() - 32 && block.getY() == player.getY())) {
                    player.setX(player.getX() - 32);
                }
                break;
            case RIGHT:
                // Check if the player is not at the right of the screen
                if (player.getX() < width - 32 && blocks.stream().noneMatch(block -> block.getX() == player.getX() + 32 && block.getY() == player.getY())) {
                    player.setX(player.getX() + 32);
                }
                break;

            case 'Q':
                // Rotate the player image 90 degrees counter-clockwise
                player.rotateQ();
                break;

            case 'E':
                // Rotate the player image 90 degrees clockwise
                player.rotateE();
                break;
        }
    }
}
