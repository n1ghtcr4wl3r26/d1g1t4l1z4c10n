package anb.general;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;


public class Log {
    
    public static String USER = "";    
    public static String JDBC = ""; // JDBC del Log
    public static String JDBC_MAIN = ""; // JDBC del Sistema
    public static String APP = "";
    public static String IP = "";
    public static String PKG_BASE = "";
    public static String PKG_BD = "";
    public static String ADUANA = "";
    public static String VERSION = "";
    public static String VERSION_ASSETS = "";
    public static String NOMBRE_SISTEMA = "";
    public static int GESTION = 0;
    public static String NIT = "";
    public static String TABLA_GENERAL = "";
    public static String TABLA_RELACION = "";
     
    private static String obtenerLineaCodigo(){
        String lugarCodigo = "";
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for(int i=stack.length-1; i>=0;i--){
            StackTraceElement ex = stack[i];
            if(ex.getClassName().startsWith(PKG_BASE)){
                lugarCodigo = ex.getClassName()+" linea "+ex.getLineNumber();
            }
        }
        return lugarCodigo;
    }
    
    private static String obtenerLineaError(Exception e){
        String lugarError = "";
        StackTraceElement[] stack = e.getStackTrace();
        for(int i=stack.length-1; i>=0;i--){
            StackTraceElement ex = stack[i];
            if(ex.getClassName().startsWith(PKG_BASE)){
                lugarError = ex.getClassName()+" linea "+ex.getLineNumber();
            }
        }
        return lugarError;
    }
    
    public static void error(String mensaje, String tipo, Exception e) {
        String referencia = obtenerLineaError(e);
        guardar("ERROR", mensaje+" - "+e.getMessage(), tipo, referencia);
    }
    
    public static void error(String mensaje, String tipo, String referencia) {
        guardar("ERROR", mensaje, tipo, referencia);
    }
       
    public static void info(String mensaje, String tipo) {
        String referencia = obtenerLineaCodigo();
        guardar("INFO", mensaje, tipo, referencia);
    }
    
    public static void advertencia(String mensaje, String tipo) {
        String referencia = obtenerLineaCodigo();
        guardar("WARNING", mensaje, tipo, referencia);
    }
    
    private static void guardar(String nivel, String mensaje, String tipo, String referencia) {
        Connection cn = null;
        CallableStatement call = null;
        
        try {
            cn = getConnection();
           
            call = cn.prepareCall("{ call " + PKG_BD + ".GUARDAR_LOG( ?,?,?,  ?,?,?, ? ) }");
            call.setString("I_APP", APP);
            call.setString("I_NIVEL", nivel);
            call.setString("I_MENSAJE", mensaje);
            call.setString("I_USUARIO", USER);
            call.setString("I_TIPO", tipo);
            call.setString("I_IP", IP);
            call.setString("I_REFERENCIA", referencia);
            call.execute();
            
        } catch (SQLException e) {
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                cn = null;
            }
        }
    }
    
    public static List<LogBean> logs(String nivel, String mensaje, String desde, String hasta, int cantidad) throws SQLException, ClassNotFoundException, NamingException {
        
        Connection cn = null;
        CallableStatement call = null;
        ResultSet rs = null;
        
        List<LogBean> logs = null;
        try {
            cn = getConnection();
            call = cn.prepareCall("{ call " + PKG_BD + ".OBTENER_LOGS( ?,?,?, ?,?,?, ? ) }");
            call.setString("I_APP", APP);
            call.setString("I_NIVEL", nivel);
            call.setString("I_MENSAJE", mensaje);
            call.setString("I_DESDE", desde);
            call.setString("I_HASTA", hasta);
            call.setInt("I_CANTIDAD", cantidad);
            call.registerOutParameter("C_LOGS", OracleTypes.CURSOR);
            call.execute();

            rs = (ResultSet)call.getObject("C_LOGS");
            if (rs != null) {
                logs = new ArrayList<LogBean>();
                while (rs.next()) {
                    LogBean log = new LogBean();
                    log.setId(rs.getInt("LOG_ID"));
                    log.setApp(rs.getString("APP"));
                    log.setNivel(rs.getString("NIVEL"));
                    log.setMensaje(rs.getString("MENSAJE"));
                    log.setIp(rs.getString("IP"));
                    log.setFecha(rs.getString("FECHA"));
                    log.setUsuario(rs.getString("USUARIO"));
                    log.setReferencia(rs.getString("REFERENCIA"));
                    log.setTipo(rs.getString("TIPO"));
                    logs.add(log);
                }
            }
        } finally {
            if (cn != null) {
                cn.close();
            }
        }
        return logs;
    }
    
    public static Respuesta<LogBean[]> getLogs(String nivel, String mensaje, String desde, String hasta, int cantidad) {
        Respuesta<LogBean[]> respuesta = new Respuesta<LogBean[]>();
        respuesta.setCodigo(-1);
        try {
            List<LogBean> result = logs(nivel, mensaje, desde, hasta, cantidad);
            if (result == null) {
                respuesta.setMensaje("Error al consultar");
            } else {
                if (result.size() == 0) {
                    respuesta.setCodigo(0);
                    respuesta.setMensaje("No existen registros");
                } else {
                    respuesta.setCodigo(1);
                    respuesta.setMensaje("OK");
                    respuesta.setResultado(result.toArray(new LogBean[result.size()]));    
                }
            }
        } catch (SQLException e) {
            respuesta.setMensaje("Error no identificado -  " + e.getMessage());
            error("Error no identificado", "BASE DE DATOS", e);
        } catch (ClassNotFoundException e) {
            respuesta.setMensaje("Error no identificado -  " + e.getMessage());
            error("Error no identificado", "BASE DE DATOS", e);
        } catch (NamingException e) {
            respuesta.setMensaje("Error no identificado -  " + e.getMessage());
            error("Error no identificado", "BASE DE DATOS", e);
        }

        return respuesta;
    }

    private static DataSource lookupDataSource() {
        try {
            Context context = new InitialContext();
            return (DataSource) context.lookup(JDBC);
        } catch (NamingException e) {
            throw new RuntimeException("Cannot find JNDI DataSource: " + JDBC, e);
        }
    }
    
    protected static Connection getConnection() throws SQLException {
        return lookupDataSource().getConnection();
    }    
}
