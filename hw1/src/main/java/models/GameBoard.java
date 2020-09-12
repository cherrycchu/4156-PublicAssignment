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
  
  public boolean isValid(Move move, Message message) {
	  message.setCode(move.getPlayer().getId()*100 + move.getMoveX()*10 + move.getMoveY());
	  if (move.getPlayer().getId() != turn) {
		  message.setMoveValidity(false);
		  message.setMessage("Not Your Turn!");
		  return false;
	  }
	  else if (boardState[move.getMoveX()][move.getMoveY()] != '\u0000') {
		  message.setMoveValidity(false);
		  message.setMessage("Invalid Move. Try Again!");
		  return false;		  
	  }
	  else {
		  message.setMoveValidity(true);
		  return true;
	  }

  }
  
  public void makeMove(Move move) {
	  char mark = move.getPlayer().getType();
	  boardState[move.getMoveX()][move.getMoveY()] = mark;
  }
  
  public Player getPlayer(int playerId) {
	  if (playerId == 1)
		  return p1;
	  else
		  return p2;
  }

}
