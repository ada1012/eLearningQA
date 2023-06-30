<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="es.ubu.lsi.ELearningQAFacade,es.ubu.lsi.WebServiceClient"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>eLearningQA-Lista de cursos</title>
    <link rel="icon" type="image/x-icon" href="Logo.png">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
     <script src="/js/bootstrap.bundle.min.js"></script>
</head>
<body>

    <%String fullname="";
      session.setAttribute("username", request.getParameter("username"));
      session.setAttribute("password", request.getParameter("password"));
      session.setAttribute("host", request.getParameter("host"));
      ELearningQAFacade fachada= new ELearningQAFacade(request.getParameter("configuration"),request.getParameter("host"));
      session.setAttribute("fachada", fachada);
      String respuesta="";
      try{
        session.setAttribute("token", fachada.conectarse((String)session.getAttribute("username"),(String)session.getAttribute("password")));
        respuesta=fachada.generarListaCursos((String)session.getAttribute("token"));
        fullname=fachada.obtenerNombreCompleto((String)session.getAttribute("token"),(String)session.getAttribute("username"));
        session.setAttribute("fullname", fullname);
        session.setAttribute("message", "");
      }catch(Exception e){
        session.setAttribute("message", "Valores incorrectos, vuelve a intentarlo");
        response.sendRedirect("");
      }
      %>
<header class="p-3 bg-dark text-white row" style="--bs-gutter-x:0;"><div class="col"><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></div><div class="col text-end">Estás registrado como <%=fullname%>.<button class="btn btn-primary ms-3 p-0" onclick="logOut()">Desconectar</button></div></header>
            <div class="d-flex justify-content-center" style="background-image: url('atardecer.jpg');
    height: 100vh;">

          <div class="card w-100 m-3 p-3" style="overflow:auto">
                    <a target="_blank" href="./informe">Informe general</a>
                    <input style="max-width:300px" type="text" id="myInput" onkeyup="filter()" placeholder="Busca un curso.." title="Buscador cursos">
                    <%=respuesta%>
          </div>
    </div>
    <footer class="d-flex justify-content-evenly p-3 bg-dark text-white">
      <p><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></p>

      <a target="_blank" href="./manual">Manual de usuario</a>
      <a target="_blank" href="./about">Acerca de</a>
      <a href="mailto:rab1002@alu.ubu.es">Contacto</a>
    </footer>
    <script>
function filter() {
    var input, filter, table, td, a, i, txtValue;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    table = document.getElementsByTagName("table")[0];
    td = table.getElementsByTagName("td");
    for (i = 0; i < td.length; i++) {
        a = td[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            td[i].style.display = "";
        } else {
            td[i].style.display = "none";
        }
    }
}

function logOut() {
  window.location.replace("./logout");
}
</script>
</body>
</html>