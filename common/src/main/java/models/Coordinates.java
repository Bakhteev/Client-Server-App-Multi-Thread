package models;

import lombok.Setter;

import java.io.Serializable;

@Setter
public class Coordinates implements Serializable {
    private static final long serialVersionUID = -6915628691877313725L;
    private int id;
    private Integer x; //Поле не может быть null
    private int y; //Максимальное значение поля: 988

    public Coordinates() {
    }

    public Coordinates(Integer x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(int id, Integer x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {return id;}

    @Override
    public String toString() {
        return "{" + " id: " + id + ";" + " x: " + x + ";" + " y: " + y + "; " + '}';
    }
}