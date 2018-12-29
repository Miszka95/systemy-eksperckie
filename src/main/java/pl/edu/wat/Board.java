package pl.edu.wat;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Board {

    private static Board INSTANCE;

    @Getter
    private Field[][] fields;

    static Board getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Board();
        }
        return INSTANCE;
    }

    private Board() {
        initializeFields();
    }

    void applyMoves(List<Move> moves) {
        resetFields();
        moves.forEach(this::applyMove);
    }

    void applyMove(Move move) {
        int column = move.getColumn();
        int row = move.getRow();

        collideFields(row, column);
        queen(row, column);
    }

    double scoreForMove(Move move) {
        double score = 0.0;
        int currentRow = move.getRow() + 1;
        for (int i = currentRow; i < fields.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                if (FieldState.EMPTY.equals(fields[i][j].getState())) {
                    score += i;
                }
            }
        }
        score = score / currentRow;
        print();
        System.out.println("SCORE: " + score);
        return score;
    }

    long countQueens() {
        return countState(FieldState.QUEEN);
    }

    void print() {
        for (int i = Algorithm.SIZE; i > 0; i--) {
            System.out.println(String.format("%4s | %s |", i, rowAsText(fields[i - 1])));
        }
    }

    private void collideFields(int row, int column) {
        collideRow(row);
        collideColumn(column);
        collideDiagonals(column, row);
    }

    private void collideRow(int row) {
        IntStream.range(0, Algorithm.SIZE)
                .forEach(i -> collide(row, i));
    }

    private void collideColumn(int column) {
        IntStream.range(0, Algorithm.SIZE)
                .forEach(i -> collide(i, column));
    }

    //TODO: refactor
    private void collideDiagonals(int column, int row) {
        for (int k = row + 1, l = 1; k < Algorithm.SIZE; k++, l++) {
            if (column - l >= 0) {
                collide(k, column - l);
            }
            if (column + l < Algorithm.SIZE) {
                collide(k, column + l);
            }
        }
    }

    private void empty(int row, int columm) {
        setField(row, columm, FieldState.EMPTY);
    }

    private void collide(int row, int column) {
        setField(row, column, FieldState.COLLIDING);
    }

    private void queen(int row, int column) {
        setField(row, column, FieldState.QUEEN);
    }

    private void setField(int row, int column, FieldState state) {
        fields[row][column].setState(state);
    }

    private long countState(FieldState fieldState) {
        return Arrays.stream(fields)
                .flatMap(Arrays::stream)
                .filter(field -> fieldState.equals(field.getState()))
                .count();
    }

    private void initializeFields() {
        fields = new Field[Algorithm.SIZE][Algorithm.SIZE];
        performOnEachField(this::createField);
    }

    private void createField(Integer i, Integer j) {
        fields[i][j] = new Field();
    }

    private void resetFields() {
        performOnEachField(this::empty);
    }

    private String rowAsText(Field[] row) {
        return Arrays.stream(row)
                .map(Field::toString)
                .collect(Collectors.joining(" "));
    }

    private void performOnEachField(BiConsumer<Integer, Integer> consumer) {
        for (int i = 0; i < Algorithm.SIZE; i++) {
            for (int j = 0; j < Algorithm.SIZE; j++) {
                consumer.accept(i, j);
            }
        }
    }
}
