public class Player extends Object {

  PImage[] currentAnimation;
  PImage[] standing = new PImage[1];
  PImage[] running = new PImage[4];
  PImage[] jumping = new PImage[1];
  int idx, frame, score = 0;
  float movSpeed = 0;
  boolean isDead = false, inAir = false;

  public Player(int x, int y) {
    super(loadImage("assets/Player/Running-mario4.png"), 1);
    running[0] = loadImage("assets/Player/Running-mario1.png");
    running[1] = loadImage("assets/Player/Running-mario2.png");
    running[2] = loadImage("assets/Player/Running-mario3.png");
    running[3] = loadImage("assets/Player/Running-mario4.png");
    jumping[0] = loadImage("assets/Player/Jumping-mario.png");
    this.centerX = x;
    this.centerY = y;
    this.standing[0] = this.image;
    frame = 0;
    idx = 0;
  }

  public void updateAnimation() {
    frame++;
    selectImages();
    if (frame % 5 == 0) {
      advanceFrame();
    }
  }

  public void selectImages() {

    if (velocityY != 0) {
      currentAnimation = jumping;
    }

    if (velocityX != 0 && velocityY == 0) {
      currentAnimation = running;
    }

    if (velocityX == 0 && velocityY == 0) {
      currentAnimation = standing;
    }
  }

  public void advanceFrame() {
    idx++;
    if (idx == currentAnimation.length || idx > currentAnimation.length) {
      idx = 0;
    }
    image = currentAnimation[idx];
    imageR = currentAnimation[idx];
    imageL = getReversePImage(currentAnimation[idx]);
  }

  public void display() {
    if (player.getTop() > 720) {
      isDead = true;
      accelaration = 0;
      velocityX=0;
      velocityY = 0;
      gravity = 0;
    }
    if (!isDead) {
      super.display();
    }
  }
}
