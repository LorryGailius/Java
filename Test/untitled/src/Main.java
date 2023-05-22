import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main extends PApplet {
    public Player player;
    public int height, width;
    private PImage grassImage;
    private PImage blockImage;
    private PImage waterImage;
    private PImage voidImage;
    private boolean gridLinesEnabled = true;
    public int currentLevel = 1;
    ArrayList<Block> blocks = new ArrayList<>();
    Flag flag;
    public boolean loadingLevel = false;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        grassImage = loadImage("assets/grass.png");
        blockImage = loadImage("assets/block.png");
        waterImage = loadImage("assets/water.png");
        voidImage = loadImage("assets/void.png");
        noSmooth();
    }

    @Override
    public void setup() {
        loadLevel(currentLevel);
        player = new Player(this);
    }

    @Override
    public void draw() {
        background(255); // White background

        for (int i = 0; i < width; i += 32) {
            for (int j = 0; j < height; j += 32) {
                image(grassImage, i, j);
            }
        }

        if (gridLinesEnabled) {
            // Draw grid lines
            stroke(0);
            for (int i = 0; i < width; i += 32) {
                line(i, 0, i, height);
            }
            for (int i = 0; i < height; i += 32) {
                line(0, i, width, i);
            }
        }

        for (Block block : blocks) {
            block.draw();
        }

        flag.update();

        player.update();
        player.draw();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case 'W':
                player.moveForward(blocks);
                break;

            case 'M':
                player.mine(blocks);
                System.out.println(player.inventory);
                break;

            case 'P':
                player.place();
                break;

            case 'Q':
                player.rotateQ();
                break;

            case 'E':
                player.rotateE();
                break;

            case 'L':
                nextLevel();
                break;

            case 'R':
                Thread codeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runCode();
                    }
                });
                codeThread.start();
                break;
        }
    }

    public void loadLevel(int number) {
        loadingLevel = true;
        blocks.clear();
        if (player != null) {
            player.inventory = 0;
            player.direction = Player.Direction.RIGHT;
            player.playerImage = loadImage("assets/player_right.png");
            player.setX(0);
            player.setY(0);
        }
        String[] lines = loadStrings("assets/levels/Level" + currentLevel + ".csv");

        for (int i = 0; i < lines.length; i++) {
            // Split the line into an array of strings
            String[] values = lines[i].split(",");

            // Loop through each value in the array
            for (int j = 0; j < values.length; j++) {
                // If the value is 1, create a block at the current position
                if (values[j].equals("1")) {
                    Block block = new Block(this, j * 32, i * 32);
                    block.setBlockImage(blockImage);
                    blocks.add(block);
                }
                // If the value is 2, create a water block at the current position
                else if (values[j].equals("2")) {
                    Block block = new Block(this, j * 32, i * 32);
                    block.setBlockImage(waterImage);
                    block.setWalkable(false);
                    block.setWater(true);
                    blocks.add(block);
                }
                // If the value is 3, create a flag at the current position
                else if (values[j].equals("3")) {
                    flag = new Flag(this, j * 32, i * 32);
                }
                // If the value is 2, create a water block at the current position
                else if (values[j].equals("4")) {
                    Block block = new Block(this, j * 32, i * 32);
                    block.setBlockImage(voidImage);
                    block.setWalkable(false);
                    block.setWater(true);
                    blocks.add(block);
                }
            }
        }

        // Get the width and height of the level
        width = lines[0].split(",").length * 32;
        height = lines.length * 32;

        // Resize the window to fit the level
        surface.setSize(width, height);
        loadingLevel = false;
    }

    public void nextLevel() {
        currentLevel++;
        loadLevel(currentLevel);
    }
    public void runCode() {
        String[] codeLines = loadStrings("code.txt");
        System.out.println("There are " + codeLines.length + " lines of code");

        // Loop through each line of code
        for (int i = 0; i < codeLines.length; i++) {
            String line = codeLines[i].trim(); // Remove leading and trailing whitespace

            // Check if the line is empty or a comment
            if (line.isEmpty() || line.startsWith("//")) {
                continue; // Skip empty lines and comments
            }

            // Split the line into tokens by whitespace
            String[] tokens = line.split("\\s+");

            // Get the command and its arguments
            String command = tokens[0];
            String[] arguments = new String[tokens.length - 1];
            System.arraycopy(tokens, 1, arguments, 0, arguments.length);

            // Parse player commands
            if (command.equals("move")) {
                if (arguments.length > 0) {
                    int steps = Integer.parseInt(arguments[0]);
                    player.moveForward(steps, blocks);
                }
            } else if (command.equals("mine")) {
                player.mine(blocks);
                System.out.println(player.inventory);
            } else if (command.equals("place")) {
                player.place();
            } else if (command.equals("rotate")) {
                if (arguments.length > 0) {
                    String direction = arguments[0];
                    if (direction.equals("left")) {
                        player.rotateQ();
                    } else if (direction.equals("right")) {
                        player.rotateE();
                    }
                }
            }


            // Handle control flow statements
            if (command.equals("for")) {
                if (arguments.length > 1) {
                    String variable = arguments[0];
                    int iterations = Integer.parseInt(arguments[1]);

                    // Start of for loop block
                    for (int j = 0; j < iterations; j++) {
                        // Loop through the code block inside the for loop
                        for (int k = i + 1; k < codeLines.length; k++) {
                            String nestedLine = codeLines[k].trim();
                            if (nestedLine.equals("endfor")) {
                                break; // End of for loop block
                            }
                            // Parse and execute nested commands (recursive)
                            parseAndExecuteCommand(nestedLine);
                        }
                    }

                    // Skip lines inside the for loop block
                    while (!line.equals("endfor")) {
                        i++;
                        line = codeLines[i].trim();
                    }
                }
            } else if (command.equals("if")) {
                if (arguments.length > 2) {
                    String variable1 = arguments[0];
                    String operator = arguments[1];
                    String variable2 = arguments[2];

                    // Evaluate the condition
                    boolean condition = evaluateCondition(variable1, operator, variable2);

                    if (condition) {
                        // Loop through the code block inside the if statement
                        for (int j = i + 1; j < codeLines.length; j++) {
                            String nestedLine = codeLines[j].trim();
                            if (nestedLine.equals("endif")) {
                                break; // End of if statement block
                            }
                            // Parse and execute nested commands (recursive)
                            parseAndExecuteCommand(nestedLine);
                        }
                    } else {
                        // Skip lines inside the if statement block
                        while (!line.equals("endif")) {
                            i++;
                            line = codeLines[i].trim();
                        }
                    }
                }
            } else if (command.equals("while")) {
                if (arguments.length > 2) {
                    String variable1 = arguments[0];
                    String operator = arguments[1];
                    String variable2 = arguments[2];

                    // Loop while the condition is true
                    while (evaluateCondition(variable1, operator, variable2)) {
                        // Loop through the code block inside the while loop
                        for (int j = i + 1; j < codeLines.length; j++) {
                            String nestedLine = codeLines[j].trim();
                            if (nestedLine.equals("endwhile")) {
                                break; // End of while loop block
                            }
                            // Parse and execute nested commands (recursive)
                            parseAndExecuteCommand(nestedLine);
                        }
                    }

                    // Skip lines inside the while loop block
                    while (!line.equals("endwhile")) {
                        i++;
                        line = codeLines[i].trim();
                    }
                }
            }
        }
    }

    private void parseAndExecuteCommand(String line) {
        // Split the line into tokens by whitespace
        String[] tokens = line.split("\\s+");

        // Get the command and its arguments
        String command = tokens[0];
        String[] arguments = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, arguments, 0, arguments.length);

        // Parse and execute the command
        if (command.equals("move")) {
            if (arguments.length > 0) {
                int steps = Integer.parseInt(arguments[0]);
                player.moveForward(steps, blocks);
            }
        } else if (command.equals("mine")) {
            player.mine(blocks);
            System.out.println(player.inventory);
        } else if (command.equals("place")) {
            player.place();
        } else if (command.equals("rotate")) {
            if (arguments.length > 0) {
                String direction = arguments[0];
                if (direction.equals("left")) {
                    player.rotateQ();
                } else if (direction.equals("right")) {
                    player.rotateE();
                }
            }
        }
    }

    private boolean evaluateCondition(String variable1, String operator, String variable2) {
        // Parse the variable values as integers
        float value1;
        float value2;

        if (variable1.equals("playerX")) {
            value1 = player.getX();
        } else if (variable1.equals("playerY")) {
            value1 = player.getY();
        } else {
            value1 = Integer.parseInt(variable1) * 32;
        }

        if (variable2.equals("playerX")) {
            value2 = player.getX();
        } else if (variable2.equals("playerY")) {
            value2 = player.getY();
        } else {
            value2 = Integer.parseInt(variable2) * 32;
        }

        // Compare the values based on the operator
        if (operator.equals("<")) {
            return value1 < value2;
        } else if (operator.equals(">")) {
            return value1 > value2;
        } else if (operator.equals("=")) {
            return value1 == value2;
        }

        return false; // Invalid operator
    }
}
