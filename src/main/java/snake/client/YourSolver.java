package snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;

public class YourSolver implements Solver<Board> {

  @Override
  public String get(Board board) {
    SmartSnake ss = new SmartSnake(board);
    Direction solved = ss.solve();
    return solved.toString();
  }

  public static void main(String[] args) {
    WebSocketRunner.runClient(
            "http://104.248.23.201/codenjoy-contest/board/player/0f6k6m3aq5l1bpo9ymmu?code=7093059984507167700",
            new YourSolver(),
            new Board()
    );
  }
}