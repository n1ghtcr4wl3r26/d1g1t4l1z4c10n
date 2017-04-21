<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="anb.general.InputAction "%>
<% InputAction.init(request); %>

<!DOCTYPE html>
<html:html>
<head>
    <title>${sessionScope.NOMBRE_SISTEMA}</title>  
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="${sessionScope.NOMBRE_SISTEMA}" />
    <meta name="Author" content="DDS" lang="es"/>
    
    <link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="lib/font-awesome/css/font-awesome.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="css/main.css?${sessionScope.VERSION_ASSETS}"/>
    <script src="lib/jquery/jquery-1.11.2.min.js"></script>
    <logic:notEmpty name="OK">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.ok('${OK}');
        });
    </script> 
    </logic:notEmpty>
    <logic:notEmpty name="ERROR">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.error('${ERROR}');
        });
    </script> 
    </logic:notEmpty>
    <logic:notEmpty name="EMAIL">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.email('${EMAIL}');
        });
    </script> 
    </logic:notEmpty>
    <logic:notEmpty name="NOTIFICATION">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.notification('${NOTIFICATION}');
        });
    </script> 
    </logic:notEmpty>
    <logic:notEmpty name="WARNING">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.notification('${WARNING}');
        });
    </script> 
    </logic:notEmpty>
    <style>
        .anb-message {
            border-radius: 0;
            margin: 0;
        }
    </style>
</head>
<body>
    <div id="loading-ajax"></div>
    <div id="container" class="container">
        <div class="logo"></div>
        <div id="anb-messages" class="anb-messages">
            <html:errors/>
        </div>
        <div class="panel">
            <div class="panel-heading">
                <div class="panel-title">
                    <i class="fa fa-download"></i> Descarga de Digitalizacion correlativos.
                    <button tabindex="0" 
                            class="info-help pull-right btn btn-link btn-sm" 
                            role="button" 
                            data-toggle="popover" 
                            data-trigger="focus" 
                            data-placement="left" 
                            data-html="true" 
                            title="<strong class='text-primary'><i class='fa fa-info-circle'></i> Descarga de Digitalizacion correlativos</strong>" 
                            data-content="Acá podrá encontrar información a cerca de su solicitud y podrá descargar archivos <strong>CSV</strong> (compatible con Microsoft Excel), cada uno de estos archivos CSV tiene como máximo <strong>250mil</strong> digitalizacion correlativos, asegurese que haya descargado todos los archivos que necesite.">
                        Ayuda <i class="fa fa-info-circle"></i>
                    </button>
                </div>
            </div>
            <div class="panel-body">
                <%@ include file="/pages/digitalizacion/download_list.jsp" %>
            </div>
        </div>
    </div>
    
    <footer class="footer" style="text-align: center; padding: 0;">
        <span class="text-muted center">&copy; ${sessionScope.GESTION} <a target="_blank" href="http://www.aduana.gob.bo">Aduana Nacional de Bolivia</a>. <span class="hidden-xs">Todos los derechos reservados.</span> Versión ${sessionScope.VERSION} </span>
        <a id="btn-scroll-top" class="btn-scroll-top" href="#">
            <i class="fa fa-chevron-up"></i>
        </a>
    </footer>
    
    <script type="text/js-tmpl" id="anb-message-tmpl">
        <div class="anb-message anb-message-{type}">
            <span class="close" title="Cerrar">x</span>
            <div class="anb-message-header"><i class="fa {icon}"></i> {title}</div>
            <div class="anb-message-body">{message}</div>
        </div>
    </script>
    
    <script src="lib/bootstrap/js/bootstrap.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="js/main.js?${sessionScope.VERSION_ASSETS}"></script>

</body>
</html:html>
