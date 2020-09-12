package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;

  public void setPlayer(Player player) {
	  this.player = player;
  }
  
  public Player getPlayer() {
	  return player;
  }
  
  public void setMoveX(int moveX) {
	  this.moveX = moveX;
  }
  
  public int getMoveX() {
	  return moveX;
  }

  public void setMoveY(int moveY) {
	  this.moveY = moveY;
  }
  
  public int getMoveY() {
	  return moveY;
  }
  
}
