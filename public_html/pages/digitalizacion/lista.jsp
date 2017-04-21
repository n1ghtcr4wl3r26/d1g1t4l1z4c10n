<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<script src="js/tiff.min.js"></script>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
            <i class="fa fa-table"></i> <strong>Lista de Documentos Digitalizados</strong>
            <button tabindex="0" 
                    class="info-help pull-right btn btn-link btn-sm" 
                    role="button" 
                    data-toggle="popover" 
                    data-trigger="focus" 
                    data-placement="left" 
                    data-html="true" 
                    title="" 
                    data-content="">
                Ayuda <i class="fa fa-info-circle"></i>
            </button>    
        </h3>
    </div>
    <div class="panel-body">
        <table class="table table-striped table-hover" id="main-table">
            <thead>
                <tr>
                    <th>N&uacute;mero de Tr&aacute;mite</th>
                    <th>C&oacute;digo Concesionario</th>
                    <th>Aduana</th>                    
                    <th>Tipo de Documento</th>
                    <th>Emisor</th>                    
                    <th>Archivo Digitalizado</th>
                    <th>Fecha de Emisión</th>
                    <th>Fecha de Proceso</th>
                    <th>Estado</th>
                    <th>Usuario</th>
                    <th>Fecha Sistema</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${tramites}" var="tramite">
                <tr>
                    <td>${tramite.nrotra}</td>
                    <td>${tramite.codcons}</td>
                    <td>${tramite.adutra}</td>                    
                    <td>${tramite.dsctipo}</td>
                    <td>${tramite.emisor}</td>
                    <td><a href='${tramite.path}' target="_blank">${tramite.nomarch}</a></td>                   
                    <td>${tramite.fecha_emi}</td>
                    <td>${tramite.fecha_pro}</td>
                    <td>${tramite.estado}</td>
                    <td>${tramite.usuario}</td>
                    <td>${tramite.fechasys}</td>   
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<div id='imagen'>


</div>



<script>
    var DT = new Anb.datatable();

    DT.$('.view').on('click', function() {
        Anb.modal.show('courierDetalles.do?' + Anb.courier.getPk(this, true), 'lg');
    });
        
</script>