<%-- 
    Document   : farmaciaProfilo
    Created on : 30 ott 2019, 15:02:55
    Author     : Alessio Gottardi
--%>

<%@page import="java.util.List"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException"%>
<%@page import="it.serviziosanitario.persistence.entities.Paziente"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException"%>
<%@page import="it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory"%>
<%@page import="it.serviziosanitario.persistence.dao.PazienteDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html>

    <head>
        <title>Demo profilo farmacia</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap 4 CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Fontawesome CSS -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" crossorigin="anonymous">
        <!-- CustomScrollbar CSS -->
        <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.min.css">
        <!-- Datatables CSS -->
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.css">
        <!-- Datatables Select CSS -->
        <link rel="stylesheet" href="https://cdn.datatables.net/select/1.3.1/css/select.dataTables.min.css">
        <!-- Select2 CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/css/select2.min.css">
        <!-- LOCAL CSS -->
        <link rel="stylesheet" href="../../css/globale.css" >
        <link rel="stylesheet" href="../../css/profilo.css" >
    </head>

    <body>
        <!-- Sidebar  -->
        <nav id="sidebar" class="text-center">
            <div id="dismiss">
                <i class="fas fa-arrow-left"></i>
            </div>
            <h2 class="sidebar-header">

            </h2>
            <h2 class="sidebar-name">
                ${farmacia.nome}
            </h2>
            <ul class="list-unstyled">
                <li class="active">
                    <a class="nav-link" href="<c:url value='/riservato/farmacia/profilo.html'/>">
                        <i class="fas fa-address-book"></i>
                        Profilo
                    </a>
                </li>
                <li>
                    <a class="nav-link" href="#modal-ricerca-paziente" data-toggle="modal"
                        data-target="#modal-ricerca-paziente">
                        <i class="fas fa-search"></i>
                        Cerca paziente
                    </a>
                </li>
            </ul>
        </nav>

        <!-- Page Content  -->
        <div id="content">

            <!-- Navbar  -->
            <nav class="navbar">
                <div class="nav-item mr-auto">
                    <a class="nav-link" href="#" id="sidebarCollapse">
                        <i class="fas fa-align-left"></i>
                        Menu
                    </a>
                </div>
                <div class="nav-item m-auto">
                    <h3>Profilo</h3>
                </div>
                <div class="nav-item ml-auto">
                    <a class="nav-link" href="<c:url value='/logout.handler'/>">
                        <i class="fas fa-sign-out-alt"></i>
                        Logout
                    </a>
                </div>
            </nav>

            <div class="container-fluid profile no-padding">
                <div class="row m-0">
                    <div class="col-sm-3">
                        <div class="profile-sidebar text-center">
                            <h2 class="profile-username">
                                ${farmacia.nome}
                            </h2>
                            <div class="change-pass">
                                <a class="nav-link" href="#">
                                    <i class="fas fa-lock"></i>
                                    Modifica password
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-9">
                        <div class="profile-content">
                            <h2>Dati Personali</h2>
                            <table class="table">
                                <tr>
                                    <th class="pr-2" scope="row">Nome:</th>
                                    <td>${farmacia.nome}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Città:</th>
                                    <td>${farmacia.città.nome}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Provincia:</th>
                                    <td>${farmacia.provincia.nome}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer  -->
            <footer class="page-footer font-small">
                <div class="footer-copyright text-center py-3">© 2019 Copyright:
                    <a href="#"> serviziosanitario.it</a>
                </div>
            </footer>

        </div>

        <div class="overlay"></div>

        <!-- Modal -->
        <div id="modal-ricerca-paziente" class="modal fade" role="dialog" aria-labelledby="modal-ricerca-paziente" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modal-ricerca-paziente">Ricerca paziente</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <select id="ricerca-paziente" name="ricerca-paziente" class="form-control select2-allow-clear" autofocus>
                            </select>
                            <small class="form-text text-muted">Premi la casella o la barra spaziatrice<br>per iniziare a digitare.</small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
                        <button type="button" class="btn btn-primary">Cerca</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- JQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.3.1.js"></script>
        <!-- Bootstrap 4 JS -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <!-- JQuery CustomScrollbar JS -->
        <script
            src="https://cdnjs.cloudflare.com/ajax/libs/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.concat.min.js"></script>
        <!-- Datatables JS -->
        <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js"></script>
        <!-- Popper JS -->
        <script type="text/javascript" charset="utf8"
            src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <!-- Datatables Select JS -->
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/select/1.3.1/js/dataTables.select.min.js"></script>
        <!-- Select2 JS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>

        <script type="text/javascript" src="../../js/globale.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                /* CUSTOM SIDEBAR */

                $("#sidebar").mCustomScrollbar({
                    theme: "minimal"
                });

                $('#dismiss, .overlay').on('click', function () {
                    $('#sidebar').removeClass('active');
                    $('.overlay').removeClass('active');
                });

                $('#sidebarCollapse').on('click', function () {
                    $('#sidebar').addClass('active');
                    $('.overlay').addClass('active');
                    $('.collapse.in').toggleClass('in');
                });

                $('#modal-ricerca-paziente').on('show.bs.modal', function (e) {
                    $('#sidebar').removeClass('active');
                    $('.overlay').removeClass('active');
                });

                $("#ricerca-paziente").select2({
                    placeholder: "Indica il codice fiscale",
                    allowClear: true,
                    ajax: {
                        url: "http://localhost:8080/ServizioSanitario/services/codici-fiscali",
                        /*url: href="<c:url value='/services/codici-fiscali'/>",*/
                        dataType: "json"
                    }
                });
                $("#ricerca-paziente").val(null).trigger("change");
            });
        </script>
    </body>

    </html>
</c:catch>
<c:if test="${not empty ex}">
    <jsp:scriptlet>
        response.sendError(500, ((Exception) pageContext.getAttribute("ex")).getMessage());
    </jsp:scriptlet>
</c:if>
