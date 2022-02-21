<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="ex">
    <!DOCTYPE html>
    <html lang="it">
    <head>
        <title>Nuova Password</title>
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
                <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
                    <div class="card card-signin my-5">
                        <h2 class="card-title text-center">Nuova Password</h2>
                        <div class="card-body">
                            <form action="GestionePassword" id="form-cambio-password" method="POST">
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
                                <input type="hidden" name="link" value="<%= request.getParameter("link")%>" />
                                <hr>
                                <div class="ml-auto">
                                    <input id="conferma-cambio-password" type="submit" class="btn btn-success btn-lg" disabled value="Conferma">
                                </div>
                            </form>
                        </div>
                     </div>
                </div>
            </div>
        </main>
        
        <!-- JQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.3.1.js"></script>
        <!-- Bootstrap 4 JS -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <!-- Popper JS -->
        <script type="text/javascript" charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>

        <script>
            
            $(document).ready(function () {
                
                function checkPasswordMatch() {
                    var password = $("#txtNewPassword").val();
                    var confirmPassword = $("#txtConfirmPassword").val();

                    if (password === confirmPassword){
                        $("#divCheckPasswordMatch").html("Le password combaciano.");
                    }
                    else{
                        $("#divCheckPasswordMatch").html("Le password non combaciano.");
                    }
                }
            
               $("#txtConfirmPassword").keyup(checkPasswordMatch);
               
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
