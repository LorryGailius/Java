public class Coin extends Object {

  public Coin(float x, float y) {
    super("assets/World/Coin.png");
    centerX = x;
    centerY = y;
    gravity = 0.6;
  }

  public void display(Player player, Iterator<Coin> iterator) {
    super.display();

    if (checkCollision(player)) {
      player.score++;
      iterator.remove();
    }
  }

  boolean checkCollision(Player player) {
    Boolean XOverlap = player.getRight() <= getLeft() || player.getLeft() >= getRight();
    Boolean YOverlap = player.getBottom() <= getTop() || player.getTop() >= getBottom();
    if (XOverlap || YOverlap) {
      return false;
    } else {
      return true;
    }
  }
}
