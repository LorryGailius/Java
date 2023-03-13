public class Enemy extends Object {

  PImage[] animation = new PImage[4];
  int idx, frame;
  boolean isDead = false;

  public Enemy(int x, int y) {
    super(loadImage("assets/World/Goomba1.png"), 1);
    animation[1] = loadImage("assets/World/Goomba2.png");
    animation[2] = loadImage("assets/World/Goomba3.png");
    animation[3] = loadImage("assets/World/Goomba4.png");
    animation[0] = this.image;
    centerX = x;
    centerY = y;
    frame = 0;
    idx = 0;
    velocityX = -1;
  }

  public void updateAnimation() {
    frame++;
    if (frame % 10 == 0) {
      advanceFrame();
    }
  }

  public void advanceFrame() {
    idx++;
    if (idx == animation.length || idx > animation.length) {
      idx = 0;
    }
    image = animation[idx];
    imageR = animation[idx];
    imageL = getReversePImage(animation[idx]);
  }

  public void display(Player player) {
    super.display();
    checkCollision(player);
  }

  public void handleMovement(ArrayList<Object> walls) {
    if (!isOnPlatform(this, walls) || isWallAhead(this, walls)) {
      velocityX = -velocityX;
    }
    centerX += velocityX;
  }

  public void checkCollision(Player player) {
    if (player.getBottom() <= getTop() || player.getTop() >= getBottom() ||
      player.getRight() <= getLeft() || player.getLeft() >= getRight()) {
      return;
    }

    if (player.getBottom() >= getTop() && player.inAir) {
      player.score += 5;
      this.isDead = true;
    } else {
      player.isDead = true;
    }
  }


  public Boolean isOnPlatform(Object s, ArrayList<Object> walls) {
    s.centerY += 64;
    s.centerX += velocityX * 64;
    ArrayList<Object> col_list = checkCollisionList(s, walls);
    s.centerY -= 64;
    s.centerX -= velocityX * 64;

    if (col_list.size() > 0) {
      return true;
    }
    return false;
  }

  public Boolean isWallAhead(Object s, ArrayList<Object> walls) {
    s.centerX += velocityX < 0 ? -1 : 1;
    ArrayList<Object> col_list = checkCollisionList(s, walls);
    s.centerX -= velocityX < 0 ? -1 : 1;

    if (col_list.size() > 0) {
      return true;
    }
    return false;
  }
}
