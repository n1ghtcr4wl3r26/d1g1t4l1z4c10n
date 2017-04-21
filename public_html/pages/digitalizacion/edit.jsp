<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">
        <strong><i class="fa fa-sort-numeric-asc"></i> Generar <small>Números Correlativos</small></strong>
    </h4>    
</div>
<html:form styleId="form-digitalizacion" action="generarNumeros.do">
<div class="modal-body form-horizontal">
    <div class="form-group">
        <label class="col-sm-5 control-label">Gestión:</label>
        <div class="col-sm-7">
            <% int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR); %>
            <select name="gestion" class="form-control required">
                <option value="-">Seleccione...</option>
            <% for(int i = year - 1, l = year + 1; i <= l; i++) {%>
                <option value="<%=i%>"><%=i%></option>
            <% } %>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-5 control-label">Tipo de Mercancía:</label>
        <div class="col-sm-7">
            <html:select property="tipo" styleClass="form-control required">
                <option value="-">Seleccione...</option>
                <html:optionsCollection name="tipos" value="id" label="descripcion"/>
            </html:select>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-5 control-label">Cantidad:</label>
        <div class="col-sm-7">
            <input type="text" name="cantidad" class="form-control required numeric" maxlength="8" data-max="99999999" />
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-5 control-label">Fecha de Solicitud:</label>
        <div class="col-sm-7">
            <input type="text" name="fec_solicitud" class="form-control required datepicker"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-5 control-label">Correo electrónico del solicitante:</label>
        <div class="col-sm-7">
            <input type="text" name="email" class="form-control required email"/>
        </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal"> 
        <i class="fa fa-ban"></i> Cancelar 
    </button>
    <button type="submit" 
            class="btn btn-primary btn-save" 
            data-loading-text='<i class="fa fa-cog fa-spin"></i> Procesando'>
        <i class="fa fa-check"></i> Guardar
    </button>
</div>
</html:form>
<script>
    Anb.form.submit('#form-digitalizacion', function (form) {
        if (Anb.validate.run(form)) {
            Anb.loading.show('Se está generando los números secuenciales, espere por favor.');
            Anb.form.ajax(form, 'generarNumeros.do', function (response) {
                if (response.state == 'OK') {
                    window.location = '';                    
                }
            });    
        }        
    });
</script>
