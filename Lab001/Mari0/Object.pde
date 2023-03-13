public class Object {

  PImage image, imageR, imageL;
  float centerX, centerY, velocityX, velocityY, w, h, accelaration, max_speed, jump_speed = 25, gravity = 1;
  Boolean onGround = true;

  public Object() {
  }

  public Object(String filePath, float x, float y, float scale) {
    image = loadImage(filePath);
    imageL = image;
    imageR = image;
    w = image.width * scale;
    h = image.height * scale;
    centerX = x;
    centerY = y;
    max_speed = 6;
    velocityX = 0;
    velocityY = 0;
  }

  public Object(PImage img, float scale) {
    image = img;
    imageL = image;
    imageR = image;
    w = image.width * scale;
    h = image.height * scale;
    centerX = 0;
    centerY = 0;
    max_speed = 6;
    velocityX = 0;
    velocityY = 0;
  }

  void setLeft(float left) {
    centerX = left + w/2;
  }
  float getLeft() {
    return centerX - w/2;
  }
  void setRight(float right) {
    centerX = right - w/2;
  }
  float getRight() {
    return centerX + w/2;
  }
  void setTop(float top) {
    centerY = top + h/2;
  }
  float getTop() {
    return centerY - h/2;
  }
  void setBottom(float bottom) {
    centerY = bottom - h/2;
  }
  float getBottom() {
    return centerY + h/2;
  }

  public Object(String filePath) {
    this(filePath, 0, 0, 1);
  }

  public void display() {
    if (velocityX > 0) {
      image = imageR;
    } else if (velocityX < 0) {
      image = imageL;
    }
    image(image, centerX, centerY, w, h);
  }

  public PImage getReversePImage( PImage image ) {
    PImage reverse;
    reverse = createImage(image.width, image.height, ARGB );

    for ( int i=0; i < image.width; i++ ) {
      for (int j=0; j < image.height; j++) {
        int xPixel, yPixel;
        xPixel = image.width - 1 - i;
        yPixel = j;
        reverse.pixels[yPixel*image.width+xPixel]=image.pixels[j*image.width+i] ;
      }
    }
    return reverse;
  }
}
