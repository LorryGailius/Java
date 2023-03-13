

public class LevelController {
  ArrayList<Object> walls = new ArrayList<Object>();
  ArrayList<LuckyBlock> luckyBlocks = new ArrayList<LuckyBlock>();
  ArrayList<Coin> coins = new ArrayList<Coin>();
  ArrayList<Enemy> enemies = new ArrayList<Enemy>();
  ArrayList<Flag> flags = new ArrayList<Flag>();
  int currentLevel = 0, prevScore = 0;
  boolean win = false;
  String[] mapNames;
  java.io.File folder = new java.io.File(sketchPath("assets/Maps"));
  PImage ground1 = loadImage("assets/World/Ground1.png"), l1 = loadImage("assets/World/LuckyBlock1.png"), l0 = loadImage("assets/World/LuckyBlock0.png");

  LevelController() {
    java.io.FilenameFilter extfilter = new java.io.FilenameFilter() {
      boolean accept(File dir, String name) {
        return name.matches("Map\\d+\\.csv");
      }
    };

    mapNames = folder.list(extfilter);
    if (mapNames.length > 0) {
      loadMap(folder+"/"+mapNames[0]);
    } else {
      println("No maps available!");
    }
  }

  public void nextLevel() {
    if (currentLevel < mapNames.length - 1) {
      currentLevel++;
      loadMap(folder+"/"+mapNames[currentLevel]);
    } else {
      win = true;
    }
  }

  public void restartLevel() {
    player.score = prevScore;
    loadMap(folder+"/"+mapNames[currentLevel]);
  }

  public void display(Player player) {

    for (Object o : walls) {
      o.display();
    }

    for (LuckyBlock l : luckyBlocks) {
      l.display(player, coins);
    }

    for (Enemy e : enemies) {
      e.updateAnimation();
      if (!e.isDead) {
        e.display(player);
        e.handleMovement(walls);
      }
    }

    for (Flag f : flags) {
      if (f.reached) {
        nextLevel();
      }
      f.display(player);
    }

    Iterator<Coin> iterator = coins.iterator();
    while (iterator.hasNext()) {
      Coin c = iterator.next();
      c.display(player, iterator);
    }
  }

  public void loadMap(String filePath) {
    clearValues();


    String[] lines = loadStrings(filePath);

    for (int row = 0; row < lines.length; row++) {
      String[] values = split(lines[row], ",");
      for (int col = 0; col < values.length; col++) {
        switch(values[col]) {
        case "1":
          {
            Object o = new Object(ground1, 1);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            walls.add(o);
            break;
          }
        case "2":
          {
            LuckyBlock o = new LuckyBlock(l1, 1);
            o.centerX = 32 + 64*col;
            o.centerY = 32 + 64*row;
            o.off = l0;
            walls.add(o);
            luckyBlocks.add(o);
            break;
          }
        case "3":
          {
            Coin o = new Coin(32 + 64*col, 32 + 64*row);
            coins.add(o);
            break;
          }
        case "4":
          {
            Enemy e = new Enemy(32 + 64*col, 32 + 64*row);
            enemies.add(e);
            break;
          }
        case "5":
          {
            Flag f = new Flag(32 + 64*col, 32 + 64*row);
            flags.add(f);
            break;
          }
        }
      }
    }
  }

  public void clearValues() {
    flags.clear();
    walls.clear();
    coins.clear();
    enemies.clear();
    luckyBlocks.clear();
    player.centerX = 288;
    player.centerY = 720 - 64;
    prevScore = player.score;
    player.gravity = 1;
    view_x = 64;
  }
}
