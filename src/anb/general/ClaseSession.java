package anb.general;

/*   
*   Nombre de la clase: ClaseSession, variables de sesion para el sistema
*
*   Fecha creación, Fecha Modificación
*
*   Autor creador, Autor Modificador
*/
 
public class ClaseSession {   
    private String usuario = "";
    private String perfil = "";
    private String nit = "";    
    private String aduana = ""; 
    private String nombreUsuario;
    private String area="";    
    private int    gestion=0;
    private String varAux="";
    private int codGerencia;
    private int codUnidad;
    private String correo;
    private String nomGerencia;
    private String nomUnidad;
    private String nomAduana;
    
    public ClaseSession() { 
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getAduana() {
        return aduana;
    }

    public void setAduana(String aduana) {
        this.aduana = aduana;
    }


    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setGestion(int gestion) {
        this.gestion = gestion;
    }

    public int getGestion() {
        return gestion;
    }

    public void setVarAux(String varAux) {
        this.varAux = varAux;
    }

    public String getVarAux() {
        return varAux;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setCodGerencia(int codGerencia) {
        this.codGerencia = codGerencia;
    }

    public int getCodGerencia() {
        return codGerencia;
    }

    public void setCodUnidad(int codUnidad) {
        this.codUnidad = codUnidad;
    }

    public int getCodUnidad() {
        return codUnidad;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setNomGerencia(String nomGerencia) {
        this.nomGerencia = nomGerencia;
    }

    public String getNomGerencia() {
        return nomGerencia;
    }

    public void setNomUnidad(String nomUnidad) {
        this.nomUnidad = nomUnidad;
    }

    public String getNomUnidad() {
        return nomUnidad;
    }

    public void setNomAduana(String nomAduana) {
        this.nomAduana = nomAduana;
    }

    public String getNomAduana() {
        return nomAduana;
    }
}
