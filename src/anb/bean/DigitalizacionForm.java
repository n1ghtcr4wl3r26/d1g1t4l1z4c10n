package anb.bean;


import org.apache.struts.action.ActionForm;


public class DigitalizacionForm extends ActionForm {
    private int id;
    //private int gestion;
    private int tipo;
    private String tipo_desc;
    private int cantidad;
    private int inicio;
    private int fin;
    private String fec_solicitud;
    private String email;
    private String fec_registro;
    private String usuario;
    private String respuesta;
    private String token;
    private String codconsignatario;
    private String tipodocumento;
    private String emisor;
    private String aduana;
    private String fechaemisionini;
    private String fechaemisonfin;
    private String duigestion;
    private String duiaduana;
    private String duinumero;
    private String tramite;
    private String tabla;
    private String nivel;
    
    private String tipofecha;
    

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /*public void setGestion(int gestion) {
        this.gestion = gestion;
    }

    public int getGestion() {
        return gestion;
    }*/

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getInicio() {
        return inicio;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getFin() {
        return fin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFec_registro(String fec_registro) {
        this.fec_registro = fec_registro;
    }

    public String getFec_registro() {
        return fec_registro;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setFec_solicitud(String fec_solicitud) {
        this.fec_solicitud = fec_solicitud;
    }

    public String getFec_solicitud() {
        return fec_solicitud;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setTipo_desc(String tipo_desc) {
        this.tipo_desc = tipo_desc;
    }

    public String getTipo_desc() {
        return tipo_desc;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setCodconsignatario(String codconsignatario) {
        this.codconsignatario = codconsignatario;
    }

    public String getCodconsignatario() {
        return codconsignatario;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setAduana(String aduana) {
        this.aduana = aduana;
    }

    public String getAduana() {
        return aduana;
    }

    public void setFechaemisionini(String fechaemisionini) {
        this.fechaemisionini = fechaemisionini;
    }

    public String getFechaemisionini() {
        return fechaemisionini;
    }

    public void setFechaemisonfin(String fechaemisonfin) {
        this.fechaemisonfin = fechaemisonfin;
    }

    public String getFechaemisonfin() {
        return fechaemisonfin;
    }


    public void setDuigestion(String duigestion) {
        this.duigestion = duigestion;
    }

    public String getDuigestion() {
        return duigestion;
    }

    public void setDuiaduana(String duiaduana) {
        this.duiaduana = duiaduana;
    }

    public String getDuiaduana() {
        return duiaduana;
    }

    public void setDuinumero(String duinumero) {
        this.duinumero = duinumero;
    }

    public String getDuinumero() {
        return duinumero;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTramite() {
        return tramite;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNivel() {
        return nivel;
    }

    public void setTipofecha(String tipofecha) {
        this.tipofecha = tipofecha;
    }

    public String getTipofecha() {
        return tipofecha;
    }
}
