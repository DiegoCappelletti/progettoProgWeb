<%-- 
    Document   : medicoBSchedaPaziente
    Created on : 30 ott 2019, 14:02:00
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
            <h1 class="sidebar-name">
                Dr. ${medico.nome} ${medico.cognome}
            </h1>
            <ul class="list-unstyled">        
                <li>
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
                                    <dt>Città:</dt>
                                    <dd>${paziente.città.nome}</dd>
                                    <dt>Data di nascita:</dt>
                                    <dd>${paziente.dataNascita}</dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-10">
                        <div class="profile-content">
                            <ul class="nav nav-tabs mt-1" id="myTab" role="tablist">
                                <li class="nav-item pt-2 ml-2">
                                        <a class="nav-link active" id="visite-eseguite-tab" data-toggle="tab" href="#visite-eseguite" role="tab" aria-controls="visite-eseguite" aria-selected="false">Visite eseguite</a>
                                </li>
                                <li class="nav-item pt-2">
                                        <a class="nav-link" id="visite-non-eseguite-tab" data-toggle="tab" href="#visite-non-eseguite" role="tab" aria-controls="visite-non-eseguite" aria-selected="false">Visite non eseguite</a>
                                </li>
                                <li class="nav-item pt-2 ml-3">
                                        <a class="nav-link" id="esami-eseguiti-tab" data-toggle="tab" href="#esami-eseguiti" role="tab" aria-controls="esami-eseguiti" aria-selected="true">Esami eseguiti</a>
                                </li>
                                <li class="nav-item pt-2">
                                        <a class="nav-link" id="esami-non-eseguiti-tab" data-toggle="tab" href="#esami-non-eseguiti" role="tab" aria-controls="esami-non-eseguiti" aria-selected="false">Esami non eseguiti</a>
                                </li>
                                <li class="nav-item pt-2 ml-3">
                                        <a class="nav-link" id="ricette-eseguite-tab" data-toggle="tab" href="#ricette-eseguite" role="tab" aria-controls="ricette-eseguite" aria-selected="true">Ricette</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane p-4" id="esami-eseguiti" role="tabpanel" aria-labelledby="esami-eseguiti-tab">
                                    <div class="text-center center-block">
                                        <button type="button" class="btn" data-toggle="modal" data-target="#modal-nuovo-esame">
                                            Eroga nuovo esame
                                        </button>
                                    </div>
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
                                <div class="tab-pane p-4" id="esami-non-eseguiti" role="tabpanel" aria-labelledby="esami-non-eseguiti-tab">
                                    <div class="text-center center-block">
                                        <button type="button" class="btn" data-toggle="modal" data-target="#modal-nuovo-esame">
                                            Eroga nuovo esame
                                        </button>
                                    </div>
                                    <table id="table-esami-non-eseguiti" class="display compact table-bordered" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo di esame</th>
                                                <th>Erogato il</th>
                                                <th>Medico</th>
                                                <th>Costo</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="esame" items="${esamiNonEseguiti}">
                                            <tr>
                                                <td>${esame.tipoEsame.nome}</td>
                                                <td><fmt:formatDate value="${esame.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td>${esame.medico.nome} ${esame.medico.cognome}</td>
                                                <td>${esame.tipoEsame.costo}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="tab-pane p-4 active" id="visite-eseguite" role="tabpanel" aria-labelledby="visite-eseguite-tab">
                                    <div class="text-center center-block">
                                        <button type="button" class="btn" data-toggle="modal" data-target="#modal-nuova-visita">
                                            Eroga nuova visita
                                        </button>
                                    </div>
                                    <table id="table-visite-eseguite" class="display compact table-bordered" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo di visita</th>
                                                <th>Medico di base</th>
                                                <th>Medico specialista</th>
                                                <th>Erogata il</th>
                                                <th>Sostenuta il</th>
                                                <th>Costo</th>
                                                <th>Risultati</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="visita" items="${visiteEseguite}">
                                            <tr>
                                                <td>${visita.tipoVisita.nome}</td>
                                                <td>${visita.medicoDiBase.nome} ${visita.medicoDiBase.cognome}</td>
                                                <td>${visita.medicoSpecialista.nome} ${visita.medicoSpecialista.cognome}</td>
                                                <td><fmt:formatDate value="${visita.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td><fmt:formatDate value="${visita.dataVisita}" pattern="yyyy-MM-dd HH:mm"/></td>
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
                                    </table>
                                </div>
                                <div class="tab-pane p-4" id="visite-non-eseguite" role="tabpanel" aria-labelledby="visite-non-eseguite-tab">
                                    <div class="text-center center-block">
                                        <button type="button" class="btn" data-toggle="modal" data-target="#modal-nuova-visita">
                                            Eroga nuova visita
                                        </button>
                                    </div>
                                    <table id="table-visite-non-eseguite" class="display compact table-bordered" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Tipo di visita</th>
                                                <th>Medico di base</th>
                                                <th>Erogata il</th>
                                                <th>Costo</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="visita" items="${visiteNonEseguite}">
                                            <tr>
                                                <td>${visita.tipoVisita.nome}</td>
                                                <td>${visita.medicoDiBase.nome} ${visita.medicoDiBase.cognome}</td>
                                                <td><fmt:formatDate value="${visita.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                <td>${visita.tipoVisita.costo}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="tab-pane p-4" id="ricette-eseguite" role="tabpanel" aria-labelledby="ricette-eseguite-tab">
                                    <div class="text-center center-block">
                                        <button type="button" class="btn" data-toggle="modal" data-target="#modal-nuova-ricetta">
                                            Eroga nuova ricetta
                                        </button>
                                    </div>
                                    <table id="table-ricette-eseguite" class="display compact table-bordered" width="100%">
                                        <thead>
                                            <tr>
                                                <th>Nome farmaco</th>
                                                <th>Erogata il</th>
                                                <th>Erogata da</th>
                                                <th>Costo</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="ricetta" items="${ricette}">
                                                <tr>
                                                    <td>${ricetta.farmaco.nome}</td>
                                                    <td><fmt:formatDate value="${ricetta.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                    <td>${ricetta.medico.nome} ${ricetta.medico.cognome}</td>
                                                    <td>${ricetta.farmaco.costo}</td>
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
                <div class="footer-copyright text-center py-3">© 2019 Copyright:
                    <a href="#"> serviziosanitario.it</a>
                </div>
            </footer>
	</div>

	<div class="overlay"></div>
        
        <!-- Modal nuovo esame -->
        <form action="<c:url context="${contextPath}" value="/services/MBase.handler" />" method="POST">
            <div id="modal-nuovo-esame" class="modal fade" role="dialog" aria-labelledby="modal-nuovo-esame"
                aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Nuovo esame</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="hidden" name="tipoRichiesta" value=nuovoEsame>
                                <input type="hidden" id="nuovoEsameMedicoId" name="nuovoEsameMedicoId" value=${medico.utenteId}>
                                <input type="hidden" id="nuovoEsamePazienteId" name="nuovoEsamePazienteId" value=${paziente.utenteId}>
                                <input type="hidden" id="esameId" name="esameId" value="">
                                <select id="tipo-esame" name="tipo-esame" class="form-control select2-allow-clear" style="width: 100%" autofocus></select>
                                <small class="form-text text-muted">Premi la casella o la barra spaziatrice<br>per iniziare a digitare.</small>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
                            <button id="crea-esame" type="sumbit" class="btn btn-primary" disabled>Conferma</button>
                        </div>
                    </div>
                </div>
            </div>
        </form> 
        
        <!-- Modal nuova visita -->
        <form action="<c:url context="${contextPath}" value="/services/MBase.handler" />" method="POST">
            <div id="modal-nuova-visita" class="modal fade" role="dialog" aria-labelledby="modal-nuova-visita"
                aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Nuova visita</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="hidden" name="tipoRichiesta" value=nuovaVisita>
                                <input type="hidden" id="nuovaVisitaMedicoId" name="nuovaVisitaMedicoId" value=${medico.utenteId}>
                                <input type="hidden" id="nuovaVisitaPazienteId" name="nuovaVisitaPazienteId" value=${paziente.utenteId}>
                                <input type="hidden" id="visitaId" name="visitaId" value="">
                                <select id="tipo-visita" name="tipo-visita" class="form-control select2-allow-clear" style="width: 100%" autofocus></select>
                                <small class="form-text text-muted">Premi la casella o la barra spaziatrice<br>per iniziare a digitare.</small>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
                            <button id="crea-visita" type="submit" class="btn btn-primary" disabled="">Conferma</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        
        <!-- Modal nuova ricetta -->
        <form action="<c:url context="${contextPath}" value="/services/MBase.handler" />" method="POST">
            <div id="modal-nuova-ricetta" class="modal fade" role="dialog" aria-labelledby="modal-nuova-ricetta"
                aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Nuova ricetta</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <input type="hidden" name="tipoRichiesta" value=nuovaRicetta>
                                <input type="hidden" id="nuovaRicettaMedicoId" name="nuovaRicettaMedicoId" value=${medico.utenteId}>
                                <input type="hidden" id="nuovaRicettaPazienteId" name="nuovaRicettaPazienteId" value=${paziente.utenteId}>
                                <input type="hidden" id="farmacoId" name="farmacoId" value="">
                                <select id="tipo-farmaco" name="tipo-farmaco" class="form-control select2-allow-clear" style="width: 100%" autofocus></select>
                                <small class="form-text text-muted">Premi la casella o la barra spaziatrice<br>per iniziare a digitare.</small>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
                            <button id="crea-ricetta" type="submit" class="btn btn-primary" disabled="">Conferma</button>
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

                let tableVisiteEseguite = $('#table-visite-eseguite').DataTable( {
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
                
                $('#table-visite-non-eseguite').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true
                } );
                
                $('#table-ricette-eseguite').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true
                } );

                $('.nav-tabs a').on('shown.bs.tab', function(){
                    $($.fn.dataTable.tables(true)).DataTable()
                    .columns.adjust().draw();
                });
                
                /* Aggiungi esame */
                
                $("#tipo-esame").select2({
                    placeholder: "Tipo di esame",
                    allowClear: true,
                    ajax: {
                        url: "http://localhost:8080/ServizioSanitario/services/nomi-esami",
                        dataType: "json"
                    }
                });
                $("#tipo-esame").val(null).trigger("change");

                $("#crea-esame").on('click', function () {
                    console.log("Nuovo Esame" +$("#nuovoEsame").val());
                    console.log("Id paziente: "+$("#nuovoEsamePazienteId").val());
                    console.log("Id medico:   "+$("#nuovoEsameMedicoId").val());
                    console.log("Id esame:    "+$("#tipo-esame").val());
                    console.log("");
                });
                
                $("#tipo-esame").on('change',function(){
                    if($(this).find('option:selected').text()==='') {
                        $("#crea-esame").attr('disabled',true);
                    }else {
                        let id = $(this).select2('data')[0].id;
                        $("#esameId").val(id);
                        $("#crea-esame").attr('disabled',false);
                    }
                });
                
                /* Aggiungi visita */
                
                $("#tipo-visita").select2({
                    placeholder: "Tipo di esame",
                    allowClear: true,
                    ajax: {
                        url: "http://localhost:8080/ServizioSanitario/services/nomi-visite",
                        dataType: "json"
                    }
                });
                $("#tipo-visita").val(null).trigger("change");
                
                $("#tipo-visita").on('change',function(){
                    if($(this).find('option:selected').text()==='') {
                        $("#crea-visita").attr('disabled',true);
                    }else {
                        let id = $(this).select2('data')[0].id;
                        $("#visitaId").val(id);
                        $("#crea-visita").attr('disabled',false);
                    }
                });
                 
                /* Aggiungi ricetta */
                 
                $("#tipo-farmaco").select2({
                    placeholder: "Farmaci",
                    allowClear: true,
                    ajax: {
                        url: "http://localhost:8080/ServizioSanitario/services/nomi-farmaci",
                        dataType: "json"
                    }
                });
                $("#tipo-farmaco").val(null).trigger("change");
                
                $("#tipo-farmaco").on('change',function(){
                    if($(this).find('option:selected').text()==='') {
                        $("#crea-ricetta").attr('disabled',true);
                    }else {
                        let id = $(this).select2('data')[0].id;
                        $("#farmacoId").val(id);
                        $("#crea-ricetta").attr('disabled',false);
                    }
                });
                
                $('#table-esami-eseguiti').on('click', 'button', function(){
                    const clickedRow = tableEsamiEseguiti.row($(this).closest('tr'));
                    const modalTitle = ""+clickedRow.data()[0];
                    const modalBody = clickedRow.data()[5];
                    $('#modal-risultati .modal-title').text(modalTitle);
                    $('#modal-risultati .modal-body .testo').text(modalBody);
                    $('#modal-risultati').modal('toggle');
                });
                
                $('#table-visite-eseguite').on('click', 'button', function(){
                    const clickedRow = tableVisiteEseguite.row($(this).closest('tr'));
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
