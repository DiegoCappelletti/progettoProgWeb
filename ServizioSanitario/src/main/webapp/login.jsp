<%-- 
    Document   : login
    Created on : 23 gen 2020, 13:07:43
    Author     : Alessio Gottardi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">

    <head>
        <title>Login</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap 4 CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <!-- Fontawesome CSS -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" crossorigin="anonymous">
        <!-- Cookie Consent CSS -->
        <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/cookieconsent@3/build/cookieconsent.min.css" />
        <!-- LOCAL CSS -->
        <link rel="stylesheet" href="css/login.css" >

    </head>
    <body>
        <main class="container-fluid login-content">
            <div class="row login-content-wrap h-100">
                <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
                    <div class="card card-signin my-5">
                        <h2 class="card-title text-center">Area di Autenticazione</h2>
                        <div class="card-body">
                            <div class="error-box">
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        ${error}
                                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                </c:if>
                            </div>
                            <form class="form-signin" action="login.handler" method="POST">
                                <div class="form-label-group">
                                    <label for="email">Email</label>
                                    <input type="email" id="email" name="username" class="form-control" placeholder="Email" required autofocus>
                                </div>

                                <div class="form-label-group">
                                    <label for="password">Password</label>
                                    <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
                                </div>

                                <div class="custom-control custom-checkbox mt-4 mb-4 ml-2">
                                    <input type="checkbox" name="rememberMe" class="custom-control-input" id="customCheck">
                                    <label class="custom-control-label" for="customCheck">Ricordami</label>
                                </div>

                                <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>

                                <div class="forgot-password text-center">
                                    <a href="changePassword.html">
                                        Dimenticato la password?
                                    </a>
                                </div>
                            </form>
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
        <!-- Cookie Consent JS -->
        <script src="https://cdn.jsdelivr.net/npm/cookieconsent@3/build/cookieconsent.min.js" data-cfasync="false"></script>
        <script>
            window.cookieconsent.initialise({
              "palette": {
                  "max-width": "20%",
                "popup": {
                  "background": "#237afc"
                },
                "button": {
                  "background": "#fff",
                  "text": "#237afc"
                }
              },
              "content": {
                "message": "Questo sito utilizza i cookie.",
                "href": "privacyPolicy.html",
                "dismiss": "Ho capito!",
                "link": "Maggiori informazioni"
              }

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
