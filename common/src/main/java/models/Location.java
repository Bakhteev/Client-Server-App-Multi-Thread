package models;

import lombok.Setter;

import java.io.Serializable;

@Setter
public class Location implements Serializable {
    private static final long serialVersionUID = -2236119481629483653L;
    private int id;
    private Long x; //Поле не может быть null
    private int y;
    private Float z; //Поле не может быть null
    private String name; //Поле не может быть null

    public Location() {
    }

    public Location(Long x, int y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public Location(int id, Long x, int y, Float z, String name) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "{" + " id: " + id + ";" + " x: " + x + ";" + " y: " + y + ";" + " z: " + z + ";" + " name: '" + name + "'; " + '}';
    }

    public int getId() {
        return id;
    }
}
