<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

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
    <link rel="stylesheet" type="text/css" href="lib/datepicker/css/bootstrap-datepicker.min.css?${sessionScope.VERSION_ASSETS}">
    <!--<link rel="stylesheet" type="text/css" href="lib/datatables/css/jquery.dataTables.min.css?${sessionScope.VERSION_ASSETS}">-->
    <link rel="stylesheet" type="text/css" href="lib/font-awesome/css/font-awesome.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="lib/select2/css/select2.min.css?${sessionScope.VERSION_ASSETS}">
    <link rel="stylesheet" type="text/css" href="css/main.css?${sessionScope.VERSION_ASSETS}"/>
    <script src="lib/jquery/jquery-1.11.2.min.js"></script>
    <logic:notEmpty name="OK">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.ok('${OK}');
        });
    </script>
    <% request.getSession().removeAttribute("OK"); %>
    </logic:notEmpty>
    <logic:notEmpty name="ERROR">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.error('${ERROR}');
        });
    </script>
    <% request.getSession().removeAttribute("ERROR"); %>
    </logic:notEmpty>
    <logic:notEmpty name="EMAIL">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.email('${EMAIL}');
        });
    </script>
    <% request.getSession().removeAttribute("EMAIL"); %>
    </logic:notEmpty>
    <logic:notEmpty name="NOTIFICATION">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.notification('${NOTIFICATION}');
        });        
    </script>
    <% request.getSession().removeAttribute("NOTIFICATION"); %>
    </logic:notEmpty>
    <logic:notEmpty name="WARNING">    
    <script type="text/javascript">
        $(document).ready(function() {            
            Anb.message.notification('${WARNING}');
        });
    </script> 
    <% request.getSession().removeAttribute("WARNING"); %>
    </logic:notEmpty>
</head>
<body>
    <div id="loading-ajax"></div>
    <div id="container">
        <tiles:insert attribute="header" />
        <tiles:insert attribute="sidebar" />
        <div class="container-fluid" id="container-main">
            <div id="anb-messages" class="anb-messages">
                <html:errors/>
            </div>
            <tiles:insert attribute="content" />
        </div>
    </div>
    <tiles:insert attribute="footer" />
    
    <div id="main-modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content"></div>
        </div>
    </div>
    <div id="second-modal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content"></div>
        </div>
    </div>
    <div id="alert-modal" class="modal"></div>        
    <script type="text/js-tmpl" id="anb-message-tmpl">
        <div class="anb-message anb-message-{type}">
            <span class="close" title="Cerrar">x</span>
            <div class="anb-message-header"><i class="fa {icon}"></i> {title}</div>
            <div class="anb-message-body">{message}</div>
        </div>
    </script>
    <script type="text/tmpl-js" id="tmpl-alert">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">{title}</h4>
                </div>
                <div class="modal-body">
                    <div>{message}</div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default {button_cancel}" data-dismiss="modal" ><i class="fa fa-ban"></i> <span>Cancelar</span></button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal"><i class="fa fa-check"></i> <span>Aceptar</span></button>
                </div>
            </div>
        </div>
    </script>
    <script type="text/tmpl-js" id="tmpl-date">
        <div class="input-group date">
            <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
        </div>
    </script>
    <script type="text/tmpl-js" id="tmpl-print">
        <!DOCTYPE html>
        <html lang="es">
            <head>
                <title>Print</title>
                <meta charset="UTF-8" />                
            </head>
            <body>
                <style>{css}</style>
                {body}
            </body>
        </html>
    </script>

    <script src="lib/bootstrap/js/bootstrap.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="lib/datepicker/js/bootstrap-datepicker.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="lib/datatables/js/jquery.dataTables.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="lib/select2/js/select2.min.js?${sessionScope.VERSION_ASSETS}"></script>
    <script src="js/main.js?${sessionScope.VERSION_ASSETS}"></script>
    <tiles:useAttribute name="jscript" scope="request" ignore="false" />

</body>
</html:html>
