package com.example.parcial1.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class listas implements Serializable {
    private Integer ID;
    private String  fecha;
    private ArrayList<articulos> items;

    public listas(){

    }

    public listas(Integer ID, String fecha, ArrayList<articulos> items) {
        this.ID = ID;
        this.fecha = fecha;
        this.items = items;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<articulos> getItems() {
        return items;
    }

    public void setItems(ArrayList<articulos> items) {
        this.items = items;
    }
}
