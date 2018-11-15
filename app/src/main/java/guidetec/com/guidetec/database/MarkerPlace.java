package guidetec.com.guidetec.database;

public class MarkerPlace {
    private String id;
    private String nombre;
    private String resena;
    private String lon;
    private String lat;
    private int interactuar;
    private int gratis;

    public MarkerPlace(String id, String nombre, String resena, String lon, String lat, int interactuar, int gratis) {
        this.id = id;
        this.nombre = nombre;
        this.resena = resena;
        this.lon = lon;
        this.lat = lat;
        this.interactuar = interactuar;
        this.gratis = gratis;
    }
    public MarkerPlace(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResena() {
        return resena;
    }

    public void setResena(String resena) {
        this.resena = resena;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public int getInteractuar() {
        return interactuar;
    }

    public void setInteractuar(int interactuar) {
        this.interactuar = interactuar;
    }

    public int getGratis() {
        return gratis;
    }

    public void setGratis(int gratis) {
        this.gratis = gratis;
    }
}
