<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">
    <head>
        <title>Cambio Password</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap 4 CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Fontawesome CSS -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" crossorigin="anonymous">
        <!-- LOCAL CSS -->
        <link rel="stylesheet" href="css/globale.css" >
        <link rel="stylesheet" href="css/login.css" >
    </head>
    <body>
        <main class="container-fluid login-content">
            <div class="row login-content-wrap h-100">
                <div class="col-sm-10 col-md-8 col-lg-7 mx-auto">
                    <div class="card card-signin my-5">
                        <h2 class="card-title text-center">Password cambiata correttamente</h2>
                        <div class="card-body">
                            <br>
                            <br>
                            <div class="text-center mx-auto" style='color:blue'>
                                <a class="nav-link" href="<c:url value='login.html'/>">Torna all'area di autenticazione</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
            
        <!-- Footer  -->
        <footer class="page-footer font-small">
            <div class="footer-copyright text-center py-3">© 2019 Copyright:
                <a href="#"> serviziosanitario.it</a>
            </div>
        </footer>
        

        <!-- JQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.3.1.js"></script>
        <!-- Bootstrap 4 JS -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <!-- Popper JS -->
        <script type="text/javascript" charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        
    </body>
    </html>
</c:catch>
<c:if test="${not empty ex}">
    <jsp:scriptlet>
        response.sendError(500, ((Exception) pageContext.getAttribute("ex")).getMessage());
    </jsp:scriptlet>
</c:if>
