package dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CoordinatesDto implements Serializable {
    private static final long serialVersionUID = 2655400176129627807L;
    private Integer x; //Поле не может быть null
    private int y; //Максимальное значение поля: 988

    public CoordinatesDto() {
    }

    public CoordinatesDto(Integer x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public String toString() {
        return "CoordinatesDto{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
