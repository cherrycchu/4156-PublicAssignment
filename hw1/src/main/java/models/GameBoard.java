package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn = 1;

  private char[][] boardState = new char[3][3];

  private int winner;

  private boolean isDraw;
  
  public Player getP1() {
	  return p1;
  }
  
  public Player getP2() {
	  return p2;
  }
  
  public void setP1(Player p1) {
	  this.p1 =  p1;
  }
  
  public void setP2(Player p2) {
	  this.p2 = p2;
  }
  
  public void switchTurn() {
	  turn = turn % 2 + 1;
  }
  
  public boolean isGameStarted() {
	  return gameStarted; 
  }
	
  public void setGameStarted(boolean gameStarted) {
	  this.gameStarted = gameStarted;
  }

}
