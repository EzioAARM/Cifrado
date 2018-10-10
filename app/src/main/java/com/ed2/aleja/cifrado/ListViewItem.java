package com.ed2.aleja.cifrado;

public class ListViewItem {

    private String Ubicacion;
    private String Nombre;
    private String Metodo;

    public ListViewItem(String ubicacion, String nombre, String metodo){
        Ubicacion = ubicacion;
        Nombre = nombre;
        Metodo = metodo;
    }

    public String getMetodo() {
        return Metodo;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getUbicacion() {
        return Ubicacion;
    }
}
