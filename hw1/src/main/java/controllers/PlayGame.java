package controllers;

import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import org.eclipse.jetty.websocket.api.Session;
import models.Player;
import models.GameBoard;
import com.google.gson.Gson;

class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
	
	GameBoard board = new GameBoard();

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    app.get("/newgame", ctx -> {
    	ctx.redirect("/tictactoe.html");
    });
 
    app.post("/startgame", ctx -> {
    	Player p1 = new Player();
    	board.setP1(p1);
        p1.setType(ctx.formParam("type").charAt(0));
        p1.setId(1);
        ctx.result(new Gson().toJson(board));
    });
    
    app.get("/joingame", ctx -> {
    	Player p2 = new Player();
    	board.setP2(p2);
    	if (board.getP1().getType() == 'X')
    		p2.setType('O');
    	else
    		p2.setType('X');
    	p2.setId(2);
        ctx.redirect("/tictactoe.html?p=2");
        board.setGameStarted(true);
        sendGameBoardToAllPlayers(new Gson().toJson(board));
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
    	  e.printStackTrace();
    	  //LOG.log("Could not do what I wanted", e);
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
