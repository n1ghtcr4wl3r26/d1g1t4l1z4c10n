<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/sqltaglib.tld" prefix="database" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="anb.general.InputAction "%>
<% InputAction.init(request); %>
<!DOCTYPE html>
<html:html lang="es">
<head>
    <title>${sessionScope.NOMBRE_SISTEMA}</title>  
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Aplicacion Crefas" />
    <meta name="Author" content="DDS" lang="es"/>
    
    <link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="lib/datepicker/css/bootstrap-datepicker.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="lib/datatables/css/jquery.dataTables.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="lib/font-awesome/css/font-awesome.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="lib/select2/css/select2.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="css/main.css?${sessionScope.VERSION_ASSETS}"/>
    <script src="lib/jquery/jquery-1.11.2.min.js"></script>
</head>
<body class="login">
    <header>
        <div class="logo"><h1>${sessionScope.NOMBRE_SISTEMA}</h1></div>
    </header>
    <div class="container-login">
        <div class="container-fluid">
            <div class="form-login">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Debe iniciar su sesión</h3>
                    </div>
                    <html:form action="/ingreso.do" styleClass="panel-body">
                        <html:errors />
                        <div class="input-group">
                            <input type="text" name="usuario" onkeyup="javascript:this.value=this.value.toUpperCase();" maxlength="15" class="form-control" value="${inputForm.usuario}" placeholder="Usuario" />
                            <span class="input-group-addon"><i class="fa fa-user"></i></span>
                        </div>
                        <div class="input-group bottom">
                            <input type="password" name="clave" class="form-control" value="${inputForm.clave}" placeholder="Contraseña" />
                            <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                        </div>
                        <button class="btn btn-primary btn-block" type="submit">Ingresar</button>
                    </html:form>        
                </div>
                 <div class="text-center">
                    <img src="img/anb1.png">
                </div>       
            </div>
        </div>
    </div>
    <div class="text-center">            
        <footer class="footer">
            <span class="text-muted center">&copy; ${sessionScope.GESTION} <a target="_blank" href="http://www.aduana.gob.bo">Aduana Nacional de Bolivia</a>. <span class="hidden-xs">Todos los derechos reservados. </span> Versión ${sessionScope.VERSION} </span>
            <a href="#" class="btn-scroll-top" id="btn-scroll-top">
                <i class="fa fa-chevron-up"></i>
            </a>
        </footer>        
    </div>
    <script src="lib/bootstrap/js/bootstrap.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="lib/datepicker/js/bootstrap-datepicker.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="lib/datatables/js/jquery.dataTables.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="lib/select2/js/select2.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="js/main.js?${sessionScope.VERSION_ASSETS}"></script>
</body>
</html:html>