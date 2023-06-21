<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
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
  <script>
    var informe = ""
    <% if (course_id != null) { %>
      informe = "./informe?courseid=" + <%=course_id%>
    <% } else { %>
      informe = "./informe"
    <% } %>
    // Código JavaScript para cargar la página del informe de forma asíncrona
    fetch(informe)
      .then(function(response) {
        if (response.ok) {
          return response.text();
        } else {
          throw new Error('Error al cargar el informe');
        }
      })
      .then(function(data) {
        window.location.href = informe;
      })
      .catch(function(error) {
        window.location.href = informe;
      });
  </script>
</head>
<body>
    <div id="spinnerContainer" class="spinner-container">
        <div class="spinner"></div>
        <p>Cargando...</p>
    </div>
</body>
</html>
