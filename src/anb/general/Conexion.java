package anb.general;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;


/*
*   Nombre de la clase: conexion, metodos de conexion al esquema de la base de datos
*
*   Fecha creación, Fecha Modificación
*
*   Autor creador, Autor Modificador
*/
public class Conexion {
    
    protected Connection cn = null;
    protected Statement st = null;
    protected CallableStatement call = null;
    protected ResultSet rs = null;
    private boolean transaccional = false;

    public static final int MODO_TRANSACCIONAL = 1;


    protected void open() throws ClassNotFoundException, SQLException, NamingException {
        InitialContext ic = new InitialContext();
        DataSource ds = (DataSource)ic.lookup(Log.JDBC_MAIN);
        cn = ds.getConnection();
        st = cn.createStatement();

        /*
        Class.forName("oracle.jdbc.driver.OracleDriver");
        cn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.2.4:1521:basy", "OEAP","034p");
        */
    }

    protected void open(String jdbc) throws ClassNotFoundException, SQLException, NamingException {
        InitialContext ic = new InitialContext();
        DataSource ds = (DataSource)ic.lookup(jdbc);
        cn = ds.getConnection();
        st = cn.createStatement();
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (call != null) {
                call.close();
                call = null;
            }
            if (cn != null) {
                cn.close();
                cn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTransaccional(boolean transaccional) {
        this.transaccional = transaccional;
    }

    public boolean esTransaccional() {
        return transaccional;
    }

}
