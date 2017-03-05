package com.example.alejandro.cajerosceres.dummy;

import java.util.ArrayList;
import java.util.List;

public class DummyContent {

    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    static{
        addItem(new DummyItem("1",1,"CajaRural","",1.2,1.2,"",true));
        addItem(new DummyItem("2",2,"CajaDuero","",1.2,1.2,"",true));
        addItem(new DummyItem("3",3,"Sabadell","",1.2,1.2,"",true));
        addItem(new DummyItem("4",4,"CajaExtremadura","",1.2,1.2,"",true));
        addItem(new DummyItem("5",5,"BBVA","",1.2,1.2,"",true));
    }

    static public class DummyItem {
        String id;
        int idCajero;
        private String entidadBancaria;
        private String uriFotoCajero;
        private double longitud;
        private double latitud;
        private String direccion;
        private boolean fav;

        public DummyItem() {

        }

        public DummyItem(String id, int idCajero, String nombreEntidadBancaria, String uriFotoCajero, double longitud,
                      double latitud, String direccion, boolean fav) {
            this.id = id;
            this.idCajero=idCajero;
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
        public boolean isFav() {
            return fav;
        }
        public void setFav(boolean fav) {
            this.fav = fav;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) { this.id = id; }
        public int getIdCajero() {
            return idCajero;
        }
        public void setIdCajero(int idCajero) { this.idCajero = idCajero; }
    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
    }

}
