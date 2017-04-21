package anb.general;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class LogAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        
        Respuesta<LogBean[]> res = null;
        if (request.getParameter("filter") != null) {
            request.setAttribute("filter", request.getParameter("filter"));
            String nivel = request.getParameter("nivel");
            String mensaje = request.getParameter("mensaje");
            String desde = request.getParameter("desde");
            String hasta = request.getParameter("hasta");
            res = Log.getLogs(nivel, mensaje, desde, hasta, 1000);
        } else {
            res = Log.getLogs("-", "", "", "", 300);
        }
        
        if (res.getCodigo() == 1) {
            request.setAttribute("logs", res.getResultado());
        } else {
            if (res.getCodigo() == 0) {
                request.setAttribute("WARNING", res.getMensaje());
            } else {
                request.setAttribute("ERROR", res.getMensaje());    
            }
        }
        return mapping.findForward("log");
    }
}
