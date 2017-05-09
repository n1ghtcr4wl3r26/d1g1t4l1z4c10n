package anb.action;


import anb.bean.DigitalizacionForm;

import anb.entidades.Aduana;
import anb.entidades.TipoDocumento;
import anb.entidades.Tramite;

import anb.general.Json;
import anb.general.Respuesta;
import anb.general.Util;

import anb.negocio.DigitalizacionNeg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;


public class DigitalizacionAction extends MappingDispatchAction {

    private final DigitalizacionNeg neg = new DigitalizacionNeg();


    public ActionForward porfiltro(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {

        Respuesta<List<TipoDocumento>> res = neg.obtenerTipoDocumento("U", "%");
        if (res.getCodigo() == 1) {
            request.setAttribute("tipos", res.getResultado());
        } else {
            request.setAttribute("tipos", null);
            if (res.getCodigo() == 0) {
                request.setAttribute("warning", res.getMensaje());
            } else {
                request.setAttribute("error", res.getMensaje());
            }
        }

        Respuesta<List<Aduana>> adu = neg.obtenerAduanas();
        if (adu.getCodigo() == 1) {
            request.setAttribute("aduanas", adu.getResultado());
        } else {
            request.setAttribute("aduanas", null);
            if (adu.getCodigo() == 0) {
                request.setAttribute("warning", adu.getMensaje());
            } else {
                request.setAttribute("error", adu.getMensaje());
            }
        }


        return mapping.findForward("consultafiltro");
    }

    public ActionForward portramite(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        Respuesta<List<TipoDocumento>> res = neg.obtenerTipoDocumento("U", "%");
        if (res.getCodigo() == 1) {
            request.setAttribute("tipos", res.getResultado());
        } else {
            request.setAttribute("tipos", null);
            if (res.getCodigo() == 0) {
                request.setAttribute("warning", res.getMensaje());
            } else {
                request.setAttribute("error", res.getMensaje());
            }
        }

        return mapping.findForward("consultatramite");
    }


    public ActionForward porrelacion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        Respuesta<List<TipoDocumento>> res = neg.obtenerTipoDocumento("U", "%");
        if (res.getCodigo() == 1) {
            request.setAttribute("tipos", res.getResultado());
        } else {
            request.setAttribute("tipos", null);
            if (res.getCodigo() == 0) {
                request.setAttribute("warning", res.getMensaje());
            } else {
                request.setAttribute("error", res.getMensaje());
            }
        }

        return mapping.findForward("consultarelacion");
    }

    public ActionForward porgrafica(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        Respuesta<List<TipoDocumento>> res = neg.obtenerTipoDocumento("U", "%");
        if (res.getCodigo() == 1) {
            request.setAttribute("tipos", res.getResultado());
        } else {
            request.setAttribute("tipos", null);
            if (res.getCodigo() == 0) {
                request.setAttribute("warning", res.getMensaje());
            } else {
                request.setAttribute("error", res.getMensaje());
            }
        }

        return mapping.findForward("consultagrafica");
    }


    public ActionForward filtroreporte(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {

        DigitalizacionForm digital = new DigitalizacionForm();
        //digital.setCodconsignatario(request.getParameter("codconsignatario"));
        digital.setTipodocumento(request.getParameter("tipodocumento"));
        //digital.setEmisor(request.getParameter("emisor"));
        digital.setAduana(request.getParameter("aduana"));
        digital.setFechaemisionini(request.getParameter("fechaemisionini"));
        digital.setFechaemisonfin(request.getParameter("fechaemisionfin"));
        //digital.setTabla(request.getParameter("tabla"));
        digital.setTipofecha(request.getParameter("tipofecha"));
        digital.setCodconsignatario("%");
        digital.setEmisor("%");

        Respuesta<Tramite[]> res = null;


        res = neg.reporte(digital);

        if (res.getCodigo() == 1) {
            request.setAttribute("tramites", res.getResultado());
            return mapping.findForward("reporte.lista");
        } else {
            if (res.getCodigo() == 0) {
                Json.warning(response, res.getMensaje());
            } else {
                Json.error(response, res.getMensaje());
            }
        }
        return null;
    }

    public ActionForward tramitereporte(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        DigitalizacionForm digital = new DigitalizacionForm();

        digital.setTipodocumento(request.getParameter("tipodocumento"));

        if (digital.getTipodocumento().equals("960")) {
            digital.setTramite(request.getParameter("duigestion") + "" + request.getParameter("duiaduana") + "C" +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else 
        if (digital.getTipodocumento().equals("932")) {
            digital.setTramite(request.getParameter("duigestion") + "" + request.getParameter("duiaduana") + "C" +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else
        if (digital.getTipodocumento().equals("B74")) {
            digital.setTramite(request.getParameter("duigestion") + "" + request.getParameter("duiaduana") + "C" +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else
        if (digital.getTipodocumento().equals("785")) {
            digital.setTramite(request.getParameter("duiaduana") + "" + request.getParameter("duigestion") + "" +
                               Util.completarCeros8(request.getParameter("duinumero")));
        } else
            digital.setTramite(request.getParameter("tramite"));


        //digital.setTabla(request.getParameter("tabla"));

        Respuesta<Tramite[]> res = null;


        res = neg.reportetramite(digital);

        if (res.getCodigo() == 1) {
            request.setAttribute("tramites", res.getResultado());
            return mapping.findForward("reporte.lista");
        } else {
            if (res.getCodigo() == 0) {
                Json.warning(response, res.getMensaje());
            } else {
                Json.error(response, res.getMensaje());
            }
        }
        return null;
    }


    public ActionForward relacionreporte(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        DigitalizacionForm digital = new DigitalizacionForm();

        digital.setTipodocumento(request.getParameter("tipodocumento"));
        //digital.setNivel(request.getParameter("nivel"));

        if (digital.getTipodocumento().equals("960")) {
            digital.setTramite(request.getParameter("duigestion") + "" + request.getParameter("duiaduana") + "C" +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else

        if (digital.getTipodocumento().equals("932")) {
            digital.setTramite(request.getParameter("duigestion") + "" + request.getParameter("duiaduana") + "C" +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else

        if (digital.getTipodocumento().equals("B74")) {
            digital.setTramite(request.getParameter("duigestion") + "" + request.getParameter("duiaduana") + "C" +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else

        if (digital.getTipodocumento().equals("785")) {
            digital.setTramite(request.getParameter("duiaduana") + "" + request.getParameter("duigestion") + "" +
                               Util.completarCeros8(request.getParameter("duinumero")));
        } else
            digital.setTramite(request.getParameter("tramite"));


        //digital.setTabla(request.getParameter("tabla"));

        Respuesta<Tramite[]> res = null;

        //if(digital.getNivel().equals("1"))
        res = neg.reportetramitenivel1(digital);
        //else
        //    res = neg.reportetramiteniveln(digital);

        if (res.getCodigo() == 1) {
            request.setAttribute("tramites", res.getResultado());
            return mapping.findForward("reporte.lista");
        } else {
            if (res.getCodigo() == 0) {
                Json.warning(response, res.getMensaje());
            } else {
                Json.error(response, res.getMensaje());
            }
        }
        return null;
    }

    public ActionForward graficareporte(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        DigitalizacionForm digital = new DigitalizacionForm();

        digital.setTipodocumento(request.getParameter("tipodocumento"));

        if (digital.getTipodocumento().equals("960")) {
            digital.setTramite(request.getParameter("duigestion") + " " + request.getParameter("duiaduana") + " C " +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else

        if (digital.getTipodocumento().equals("932")) {
            digital.setTramite(request.getParameter("duigestion") + " " + request.getParameter("duiaduana") + " C " +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else

        if (digital.getTipodocumento().equals("B74")) {
            digital.setTramite(request.getParameter("duigestion") + " " + request.getParameter("duiaduana") + " C " +
                               Util.completarCeros7(request.getParameter("duinumero")));
        } else

        if (digital.getTipodocumento().equals("785")) {
            digital.setTramite(request.getParameter("duiaduana") + " " + request.getParameter("duigestion") + " " +
                               Util.completarCeros8(request.getParameter("duinumero")));
        } else
            digital.setTramite(request.getParameter("tramite"));


        //digital.setTabla(request.getParameter("tabla"));

        Respuesta<Tramite[]> res = null;


        res = neg.reportetramite(digital);

        if (res.getCodigo() == 1) {
            request.setAttribute("tramites", res.getResultado());
            return mapping.findForward("reporte.lista");
        } else {
            if (res.getCodigo() == 0) {
                Json.warning(response, res.getMensaje());
            } else {
                Json.error(response, res.getMensaje());
            }
        }
        return null;
    }


    public ActionForward tramitereportenivel1(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {

        DigitalizacionForm digital = new DigitalizacionForm();
        digital.setTramite(request.getParameter("tramite"));
        //digital.setTabla(request.getParameter("tabla"));

        Respuesta<Tramite[]> res = null;


        res = neg.reportetramitenivel1(digital);

        if (res.getCodigo() == 1) {
            request.setAttribute("tramites", res.getResultado());
            return mapping.findForward("reporte.lista");
        } else {
            if (res.getCodigo() == 0) {
                Json.warning(response, res.getMensaje());
            } else {
                Json.error(response, res.getMensaje());
            }
        }
        return null;
    }

    public ActionForward tramitereporteniveln(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {

        DigitalizacionForm digital = new DigitalizacionForm();
        digital.setTramite(request.getParameter("tramite"));
        //digital.setTabla(request.getParameter("tabla"));

        Respuesta<Tramite[]> res = null;


        res = neg.reportetramiteniveln(digital);

        if (res.getCodigo() == 1) {
            request.setAttribute("tramites", res.getResultado());
            return mapping.findForward("reporte.lista");
        } else {
            if (res.getCodigo() == 0) {
                Json.warning(response, res.getMensaje());
            } else {
                Json.error(response, res.getMensaje());
            }
        }
        return null;
    }


}
