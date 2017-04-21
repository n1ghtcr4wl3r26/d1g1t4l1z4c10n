<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="cliente.bean.ClaseOpcion"%>
<%@ page import="anb.general.*" %>

<script>
function env(opcion) {    
  var f=document.forms["beanMenu"];
  f.opcion.value=opcion;  
  f.submit();  
}
</script>
<html:form action="/MenuAction">
    <html:hidden property="opcion"/>
</html:form>

<%
boolean flag = false;
//String icons[] = {"envelope-square", "file-text-o", "cog", "list-alt", "cog paperclip"};
//String icons[] = {"file-text-o", "cog", "list-alt", "envelope-square", "cog paperclip", "cog paperclip", "cog paperclip"};
String icons[] = {"cog", "file-text", "user", "cubes", "file-pdf-o", "cog paperclip", "cog paperclip"};
int i = 0;
String ACCION = "";
%>
<nav id="sidebar">
    <div class="navigation">
        <div class="user-data">
            <div class="user-icon pull-left">
                <i class="fa fa-user"></i>
            </div>
           <h4 class="user-details pull-left">
                ${sessionScope.ClaseSession.nombreUsuario}<br>
                 ${sessionScope.ClaseSession.nomAduana}<br>
           </h4>
       </div>
       <div class="menu-title">
           Menú principal
       </div>
       <ul class="list-unstyled">
           <!--<li class="${ACCION == 'dashboard' ? 'active' : ''}">
               <a href="dashboard.do">
                   <i class="fa fa-home"></i>
                   <span class="nav-label">Dashboard</span>
               </a>
           </li>-->
       <logic:iterate id="opcion" name="InputForm" property="opciones" type="ClaseOpcion" scope="session">
           <%
           String accion = opcion.getAccion();
           if (! opcion.getCodopc().equals("") && opcion.getCodant().equals("0")) {
               if (flag) {
           %>
               </ul>
           </li>
           <%
               }
               flag = true;
           %>
           <li class="has-submenu">
               <a href="#">
                   <i class="fa fa-<%=icons[i]%>"></i> 
                   <span class="nav-label">${opcion.desc}</span>
                   <i class="fa fa-chevron-right pull-right"></i>
               </a>
               <ul class="list-unstyled">
           <%
           i++;
           } else {
                if (opcion.getAccion().equals("32")) continue;
           %>  
                   <li class="${sessionScope.opcion == opcion.accion ? 'active' : ''}">
                       <a href="javascript:env('${opcion.accion}')">${opcion.desc}</a>
                   </li>
           <%
           }
           %>  
           </logic:iterate>
           <% if (flag) {
           %>
               </ul>
           </li>
           <%
               } %>
           <li>
               <a href="logout.do">
                   <i class="fa fa-power-off"></i> 
                   <span class="nav-label">Cerrar Sesión</span>
               </a>
           </li>
       </ul>
   </div>
</nav>