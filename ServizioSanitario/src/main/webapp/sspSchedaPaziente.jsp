<%-- 
    Document   : sspSchedaPaziente
    Created on : 23 jan 2019, 15:52:32
    Author     : Alessio Gottardi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">

    <head>
        <title>Scheda paziente</title>
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
        <!-- Select2 CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/css/select2.min.css">
        <!-- LOCAL CSS -->
        <link rel="stylesheet" href="../../css/globale.css" >
        <link rel="stylesheet" href="../../css/medico-scheda-paziente.css" >
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
                ${ssp.nome}
            </h2>
            <ul class="list-unstyled">        
                <li>
                    <a class="nav-link" href="<c:url value='/riservato/ssp/profilo.html'/>">
                        <i class="fas fa-address-book"></i>
                        Profilo
                    </a>
                </li>
                <li>
                    <a class="nav-link" href="<c:url value='/riservato/ssp/pazienti.html'/>">
                        <i class="fas fa-list"></i>
                        Pazienti
                    </a>
                </li>
                <li>
                    <a class="nav-link" href="<c:url value='/services/Report.handler'/>">
                        <i class="fas fa-list"></i>
                        Crea report
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
                    <h3>Paziente</h3>
                </div>
                <div class="nav-item ml-auto">
                    <a class="nav-link" href="<c:url value='/logout.handler'/>">
                        <i class="fas fa-sign-out-alt"></i>
                        Logout
                    </a>
                </div>
            </nav>
		
            <div class="container-fluid scheda no-padding">
                <div class="row m-0">
                    <div class="col-sm-2">
                        <div class="scheda-sidebar">
                            <div class="scheda-userpic">
                                <img src="${foto[0].path}" class="img-responsive" alt="">
                            </div>
                            <div class="scheda-dati">
                                <dl>
                                    <dt>Nome:</dt>
                                    <dd>${paziente.nome} ${paziente.cognome}</dd>
                                    <dt>Email:</dt>
                                    <dd>${paziente.email}</dd>
                                    <dt>Codice fiscale:</dt>
                                    <dd>${paziente.codiceFiscale}</dd>
                                    <dt>Sesso:</dt>
                                    <dd>${paziente.sesso}</dd>
                                    <dt>Citt??:</dt>
                                    <dd>${paziente.citt??.nome}</dd>
                                    <dt>Data di nascita:</dt>
                                    <dd>${paziente.dataNascita}</dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-10">
                        <div class="profile-content">
                            <ul class="nav nav-tabs mt-1" id="myTab" role="tablist">
                                <li class="nav-item pt-2 ml-3">
                                        <a class="nav-link" id="esami-eseguiti-tab" data-toggle="tab" href="#esami-eseguiti" role="tab" aria-controls="esami-eseguiti" aria-selected="false">Esami eseguiti</a>
                                </li>
                                <li class="nav-item pt-2">
                                        <a class="nav-link active" id="esami-non-eseguiti-tab" data-toggle="tab" href="#esami-non-eseguiti" role="tab" aria-controls="esami-non-eseguiti" aria-selected="false">Esami non eseguiti</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane p-4" id="esami-eseguiti" role="tabpanel" aria-labelledby="esami-eseguiti-tab">
                                    <table id="table-esami-eseguiti" class="display compact table-bordered" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo di esame</th>
                                                <th>Erogato il</th>
                                                <th>Sostenuto il</th>
                                                <th>Medico</th>
                                                <th>Costo</th>
                                                <th>Risultati</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="esame" items="${esamiEseguiti}">
                                            <tr>
                                                <td>${esame.tipoEsame.nome}</td>
                                                <td><fmt:formatDate value="${esame.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td><fmt:formatDate value="${esame.dataEsame}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td>${esame.medico.nome} ${esame.medico.cognome}</td>
                                                <td>${esame.tipoEsame.costo}</td>
                                                <c:choose>
                                                    <c:when test="${esame.eseguito}">
                                                        <td>${esame.risultati}</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>Non eseguito</td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="tab-pane p-4 active" id="esami-non-eseguiti" role="tabpanel" aria-labelledby="esami-non-eseguiti-tab">
                                    <table id="table-esami-non-eseguiti" class="display compact table-bordered" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo di esame</th>
                                                <th>Erogato il</th>
                                                <th>Medico</th>
                                                <th>Costo</th>
                                                <th>Esegui</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="esame" items="${esamiNonEseguiti}">
                                            <tr>
                                                <td>${esame.tipoEsame.nome}</td>
                                                <td><fmt:formatDate value="${esame.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td>${esame.medico.nome} ${esame.medico.cognome}</td>
                                                <td>${esame.tipoEsame.costo}</td>
                                                <td>
                                                    <div class="text-center center-block">
                                                        <button type="button" class="btn" data-toggle="modal" data-target="#modal-esegui-esame" data-esame-id="${esame.esameId}">
                                                            Esegui
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer  -->
            <footer class="page-footer font-small">
                <div class="footer-copyright text-center py-3">?? 2019 Copyright:
                    <a href="#"> serviziosanitario.it</a>
                </div>
            </footer>
	</div>

	<div class="overlay"></div>
        
        <!-- Modal esegui esame -->
        <form action="<c:url context="${contextPath}" value="/services/Ssp.handler" />" method="POST">
            <div id="modal-esegui-esame" class="modal fade" role="dialog" aria-labelledby="modal-esegui-esame"
                aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Esegui esame</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="risultati">Risultati:</label>
                                <textarea id="risultati" name="risultati" rows="10" style="width:100%"></textarea>
                                <input type="hidden" id="esameId" name="esameId" value="">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
                            <button id="esegui-esame" type="submit" class="btn btn-primary">Conferma</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
            
        <!-- Modal Risultati -->
        <div id="modal-risultati" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <h5>Risultati:</h5>
                        <div class="testo">
                            
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Chiudi</button>
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
        <!-- Select2 JS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>
        
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

                /* DATATABLE */

                let tableEsamiEseguiti = $('#table-esami-eseguiti').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true,
                    "columnDefs": [
                        {
                            "targets": [5],
                            "render": data => `<button class="btn">Visualizza</button>`
                        }
                    ]
                } );
                
                $('#table-esami-non-eseguiti').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true
                } );

                $('.nav-tabs a').on('shown.bs.tab', function(){
                    $($.fn.dataTable.tables(true)).DataTable()
                    .columns.adjust().draw();
                });
                
                /* Esegui esame */
                
                $("#esegui-esame").on('click', function () {
                    console.log("Esegui Esame");
                    console.log("Risultati: "+$("#risultati").val());
                    console.log("");
                });
                
                $("#modal-esegui-esame").on("show.bs.modal", function (e) {
                    var target = $(e.relatedTarget);
                    var esameId = target.data("esame-id");
                    $("#esameId").val(esameId);
                });
                
                $('#table-esami-eseguiti').on('click', 'button', function(){
                    const clickedRow = tableEsamiEseguiti.row($(this).closest('tr'));
                    const modalTitle = ""+clickedRow.data()[0];
                    const modalBody = clickedRow.data()[5];
                    $('#modal-risultati .modal-title').text(modalTitle);
                    $('#modal-risultati .modal-body .testo').text(modalBody);
                    $('#modal-risultati').modal('toggle');
                });
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
