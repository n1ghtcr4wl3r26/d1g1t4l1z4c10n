<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

 
<div class="panel panel-default">


<div class="panel-heading">
        <h4 class="panel-title"><strong>DOCUMENTOS DIGITALIZADOS</strong></h4>
    </div>
  

  <div class="modal-body form-horizontal">
  
<html:form styleId="form-digitalizacion" action="consultafiltro.do">
  

    <%/*<div class="form-group">
        <label class="col-sm-3 control-label">Codigo Consignatario:</label>
        <div class="col-sm-3">
           <input type="text" name="codconsignatario" class="form-control required numeric" maxlength="12"  />
        </div>
    
        <label class="col-sm-3 control-label">Emisor:</label>
        <div class="col-sm-3">
            <input type="text" name="emisor" class="form-control required numeric" maxlength="12" />
        </div>
    </div>*/%>
    <div class="form-group">
        <label class="col-sm-3 control-label">Tipo de Documento:</label>
        <div class="col-sm-9">
            <html:select property="tipodocumento" styleClass="form-control required">
                <option value="-">Seleccione...</option>
                <option value="%">TODOS LOS TIPOS DE DOCUMENTOS</option>
                <html:optionsCollection name="tipos" value="codigo" label="descripcion"/>
            </html:select>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">Aduana:</label>
        <div class="col-sm-9">
            <html:select property="aduana" styleClass="form-control required">
                <option value="-">Seleccione...</option>
                <option value="%">TODAS LAS ADUANAS</option>
                <html:optionsCollection name="aduanas" value="codigo" label="descripcion"/>
            </html:select>
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-3 control-label">Fecha de desde:</label>
        <div class="col-sm-3">
            <input type="text" name="fechaemisionini" id="fechaemisionini" class="form-control required datepicker date-less-than" />
        </div>
   
        <label class="col-sm-3 control-label">Fecha de hasta:</label>
        <div class="col-sm-3">
            <input type="text" name="fechaemisionfin" id="fechaemisionfin" class="form-control required datepicker date-less-than" />
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-3 control-label">Tipo de Fecha:</label>
        <div class="col-sm-9">
            <html:select property="tipofecha" styleClass="form-control required">
                <option value="-">Seleccione...</option>
                <option value="EMISION">Fecha de Emisi&oacute;n</option>
                <option value="PROCESAMIENTO">Fecha de Procesamiento</option>
            </html:select>
        </div>
    </div>
  
    <% /*<div class="form-group">
        <label class="col-sm-3 control-label">Tabla a Consultar:</label>
        <div class="col-sm-9">
            <html:select property="tabla" styleClass="form-control required">
                <option value="-">Seleccione...</option>
                <option value="GENERAL">GENERAL</option>
                <option value="GENERAL_OTRO">GENERAL_OTRO</option>
                <option value="GENERAL_OTRO1">GENERAL_OTRO1</option>
            </html:select>
        </div>
    </div> */ %>
  
     <div class="btn-container">
            <button type="submit" id="boton" class="btn btn-primary" >Consultar</button>            
     </div>
     <div class="form-group">
            <label class="label-message-required">
                * Campos Obligatorios
            </label>
     </div>       
</html:form>
</div>
</div>

<div id="container-reporte"></div>

<script>
    $(document).ready(function () {

        var $container_reporte = $('#container-reporte');
        Anb.form.submit('#form-digitalizacion', function (form) {
        Anb.form.cleanErrors(form);
        if (Anb.validate.run(form)) {
            var dias = diasentrefechas($('#fechaemisionini').val(),$('#fechaemisionfin').val());
                
            if(dias<0){
                alert("La 'Fecha de Emisión hasta', no puede ser menor a la 'Fecha de Emisión desde'");
                return false;
            }
            else{
                if(dias>366){
                    alert("El rango de fechas no puede ser mayor a un año'");
                    return false;
                }
                else{
                    Anb.loading.show("Procesando consulta, espere por favor");
                    $.post('filtroReporte.do', $(form).serialize(), function (response) {
                    $container_reporte.html(response);
                    Anb.load.render('#container-reporte');
                    });
                }
            }    
        }
        });
    
        $('#limpiar').on('click', function () {
            $container_reporte.html("");
        });
        
        function diasentrefechas(fechadesde,fechahasta){
            var dia1 = fechadesde.substring(0,2);
            var mes1 = fechadesde.substring(3,5);
            var anio1= fechadesde.substring(6,10);
            var dia2 = fechahasta.substring(0,2);
            var mes2 = fechahasta.substring(3,5);
            var anio2 = fechahasta.substring(6,10);
            var fecha1 = new Date(anio1,mes1-1,dia1);
            var fecha2 = new Date(anio2,mes2-1,dia2);
            var dias = Math.floor((fecha2-fecha1)/(1000*60*60*24));
            return dias;
        };
    });
</script>