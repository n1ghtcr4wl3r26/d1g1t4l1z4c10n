package anb.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.TilesRequestProcessor;


public class AnbRequestProcessor extends TilesRequestProcessor {
    
    @Override
    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String path = request.getServletPath();
        
        if (path.equals("/logout.do") || 
            path.equals("/ingreso.do") || 
            path.equals("/download.do") || 
            path.equals("/descarga.do") ||
            path.equals("/cambiarEstadoDescarga.do") ||
            (session != null && session.getAttribute("user.data") != null)) {
            System.out.println("Tiene acceso a " + path);
            
            return true;
        }
               
        System.out.println("Sesion expirada");
        
        request.getSession().removeAttribute("user.data");
        request.getSession().removeAttribute("opcion");
        request.getSession().removeAttribute("ClaseSession");
        
        request.setAttribute("message", "Su sesión a expirado, por favor ingrese nuevamente.");

        Util.redirect(request, response, "/input.jsp");
        
        return false;
    }
    
}
