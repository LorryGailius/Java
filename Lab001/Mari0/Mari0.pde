import java.util.Iterator;
Player player;
LevelController levelController;

final static float RIGHT_MARGIN = 400;
final static float LEFT_MARGIN = 300;


float view_x = 64;

void setup() {
  size(1280, 720, P2D);
  noSmooth();
  player = new Player(400, 300);
  levelController = new LevelController();
}

void draw() {
  scroll();
  imageMode(CENTER);
  background(#add5f8);
  if (levelController.win) {
    image(loadImage("assets/win.png"), 1280/2, 720/2);
    text("Your score: " + player.score, 1280/2 - 400, 720/2 + 100);
  } else {
    player.updateAnimation();
    if (!isOnPlatform(player, levelController.walls)) {
      player.inAir = true;
    } else {
      player.inAir = false;
    }
    levelController.display(player);
    resolvePlatformCollisions(player, levelController.walls);
    player.display();
    textSize(32);
    if (player.isDead) {
      text("YOU ARE DEAD\nYOUR SCORE:  " + player.score, view_x + 500, 360);
    } else {
      text(player.score, view_x + 50, 50);
    }
  }
  if (levelController.win) {
    image(loadImage("assets/win.png"), 1280/2 + view_x, 720/2);
    text("Your Score: " + player.score, 1280/2 - 400 + view_x, 720/2 + 100);
  }
}

Boolean checkCollision(Object o1, Object o2) {
  Boolean noXOverlap = o1.getRight() <= o2.getLeft() || o1.getLeft() >= o2.getRight();
  Boolean noYOverlap = o1.getBottom() <= o2.getTop() || o1.getTop() >= o2.getBottom();
  if (noXOverlap || noYOverlap) {
    return false;
  } else {
    return true;
  }
}

public Boolean isOnPlatform(Object s, ArrayList<Object> walls) {
  s.centerY += 5;
  ArrayList<Object> col_list = checkCollisionList(s, walls);
  s.centerY -=5;

  if (col_list.size() > 0) {
    return true;
  }
  return false;
}

public ArrayList<Object> checkCollisionList(Object o, ArrayList<Object> list) {
  ArrayList<Object> collision_list = new ArrayList<Object>();
  for (Object t : list) {
    if (checkCollision(o, t)) {
      collision_list.add(t);
    }
  }
  return collision_list;
}

public void resolvePlatformCollisions(Object s, ArrayList<Object> walls) {
  s.velocityY += s.gravity;
  s.centerY += s.velocityY;

  ArrayList<Object> col_list = checkCollisionList(s, walls);

  if (col_list.size() > 0) {
    Object collided = col_list.get(0);
    if (s.velocityY > 0) {
      player.movSpeed = 0.25;
      s.setBottom(collided.getTop());
    } else if (s.velocityY < 0) {
      s.setTop(collided.getBottom());
    }
    s.velocityY = 0;
  }

  if (s.velocityX < s.max_speed && s.velocityX > -s.max_speed) {
    s.velocityX += s.accelaration;
  }
  s.centerX += s.velocityX;
  col_list = checkCollisionList(s, walls);

  if (col_list.size() > 0) {
    Object collided = col_list.get(0);
    if (s.velocityX > 0) {
      s.setRight(collided.getLeft());
    } else if (s.velocityX < 0) {
      s.setLeft(collided.getRight());
    }
  }
}

void scroll() {
  float right_boundary = view_x + width - RIGHT_MARGIN;
  if (player.getRight() > right_boundary) {
    view_x += player.getRight() - right_boundary;
  }

  float left_boundary = view_x + LEFT_MARGIN;
  if (player.getLeft() < left_boundary) {
    if (view_x - left_boundary + player.getLeft() > 64) {
      view_x -= left_boundary - player.getLeft();
    }
  }

  translate(-view_x, 0);
}


void keyPressed() {
  switch(key) {
  case 'd':
    {
      if (player.velocityX < 0) { // if moving left, slow down first
        player.accelaration = 0;
      }
      player.accelaration = player.movSpeed;
      break;
    }
  case 'a':
    {
      if (player.velocityX > 0) { // if moving right, slow down first
        player.accelaration = 0;
      }
      player.accelaration = -player.movSpeed;
      break;
    }
  case ' ':
    {
      if (isOnPlatform(player, levelController.walls)) {
        player.velocityY -= player.jump_speed;
      }
      break;
    }
  case 'r':
    {
      player.isDead = false;
      levelController.restartLevel();
      break;
    }
  case 'n':
    {
      levelController.nextLevel();
      break;
    }
  case 'q':
    {
      exit();
      break;
    }
  }
}

void keyReleased() {
  switch(key) {
  case 'd':
    {
      if (player.accelaration != 0) {
        player.accelaration = 0;
        player.velocityX = 0;
      }
      break;
    }
  case 'a':
    {
      if (player.accelaration != 0) {
        player.accelaration = 0;
        player.velocityX = 0;
      }
      break;
    }
  }
}
