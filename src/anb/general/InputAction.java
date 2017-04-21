package anb.general;


import cliente.ClaseEnvio;
import cliente.ServiciosUsuario;

import cliente.bean.ClaseOpcion;
import cliente.bean.ClaseUsuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;


/*
*   Nombre de la clase: inputAction, Clase de autentificacion del usuario
*
*   Fecha creación, Fecha Modificación
*
*   Autor creador, Autor Modificador
*/
public class InputAction extends Action {
    
    private String GetTagXML(Document doc, String tag) {
        try {
            NodeList listaNodosHijos = doc.getElementsByTagName(tag);
            return listaNodosHijos.item(0).getFirstChild().getNodeValue();
        } catch (Exception e) {
            return "";
        }
    }

    private String GetTagXML(Element doc, String tag) {
        try {
            return (doc.getElementsByTagName(tag).item(0).getFirstChild().getNodeValue());
        } catch (Exception e) {
            return "";
        }
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {        
        
        Log.IP = request.getRemoteHost();
        
        request.getSession().removeAttribute("ClaseSession");

        ClaseUsuario user = new ClaseUsuario();
        InputForm ini = (InputForm)form;
        request.getSession().setAttribute("InputForm", ini);
        ActionMessages errors = new ActionMessages();

        ServiciosUsuario serviciosUsuario = new ServiciosUsuario();
        ClaseEnvio claseEnvio = serviciosUsuario.getServiciosUsuario();

        try {

            // Inicio Autentificacion Virtual

            String xmlv = "";
            String vparam = request.getParameter("vsessionid");
            DocumentBuilderFactory factoryv = DocumentBuilderFactory.newInstance();
            DocumentBuilder builderv = factoryv.newDocumentBuilder();
            if (vparam != null) {
                try {
                    xmlv = claseEnvio.fUsuarioVirtual(vparam);
                    Document doc = builderv.parse(new InputSource(new StringReader(xmlv)));
                    doc.getDocumentElement().normalize();
                    ini.setUsuario(GetTagXML(doc, "Usuario"));
                    ini.setClave(GetTagXML(doc, "Clave"));
                } catch (Exception ex) {
                    ;
                }
            }
            //

            String xml =
                claseEnvio.listaOpcionesXML(ini.getUsuario().toUpperCase(), ini.getClave(), Log.APP); // (USUARIO,CLAVE, NOMBRE DEL SISTEMA)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();

            request.getSession().setAttribute("user", ini.getUsuario().toUpperCase());
            request.getSession().setAttribute("estado_reg", "0");

            request.getSession().setAttribute("user.data", user);

            ClaseSession cs = new ClaseSession();
            request.getSession().setAttribute("ClaseSession", cs);
            cs.setUsuario(ini.getUsuario().toUpperCase());
            cs.setNombreUsuario(GetTagXML(doc, "Usuario"));
            cs.setPerfil(GetTagXML(doc, "Perfil"));
            cs.setNit(GetTagXML(doc, "Nit"));
            /*cs.setCodGerencia(Integer.parseInt(GetTagXML(doc, "CodigoGerencia")));
            cs.setCodUnidad(Integer.parseInt(GetTagXML(doc, "CodigoUnidad")));
            cs.setNomGerencia(GetTagXML(doc, "NombreGerencia"));
            cs.setNomUnidad(GetTagXML(doc, "NombreUnidad"));
            cs.setCorreo(GetTagXML(doc, "Correo"));
            cs.setAduana(GetTagXML(doc, "Aduana"));*/
            cs.setNomAduana(ini.getNombreAduana());
            cs.setAduana(ini.getAduana());
            cs.setGestion(Log.GESTION);

            ini.getOpciones().clear();
            ini.setOpciones(new ArrayList());
            NodeList OpcionesLista =
                ((Element)doc.getElementsByTagName("Opciones").item(0)).getElementsByTagName("Opcion");
            if (OpcionesLista != null) {
                for (int i = 0; i < OpcionesLista.getLength(); i++) {
                    Element itemOpcion = (Element)OpcionesLista.item(i);
                    ClaseOpcion bean = new ClaseOpcion();
                    bean.setCodopc(GetTagXML(itemOpcion, "Codigo"));
                    bean.setDesc(GetTagXML(itemOpcion, "Descripcion"));
                    bean.setCodant(GetTagXML(itemOpcion, "NivelSuperior"));
                    bean.setAccion(GetTagXML(itemOpcion, "Accion"));
                    ini.getOpciones().add(bean);
                }
            }
            
            String aduana = cs.getAduana();
            String nit = cs.getNit();
            request.getSession().setAttribute("aduana", aduana);
            
            // Datos para el sistema de Notificaciones
            String USER = cs.getUsuario().toUpperCase();
            request.getSession().setAttribute("USER", USER);
            request.getSession().setAttribute("APP", Log.APP);
            //request.getSession().setAttribute("TOKEN", Notifica.saveToken(USER, APP, request.getLocalAddr(), request.getHeader("User-Agent")));
            // Fin de los datos para Notificaciones
            
            Log.USER = USER;
            Log.ADUANA = aduana;
            Log.NIT = nit;
            //Log.info("El usuario '" + Log.USER + "' ingreso al sistema", "LOGIN");
            request.getSession().setAttribute("opcion", 10);

        } catch (Exception e) {
            errors.add("error", new ActionMessage("error", "No es posible autentificar el usuario y clave"));
            saveErrors(request, errors);
            e.printStackTrace();
            return mapping.findForward("nook");

        } finally {
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("nook");
            }
        }

        return mapping.findForward("ok");
    }
    
    protected void readConfigProperties(HttpServletRequest request) throws IOException {
        
        // Leyendo propiedades de configuración
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("config.properties");
        prop.load(in);
        
        // Datos del sistema
        Log.NOMBRE_SISTEMA = prop.getProperty("system.name");
        Log.APP = prop.getProperty("system.app");
        
        // JDBC para el módulo de persistencia
        Log.JDBC_MAIN = prop.getProperty("system.jdbc");
                
        // Datos del log
        Log.PKG_BASE = prop.getProperty("log.package.base");
        Log.PKG_BD = prop.getProperty("log.package.bd");
        Log.JDBC = prop.getProperty("log.jdbc");
        
        // Tablas de extraccion de informacion
        Log.TABLA_GENERAL = prop.getProperty("system.tabla.general");
        Log.TABLA_RELACION = prop.getProperty("system.tabla.relacion");
        
        request.getSession().setAttribute("NOMBRE_SISTEMA", Log.NOMBRE_SISTEMA);
        
        in.close();
    }
    
    protected void readVersion (HttpServletRequest request) throws IOException {
        // Leyendo propiedades de configuración
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("config.properties");
        prop.load(in);
        
        Log.VERSION = prop.getProperty("system.version");
        Log.VERSION_ASSETS = prop.getProperty("system.version.assets");
        
        Calendar cal = Calendar.getInstance();
        Log.GESTION = cal.get(Calendar.YEAR);
        
        request.getSession().setAttribute("VERSION", Log.VERSION);
        request.getSession().setAttribute("VERSION_ASSETS", Log.VERSION_ASSETS);
        request.getSession().setAttribute("GESTION", Log.GESTION);
        
    }
    
    public static void init (HttpServletRequest request) throws IOException {
        InputAction i = new InputAction();
        i.readVersion(request);
        i.readConfigProperties(request);
    }
    
}
