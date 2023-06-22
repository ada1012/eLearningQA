<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="en">
<head>
  <title>Cargando informe</title>
  <link rel="icon" type="image/x-icon" href="Logo.png">
  <style>
    /* Estilos CSS para la página de carga */
    .spinner-container {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(255, 255, 255, 0.8);
        z-index: 9999;
        text-align: center;
    }

    .spinner {
        margin: 20% auto;
        width: 50px;
        height: 50px;
        border: 3px solid #ccc;
        border-top-color: #333;
        border-radius: 50%;
        animation: spin 1s infinite linear;
    }

    @keyframes spin {
        to {
            transform: rotate(360deg);
        }
    }
  </style>
  <% String course_id = request.getParameter("courseid"); %>
</head>
<body>
    <div id="spinnerContainer" class="spinner-container">
        <div class="spinner"></div>
        <p>Cargando...</p>
    </div>
    <script>
      var informe = "";
      <% if (course_id != null) { %>
        informe = "./informe?courseid=" + <%=course_id%>
      <% } else { %>
        informe = "./informe"
      <% } %>
  
      // redirigir a la página del informe
      window.location.href = informe;
    </script>
</body>
</html>
