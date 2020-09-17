package controllers;

import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import org.eclipse.jetty.websocket.api.Session;
import models.Player;
import models.GameBoard;
import models.Move;
import models.Message;
import com.google.gson.Gson;

final class PlayGame {

  /**
   * Private constructor that prevent utility class instantiation.
   */
  private PlayGame() {
    // Throw an exception if this ever *is* called
    throw new AssertionError("Instantiating utility class.");
  }

  /**
   *  Set listening port.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   * Create Javalin instance.
   */
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
      board.setBoardState();
      Player p1 = new Player();
      board.setP1(p1);
      p1.setType(ctx.formParam("type").charAt(0));
      p1.setId(1);
      ctx.result(new Gson().toJson(board));
    });

    app.get("/joingame", ctx -> {
      Player p2 = new Player();
      board.setP2(p2);
      if (board.getP1().getType() == 'X') {
        p2.setType('O');
      } else {
        p2.setType('X');
      }
      p2.setId(2);
      ctx.redirect("/tictactoe.html?p=2");
      board.setGameStarted(true);
      sendGameBoardToAllPlayers(new Gson().toJson(board));
    });

    app.post("/move/:playerId", ctx -> {
      int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      Move move = new Move();
      Message message = new Message();
      move.setPlayer(board.getPlayer(playerId));
      move.setMoveX(Integer.parseInt(ctx.formParam("x")));
      move.setMoveY(Integer.parseInt(ctx.formParam("y")));
      if (board.isValid(move, message)) {
        board.makeMove(move);
        if (!board.isOver(move)) {
          board.switchTurn();
        }
      }
      ctx.result(new Gson().toJson(message));
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

  /**
   * Stop the application.
   */
  public static void stop() {
    app.stop();
  }
}
