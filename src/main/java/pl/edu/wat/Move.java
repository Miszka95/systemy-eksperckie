package pl.edu.wat;

import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Move {

    private static int counter = 0;

    private int id;
    private int row;
    private int column;
    private double score;
    private boolean lastQueen;

    static Optional<Move> createMove(List<Move> moves, int targetRow, int targetColumn) {
        Board board = Board.getInstance();
        board.applyMoves(moves);

        Move move = new Move(targetRow, targetColumn);
        if (!move.isValid()) {
            return Optional.empty();
        }

        board.applyMove(move);
        move.setScore(board.scoreForMove(move));
        move.setLastQueen(board.countQueens() == Algorithm.SIZE);
        return Optional.of(move);
    }

    private Move(int row, int column) {
        this.row = row;
        this.column = column;
        this.id = ++counter;
    }

    private boolean isValid() {
        return FieldState.EMPTY.equals(Board.getInstance().getFields()[row][column].getState());
    }

    private void log() {
        out.println(String.format("pl.edu.wat.Move %s created for row %s and column %s, score: %s", id, row, column, score));
    }
}
