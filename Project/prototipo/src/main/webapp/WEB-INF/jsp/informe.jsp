<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Arrays, java.util.ArrayList, java.util.Map, java.util.HashMap, es.ubu.lsi.ELearningQAFacade, es.ubu.lsi.AlertLog, es.ubu.lsi.RegistryIO, es.ubu.lsi.AnalysisSnapshot, es.ubu.lsi.model.Course, es.ubu.lsi.model.QuizSummary, es.ubu.lsi.model.Quiz, es.ubu.lsi.model.User, es.ubu.lsi.model.Forum, es.ubu.lsi.model.Post, es.ubu.lsi.model.EstadisticasForo, org.apache.logging.log4j.LogManager, org.apache.logging.log4j.Logger" %>
<html lang="en">
<head>
    <%String informe="";
          String matriz="";
          String fases="";
          String nombreCurso="";
          String token=(String)session.getAttribute("token");
          String host=(String)session.getAttribute("host");
          String fullname=(String)session.getAttribute("fullname");
          String grafico="";
          float[] porcentajes;
          AlertLog alertas= new AlertLog();
          double[] puntosComprobaciones;
          double[] puntosCurso;
          List<Quiz> quizzes = new ArrayList<>();
          List<User> usuarios = new ArrayList<>();
          List<Forum> foros = new ArrayList<>();
          List<Post> posts = new ArrayList<>();
          Map<Integer, Double> estadisticasCuestionarios = new HashMap<>();
          List<QuizSummary> resumenCuestionarios = new ArrayList<>();
          List<EstadisticasForo> resumenForos = new ArrayList<>();
          Map<Integer, String> cuestionarios = new HashMap<>();
          Map<Integer, String> estadisticasForos = new HashMap<>();

          // Relación de cuestionarios con sus gráficos
          // graficoPreguntas es un mapa que relaciona el id del cuestionario con un array del id de las preguntas
          // graficoNotas es un mapa que relaciona el id del cuestionario con un array de las notas de las preguntas
          Map<Integer, int[]> graficoPreguntas = new HashMap<>();
          Map<Integer, double[]> graficoNotas = new HashMap<>();

          String vinculo=(String)session.getAttribute("host")+"/course/view.php?id=";
          try{ELearningQAFacade fachada=(ELearningQAFacade)session.getAttribute("fachada");
          String courseid= request.getParameter("courseid");
          vinculo+=courseid;
          if(courseid==null){
            puntosComprobaciones=new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            List<Course> listaCursos=fachada.getListaCursos(token);
            for(Course curso:listaCursos){
              usuarios=fachada.getListaUsuarios(token, curso.getId());
              foros=fachada.getListaForos(token, curso.getId());
              posts=fachada.getListaPosts(token, foros);
              alertas.guardarTitulo(curso.getFullname());
              puntosCurso = fachada.realizarComprobaciones(token, curso.getId(), alertas, resumenCuestionarios, quizzes, usuarios, posts, foros);
              for(int i=0;i<puntosComprobaciones.length;i++){
                puntosComprobaciones[i]+=puntosCurso[i];
              }
            }
            nombreCurso="Informe general de cursos";
            porcentajes=fachada.calcularPorcentajesMatriz(puntosComprobaciones, listaCursos.size());
            matriz=fachada.generarMatrizRolPerspectiva(porcentajes);
            fases=fachada.generarInformeFases(puntosComprobaciones, resumenCuestionarios, resumenForos, listaCursos.size());
          }else{
            Course curso= fachada.getCursoPorId(token, Integer.parseInt(courseid));
            usuarios=fachada.getListaUsuarios(token, curso.getId());
            foros=fachada.getListaForos(token, curso.getId());
            posts=fachada.getListaPosts(token, foros);
            alertas.setCourseid(Integer.parseInt(courseid));
            nombreCurso=curso.getFullname();
            session.setAttribute("coursename",nombreCurso);
            // Obtener cuestionarios
            quizzes=fachada.getQuizzes(token, Integer.parseInt(courseid));
            // Obtener resumenes de cuestionarios
            resumenCuestionarios=fachada.generarListaCuestionarios(token, Integer.parseInt(courseid), quizzes);
            // Generamos los informes de los cuestionarios
            cuestionarios=fachada.generarInformesCuestionarios(resumenCuestionarios);
            // Generamos primer grafico
            // Obtenemos los datos para el grafico
            graficoPreguntas=fachada.generarGraficoPreguntas(token, quizzes);
            graficoNotas=fachada.generarGraficoNotas(token, quizzes);
            puntosComprobaciones = fachada.realizarComprobaciones(token, Integer.parseInt(courseid), alertas, resumenCuestionarios, quizzes, usuarios, posts, foros);
            porcentajes=fachada.calcularPorcentajesMatriz(puntosComprobaciones,1);
            matriz=fachada.generarMatrizRolPerspectiva(porcentajes);
            // Obtener resumenes de foros
            resumenForos=fachada.porcentajeAlumnosForos(token, foros, usuarios, alertas);
            // Generamos los informes de los foros
            estadisticasForos=fachada.generarInformesForos(resumenForos);
            fases=fachada.generarInformeFases(puntosComprobaciones, resumenCuestionarios, resumenForos, 1);
            RegistryIO.guardarResultados(host, fullname, courseid,
                       new AnalysisSnapshot(nombreCurso, puntosComprobaciones, porcentajes, alertas.toString()));
            grafico=RegistryIO.generarGraficos(host, fullname, courseid);
          }
          }catch(Exception e){
            Logger LOGGER = LogManager.getLogger();
            LOGGER.error("exception", e);
            response.sendRedirect("error");
          }

          %>
    <meta charset="UTF-8">
    <title><%=nombreCurso%>-Informe</title>
    <link rel="icon" type="image/x-icon" href="Logo.png">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="/js/bootstrap.bundle.min.js"></script>
    <script src="/js/plotly-latest.min.js"></script>
    <style type="text/css">
    .tg  {border-collapse:collapse;border-spacing:0;}
    .tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
      overflow:hidden;padding:0px 0px;word-break:normal;}
    .tg .tg-plgr{background-color:#C0C0C0;font-weight:bold;text-align:center;vertical-align:middle}
    .tg .tg-ltgr{background-color:#EFEFEF;text-align:right;vertical-align:middle}
    .tg tr.active{-webkit-filter: brightness(90%);border-style:solid;border-width:2px;}
    .tg tr:hover{-webkit-filter: brightness(80%);}
    .tg .tg-pgre{background-color:#198754;text-align:center;vertical-align:middle}
    .tg .tg-char{background-color:#66cc00;text-align:center;vertical-align:middle}
    .tg .tg-yell{background-color:#ffc107;text-align:center;vertical-align:middle}
    .tg .tg-oran{background-color:#fd7e14;text-align:center;vertical-align:middle}
    .tg .tg-pred{background-color:#dc3545;text-align:center;vertical-align:middle}
    tr:nth-child(even) {
      background-color: #efefef;
    }
    tr:nth-child(odd) {
      background-color: #ffffff;
    }

    .tabcontent {
      display: none;
      padding: 6px 6px;
    }

    .info {
      display:none;
      overflow: auto;
    }

    .accordion-button:not(.collapsed){
      color:#842029;background-color:#f8d7da;border-color:#f5c2c7
    }

    .toggle-cuestionarios, .cuestionarios, .cuestionario, .toggle-foros, .foros{
      display: none;
    }

    h1 {
      font-size: 24px;
      font-weight: bold;
      text-align: center;
    }

    p {
      margin-bottom: 5px;
    }
    </style>
</head>
<body>
  <header class="bg-dark text-white row" style="--bs-gutter-x:0;">
        <div class="col m-3 text-center">
          <img src="FullLogo.png" width="200" height="32" alt="eLearningQA">
        </div>
        <div class="btn-group col" role="group">
          <button class="tablink btn btn-primary active" style="box-shadow: none;" onclick="openTab(event, 'Fases')">Informe de fases</button>
          <button class="tablink btn btn-primary" style="box-shadow: none;" onclick="openTab(event, 'Matriz')">Evolución del rendimiento</button>
        </div>
        <div class="col m-3 text-center">
          <a target="_blank" href=<%=vinculo%>><%=nombreCurso%></a>
        </div>
      </header>
      <div class="d-flex justify-content-center" style="height:85vh;background-image: url('atardecer.jpg');">
        <div id="Fases" class="tabcontent w-100 p-0" style="display:flex">
          <div class="card m-2 me-0 p-1" style="width: 60%;overflow:auto;">
              <%=fases%>
          </div>
          <div class="card m-2 ms-0 p-1 alertas" style="width: 40%;overflow:auto;">
            <%=alertas%>
          </div>
          <div class="card m-2 ms-0 p-1 cuestionarios" style="width: 40%;overflow:auto;">
            <% for (Map.Entry<Integer, String> entry : cuestionarios.entrySet()) { %>
              <% if (entry.getValue() instanceof String) { %>
                <%= entry.getValue() %>
                <div class="cuestionario" id="grafico<%=entry.getKey()%>">
                  <script>
                    var idPreguntas = <%=Arrays.toString(graficoPreguntas.get(entry.getKey()))%>;
                    var notasMedias = <%=Arrays.toString(graficoNotas.get(entry.getKey()))%>;

                    // Crea los datos para el gráfico de barras
                    var data = [{
                      type: 'bar',
                      x: idPreguntas,
                      y: notasMedias
                    }];

                    // Crea el diseño del gráfico
                    var layout = {
                      title: 'Gráfico de barras de los alumnos con calificación',
                      xaxis: { title: 'ID de Pregunta' },
                      yaxis: { title: 'Porcentaje Nota Media' }
                    };
                    
                    if (idPreguntas.length > 0) {
                      // Crea el gráfico
                      Plotly.newPlot('grafico<%=entry.getKey()%>', data, layout);
                    }
                  </script>
                </div>
              <% } %>
            <% } %>
          </div>
          <div class="card m-2 ms-0 p-1 foros" style="width: 40%;overflow:auto;">
            <% for (Map.Entry<Integer, String> entry : estadisticasForos.entrySet()) { %>
              <% if (entry.getValue() instanceof String) { %>
                <%= entry.getValue() %>
              <% } %>
            <% } %>
          </div>
        </div>
        <div id="Matriz" class="tabcontent w-100 p-0" style="display:none">
            <div class="card m-3 p-3 w-100">
            <%=matriz%>
            <div id="grafico"></div>
            </div></div>
      </div>
          <footer class="d-flex justify-content-evenly p-3 bg-dark text-white">
            <p><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></p>

            <a target="_blank" href="./manual">Manual de usuario</a>
            <a target="_blank" href="./about">Acerca de</a>
            <a href="mailto:rab1002@alu.ubu.es">Contacto</a>
          </footer>
          <script>
          <%=grafico%>

          function openTab(evt, tab) {
            var i, tabcontent, tablinks;

            const alertas = document.getElementsByClassName('alertas');
            for (i = 0; i < alertas.length; i++) {
              alertas[i].style.display = "block";
            }
            const cuestionariosDiv = document.getElementsByClassName('cuestionarios');
            for (i = 0; i < cuestionariosDiv.length; i++) {
              cuestionariosDiv[i].style.display = "none";
            }
            const forosDiv = document.getElementsByClassName('foros');
            for (i = 0; i < forosDiv.length; i++) {
              forosDiv[i].style.display = "none";
            }

            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
              tabcontent[i].style.display = "none";
            }
            tablinks = document.getElementsByClassName("tablink");
            for (i = 0; i < tablinks.length; i++) {
              tablinks[i].className = tablinks[i].className.replace(" active", "");
            }
            document.getElementById(tab).style.display = "flex";
            evt.currentTarget.className += " active";
          }



          function openInfo(evt, category) {
            // Oculta los cuestionarios
            const cuestionarios = document.getElementsByClassName('cuestionarios');
            for (i = 0; i < cuestionarios.length; i++) {
              cuestionarios[i].style.display = "none";
            }
            // Oculta los foros
            const foros = document.getElementsByClassName('foros');
            for (i = 0; i < foros.length; i++) {
              foros[i].style.display = "none";
            }
            // Muestra las alertas
            const alertas = document.getElementsByClassName('alertas');
            for (i = 0; i < alertas.length; i++) {
              alertas[i].style.display = "block";
            }

            var i, ltgr, infolines, wanted;
            ltgr = document.getElementsByTagName("tr");
            for (i = 0; i < ltgr.length; i++) {
              ltgr[i].className = ltgr[i].className.replace(" active", "");
            }
            infolines = document.getElementsByClassName('infoline');
            wanted = [...infolines].filter(element => element.classList.contains(category));
            for (i = 0; i < infolines.length; i++) {
              infolines[i].style.display = "none";
            }
            for (i = 0; i < wanted.length; i++) {
              wanted[i].style.display = "block";
            }
            evt.currentTarget.className += " active";
          }



          function toggleCuestionarios() {
            const cuestionarios = document.querySelectorAll('.toggle-cuestionarios');
            for (let i = 0; i < cuestionarios.length; i++) {
              const cuestionario = cuestionarios[i];
              if (cuestionario.style.display === 'none') {
                cuestionario.style.display = 'table-row';
              } else {
                cuestionario.style.display = 'none';
              }
            }
          }



          function toggleForos() {
            const foros = document.querySelectorAll('.toggle-foros');
            for (let i = 0; i < foros.length; i++) {
              const foro = foros[i];
              if (foro.style.display === 'none') {
                foro.style.display = 'table-row';
              } else {
                foro.style.display = 'none';
              }
            }
          }



          function muestraCuestionario(idCuestionario) {
            // Oculta todas las alertas
            const alertas = document.getElementsByClassName('alertas');
            for (i = 0; i < alertas.length; i++) {
              alertas[i].style.display = "none";
            }
            // Oculta todos los foros
            const foros = document.getElementsByClassName('foros');
            for (i = 0; i < foros.length; i++) {
              foros[i].style.display = "none";
            }

            // Muestra el componente de cuestionarios
            const cuestionariosDiv = document.getElementsByClassName('cuestionarios');
            for (i = 0; i < cuestionariosDiv.length; i++) {
              cuestionariosDiv[i].style.display = "block";
            }
            // Oculta todos los cuestionarios
            const cuestionarios = document.getElementsByClassName('cuestionario');
            for (i = 0; i < cuestionarios.length; i++) {
              cuestionarios[i].style.display = "none";
            }
            // Muestra el cuestionario seleccionado
            const cuestionario = document.getElementById(idCuestionario);
            cuestionario.style.display = "block";
            // Muestra el gráfico del cuestionario seleccionado
            const grafico = document.getElementById("grafico"+idCuestionario);
            grafico.style.display = "block";
          }



          function muestraForo(idForo) {
            // Oculta todas las alertas
            const alertas = document.getElementsByClassName('alertas');
            for (i = 0; i < alertas.length; i++) {
              alertas[i].style.display = "none";
            }
            // Oculta todos los cuestionarios
            const cuestionarios = document.getElementsByClassName('cuestionarios');
            for (i = 0; i < cuestionarios.length; i++) {
              cuestionarios[i].style.display = "none";
            }

            // Muestra el componente de foros
            const forosDiv = document.getElementsByClassName('foros');
            for (i = 0; i < forosDiv.length; i++) {
              forosDiv[i].style.display = "block";
            }
            // Oculta todos los foros
            const foros = document.getElementsByClassName('foro');
            for (i = 0; i < foros.length; i++) {
              foros[i].style.display = "none";
            }
            // Muestra el foro seleccionado
            const foro = document.getElementById("foro"+idForo);
            foro.style.display = "block";
          }
          </script>
          <script>
          var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
          var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
          return new bootstrap.Tooltip(tooltipTriggerEl)
          })
          </script>

</body>
</html>