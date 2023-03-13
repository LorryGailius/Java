public class Flag extends Object {

  boolean reached = false;

  public Flag(int x, int y) {
    super(loadImage("assets/World/Flag.png"), 1);
    centerX = x;
    centerY = y;
  }

  public void display(Player player) {
    super.display();
    checkCollision(player);
  }

  void checkCollision(Player player) {
    Boolean XOverlap = player.getRight() <= getLeft() || player.getLeft() >= getRight();
    Boolean YOverlap = player.getBottom() <= getTop() || player.getTop() >= getBottom();
    if (XOverlap || YOverlap) {
      reached = false;
    } else {
      reached = true;
    }
  }
}
