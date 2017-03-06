package com.example.alejandro.cajerosceres.DB_Cajeros;

public class Cajero {
    private int id;
    private String entidadBancaria;
    private String uriFotoCajero;
    private double longitud;
    private double latitud;
    private String direccion;
    private int fav;

    public Cajero(){

    }
    public Cajero(int id, String nombreEntidadBancaria, String uriFotoCajero, double longitud,
                  double latitud, String direccion, int fav) {
        this.id = id;
        this.entidadBancaria = nombreEntidadBancaria;
        this.uriFotoCajero = uriFotoCajero;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
        this.fav = fav;
    }

    public String getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(String nombreEntidadBancaria) {
        this.entidadBancaria = nombreEntidadBancaria;
    }

    public String getUriFotoCajero() {
        return uriFotoCajero;
    }

    public void setUriFotoCajero(String uriFotoCajero) {
        this.uriFotoCajero = uriFotoCajero;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int isFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
