int screen_width = 1280, screen_height = 720, selection = 1, hidx = -1;
float btnW = 150, btnH = 50, btnPaddingH = 25, btnPaddingV= 18;
boolean btnOver = false;
PImage[] tiles;
PImage frame, player;
ArrayList<Object> objects;
LevelController lvlCntrl;
Object placement;
int viewportX = 0, scrollSpeed = 10;
String filePath;

void setup() {
  selectInput("Select a .csv map file", "fileSelected");
  size(1280, 720);
  tiles = new PImage[6];
  tiles[1] = loadImage("assets/World/Ground1.png");
  tiles[2] = loadImage("assets/World/LuckyBlock1.png");
  tiles[3] = loadImage("assets/World/Coin.png");
  tiles[4] = loadImage("assets/World/Goomba1.png");
  tiles[5] = loadImage("assets/World/Flag.png");
  player = loadImage("assets/Player/Running-mario4.png");
  frame = loadImage("assets/GUI/frame.png");
  objects = new ArrayList<Object>();
  lvlCntrl = new LevelController();
}

void draw() {
  translate(-viewportX, 0);
  imageMode(CENTER);
  background(#add5f8);
  lvlCntrl.display(objects);
  tint(255, 126);
  image(player, 288, 608);
  translate(viewportX, 0);
  handleMouseEditing();
  noTint();
  image(frame, screen_width - 64, screen_height - 64);
  image(tiles[selection], screen_width - 64, screen_height - 64);
}

void handleMouseEditing() {
  btnOver = false;
  hidx = -1;
  for (int i = 0; i < objects.size(); i++) {
    Object o = objects.get(i);
    if (overBtn(o.centerX - o.w/2 - viewportX, o.centerY - o.h/2, o.w, o.h)) {
      btnOver = true;
      o.highlighted = true;
      hidx = i;
    } else {
      o.highlighted = false;
    }
  }

  if (!btnOver) {
    int w = 64;
    int h = 64;

    placement = new Object(tiles[selection], selection);

    int snapX = round((mouseX + viewportX) / w) * w + w/2 - viewportX;
    int snapY = round(mouseY / h) * h + h/2;

    placement.centerX = snapX;
    placement.centerY = snapY;
    placement.highlighted = true;
    placement.display();
  }
}

void mousePressed() {
  if (mouseButton == LEFT) {
    if (!btnOver) {
      placement.centerX += viewportX;
      placement.id = selection;
      objects.add(placement);
      placement = null;
      hidx = -1;
    }
  } else if (mouseButton == RIGHT) {
    if (hidx != -1) {
      objects.remove(hidx);
      hidx = -1;
    }
  }
}

void keyPressed() {
  switch(key) {
  case '1':
    {
      selection = 1;
      break;
    }
  case '2':
    {
      selection = 2;
      break;
    }
  case '3':
    {
      selection = 3;
      break;
    }
  case '4':
    {
      selection = 4;
      break;
    }
  case '5':
    {
      selection = 5;
      break;
    }
  case 'd':
    {
      viewportX += scrollSpeed;
      break;
    }
  case 'a':
    {
      viewportX = viewportX - scrollSpeed > 0 ? viewportX - scrollSpeed : 0;
      break;
    }
  case 's':
    {
      lvlCntrl.saveMap(filePath, objects);
      break;
    }
  case 'l':
    {
      selectInput("Load", "fileSelected");
      break;
    }
  case 'c':
    {
      lvlCntrl.saveMap(null, objects, "Save new");
      break;
    }
  case 'q':
    {
      exit();
    }
  }
}

boolean overBtn(int x, int y, int width, int height) {
  if (mouseX >= x && mouseX <= x+width &&
    mouseY >= y && mouseY <= y+height) {
    return true;
  } else {
    return false;
  }
}

void button(float x, float y, float btnW, float btnH, float btnPaddingH, float btnPaddingV, color btnColor, String text, color textColor) {
  fill(btnColor);
  rect(x - btnW/2, y - btnH/2, btnW, btnH);
  fill(textColor);
  text(text, x - btnW/2 + btnPaddingH, y + btnH/2 - btnPaddingV);
  textSize(24);
}

void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } else {
    if (selection.getAbsolutePath().endsWith(".csv")) {
      filePath = selection.getAbsolutePath();
      lvlCntrl.loadMap(filePath, tiles, objects);
    } else {
      println("Selected file is not .csv");
    }
  }
}
