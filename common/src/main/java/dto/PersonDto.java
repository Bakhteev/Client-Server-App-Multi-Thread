package dto;


import lombok.Getter;
import lombok.Setter;
import models.Coordinates;
import models.EyesColor;
import models.HairsColor;
import models.Location;

import java.io.Serializable;

@Setter
@Getter
public class PersonDto implements Serializable {
    private static final long serialVersionUID = 8888793084928896959L;
    private String name = null; //Поле не может быть null, Строка не может быть пустой
    private CoordinatesDto coordinatesDto = null; //Поле не может быть null
    private Long height = null; //Поле не может быть null, Значение поля должно быть больше 0
    private float weight = 0; //Значение поля должно быть больше 0
    private EyesColor eyesColor = null; //Поле не может быть null
    private HairsColor hairsColor = null; //Поле не может быть null
    private LocationDto locationDto = null; //Поле не может быть null
    private int ownerId = 0;

    public PersonDto() {
    }

    public PersonDto(String name, CoordinatesDto coordinatesDto, Long height, float weight, EyesColor eyesColor,
                     HairsColor hairsColor, LocationDto locationDto) throws IllegalArgumentException {
        this.name = name;
        this.coordinatesDto = coordinatesDto;
        this.height = height;
        this.weight = weight;
        this.eyesColor = eyesColor;
        this.hairsColor = hairsColor;
        this.locationDto = locationDto;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinatesDto.toString() +
                ", height=" + height +
                ", weight=" + weight +
                ", eyesColor=" + eyesColor +
                ", hairsColor=" + hairsColor +
                ", location=" + locationDto.toString() +
                '}';
    }
}
