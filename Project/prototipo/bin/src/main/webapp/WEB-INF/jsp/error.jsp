<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="en">
<head>
  <title>Error inesperado</title>
  <meta charset="utf-8">
  <link rel="icon" type="image/x-icon" href="Logo.png">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="/js/bootstrap.bundle.min.js"></script>
</head>
<body>
  <header class="bg-dark text-white row" style="--bs-gutter-x:0;">
    <div class="col m-3">
      <img src="FullLogo.png" width="200" height="32" alt="eLearningQA">
    </div>
    </header>
            <div class="justify-content-center p-3 text-white" style="background-color: #dc3545;
    height: 100vh;">
    <%String curso=(String)session.getAttribute("coursename");%>
    <h1>Ha habido un problema inesperado intentando acceder al curso <%=curso%></h1><br>
    <p>Si sospechas qué ha podido pasar informa del error en el siguiente link:</p>
    <a class="text-white" href="https://github.com/ada1012/eLearningQA/issues/new">https://github.com/ada1012/eLearningQA/issues/new</a>
    <p><br>También puedes mandar un correo a:</p>
    <a class="text-white" href="mailto:rab1002@alu.ubu.es">ada1012@alu.ubu.es</a>
    </div>
    <footer class="d-flex justify-content-evenly p-3 bg-dark text-white">
      <p><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></p>

      <a target="_blank" href="./manual">Manual de usuario</a>
      <a target="_blank" href="./about">Acerca de</a>
      <a href="mailto:rab1002@alu.ubu.es">Contacto</a>
    </footer>
</body>
</html>
