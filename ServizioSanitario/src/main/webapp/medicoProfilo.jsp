<%-- 
    Document   : pazienteProfilo
    Created on : 30 ott 2019, 14:02:00
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
        <title>Demo profilo medico</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap 4 CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Fontawesome CSS -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" crossorigin="anonymous">
        <!-- CustomScrollbar CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.min.css">
        <!-- Datatables CSS -->
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.css">
        <!-- Datatables Select CSS -->
        <link rel="stylesheet" href="https://cdn.datatables.net/select/1.3.1/css/select.dataTables.min.css"></link>
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
            <h1 class="sidebar-name">
                Dr. ${medico.nome} ${medico.cognome}
            </h1>
            <ul class="list-unstyled">        
                <li class="active">
                    <a class="nav-link" href="<c:url value='/riservato/medico/profilo.html'/>">
                        <i class="fas fa-address-book"></i>
                        Profilo
                    </a>
                </li>
                <li>
                    <a class="nav-link" href="<c:url value='/riservato/medico/pazienti.html'/>">
                        <i class="fas fa-list"></i>
                        Pazienti
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
                            <h1 class="profile-username">
                                Dr. ${medico.nome}<br>${medico.cognome}
                            </h1>
                            <div class="change-pass">
                                <a class="nav-link" href="#modal-cambio-password" data-toggle="modal" data-target="#modal-cambio-password">
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
                                    <td>${medico.nome}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Cognome:</th>
                                    <td>${medico.cognome}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Email:</th>
                                    <td>${medico.email}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Codice fiscale:</th>
                                    <td>${medico.codiceFiscale}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Città:</th>
                                    <td>${medico.città.nome}</td>
                                </tr>
                                <tr>
                                    <th class="pr-2" scope="row">Provincia:</th>
                                    <td>${medico.provincia.nome}</td>
                                </tr>
                                <c:if test="${not empty medico.specializzazione}">
                                    <tr>
                                        <th class="pr-2" scope="row">Specializzazione:</th>
                                        <td>${medico.specializzazione.nome}</td>
                                    </tr>
                                </c:if>
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
        
        <!--Modal-cambio-password-->
        <div class="modal" id="modal-cambio-password">
            <div class="modal-dialog modal-lg modal-fluid modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Cambia password</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <form action="<c:url context="${contextPath}" value="/services/CambiaPsw.handler" />" id="form-cambio-password" method="POST">
                            <input type="password" class="input-lg form-control" name="password1" id="password1" placeholder="Nuova Password" autocomplete="off">
                            <div class="row">
                                <div class="col-sm-6">
                                    <i id="4char" style="color:#FF0004;" class="fas fa-times"></i>
                                    Minimo 4 Caratteri
                                </div>
                            </div>
                            <hr>
                            <input type="password" class="input-lg form-control" name="password2" id="password2" placeholder="Ripeti Password" autocomplete="off">
                            <div class="row">
                                <div class="col-sm-12">
                                    <i id="pwmatch" style="color:#FF0004;" class="fas fa-times"></i>
                                    Le Password Devono Combaciare
                                </div>
                            </div>
                            <hr>
                            <div class="ml-auto">
                                <input id="conferma-cambio-password" type="submit" class="btn btn-success btn-lg" disabled value="Conferma">
                            </div>
                        </form>
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
        <script src="https://cdnjs.cloudflare.com/ajax/libs/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.concat.min.js"></script>
        <!-- Datatables JS -->
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js"></script>
        <!-- Popper JS -->
        <script type="text/javascript" charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <!-- Datatables Select JS -->
        <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/select/1.3.1/js/dataTables.select.min.js"></script>

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
                
                /* CAMBIO PASSWORD */
                
                $("input[type=password]").keyup(function(){
                    
                    let length = false;
                    let match = false;
                    
                    if($("#password1").val().length >= 4){
                        length = true;
                        $("#4char").removeClass("fa-times");
                        $("#4char").addClass("fa-check");
                        $("#4char").css("color","#00A41E");
                    }else{
                        $("#4char").removeClass("fa-check");
                        $("#4char").addClass("fa-times");
                        $("#4char").css("color","#FF0004");
                    }

                    if($("#password1").val() === $("#password2").val()){
                        match = true;
                        $("#pwmatch").removeClass("fa-times");
                        $("#pwmatch").addClass("fa-check");
                        $("#pwmatch").css("color","#00A41E");
                    }else{
                        $("#pwmatch").removeClass("fa-check");
                        $("#pwmatch").addClass("fa-times");
                        $("#pwmatch").css("color","#FF0004");
                    }
                    
                    if(length && match) {
                        console.log("ok")
                        /*$('#form-cambio-password').submit(true);
                        $("#conferma-cambio-password").disabled = false;*/
                        $('#conferma-cambio-password').prop('disabled', false);
                    }else {                        
                        console.log("no")
                        /*$('#form-cambio-password').submit(false);
                        $("#conferma-cambio-password").disabled = true;*/
                        $('#conferma-cambio-password').prop('disabled', true);
                    }
                } );
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
