import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;

public class Player extends PApplet {

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    private float x;
    private float y;
    private Main main;
    public PImage playerImage;
    Direction direction = Direction.RIGHT;
    public int inventory = 0;

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
        switch (direction){
            case UP -> {
                direction = Direction.LEFT;
                playerImage = main.loadImage("assets/player_left.png");
            }
            case DOWN -> {
                direction = Direction.RIGHT;
                playerImage = main.loadImage("assets/player_right.png");
            }
            case LEFT -> {
                direction = Direction.DOWN;
                playerImage = main.loadImage("assets/player_down.png");
            }
            case RIGHT -> {
                direction = Direction.UP;
                playerImage = main.loadImage("assets/player_up.png");
            }
        }
    }

    public void rotateE()
    {
        // Rotate right
        switch (direction){
            case UP -> {
                direction = Direction.RIGHT;
                playerImage = main.loadImage("assets/player_right.png");
            }
            case DOWN -> {
                direction = Direction.LEFT;
                playerImage = main.loadImage("assets/player_left.png");
            }
            case LEFT -> {
                direction = Direction.UP;
                playerImage = main.loadImage("assets/player_up.png");
            }
            case RIGHT -> {
                direction = Direction.DOWN;
                playerImage = main.loadImage("assets/player_down.png");
            }
        }
    }

    public void moveForward(List<Block> blocks)
    {
        switch (direction){
            case UP -> {
                if(!checkCollision(blocks)) y -= 32;
            }
            case DOWN -> {
                if(!checkCollision(blocks)) y += 32;
            }
            case LEFT -> {
                if(!checkCollision(blocks)) x -= 32;
            }
            case RIGHT -> {
                if(!checkCollision(blocks)) x += 32;
            }
        }
    }

    public void moveForward(int steps, List<Block> blocks)
    {
        for (int i = 0; i < steps; i++) {
            moveForward(blocks);

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkCollision(List<Block> blocks)
    {
        switch (direction){
            case UP -> {
                return blocks.stream().anyMatch(block -> block.getX() == x && block.getY() == y - 32 && !block.isWalkable()) || y - 32 < 0;
            }
            case DOWN -> {
                return blocks.stream().anyMatch(block -> block.getX() == x && block.getY() == y + 32 && !block.isWalkable()) || y + 32 >= main.height;
            }
            case LEFT -> {
                return blocks.stream().anyMatch(block -> block.getX() == x - 32 && block.getY() == y && !block.isWalkable()) || x - 32 < 0;
            }
            case RIGHT -> {
                return blocks.stream().anyMatch(block -> block.getX() == x + 32 && block.getY() == y && !block.isWalkable()) || x + 32 >= main.width;
            }
        }
        return false;
    }

    public void mine(List<Block> blocks)
    {
        switch (direction){
            case UP -> {
                // Remove block above player and add to inventory if it exists
                blocks.stream().filter(block -> block.getX() == x && block.getY() == y - 32 && !block.isWalkable() && !block.isWater()).findFirst().ifPresent(block -> {
                    blocks.remove(block);
                    inventory++;
                });
            }
            case DOWN -> {
                // Remove block below player and add to inventory if it exists
                blocks.stream().filter(block -> block.getX() == x && block.getY() == y + 32 && !block.isWalkable() && !block.isWater()).findFirst().ifPresent(block -> {
                    blocks.remove(block);
                    inventory++;
                });
            }
            case LEFT -> {
                // Remove block to the left of player and add to inventory if it exists
                blocks.stream().filter(block -> block.getX() == x - 32 && block.getY() == y && !block.isWalkable() && !block.isWater()).findFirst().ifPresent(block -> {
                    blocks.remove(block);
                    inventory++;
                });
            }
            case RIGHT -> {
                // Remove block to the right of player and add to inventory if it exists
                blocks.stream().filter(block -> block.getX() == x + 32 && block.getY() == y && !block.isWalkable() && !block.isWater()).findFirst().ifPresent(block -> {
                    blocks.remove(block);
                    inventory++;
                });
            }
        }
    }

    public void place() {
        if(inventory == 0) return;

        // Check if block does not exist in the direction the player is facing and place a block if not
        switch (direction){
            case UP -> {
                if(main.blocks.stream().noneMatch(block -> block.getX() == x && block.getY() == y - 32 && !block.isWater())) {

                    Block block = new Block(main, x, y - 32);

                    // Check if block is placed on water
                    if(main.blocks.stream().anyMatch(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater())) {
                        main.blocks.removeIf(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater());
                        block.setWalkable(true);
                    }

                    main.blocks.add(block);
                    inventory--;
                }
            }
            case DOWN -> {
                if(main.blocks.stream().noneMatch(block -> block.getX() == x && block.getY() == y + 32 && !block.isWater())) {

                    Block block = new Block(main, x, y + 32);

                    // Check if block is placed on water
                    if(main.blocks.stream().anyMatch(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater())) {
                        main.blocks.removeIf(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater());
                        block.setWalkable(true);
                    }

                    main.blocks.add(block);
                    inventory--;
                }
            }
            case LEFT -> {
                if(main.blocks.stream().noneMatch(block -> block.getX() == x - 32 && block.getY() == y && !block.isWater())) {

                    Block block = new Block(main, x - 32, y);

                    // Check if block is placed on water
                    if(main.blocks.stream().anyMatch(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater())) {
                        main.blocks.removeIf(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater());
                        block.setWalkable(true);
                    }

                    main.blocks.add(block);
                    inventory--;
                }
            }
            case RIGHT -> {
                if(main.blocks.stream().noneMatch(block -> block.getX() == x + 32 && block.getY() == y && !block.isWater())) {

                    Block block = new Block(main, x + 32, y);

                    // Check if block is placed on water
                    if(main.blocks.stream().anyMatch(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater())) {
                        main.blocks.removeIf(b -> b.getX() == block.getX() && b.getY() == block.getY() && b.isWater());
                        block.setWalkable(true);
                    }

                    main.blocks.add(block);
                    inventory--;
                }
            }
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
