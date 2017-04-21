package anb.general;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

public class InputForm extends ActionForm{
    
  String usuario;
  String clave;
  String aduana;
  String nombreAduana;
  
  private ArrayList opciones=new ArrayList();

  /**
   * Reset all properties to their default values.
   * @param mapping The ActionMapping used to select this instance.
   * @param request The HTTP Request we are processing.
   */
  public void reset(ActionMapping mapping, HttpServletRequest request){
    super.reset(mapping, request);
  }

  /**
   * Validate all properties to their default values.
   * @param mapping The ActionMapping used to select this instance.
   * @param request The HTTP Request we are processing.
   * @return ActionErrors A list of all errors found.
   */
  
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
        return super.validate(mapping, request);
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String newUsuario){
        usuario = newUsuario;
    }

    public String getClave(){
        return clave;
    }

    public void setClave(String newClave){
        clave = newClave;
    }
    public ArrayList getOpciones() {
        return opciones;
    }

    public void setOpciones(ArrayList opciones) {
        this.opciones = opciones;
    }

    public void setAduana(String aduana) {
        this.aduana = aduana;
    }

    public String getAduana() {
        return aduana;
    }

    public void setNombreAduana(String nombreAduana) {
        this.nombreAduana = nombreAduana;
    }

    public String getNombreAduana() {
        return nombreAduana;
    }
}
