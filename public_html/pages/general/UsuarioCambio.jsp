<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/CambioAction" onsubmit="return fEvalua();" focus="clave">

    <html:hidden property="usuario" value="${sessionScope.ClaseSession.usuario}"/>
       
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="fa fa-user"></i> <strong>Mi cuenta</strong><small> ${sessionScope.ClaseSession.usuario}</small>
               
            </h3>
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#data" aria-controls="data" role="tab" data-toggle="tab">Mis datos personales</a></li>
                <li role="presentation"><a href="#password" aria-controls="password" role="tab" data-toggle="tab">Cambiar contraseña</a></li>
            </ul>
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane fade in active" id="data">
                    <div class="panel-body form-horizontal user-data">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Nombre completo:</label>
                            <div class="col-sm-10">
                                <div class="form-control-static">${sessionScope.ClaseSession.nombreUsuario}</div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Aduana:</label>
                            <div class="col-sm-10">
                                <div class="form-control-static">${sessionScope.aduana}</div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Correo electrónico:</label>
                            <div class="col-sm-10">
                                <div class="form-control-static">${sessionScope.ClaseSession.correo}</div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Gerencia:</label>
                            <div class="col-sm-10">
                                <div class="form-control-static">${sessionScope.ClaseSession.nomGerencia}</div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Unidad:</label>
                            <div class="col-sm-10">
                                <div class="form-control-static">${sessionScope.ClaseSession.nomUnidad}</div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Perfil(es):</label>
                            <div class="col-sm-10">
                                <div class="form-control-static">${sessionScope.ClaseSession.perfil}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane fade" id="password">
                    <div class="panel panel-body margin-0">
                        <div class="alert alert-info">
                            <strong>Nota.-</strong>Hay diferencias entre May&uacute;sculas y Min&uacute;sculas
                        </div>
                        <table class="table-form">           
                            <tr>
                                <th><span style="color:crimson;">*</span> Anterior Contrase&ntilde;a :</th>
                                <td><html:password property="clave" maxlength="15" styleClass="form-control" redisplay="false"/></td>
                            </tr>            
                            <tr>
                                <th><span style="color:crimson;">*</span> Nueva Contrase&ntilde;a :</th>
                                <td><html:password property="confnuevo" maxlength="15" styleClass="form-control" redisplay="false"/></td>
                            </tr>
                            <tr>
                                <th><span style="color:crimson;">*</span> Confirmaci&oacute;n Contrase&ntilde;a :</th>
                                <td><html:password property="nuevo" maxlength="15" styleClass="form-control" redisplay="false"/></td>
                            </tr>   
                        </table>
                        <div class="btn-container text-center">
                            <html:hidden property="opcion" />
                            <html:submit styleClass="btn btn-primary" value="Cambiar contraseña"  onclick="opcion.value=1;bCancel=false"/>
                        </div>
                    </div>
                </div>            
            </div>
        </div>
    </div>
</html:form>
<script>
var bCancel = false;
function fEvalua()
{            
  var Mensaje = "";
  f = document.forms["beanCambioForm"];            
  f.usuario.value = f.usuario.value.toUpperCase;    
  if(bCancel)  {
    return true;  
  }
  if(Anb.filter.empty(f.usuario.value))
    Mensaje = Mensaje + "- 'Usuario' es obligatorio<br>";
  if(Anb.filter.empty( f.clave.value ) )
    Mensaje = Mensaje + "- 'Anterior Contraseña' es obligatorio<br>";
  if(Anb.filter.empty( f.nuevo.value ) )
    Mensaje = Mensaje + "- 'Nueva Contraseña' es obligatorio<br>";
  if(Anb.filter.empty( f.confnuevo.value ) )
    Mensaje = Mensaje + "- 'Confirmación Contrase\u00f1a' es obligatorio<br>";
  if( f.nuevo.value != f.confnuevo.value )
    Mensaje = Mensaje + "- 'Nueva Contraseña' y 'Confirmación Contrase\u00f1a' no son iguales<br>";    
  if( Mensaje == "" ) {
    Anb.confirm('Est\u00e1 seguro de continuar?', function () {
      f.submit();
    });
  } else {
    Mensaje = Mensaje + "<br> Corrija los anteriores errores para continuar";
    Anb.alert( Mensaje );
    return false;
  }
  return false;
}
</script>