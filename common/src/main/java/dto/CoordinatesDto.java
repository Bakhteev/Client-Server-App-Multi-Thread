package dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CoordinatesDto implements Serializable {
    private Integer x; //Поле не может быть null
    private int y; //Максимальное значение поля: 988

    public CoordinatesDto() {
    }

    public CoordinatesDto(Integer x, int y) {
        this.x = x;
        this.y = y;
    }


}
