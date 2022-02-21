<%-- 
    Document   : pazienteRicette
    Created on : 22 nov 2019, 14:07:45
    Author     : Alessio Gottardi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">

    <head>
	<title>Ricette paziente</title>
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
                <li>
                    <a class="nav-link" href="<c:url value='/riservato/paziente/visite.html'/>">
                        <i class="fas fa-notes-medical"></i>
                        Visite
                    </a>
                </li>
                <li class="active">
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
                    <h3>Ricette</h3>
                </div>
                <div class="nav-item ml-auto">
                    <a class="nav-link" href="<c:url value='/logout.handler'/>">
                        <i class="fas fa-sign-out-alt"></i>
                        Logout
                    </a>
                </div>
            </nav>

            <div class="patient-info-content p-3">
                <table id="table-tutti" class="display compact table-bordered" width="100%">
                    <thead>
                        <tr>
                            <th>Nome farmaco</th>
                            <th>Erogata il</th>
                            <th>Erogata da</th>
                            <th>Costo</th>
                            <th>Download</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ricetta" items="${ricette}">
                            <tr>
                                <td>${ricetta.farmaco.nome}</td>
                                <td><fmt:formatDate value="${ricetta.dataErogazione}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td>Dott. ${ricetta.medico.nome}  ${ricetta.medico.cognome}</td>
                                <td>${ricetta.farmaco.costo}</td>
                                <td>
                                    <a href="<c:url value='/services/QR.handler?id=${ricetta.ricettaId}'/>">
                                        <i class="fa fa-download" aria-hidden="true"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Footer  -->
            <footer class="page-footer font-small">
                <div class="footer-copyright text-center py-3">© 2019 Copyright:
                    <a href="#"> serviziosanitario.it</a>
                </div>
            </footer>
        </div>

        <div class="overlay"></div>

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

                $('#table-tutti').DataTable( {
                    language: italian,
                    responsive: true,
                    scrollX: true
                } );

                $('#table-eseguiti').DataTable( {
                        language: italian,
                        responsive: true,
                        scrollX: true
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
