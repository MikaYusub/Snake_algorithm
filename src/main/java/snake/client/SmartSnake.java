package snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import snake.model.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmartSnake {
    private Point stone;
    private Point apple;
    private Lee lee;
    private Point snake_head;
    private Point snake_tail;
    private int snake_length;
    private boolean gameOver;
    private ArrayList<Point> obstacles = new ArrayList<>();
    private Direction currDir;


    public SmartSnake(Board board) {
        List<Point> walls = board.getWalls();
        gameOver = board.isGameOver();
        stone = board.getStones().get(0);
        apple = board.getApples().get(0);
        List<Point> snake = board.getSnake();
        snake_head = board.getHead();
        snake_length = board.getSnake().size();
        currDir = board.getSnakeDirection();
        snake_tail = board.get(
                Elements.TAIL_END_DOWN,
                Elements.TAIL_END_LEFT,
                Elements.TAIL_END_RIGHT,
                Elements.TAIL_END_UP).get(0);
        int dimX = walls.stream().mapToInt(Point::getX).max().orElse(0) + 1;
        int dimY = walls.stream().mapToInt(Point::getY).max().orElse(0) + 1;
        obstacles.addAll(walls);
        obstacles.add(stone);
        obstacles.addAll(snake);
        lee = new Lee(dimX, dimY);
    }

    Direction solve() {
        if (gameOver) return Direction.DOWN;
        Optional<List<LeePoint>> solution_apple = lee.trace(snake_head, apple, obstacles);
        Optional<List<LeePoint>> solution_tail = lee.trace(snake_head, snake_tail, obstacles);
        Optional<List<LeePoint>> solution_stone = lee.trace(snake_head, stone, obstacles);
        Optional<List<LeePoint>> next_solution_tail;
        if (solution_apple.isPresent() & snake_length < 55) {
            Direction next_dir = coord_to_direction(snake_head, solution_apple.get().get(1));
            next_solution_tail = lee.trace(next_position(snake_head, next_dir), snake_tail, obstacles);
            if (!next_solution_tail.isPresent()
                    && solution_tail.isPresent()) return coord_to_direction(snake_head, solution_tail.get().get(0));
            return coord_to_direction(snake_head, solution_apple.get().get(0));
        }
        if (solution_tail.isPresent() & snake_length < 55)
            return coord_to_direction(snake_head, solution_tail.get().get(0));
        if (solution_stone.isPresent())
            return coord_to_direction(snake_head, solution_stone.get().get(0));
        return Direction.DOWN;
    }

    private Point next_position(Point snake_head, Direction dir) {
        if (dir == Direction.LEFT) {
            snake_head.setX(snake_head.getX() + 1);
            return snake_head;
        }
        if (dir == Direction.RIGHT) {
            snake_head.setX(snake_head.getX() - 1);
            return snake_head;
        }
        if (dir == Direction.UP) {
            snake_head.setY(snake_head.getY() - 1);
            return snake_head;
        }
        if (dir == Direction.DOWN) {
            snake_head.setY(snake_head.getY() + 1);
            return snake_head;
        }
        return snake_head;
    }

    private Direction coord_to_direction(Point from, LeePoint to) {
        if (to.x() < from.getX()) return Direction.LEFT;
        if (to.x() > from.getX()) return Direction.RIGHT;
        if (to.y() > from.getY()) return Direction.UP;   // vise versa because of reverted board
        if (to.y() < from.getY()) return Direction.DOWN; // vise versa because of reverted board
        throw new RuntimeException("you shouldn't be there...");
    }

}
