<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">
        <strong><i class="fa fa-download"></i> Descarga de correlativos</strong>
    </h4>    
</div>
<div class="modal-body">
    <%@ include file="/pages/digitalizacion/download_list.jsp" %>
</div>
<style>
    .btn-download {display: none;}
</style>
