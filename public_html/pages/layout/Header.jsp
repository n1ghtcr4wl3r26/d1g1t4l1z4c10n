<%@ page contentType="text/html;charset=utf-8"%>

<header>
    <div class="logomain" id="logomain"><h1>Courier</h1></div>
    <div class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header pull-left">
                <button class="navbar-toggle pull-left visible-xs toggle-menu" type="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <button class="btn btn-default toggle-menu hidden-xs" type="button">
                    <span class="hide-menu"><i class="glyphicon glyphicon-menu-left"></i> <span>Ocultar menú</span></span>
                    <span class="show-menu"><span>Mostrar menú</span> <i class="glyphicon glyphicon-menu-right"></i></span>
                </button>
            </div>
            <div class="pull-right">
                <ul class="breadcrumb pull-left">
                    <c:if test="${!empty parentURI}">
                        <li>${parentURI}</li>
                    </c:if>
                    <li class="active">${URI}</li>
                </ul>
                <ul class="header-menu nav navbar-nav pull-left pull-left">
                    <li>
                        <a id="btn-banner" class="btn-banner hidden-xs" href="#" data-toggle="tooltip" data-placement="bottom" title="Ocultar banner">
                            <i class="fa fa-photo"></i>
                        </a>
                    </li>
                    <li>
                        <a id="btn-fullscreen" class="btn-fullscreen hidden-xs" href="#" data-toggle="tooltip" data-placement="bottom" title="Pantalla completa">
                            <i class="fa fa-expand"></i>
                            <i class="fa fa-compress"></i>
                        </a>
                    </li>
                    <li style="display: none;">
                        <a href="#" class="not-open" data-toggle="tooltip" data-placement="bottom" title="Notificaciones">
                            <i class="fa fa-bell-o"></i>
                            <span class="badge badge-danger badge-header" id="not-number-state"></span>
                        </a>
                    </li>
                </ul>
            </div>
        </div><!-- /.container-fluid -->
    </div>
</header>


