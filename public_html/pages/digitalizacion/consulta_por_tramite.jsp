<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

 
<div class="panel panel-default">


<div class="panel-heading">
        <h4 class="panel-title"><strong>BUSQUEDA POR TRAMITE</strong></h4>
    </div>
  

  <div class="modal-body form-horizontal">
  
<html:form styleId="form-digitalizacion" action="consultafiltro.do">
  
    <div class="form-group">
        <label class="col-sm-3 control-label">Tipo de Documento:</label>
        <div class="col-sm-7">
            <html:select property="tipodocumento" styleId="tipodocumento" styleClass="form-control required">
                <option value="-">Seleccione...</option>
                <html:optionsCollection name="tipos" value="codigo" label="descripcion"/>
            </html:select>
        </div>
    </div>
    <div id="parametros">
        <div class="form-group">
            <label class="col-sm-3 control-label">Número de Tr&aacute;mite:</label>
            <div class="col-sm-4">
                <input type="text" name="tramite" class="form-control required" maxlength="50"  />
            </div>
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
            <button type="submit" class="btn btn-primary" >Consultar</button>
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
            if (Anb.validate.run(form)) {
                Anb.loading.show("Procesando consulta, espere por favor");
                $.post('tramiteReporte.do', $(form).serialize(), function (response) {
                    $container_reporte.html(response);
                    Anb.load.render('#container-reporte');
                });
            }
        });
        
        $('#limpiar').on('click', function () {
            $container_reporte.html("");
        });
        var $parametros = $('#parametros');
        $('#tipodocumento').change(function() {
            if($('#tipodocumento').val()=='960' || $('#tipodocumento').val()=='932' || $('#tipodocumento').val()=='B74'){
                $parametros.html("<div class='form-group'><label class='col-sm-3 control-label label-required'>Gestión:</label><div class='col-sm-4'><input type='text' name='duigestion' class='form-control required numeric' maxlength='4'  /></div></div>"+
                "<div class='form-group'><label class='col-sm-3 control-label label-required'>Aduana:</label><div class='col-sm-4'><input type='text' name='duiaduana' class='form-control required numeric' maxlength='3'  /></div></div>"+
                "<div class='form-group'><label class='col-sm-3 control-label label-required'>Número:</label><label class='col-sm-1 control-label'>C - </label><div class='col-sm-3'><input type='text' name='duinumero' class='form-control required numeric' maxlength='8'  /></div></div>");
            }else
            if($('#tipodocumento').val()=='785'){
                $parametros.html("<div class='form-group'><label class='col-sm-3 control-label label-required'>Gestión:</label><div class='col-sm-4'><input type='text' name='duigestion' class='form-control required numeric' maxlength='4'  /></div></div>"+
                "<div class='form-group'><label class='col-sm-3 control-label label-required'>Aduana:</label><div class='col-sm-4'><input type='text' name='duiaduana' class='form-control required numeric' maxlength='3'  /></div></div>"+
                "<div class='form-group'><label class='col-sm-3 control-label label-required'>Número:</label><div class='col-sm-4'><input type='text' name='duinumero' class='form-control required numeric' maxlength='8'  /></div></div>");
            }
            else
            {
              $parametros.html("<div class='form-group'><label class='col-sm-3 control-label label-required'>Número de Tr&aacute;mite:</label><div class='col-sm-4'><input type='text' name='tramite' class='form-control required' maxlength='50'  /></div></div>");
            }
        });
    });
</script>
