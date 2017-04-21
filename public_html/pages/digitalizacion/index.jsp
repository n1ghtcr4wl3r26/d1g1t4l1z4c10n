<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title"><i class="fa fa-edit"></i> <strong>Generación de números</strong></h4>
    </div>
    <div class="panel-body">
        <div class="btn-container">
            <button type="button" class="btn btn-primary" onclick="Anb.modal.show('digitalizacionForm.do');"><i class="fa fa-plus"></i> Generar Números</button>
        </div>
        <table class="table table-striped table-hover" id="main-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th class="edit">Opciones</th>
                    <th>Gestión</th>
                    <th>Tipo</th>
                    <th>Cantidad</th>
                    <th>Inicio/Fin</th>
                    <th>Fecha Solicitud</th>
                    <th>Email solicitante</th>
                    <th>Creado por</th>
                </tr>
            </thead>
            <tbody>
            <logic:notEmpty name="digitalizacion">
                <logic:iterate name="digitalizacion" id="digitalizacion">
                <tr>
                    <td>${digitalizacion.id}</td>
                    <td class="edit">
                        <button class="btn btn-default btn-sm" onclick="Anb.modal.show('descargas.do?id=${digitalizacion.id}&t=${digitalizacion.token}', 'lg')"><i class="fa fa-download"></i> Ver descargas</button>
                    </td>
                    <td>${digitalizacion.gestion}</td>
                    <td>${digitalizacion.tipo_desc}</td>
                    <td>${digitalizacion.cantidad}</td>
                    <td>del ${digitalizacion.inicio} al ${digitalizacion.fin}</td>
                    <td><small><em class="date-literal">${digitalizacion.fec_solicitud}</em></small></td>
                    <td>${digitalizacion.email}</td>
                    <td><small><em><strong>${digitalizacion.usuario}</strong> el <span class="datetime-literal">${digitalizacion.fec_registro}</span></em></small></td>
                </tr>
                </logic:iterate>
            </logic:notEmpty>                
            </tbody>
        </table>
    </div>
</div>
<script>
    $(document).ready(function () {        
        new Anb.datatable({
            noSortable : [0],
            order : [[ 0, "asc" ]]
        });
    });
</script>