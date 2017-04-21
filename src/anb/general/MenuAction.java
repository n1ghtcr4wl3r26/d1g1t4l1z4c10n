package anb.general;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/*
*   Nombre de la clase: MenuAction, Clase donde se define las pantallas para cada opcion del sistema
*
*   Fecha creación, Fecha Modificación
*
*   Autor creador, Autor Modificador
*/

public class MenuAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        MenuForm bMenu = (MenuForm)form;

        request.getSession().setAttribute("opcion", bMenu.getOpcion());
        
        switch (bMenu.getOpcion()) {

        case 20:
            return mapping.findForward("consultafiltro");

        case 21:
            return mapping.findForward("consultatramite");
        
        case 22:
            return mapping.findForward("consultarelacion");
        
        case 24:
            return mapping.findForward("consultagrafica");
                                                            
        case 90:
            return mapping.findForward("usuario");
        
        case 91:
            return mapping.findForward("log");
        
        default:
            request.getSession().removeAttribute("user.data");
            request.getSession().removeAttribute("opcion");
            request.getSession().removeAttribute("ClaseSession");
            return mapping.findForward("exit");
        }

    }
}
