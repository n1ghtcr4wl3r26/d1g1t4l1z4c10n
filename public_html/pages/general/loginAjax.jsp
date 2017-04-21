<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title">Inicie su sesión de nuevo</h4>
        </div>
        <div class="modal-body">
            <div class="form-login" style="margin: 0 auto 20px;">
                <div class="alert alert-danger">
                    Su sesión ha expirado o no tiene los permisos necesarios.
                </div>
                <html:form action="loginAjax.do" method="post" onsubmit="return Anb.form.login(this)" >
                    <html:errors property="message"/>
                    <logic:notEmpty name="message">
                    <div class="alert alert-danger">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${message}
                    </div>
                    </logic:notEmpty>
                    <div class="input-group">
                        <input type="text" name="usuario" class="form-control" value="${AuthForm.usuario}" placeholder="Usuario" focus />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                    </div>
                    <html:errors property="usuario"/>
                    <div class="input-group bottom">
                        <input type="password" name="clave" class="form-control" value="${AuthForm.clave}" placeholder="Contraseña" />
                        <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                    </div>
                    <html:errors property="clave"/>
                    <button class="btn btn-primary btn-block" name="submit" type="submit">Ingresar</button>
                </html:form>
            </div>
        </div>
    </div>
</div>