package pl.edu.wat;

import lombok.Getter;
import lombok.Setter;

public class Field {

    @Getter
    @Setter
    private FieldState state = FieldState.EMPTY;

    @Override
    public String toString() {
        return String.valueOf(state.getSign());
    }
}
