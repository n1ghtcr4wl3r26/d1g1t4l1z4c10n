package anb.entidades;

public class Descarga {
    
    private int id;
    private int id_digitalizacion;
    private long inicio;
    private long fin;
    private String descargado;
    
    private int tipo;
    private int gestion;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId_digitalizacion(int id_digitalizacion) {
        this.id_digitalizacion = id_digitalizacion;
    }

    public int getId_digitalizacion() {
        return id_digitalizacion;
    }

    public void setInicio(long inicio) {
        this.inicio = inicio;
    }

    public long getInicio() {
        return inicio;
    }

    public void setFin(long fin) {
        this.fin = fin;
    }

    public long getFin() {
        return fin;
    }

    public void setDescargado(String descargado) {
        this.descargado = descargado;
    }

    public String getDescargado() {
        return descargado;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setGestion(int gestion) {
        this.gestion = gestion;
    }

    public int getGestion() {
        return gestion;
    }
}
