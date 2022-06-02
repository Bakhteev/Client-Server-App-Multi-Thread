package dto;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class LocationDto implements Serializable {
    private Long x; //Поле не может быть null
    private int y;
    private Float z; //Поле не может быть null
    private String name; //Поле не может быть null

    public LocationDto() {
    }

    public LocationDto(Long x, int y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
}
