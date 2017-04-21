package anb.general;


import cliente.ClaseEnvio;
import cliente.ServiciosUsuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


/*
*   Nombre de la clase: CambioAction, Gestiona el cambio de contraseña
*
*   Fecha creación, Fecha Modificación
*
*   Autor creador, Autor Modificador
*/

public class CambioAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        CambioForm bCambio = (CambioForm)form;

        ActionMessages msg = new ActionMessages();
        ServiciosUsuario serviciosUsuario = new ServiciosUsuario();
        ClaseEnvio claseEnvio = serviciosUsuario.getServiciosUsuario();
        try {
            if (bCambio.getOpcion() == 1) {
                Integer Clave = claseEnvio.cambiaClave(bCambio.getUsuario(), bCambio.getClave(), bCambio.getNuevo());
                int i = (int)Clave.longValue();
                if (i == 0) {
                    msg.add("error", new ActionMessage("error", "No se pudo cambiar la Contrase&ntilde;a, por favor, vuelva a intentar"));
                } else {
                    msg.add("bien", new ActionMessage("bien", "Se cambio correctamente la Contrase&ntilde;a"));
                    request.getSession().removeAttribute("ClaseSession");
                }
            }
            return mapping.findForward("usuario");
        } catch (Exception e) {
            msg.add("error", new ActionMessage("error", e.getMessage()));
            return mapping.findForward("cambio");
        } finally {
            if (!msg.isEmpty())
                this.saveErrors(request, msg);
        }
    }
}
