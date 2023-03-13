public class LuckyBlock extends Object {

  Boolean isCollected = false;

  PImage on, off;


  public LuckyBlock(PImage img, float scale) {
    super(img, scale);
  }


  public void display(Player player, ArrayList<Coin> coins) {
    super.display();
    if (checkCollision(player) && !isCollected) {
      isCollected = true;
      Coin c = new Coin(centerX, centerY - 64);
      coins.add(c);
      c.velocityY -= 5;
      image = off;
    }
  }

  boolean checkCollision(Player player) {
    Boolean XOverlap = player.getRight() <= getLeft() || player.getLeft() >= getRight();
    Boolean YOverlap = player.getBottom() <= getTop() || player.getTop() - 3 >= getBottom();
    if (XOverlap || YOverlap) {
      return false;
    } else {
      return true;
    }
  }
}
