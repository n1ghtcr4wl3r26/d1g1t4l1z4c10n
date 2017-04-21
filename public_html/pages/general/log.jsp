<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><i class="fa fa-table"></i> <strong>Logs</strong><sub> del sistema</sub></h3>
    </div>
    <div class="panel-body">
        <form action="log.do" method="post" class="filter">
            <div class="form-group daterange-group">
                <label>Fecha de registro</label>
                <div class="input-daterange input-group date-less-than">
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                    <input type="text" class="form-control" name="desde" value="${param.desde}" />
                    <span class="input-group-addon">al</span>
                    <input type="text" class="form-control" name="hasta" value="${param.hasta}" />
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                </div>
            </div>
            <div class="form-group">
                <label>Mensaje</label>
                <input type="text" name="mensaje" class="form-control" value="${param.mensaje}" />
            </div>
            <div class="form-group">
                <label>Nivel</label>
                <select class="form-control" name="nivel">
                    <option value="-" ${param.nivel == '-' ? 'selected' : ''}>TODOS</option>
                    <option value="ERROR" ${param.nivel == 'ERROR' ? 'selected' : ''}>ERROR</option>
                    <option value="INFO" ${param.nivel == 'INFO' ? 'selected' : ''}>INFO</option>
                    <option value="WARNING" ${param.nivel == 'WARNING' ? 'selected' : ''}>WARNING</option>
                </select>
            </div>
            <div class="btn-container">
                <label>&nbsp;</label>
                <button type="submit" class="btn btn-primary">
                    <i class="fa fa-search"></i> Buscar    
                </button>
                <logic:notEmpty name="filter">
                    <button type="button" class="btn btn-default" onclick="window.location=''">
                        <i class="fa fa-cancel"></i> Terminar búsqueda
                    </button>
                </logic:notEmpty>
                <input type="hidden" name="filter" value="SI" />
            </div>
        </form>
        <logic:notEmpty name="logs">
        <table class="table table-striped table-hover" id="main-table">
            <thead>
                <tr>
                    <th>Nivel</th>
                    <th>Tipo</th>
                    <th>Mensaje</th>
                    <th>Fecha</th>
                    <th>Referencia</th>
                    <th>IP</th>
                    <th>Usuario</th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate name="logs" id="log">
                <tr>
                    <td>
                        <span class="label label-${log.nivel == 'ERROR' ? 'danger' : log.nivel == 'INFO' ? 'info' : 'warning'}">${log.nivel}</span>
                    </td>
                    <td>${log.tipo}</td>
                    <td>${log.mensaje}</td>                   
                    <td>${log.fecha}</td>
                    <td>${log.referencia}</td>
                    <td>${log.ip}</td>
                    <td>${log.usuario}</td>
                </tr>
                </logic:iterate>
            </tbody>
        </table>
        </logic:notEmpty>
    </div>
</div>

<script>
    var DT = null;

    $(document).ready(function () {
        DT = new Anb.datatable({ "order": [[ 3, "desc" ]] }); 
    });

</script>