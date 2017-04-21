package anb.general;

public class Respuesta<T> {
    private int codigo;
    private String mensaje;
    private T resultado;
    private int cantidad;

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje.replace("'","&#39;").replace("\n", "<br>");
    }

    public void setResultado(T resultado) {
        this.resultado = resultado;
    }

    public T getResultado() {
        return resultado;
    }

    public String getMensaje1() {
        return mensaje;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }
}
