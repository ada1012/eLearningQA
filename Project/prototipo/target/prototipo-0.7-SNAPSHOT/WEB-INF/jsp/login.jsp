<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.FileReader,java.util.Properties,java.io.File"%>
<html lang="en">
<head>
  <title>eLearningQA-Login</title>
  <link rel="icon" type="image/x-icon" href="Logo.png">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="/js/bootstrap.bundle.min.js"></script>
</head>
<%File configs=new File("configurations");
  String[] configArray=configs.list();
  String configurations="";
  String mensaje=(String)session.getAttribute("message");
  String error=(mensaje==null)?"":mensaje;
  session.setAttribute("message", "");
  for(int i=0;i<configArray.length;i++){
    configurations+="<option value="+configArray[i]+">"+configArray[i]+"</option>";
  }
  %>
<body>
  <header class="p-3 bg-dark text-white row" style="--bs-gutter-x:0;"><div class="col"><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></div><div class="col text-end">No estás registrado</div></header>
            <div class="d-flex justify-content-center" style="background-image: url('atardecer.jpg');height: 100vh;">

          <div class="card w-25 p-3 align-self-center" style="min-width: 300px">
                    <h2 class="text-primary">eLearningQA - Login</h2>
                    <form action="list" method="post">
                        <div class="form-floating mb-3 mt-3">
                            <input type="text" class="form-control" id="user" placeholder="Enter username" name="username">
                            <label class="text-primary" for="user">Username</label>
                        </div>
                        <div class="form-floating mt-3 mb-3">
                            <input type="password" class="form-control" id="pwd" placeholder="Enter password" name="password">
                            <label class="text-primary" for="pwd">Password</label>
                        </div>
                        <div class="form-floating mb-3 mt-3">
                            <input type="text" class="form-control" id="host" placeholder="Enter host to connect to" name="host" value="https://school.moodledemo.net">
                            <label class="text-primary" for="host">Host</label>
                        </div>
                    <p class="text-danger"><%=error%></p>
                    <label for="configuration">Tipo de curso:</label><select name="configuration">
                      <%=configurations%>
                    </select>
                    <button type="submit" class="btn btn-primary">Entrar</button>
                </form>
          </div>
    </div>
    <footer class="d-flex justify-content-evenly p-3 bg-dark text-white">
      <p><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></p>

      <a target="_blank" href="./manual">Manual de usuario</a>
      <a target="_blank" href="./about">Acerca de</a>
      <a href="mailto:rab1002@alu.ubu.es">Contacto</a>
    </footer>
</body>
</html>
