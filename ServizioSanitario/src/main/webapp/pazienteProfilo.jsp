<%-- 
    Document   : pazienteProfilo
    Created on : 30 ott 2019, 14:02:00
    Author     : Alessio Gottardi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">
    <head>
        <title>Profilo paziente</title>
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
        <link rel="stylesheet" href="../../css/paziente-profilo.css" >
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
                ${paziente.nome}<br>${paziente.cognome}
            </h1>
            <ul class="list-unstyled">        
            <li class="active">
                <a class="nav-link" href="<c:url value='/riservato/paziente/profilo.html'/>">
                    <i class="fas fa-address-book"></i>
                    Profilo
                </a>
            </li>
            <li>
                <a class="nav-link" href="<c:url value='/riservato/paziente/esami.html'/>">
                    <i class="fas fa-list"></i>
                    Esami
                </a>
            </li>
            <li>
                <a class="nav-link" href="<c:url value='/riservato/paziente/visite.html'/>">
                    <i class="fas fa-notes-medical"></i>
                    Visite
                </a>
            </li>
            <li>
                <a class="nav-link" href="<c:url value='/riservato/paziente/ricette.html'/>">
                    <i class="fas fa-prescription-bottle-alt"></i>
                    Ricette
                </a>
            </li>
            <li>
                <a href="<c:url context="${contextPath}" value="/services/PDF.handler" />" class="nav-link" >
                    <i class="fas fa-ticket-alt"></i>
                    Tickets
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
                            <div class="profile-userpic">
                                <img src="${foto[0].path}" class="img-responsive" alt="">
                            </div>
                            <h2 class="profile-username">
                                ${paziente.nome} ${paziente.cognome}
                            </h2>
                            <div class="profile-usermenu">
                                <ul class="nav flex-column">
                                    <li class="nav-item">
                                        <a class="nav-link" href="#modal-cambio-password" data-toggle="modal" data-target="#modal-cambio-password">
                                        <i class="fas fa-lock"></i>
                                        Modifica password </a>
                                    </li>
                                    <li class="nav-item" data-toggle="modal" data-target="#editUserModal"></i>
                                        <a class="nav-link" href="#">
                                        <i class="fas fa-portrait"></i>
                                        Cambia foto </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="#modal-scelta-medico" data-toggle="modal" data-target="#modal-scelta-medico">
                                        <i class="fas fa-user-md"></i>
                                        Cambia medico curante </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-9">
                        <div class="profile-content">
                            <h2>Dati Personali</h2>
                            <div class="row">
                                <div class="col-sm-6 mx-auto">
                                    <table class="table">
                                        <tr>
                                            <th class="pr-2" scope="row">Nome:</th>
                                            <td>${paziente.nome}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Cognome:</th>
                                            <td>${paziente.cognome}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Email:</th>
                                            <td>${paziente.email}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Codice fiscale:</th>
                                            <td>${paziente.codiceFiscale}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Sesso:</th>
                                            <td>${paziente.sesso}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Città:</th>
                                            <td>${paziente.città.nome}</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="col-sm-6">
                                    <table class="table">
                                        <tr>
                                            <th class="pr-2" scope="row">Provincia:</th>
                                            <td>${paziente.provincia.nome}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Residenza:</th>
                                            <td>${paziente.indirizzo}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Data di nascita:</th>
                                            <td>${paziente.dataNascita}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Luogo di nascita:</th>
                                            <td>${paziente.luogoNascita}</td>
                                        </tr>
                                        <tr>
                                            <th class="pr-2" scope="row">Medico curante:</th>
                                            <td>${paziente.medico.nome} ${paziente.medico.cognome}</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

                                        
            <form action="<c:url context="${contextPath}" value="/services/foto.handler" />" method="POST"  enctype="multipart/form-data">
                <div class="modal fade" id="editUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3> Carica una nuova foto </h3>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><i class="fas fa-window-close red-window-close"></i></span></button>
                            </div>
                            <div class="modal-body">
                                <div class="form-label-group">
                                    <input type="file" name="avatar" id="avatar" placeholder="Avatar" >
                                    
                                </div>
                                
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Save</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>                                   
                                        
               
            <!-- Footer  -->
            <footer class="page-footer font-small">
                <div class="footer-copyright text-center py-3">© 2019 Copyright:
                    <a href="#"> serviziosanitario.it</a>
                </div>
            </footer>
        </div>

        <div class="overlay"></div>

        <!--Modal-scelta-medico-->
        <div class="modal" id="modal-scelta-medico">
            <div class="modal-dialog modal-lg modal-fluid modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Cambia medico curante</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <form action="<c:url context="${contextPath}" value="/services/Paziente.handler" />" id="form-scelta-medico" method="POST">
                            <table id="table-scelta-medico" class="display compact table-bordered" width="100%">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nome</th>
                                        <th>Cognome</th>
                                        <th>Provincia</th>
                                        <th>Email</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="medico" items="${medici}">
                                        <tr>
                                            <td>${medico.utenteId}</td>
                                            <td>${medico.nome}</td>
                                            <td>${medico.cognome}</td>
                                            <td>${medico.provincia.nome}</td>
                                            <td>${medico.email}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nome</th>
                                        <th>Cognome</th>
                                        <th>Provincia</th>
                                        <th>Email</th>
                                    </tr>
                                </tfoot>
                            </table>
                            <input type="hidden" name="richiesta" value="CambiaMedico">
                            <input type="hidden" id="medicoId" name="medicoId" value="NULL">
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button id="conferma-medico" type="submit" class="btn btn-success" form="form-scelta-medico" disabled >Conferma</button>
                    </div>
                </div>
            </div>
        </div>
                            
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
        <script>
            $(document).ready(function() {
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

                /* MODAL SCELTA MEDICO */

                $('#modal-scelta-medico').on('shown.bs.modal', function (e) {
                    $($.fn.dataTable.tables(true)).DataTable().columns.adjust();
                });

                /* DATATABLE SCELTA MEDICO */

                let table = $('#table-scelta-medico').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true,
                    select: {
                        style: 'single',
                        info: false
                    },
                    "columnDefs": [
                        {
                            "targets": [0],
                            "visible": false,
                            "searchable": false
                        }
                    ]
                } );
                
                table.on('select', function ( e, dt, type, indexes ) {
                    let id = table.rows( indexes ).data()[0][0];
                    $('#medicoId').val(id);
                    $("#conferma-medico").attr('disabled',false);
                    console.log(id);
                } );
                
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
            } );
        </script>
    </body>
    </html>
</c:catch>
<c:if test="${not empty ex}">
    <jsp:scriptlet>
        response.sendError(500, ((Exception) pageContext.getAttribute("ex")).getMessage());
    </jsp:scriptlet>
</c:if>
