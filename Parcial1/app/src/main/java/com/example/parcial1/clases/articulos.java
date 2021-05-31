package com.example.parcial1.clases;

import java.io.Serializable;

public class articulos implements Serializable {

    private boolean active = false;
    private String nombre;
    private Integer ID;
    private boolean estado = false;

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public articulos() {

    }
    public articulos(String nombre, Integer ID) {
        this.nombre = nombre;
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
