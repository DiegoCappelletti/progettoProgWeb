<%-- 
    Document   : pazienteRicette
    Created on : 22 nov 2019, 14:11:22
    Author     : Alessio Gottardi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">

    <head>
	<title>Visite paziente</title>
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
        <link rel="stylesheet" href="../../css/paziente-info.css" >
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
                <li>
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
                <li class="active">
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

            <!--
            <div class="intro text-center">
                    <h1>Esami</h1>
            </div>
            -->
            <!-- Navbar  -->
            <nav class="navbar">
                <div class="nav-item mr-auto">
                    <a class="nav-link" href="#" id="sidebarCollapse">
                        <i class="fas fa-align-left"></i>
                        Menu
                    </a>
                </div>
                <div class="nav-item m-auto">
                    <h3>Visite</h3>
                </div>
                <div class="nav-item ml-auto">
                    <a class="nav-link" href="<c:url value='/logout.handler'/>">
                        <i class="fas fa-sign-out-alt"></i>
                        Logout
                    </a>
                </div>
            </nav>

            <div class="patient-info-content no-padding">
                <ul class="nav nav-tabs mt-1 justify-content-center" id="myTab" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" id="tutti-tab" data-toggle="tab" href="#tutti" role="tab" aria-controls="tutti" aria-selected="true">Tutte</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="eseguiti-tab" data-toggle="tab" href="#eseguiti" role="tab" aria-controls="eseguiti" aria-selected="false">Eseguite</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="non-eseguiti-tab" data-toggle="tab" href="#non-eseguiti" role="tab" aria-controls="non-eseguiti" aria-selected="false">Non eseguite</a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane p-4 active" id="tutti" role="tabpanel" aria-labelledby="tutti-tab">
                        <table id="table-tutti" class="display compact table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th>Tipo di visita</th>
                                    <th>Erogata il</th>
                                    <th>Sostenuta il</th>
                                    <th>Erogata da</th>
                                    <th>Eseguita da</th>
                                    <th>Costo</th>
                                    <th>Risultati</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="visita" items="${visite}">
                                    <tr>
                                        <td>${visita.tipoVisita.nome}</td>
                                       <td><fmt:formatDate value="${visita.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                       <td><fmt:formatDate value="${visita.dataVisita}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td>${visita.medicoDiBase.nome} ${visita.medicoDiBase.cognome}</td>
                                        <td>${visita.medicoSpecialista.nome} ${visita.medicoSpecialista.cognome}</td>
                                        <td>${visita.tipoVisita.costo}</td>
                                        <c:choose>
                                            <c:when test="${visita.eseguita}">
                                                <td>${visita.risultati}</td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>Non eseguita</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <th>Tipo di visita</th>
                                    <th>Erogata il</th>
                                    <th>Sostenuta il</th>
                                    <th>Erogata da</th>
                                    <th>Eseguita da</th>
                                    <th>Costo</th>
                                    <th>Risultati</th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                    <div class="tab-pane p-4" id="eseguiti" role="tabpanel" aria-labelledby="eseguiti-tab">
                        <table id="table-eseguiti" class="display compact table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th>Tipo di visita</th>
                                    <th>Erogata il</th>
                                    <th>Sostenuta il</th>
                                    <th>Erogata da</th>
                                    <th>Eseguita da</th>
                                    <th>Costo</th>
                                    <th>Risultati</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="visita" items="${visiteEseguite}">
                                    <tr>
                                        <td>${visita.tipoVisita.nome}</td>
                                        <td><fmt:formatDate value="${visita.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td><fmt:formatDate value="${visita.dataVisita}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td>${visita.medicoDiBase.nome} ${visita.medicoDiBase.cognome}</td>
                                        <td>${visita.medicoSpecialista.nome} ${visita.medicoSpecialista.cognome}</td>
                                        <td>${visita.tipoVisita.costo}</td>
                                        <c:choose>
                                            <c:when test="${visita.eseguita}">
                                                <td>${visita.risultati}</td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>Non eseguita</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <th>Tipo di visita</th>
                                    <th>Erogata il</th>
                                    <th>Sostenuta il</th>
                                    <th>Erogata da</th>
                                    <th>Eseguita da</th>
                                    <th>Costo</th>
                                    <th>Risultati</th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                    <div class="tab-pane p-4" id="non-eseguiti" role="tabpanel" aria-labelledby="non-eseguiti-tab">
                        <table id="table-non-eseguiti" class="display compact table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th>Tipo di visita</th>
                                    <th>Erogata il</th>
                                    <th>Erogata da</th>
                                    <th>Costo</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="visita" items="${visiteNonEseguite}">
                                    <tr>
                                        <td>${visita.tipoVisita.nome}</td>
                                        <td><fmt:formatDate value="${visita.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td>${visita.medicoDiBase.nome} ${visita.medicoDiBase.cognome}</td>
                                        <td>${visita.tipoVisita.costo}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <th>Tipo di visita</th>
                                    <th>Erogata il</th>
                                    <th>Erogata da</th>
                                    <th>Costo</th>
                                </tr>
                            </tfoot>
                        </table>
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

                /* DATATABLES */

                let tableTutti = $('#table-tutti').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true,
                    "columnDefs": [
                        {
                            "targets": [6],
                            "render": data => `<button class="btn">Visualizza</button>`
                        }
                    ]
                } );

                let tableEseguiti = $('#table-eseguiti').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true,
                    "columnDefs": [
                        {
                            "targets": [6],
                            "render": data => `<button class="btn">Visualizza</button>`
                        }
                    ]
                } );

                $('#table-non-eseguiti').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true
                } );

                $('.nav-tabs a').on('shown.bs.tab', function(){
                    $($.fn.dataTable.tables(true)).DataTable()
                    .columns.adjust().draw();
                });
                
                $('#table-tutti').on('click', 'button', function(){
                    const clickedRow = tableTutti.row($(this).closest('tr'));
                    const modalTitle = ""+clickedRow.data()[0];
                    const modalBody = clickedRow.data()[6];
                    $('#modal-risultati .modal-title').text(modalTitle);
                    $('#modal-risultati .modal-body .testo').text(modalBody);
                    $('#modal-risultati').modal('toggle');
                });
                
                $('#table-eseguiti').on('click', 'button', function(){
                    const clickedRow = tableEseguiti.row($(this).closest('tr'));
                    const modalTitle = ""+clickedRow.data()[0];
                    const modalBody = clickedRow.data()[6];
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
