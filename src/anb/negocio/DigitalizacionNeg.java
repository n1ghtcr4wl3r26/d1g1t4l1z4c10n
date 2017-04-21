package anb.negocio;


import anb.bean.DigitalizacionForm;

import anb.entidades.Aduana;
import anb.entidades.TipoDocumento;
import anb.entidades.Tramite;

import anb.general.Log;
import anb.general.Respuesta;

import anb.persistencia.DigitalizacionDao;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;


public class DigitalizacionNeg {
    private final DigitalizacionDao dao = new DigitalizacionDao();
    private final String CORRECTO_BD = "CORRECTO";
    private final String ERROR_BD = "ERROR";
    
    private boolean estaConectadoBd() {
        return dao != null;
    }
    
    // LLAMADA A FUNCIONES PARAMETRICAS
    
    public Respuesta<List<Aduana>> obtenerAduanas(){
        Respuesta<List<Aduana>> respuesta = new Respuesta<List<Aduana>>();
        respuesta.setCodigo(-1);
        if(estaConectadoBd()){
            try{
                List<Aduana> res = dao.obtenerAduanas();
                if(res == null && res.size() == 0){
                     respuesta.setCodigo(0);
                     respuesta.setMensaje("No existen aduanas registradas");
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(res);
                    }               
                }
            catch (SQLException e){
                respuesta.setMensaje("Error no identificado - "+ e.getMessage());
                Log.error("Error no identificado","BASE DE DATOS",e);
                e.printStackTrace();
                }
            catch (ClassNotFoundException e){
                respuesta.setMensaje("Error no identificado - "+ e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);                
                }
            catch (NamingException e){
                  respuesta.setMensaje("Error no identificado - "+e.getMessage());
                  Log.error("Error no identificado", "BASE DE DATOS", e);
                }            
        } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
            }
        
        return respuesta;        
        }
    
    public Respuesta<List<TipoDocumento>> obtenerTipoDocumento(String lstope, String nitemi){
        Respuesta<List<TipoDocumento>> respuesta = new Respuesta<List<TipoDocumento>>();
        respuesta.setCodigo(-1);
        if(estaConectadoBd()){
            try{
                List<TipoDocumento> res = dao.obtenerTipoDocumentos(lstope, nitemi);
                if(res == null && res.size() == 0){
                    respuesta.setCodigo(0);
                    respuesta.setMensaje("No existen tipos de documentos registrados");
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(res);
                }

                }
            catch(SQLException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado","BASE DE DATOS",e);
                e.printStackTrace();
                }
            catch(ClassNotFoundException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado","BASE DE DATOS", e);
                }
            catch(NamingException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado","BASE DE DATOS",e);
                } 
            } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
                }
            return respuesta;            
        }
    
    // LLAMADA A FUNCIONES DE CONSULTA
    
    public Respuesta<List<Tramite>> consultaTramitePorFiltro (DigitalizacionForm digital){
        Respuesta<List<Tramite>> respuesta = new Respuesta<List<Tramite>>();
        if(estaConectadoBd()){
            try{
                List<Tramite> res = dao.consultaTramitePorFiltro(digital);
                if(res == null && res.size() == 0){
                    respuesta.setCodigo(0);
                    respuesta.setMensaje("No se encontraron coincidencias para la consulta");
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(res);                    
                    }
            }
            catch (SQLException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
                e.printStackTrace();
                }
            catch(ClassNotFoundException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
                }
            catch(NamingException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
                }
            
        } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
            }        
        return respuesta;
        }
    
    
    public Respuesta<Tramite[]> reporte(DigitalizacionForm digital) {
        Respuesta<Tramite[]> respuesta = new Respuesta<Tramite[]>();
        respuesta.setCodigo(-1);
        if (estaConectadoBd()) {
            try {
                List<Tramite> result = dao.consultaTramitePorFiltro2(digital);
                if (result == null || result.size() == 0) {
                    respuesta.setMensaje("No existen registros");
                    respuesta.setCodigo(0);
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(result.toArray(new Tramite[result.size()]));
                }
            } catch (SQLException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            } catch (ClassNotFoundException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            } catch (NamingException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            }
        } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
        }

        return respuesta;
    }
    
    
    public Respuesta<Tramite[]> reportetramite(DigitalizacionForm digital) {
        Respuesta<Tramite[]> respuesta = new Respuesta<Tramite[]>();
        respuesta.setCodigo(-1);
        if (estaConectadoBd()) {
            try {
                List<Tramite> result = dao.consultaTramite(digital);
                if (result == null || result.size() == 0) {
                    respuesta.setMensaje("No existen registros");
                    respuesta.setCodigo(0);
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(result.toArray(new Tramite[result.size()]));
                }
            } catch (SQLException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            } catch (ClassNotFoundException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            } catch (NamingException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            }
        } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
        }

        return respuesta;
    }
    
    public Respuesta<Tramite[]> reportetramitenivel1(DigitalizacionForm digital) {
        Respuesta<Tramite[]> respuesta = new Respuesta<Tramite[]>();
        DigitalizacionForm dig = new DigitalizacionForm();
        
        List<Tramite> tramites = null;
        respuesta.setCodigo(-1);
        
        if (estaConectadoBd()) {
            try {
                List<Tramite> result = new ArrayList<Tramite>();
                List<String> res = dao.consultaTramitenivel1(digital);
                String nodos;
                nodos = res.get(0);
                List<String> listanodos = Arrays.asList(nodos.replace(";", "&").split("&"));

                String tram = "";

                for (int i = 0; i < listanodos.size(); i++) {
                    tram = listanodos.get(i);
                    tramites = dao.consultaTramite2(tram);
                    if (tramites.size() > 0)
                        result.addAll(tramites);
                    else{
                        Tramite tramvacio = new Tramite();
                        tramvacio.setNrotra(tram);
                        tramvacio.setAdutra("-");
                        tramvacio.setCodcons("NO EXISTE DOCUMENTO ESCANEADO");
                        tramvacio.setDsctipo("-");
                        tramvacio.setEmisor("-");
                        tramvacio.setEstado("-");
                        tramvacio.setFecha_emi("-");
                        tramvacio.setNomarch("-");
                        tramvacio.setPath("");
                        tramvacio.setTipodoc("-");
                        result.add(tramvacio);
                    }
                }
                if (result == null || result.size() == 0) {
                    respuesta.setMensaje("No existen registros");
                    respuesta.setCodigo(0);
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(result.toArray(new Tramite[result.size()]));
                }
            } catch (SQLException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            } catch (ClassNotFoundException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            } catch (NamingException e) {
                respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
            }
        } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
        }

        return respuesta;
    }
    
    public Respuesta<Tramite[]> reportetramiteniveln(DigitalizacionForm digital) {
            Respuesta<Tramite[]> respuesta = new Respuesta<Tramite[]>();
            DigitalizacionForm dig = new DigitalizacionForm();
            
            List<Tramite> tramites = null;
            respuesta.setCodigo(-1);
            if (estaConectadoBd()) {
                try {
                    List<Tramite> result = new ArrayList<Tramite>();
                    List<String> res = dao.consultaTramiteniveln(digital);
                    String nodos;
                    nodos = res.get(0);
                    List<String> listanodos = Arrays.asList(nodos.replace(";", "&").split("&"));

                    String tram = "";

                    for (int i = 0; i < listanodos.size(); i++) {
                        tram = listanodos.get(i);
                        tramites = dao.consultaTramite(tram);
                        if (tramites.size() > 0)
                            result.addAll(tramites);
                    }
                    if (result == null || result.size() == 0) {
                        respuesta.setMensaje("No existen registros");
                        respuesta.setCodigo(0);
                    } else {
                        respuesta.setCodigo(1);
                        respuesta.setMensaje("OK");
                        respuesta.setResultado(result.toArray(new Tramite[result.size()]));
                    }
                } catch (SQLException e) {
                    respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                    Log.error("Error no identificado", "BASE DE DATOS", e);
                } catch (ClassNotFoundException e) {
                    respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                    Log.error("Error no identificado", "BASE DE DATOS", e);
                } catch (NamingException e) {
                    respuesta.setMensaje("Error no identificado -  " + e.getMessage());
                    Log.error("Error no identificado", "BASE DE DATOS", e);
                }
            } else {
                respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
            }

            return respuesta;
        }
    
    
    public Respuesta<List<Tramite>> consultaTramitePorDui (DigitalizacionForm digital){
        Respuesta<List<Tramite>> respuesta = new Respuesta<List<Tramite>>();
        if(estaConectadoBd()){
            try{
                List<Tramite> res = dao.consultaTramitePorDui(digital);
                if(res == null && res.size() == 0){
                    respuesta.setCodigo(0);
                    respuesta.setMensaje("No se encontraron coincidencias para la consulta");
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(res);                    
                    }
            }
            catch (SQLException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
                e.printStackTrace();
                }
            catch(ClassNotFoundException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
                }
            catch(NamingException e){
                respuesta.setMensaje("Error no identificado - "+e.getMessage());
                Log.error("Error no identificado", "BASE DE DATOS", e);
                }
            
        } else {
            respuesta.setMensaje("Error. No se puede conectar a la base de datos.");
            }        
        return respuesta;
        }
    
  
}
