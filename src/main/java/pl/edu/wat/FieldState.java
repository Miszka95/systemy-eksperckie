package pl.edu.wat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FieldState {
    EMPTY(' '),
    COLLIDING('X'),
    QUEEN('#');

    @Getter
    private char sign;
}

