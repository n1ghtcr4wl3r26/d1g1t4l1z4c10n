<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div class="alert alert-info">
    <div class="form-horizontal form-static flex-row">
    <logic:notEmpty name="digitalizacion">
        <div class="flex-col-6">
            <div class="form-group">
                <label class="col-sm-4 control-label">Gestión:</label>
                <div class="col-sm-8">
                    <p class="form-control-static">${digitalizacion.gestion}</p>
                </div>
            </div>
        </div>
        <div class="flex-col-6">
            <div class="form-group">
                <label class="col-sm-4 control-label">Tipo:</label>
                <div class="col-sm-8">
                    <p class="form-control-static">${digitalizacion.tipo_desc}</p>
                </div>
            </div>
        </div>
        <div class="flex-col-6">
            <div class="form-group">
                <label class="col-sm-4 control-label">Cantidad:</label>
                <div class="col-sm-8">
                    <p class="form-control-static">${digitalizacion.cantidad}</p>
                </div>
            </div>
        </div>
        <div class="flex-col-6">
            <div class="form-group">
                <label class="col-sm-4 control-label">Inicio/Fin:</label>
                <div class="col-sm-8">
                    <p class="form-control-static">Del ${digitalizacion.inicio} al ${digitalizacion.fin}</p>
                </div>
            </div>
        </div>
        <div class="flex-col-6">
            <div class="form-group">
                <label class="col-sm-4 control-label">Fecha Solicitud:</label>
                <div class="col-sm-8">
                    <p class="form-control-static"><small><em class="date-literal">${digitalizacion.fec_solicitud}</em></small></p>
                </div>
            </div>
        </div>
        <div class="flex-col-6">
            <div class="form-group">
                <label class="col-sm-4 control-label">Email solicitante:</label>
                <div class="col-sm-8">
                    <p class="form-control-static">${digitalizacion.email}</p>
                </div>
            </div>
        </div>
        <div class="flex-col-12">
            <div class="form-group">
                <label class="col-sm-2 control-label">Creado por:</label>
                <div class="col-sm-10">
                    <p class="form-control-static"><small><em><strong>${digitalizacion.usuario}</strong> el <span class="datetime-literal">${digitalizacion.fec_registro}</span></em></small></p>
                </div>
            </div>
        </div>
    </logic:notEmpty>
    </div>        
</div>
<% int c = 0;%>
<div class="download" id="download">
    <div class="flex-row" style="margin: 0;">
        <logic:notEmpty name="descargas">                
            <logic:iterate name="descargas" id="descarga">
                <div class="flex-col-4">
                    <div class="alert alert-success ${descarga.descargado == 'SI' ? 'active' : ''}">
                        <label><i class="fa fa-check"></i> <%=++c%>. Del ${descarga.inicio} al ${descarga.fin}</label> 
                        <button type="button" class="btn btn-primary btn-sm btn-download" title="Descargar" data-id="${descarga.id}"><i class="fa fa-download"></i> Descargar</button>
                    </div>
                </div>
            </logic:iterate>
        </logic:notEmpty>
    </div>
</div>
<div class="alert alert-warning">
    <div class="flex-row description">
        <div class="flex-col-4"><strong><span id="download-total"><%=c%></span> archivos en total para descargar.</strong></div>
        <div class="flex-col-4"><strong><i class="fa fa-check" style="color: #04a875;"></i> <span id="download-success"></span> archivos descargados.</strong></div>
        <div class="flex-col-4"><strong><i class="fa fa-check" style="color: #cccccc;"></i> <span id="download-pending"></span> archivos sin descargar.</strong></div>
    </div>
</div>

<script>
    var $success = $('#download-success'),
        $pending = $('#download-pending'),
        $total = $('#download-total'),
        token = '${digitalizacion.token}';
    $('.btn-download').on('click', function () {
        var $this = $(this);
        var id = this.getAttribute("data-id");
        $.get('cambiarEstadoDescarga.do?id=' + id + '&t=' + token, function (response) {
            if (response.state == 'OK') {
                Anb.message.ok(response.data);
                $this.parent().addClass('active');
                var successLength = $('#download').find('.active').length;
                $success.html(successLength);
                $pending.html($total.html() - success);
                window.location = 'descarga.do?id=' + id + '&t=' + token;
            }            
        });
    });
    var success = $('#download').find('.active').length;
    $success.html(success);
    $pending.html($total.html() - success);    
</script>
