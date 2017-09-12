package anb.persistencia;


import anb.bean.DigitalizacionForm;

import anb.entidades.Aduana;
import anb.entidades.TipoDocumento;
import anb.entidades.Tramite;

import anb.general.Conexion;
import anb.general.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import oracle.jdbc.OracleTypes;


public class DigitalizacionDao extends Conexion {
    public DigitalizacionDao() {
        super();
    }


    public List<Aduana> obtenerAduanas() throws SQLException, ClassNotFoundException, NamingException {
        List<Aduana> aduanas = new ArrayList<Aduana>();
        try {
            open();
            call = cn.prepareCall("{ call digital.PKG_DIGITALIZACION.OBTENER_ADUANAS_ACTIVAS (?)}");
            call.registerOutParameter("C_ADUANAS", OracleTypes.CURSOR);
            call.execute();

            rs = (ResultSet)call.getObject("C_ADUANAS");
            if (rs != null) {
                aduanas = new ArrayList<Aduana>();
                while (rs.next()) {
                    Aduana aduana = new Aduana();
                    aduana.setCodigo(rs.getString("CUO_COD"));
                    aduana.setDescripcion(rs.getString("CUO_NAM"));
                    aduanas.add(aduana);
                }
            }
        } finally {
            //if (!esTransaccional()) {
            close();
            //}
        }
        return aduanas;
    }

    public List<Tramite> consultaTramitePorFiltro2(DigitalizacionForm digital) throws SQLException,
                                                                                      ClassNotFoundException,
                                                                                      NamingException {
        List<Tramite> tramites = new ArrayList<Tramite>();
        try {
            open();
            call = cn.prepareCall("{ call digital.PKG_DIGITALIZACION.CONSULTA_POR_FILTRO2 (?,?,? ,?,?,? ,?,? ,?)}");
            call.setString("PRM_CODCONSIGNATARIO", digital.getCodconsignatario());
            call.setString("PRM_TIPODOCUMENTO", digital.getTipodocumento());
            call.setString("PRM_EMISOR", digital.getEmisor());
            call.setString("PRM_ADUANA", digital.getAduana());
            call.setString("PRM_FECHAEMISION_INI", digital.getFechaemisionini());
            call.setString("PRM_FECHAEMISION_FIN", digital.getFechaemisonfin());
            call.setString("PRM_TABLA", Log.TABLA_GENERAL);
            call.setString("PRM_TIPOFECHA", digital.getTipofecha());
            call.registerOutParameter("C_RESULTADO", OracleTypes.CURSOR);
            call.execute();

            rs = (ResultSet)call.getObject("C_RESULTADO");
            if (rs != null)
                tramites = new ArrayList<Tramite>();
            while (rs.next()) {
                Tramite tramite = new Tramite();
                tramite.setCodcons(rs.getString("CNS_CODCONC"));
                tramite.setAdutra(rs.getString("CNS_ADUTRA"));
                tramite.setNrotra(rs.getString("CNS_NROTRA"));
                tramite.setTipodoc(rs.getString("CNS_TIPODOC"));
                tramite.setDsctipo(rs.getString("DSC_TIP"));
                tramite.setEmisor(rs.getString("CNS_EMISOR"));
                tramite.setPath(rs.getString("PATH"));
                tramite.setNomarch(rs.getString("CNS_NOMARCH"));
                tramite.setFecha_emi(rs.getString("CNS_FECHA_EMI"));
                tramite.setFecha_pro(rs.getString("CNS_FECHA_PRO"));
                tramite.setEstado(rs.getString("CNS_ESTADO"));
                tramite.setUsuario(rs.getString(12));
                tramite.setFechasys(rs.getString("CNS_FECHASYS"));
                tramites.add(tramite);
            }
        } finally {
            //if (!esTransaccional())
            close();
        }
        return tramites;

    }

    public List<Tramite> consultaTramite(DigitalizacionForm digital) throws SQLException, ClassNotFoundException,
                                                                            NamingException {
        List<Tramite> tramites = new ArrayList<Tramite>();
        try {
            open();
            call = cn.prepareCall("{ call digital.PKG_DIGITALIZACION.CONSULTA_POR_TRAMITE2 (?,?,? )}");
            call.setString("PRM_TRAMITE", digital.getTramite() + "-" + digital.getTipodocumento());
            call.setString("PRM_TABLA", Log.TABLA_GENERAL);
            call.registerOutParameter("C_RESULTADO", OracleTypes.CURSOR);
            call.execute();

            rs = (ResultSet)call.getObject("C_RESULTADO");
            if (rs != null)
                while (rs.next()) {
                    Tramite tramite = new Tramite();
                    tramite.setCodcons(rs.getString("CNS_CODCONC"));
                    tramite.setAdutra(rs.getString("CNS_ADUTRA"));
                    tramite.setNrotra(rs.getString("CNS_NROTRA"));
                    tramite.setTipodoc(rs.getString("CNS_TIPODOC"));
                    tramite.setDsctipo(rs.getString("DSC_TIP"));
                    tramite.setEmisor(rs.getString("CNS_EMISOR"));
                    tramite.setPath(rs.getString("PATH"));
                    tramite.setNomarch(rs.getString("CNS_NOMARCH"));
                    tramite.setFecha_emi(rs.getString("CNS_FECHA_EMI"));
                    tramite.setFecha_pro(rs.getString("CNS_FECHA_PRO"));

                    tramite.setEstado(rs.getString("CNS_ESTADO"));
                    tramite.setUsuario(rs.getString(12));
                    tramite.setFechasys(rs.getString("CNS_FECHASYS"));
                    tramites.add(tramite);
                }
        } finally {
            //if (!esTransaccional())
            close();
        }
        return tramites;

    }

    public List<Tramite> consultaTramite2(String digital) throws SQLException, ClassNotFoundException,
                                                                 NamingException {
        List<Tramite> tramites = new ArrayList<Tramite>();
        try {
            open();
            call = cn.prepareCall("{ call digital.PKG_DIGITALIZACION.CONSULTA_POR_TRAMITE2 (?,?,? )}");
            call.setString("PRM_TRAMITE", digital);
            call.setString("PRM_TABLA", Log.TABLA_GENERAL);
            call.registerOutParameter("C_RESULTADO", OracleTypes.CURSOR);
            call.execute();

            rs = (ResultSet)call.getObject("C_RESULTADO");
            if (rs != null)
                tramites = new ArrayList<Tramite>();
            while (rs.next()) {
                Tramite tramite = new Tramite();
                tramite.setCodcons(rs.getString("CNS_CODCONC"));
                tramite.setAdutra(rs.getString("CNS_ADUTRA"));
                tramite.setNrotra(rs.getString("CNS_NROTRA"));
                tramite.setTipodoc(rs.getString("CNS_TIPODOC"));
                tramite.setDsctipo(rs.getString("DSC_TIP"));
                tramite.setEmisor(rs.getString("CNS_EMISOR"));
                tramite.setPath(rs.getString("PATH"));
                tramite.setNomarch(rs.getString("CNS_NOMARCH"));
                tramite.setFecha_emi(rs.getString("CNS_FECHA_EMI"));
                tramite.setFecha_pro(rs.getString("CNS_FECHA_PRO"));

                tramite.setEstado(rs.getString("CNS_ESTADO"));
                tramite.setUsuario(rs.getString(12));
                tramite.setFechasys(rs.getString("CNS_FECHASYS"));
                tramites.add(tramite);
            }
        } finally {
            //if (!esTransaccional())
            close();
        }
        return tramites;

    }

    public List<String> consultaTramitenivel1(DigitalizacionForm digital) throws SQLException, ClassNotFoundException,
                                                                                 NamingException {
        List<String> tramites = new ArrayList<String>();

        try {
            open();
            call = cn.prepareCall("{  ? = call digital.PKG_DIGITALIZACION.RECORRIDO2NIVEL1 (?,?,?,? )}");
            call.registerOutParameter(1, OracleTypes.VARCHAR);
            call.setString(2, digital.getTramite());
            call.setString(3, digital.getTipodocumento());
            call.registerOutParameter(4, OracleTypes.VARCHAR);
            call.setString(5, Log.TABLA_GENERAL);
            call.execute();

            String nodos = (String)call.getObject(4);
            String relaciones = (String)call.getObject(1);

            tramites.add(nodos);
            tramites.add(relaciones);

        } finally {
            //if (!esTransaccional())
            close();
        }
        return tramites;

    }

    public List<TipoDocumento> obtenerTipoDocumentos(String lstope, String nitemi) throws SQLException,
                                                                                          ClassNotFoundException,
                                                                                          NamingException {
        List<TipoDocumento> tipodocumentos = new ArrayList<TipoDocumento>();
        try {
            open();
            call = cn.prepareCall("{ call digital.PKG_DIGITALIZACION.OBTENER_TIPO_DOC (?,?,?)}");
            call.setString("PRM_LSTOPE", lstope);
            call.setString("PRM_NIT_EMI", nitemi);
            call.registerOutParameter("C_TIPO_DOC", OracleTypes.CURSOR);
            call.execute();

            rs = (ResultSet)call.getObject("C_TIPO_DOC");
            if (rs != null) {
                tipodocumentos = new ArrayList<TipoDocumento>();
                while (rs.next()) {
                    TipoDocumento tipodocumento = new TipoDocumento();
                    tipodocumento.setCodigo(rs.getString("KEY_TIP"));
                    tipodocumento.setDescripcion(rs.getString("DSC_TIP"));
                    tipodocumento.setLstope(rs.getString("LST_OPE"));
                    tipodocumento.setNitemi(rs.getString("NIT_EMI"));
                    tipodocumentos.add(tipodocumento);
                }
            }
        } finally {
            //if (!esTransaccional()) {
            close();
            //}
        }
        return tipodocumentos;
    }


}
