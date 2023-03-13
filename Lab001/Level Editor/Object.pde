public class Object {

  int centerX = 0, centerY = 0;
  boolean highlighted = false;
  int w, h;
  PImage sprite;
  int id;

  public Object(PImage image, int id) {
    sprite = image;
    this.id = id;
    w = image.width;
    h = image.height;
  }

  public void display() {
    if (highlighted) {
      tint(255, 126);
    } else {
      noTint();
    }
    image(sprite, centerX, centerY, w, h);
  }
}
