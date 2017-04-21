package anb.entidades;

public class TipoDocumento {
    
    private String codigo;
    private String descripcion;
    private String lstope;
    private String nitemi;

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setLstope(String lstope) {
        this.lstope = lstope;
    }

    public String getLstope() {
        return lstope;
    }

    public void setNitemi(String nitemi) {
        this.nitemi = nitemi;
    }

    public String getNitemi() {
        return nitemi;
    }
}
