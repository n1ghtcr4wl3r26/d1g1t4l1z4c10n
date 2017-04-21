package anb.general;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CambioForm extends ActionForm
{
	private String usuario = "";
	private String clave = "";
	private String nuevo = "";
	private String confnuevo = "";
	private int opcion = 0;

	public void reset( ActionMapping mapping, HttpServletRequest request )
	{
		super.reset( mapping, request );
	}

	public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
	{
		return super.validate( mapping, request );
	}

	public String getUsuario()
	{
		return usuario;
	}

	public void setUsuario( String newUsuario )
	{
		usuario = newUsuario;
	}

	public String getClave()
	{
		return clave;
	}

	public void setClave( String newClave )
	{
		clave = newClave;
	}

	public String getNuevo()
	{
		return nuevo;
	}

	public void setNuevo( String newNuevo )
	{
		nuevo = newNuevo;
	}

	public String getConfnuevo()
	{
		return confnuevo;
	}

	public void setConfnuevo( String newConfnuevo )
	{
		confnuevo = newConfnuevo;
	}

	public int getOpcion()
	{
		return opcion;
	}

	public void setOpcion( int newOpcion )
	{
		opcion = newOpcion;
	}
}