<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="es.ubu.lsi.ELearningQAFacade,es.ubu.lsi.WebServiceClient"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>eLearningQA-Manual</title>
    <link rel="icon" type="image/x-icon" href="Logo.png">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
     <script src="/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<header class="p-3 bg-dark text-white row" style="--bs-gutter-x:0;"><div class="col"><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></div><div class="col text-end"></div></header>
            <div class="d-flex justify-content-center" style="background-image: url('atardecer.jpg');">

          <div class="card w-100 m-3 p-3">
                    <h2>Manual de usuario:</h2>
                    <h3>Pantallas:</h3>
                    <p><strong>Página de login:</strong> la página de login contiene un formulario de inicio de sesión, un link &quot;Acerca de&quot;, un link &quot;Manual de usuario&quot; que muestra la versión web de este manual, y un link de contacto.</p>
                    <img src="Login.png" class="mx-auto" alt="Página de login">
                    <p><strong>Página principal:</strong> la página principal contiene una cabecera que indica el nombre de usuario con el que se ha registrado, un botón para desconectarse, un enlace para generar un informe global de todos los cursos, y una tabla con una lista de enlaces para generar los informes específicos de cada curso con una caja de búsqueda.</p>
                    <img src="ListaCursos.png" class="mx-auto" alt="Página principal">
                    <p><strong>Página de informe:</strong> la página de informe genera el informe dependiendo de cómo se haya accedido a esta, ya sea mediante el enlace &quot;Generar informe global&quot; o por medio de los enlaces a informes de cursos específicos.</p>
                    <p>Los informes contienen los siguientes elementos:</p>
                    <ul>
                        <li><strong>Puntuaciones por rol y perspectiva:</strong> el informe contiene una matriz de roles y perspectivas que da distintas puntuaciones a cada uno de los roles (Diseñador, Facilitador, y Proveedor) en cada una de las tres perspectivas definidas (Pedagógica, Tecnológica, y Estratégica) según los resultados del análisis.</li>
                        <li><strong>Gráfico de evolución:</strong> la aplicación almacena estas puntuaciones cada vez que se carga un informe para generar un gráfico interactivo de la evolución de estos valores.</li>
                        <img src="Evolucion.png" class="mx-auto" alt="Página de evolución del rendimiento">
                        <li><strong>Informe de fases:</strong> el informe contiene también una tabla que muestra los resultados en cada una de las comprobaciones del análisis y las agrupa en las distintas fases del diseño instruccional (Diseño, Implementación, Realización, y Evaluación) además de dar una puntuación general.</li>
                        <li><strong>Lista de mejoras:</strong> por último, el informe muestra una lista de motivos concretos que producen resultados negativos en el análisis para subsanarlos o tenerlos en cuenta de cara al futuro. Haciendo clic en las filas de la tabla se puede filtrar la lista de mejoras.</li>
                        <img src="InformeFases.png" class="mx-auto" alt="Página de informe de fases">
                    </ul>
                    <p>El informe especifico muestra los resultados de las comprobaciones en el informe de fases de forma absoluta, es decir, que solo indica si el criterio se cumple o no, sin embargo, en el informe global, se muestra un resultado u otro dependiendo de la cantidad de cursos que satisfacen dicho criterio.</p>

                    <h3>Acciones del usuario:</h3>
                    <ul>
                    <li><strong>Login:</strong> para acceder a la aplicación son necesarias las credenciales de acceso a una cuenta de la plataforma a la que pretenda conectarse. Debe introducir su usuario y contraseña en los campos &quot;Username&quot; y &quot;Password&quot;. También es necesario indicar el host al que conectarse y se puede elegir la configuración de las comprobaciones en el menú desplegable. Para acceder a la página principal, pulse el botón &quot;Entrar&quot; tras haber rellenado el formulario.</li>
                    <li><strong>Desconectar:</strong> desde la página principal, si desea finalizar su sesión, pulse el botón &quot;Desconectar&quot;. Esto invalidará sus credenciales y le impedirá acceder a la aplicación hasta que se registre de nuevo con unas credenciales válidas.</li>
                    <li><strong>Generar informe específico:</strong> la página principal muestra una tabla con todos los cursos en los que se encuentra matriculado el usuario registrado en formato de enlace. Al clicar un enlace, se generará un informe en una pestaña aparte del navegador que mostrará los resultados del análisis que ha realizado la aplicación sobre el curso correspondiente.</li><li><strong>Generar informe global:</strong> en la página principal hay un enlace llamado &quot;Generar informe global&quot;. Al hacer clic sobre este, se generará un informe en una pestaña aparte del navegador que mostrará un resumen de los análisis de todos los cursos en los que se encuentra matriculado el profesor.</li>
                    </ul>
                    <h3>Explicación de las comprobaciones de los informes:</h3>
                    <p>Las siguientes comprobaciones están relacionadas con los roles, fases, y perspectivas mencionados anteriormente. Los distintos procesos del diseño instruccional se encuentran divididos en fases, con ciertas perspectivas en mente, y son responsabilidad directa o indirecta de ciertos roles. Al estar las comprobaciones ligadas a esos procesos se muestran agrupadas por fases, y después de la explicación se indican los roles responsables e involucrados, además de las perspectivas correspondientes.</p>
                    <p><strong>Diseño:</strong></p>
                    <ul>
                    <li><strong>Las opciones de progreso del estudiante están activadas</strong>: se comprueba que estén habilitadas las opciones de progreso de los estudiantes en el curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador <u>Perspectivas:</u> Pedagógica</li>
                    <li><strong>Se proporcionan contenidos en diferentes formatos:</strong> se comprueba que haya variedad de formatos en los recursos del curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>El curso tiene grupos:</strong> se comprueba que existan grupos definidos en el curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica</li>
                    <li><strong>El curso tiene actividades grupales:</strong> se comprueba que existan actividades con entrega grupal habilitada en el curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica</li>
                    <li><strong>Los estudiantes pueden ver las condiciones necesarias para completar una actividad:</strong> se comprueba que esté habilitada la opción de mostrar las condiciones para completar una actividad en el curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica</li>
                    <li><strong>Todas las actividades tienen la misma nota máxima en el calificador:</strong> se comprueba que exista una consistencia en las notas máximas de los items de calificación (tareas, entregas, cuestionarios) del curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica</li>
                    </ul>
                    <p><strong>Implementación:</strong></p>
                    <ul>
                    <li><strong>Los recursos están actualizados:</strong> se comprueba que los recursos del curso tengan una fecha de creación reciente. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>Fechas de apertura y cierre de tareas son correctas:</strong> se comprueba que las fechas de apertura y cierre de tareas y cuestionarios no se solapen de forma erronea con las fechas de inicio y fin del curso. <u>Responsable:</u> Facilitador <u>Involucrados:</u> Diseñador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>Se detallan los criterios de evaluación:</strong> se comprueba que exista en al menos una actividad una rúbrica o una guía de calificación en el curso. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>El calificador no tiene demasiado anidamiento:</strong> se comprueba que la estructura de las categorías del calificador no sea demasiado enrevesada. <u>Responsable:</u> Diseñador <u>Involucrados:</u> Facilitador y Proveedor <u>Perspectivas:</u> Pedagógica y Estratégica</li>
                    <li><strong>Todos los alumnos están en algún grupo:</strong> se comprueba que cada alumno pertenezca a un grupo. <u>Responsable:</u> Proveedor <u>Involucrados:</u> Diseñador <u>Perspectivas:</u> Tecnológica y Estratégica</li>
                    </ul>
                    <p><strong>Realización:</strong></p>
                    <ul>
                    <li><strong>El profesor responde en los foros dentro del límite de 48 horas lectivas desde que se plantea la duda:</strong> se comprueba que no hayan preguntas por parte de alumnos que estén sin responder en un tiempo razonable. <u>Responsable:</u> Facilitador <u>Involucrados:</u> Diseñador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>Se ofrece retroalimentación de las tareas:</strong> se comprueba que el profesor deje comentarios en la mayoría de calificaciones que haga. <u>Responsable:</u> Facilitador <u>Involucrados:</u> Diseñador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>Las tareas están calificadas:</strong> se comprueba que no hayan entregas de alumnos que hayan pasado una semana sin calificación. <u>Responsable:</u> Facilitador <u>Involucrados:</u> Diseñador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    <li><strong>El calificador muestra cómo ponderan las diferentes tareas:</strong> se comprueba que el calificador muestre los pesos de los items de calificación. <u>Responsable:</u> Facilitador <u>Involucrados:</u> Diseñador y Proveedor <u>Perspectivas:</u> Pedagógica y Tecnológica</li>
                    </ul>
                    <p><strong>Evaluación:</strong></p>
                    <ul>
                    <li><strong>La mayoría de alumnos responden a los feedbacks:</strong> se comprueba que no hayan muchos alumnos que no respondan a los feedbacks. <u>Responsable:</u> Proveedor <u>Involucrados:</u> Diseñador y Facilitador <u>Perspectivas:</u> Pedagógica, Tecnológica, y Estratégica</li>
                    <li><strong>Se utilizan encuestas de opinión:</strong> se comprueba que el curso contenga encuestas de opinión. <u>Responsable:</u> Proveedor <u>Involucrados:</u> Diseñador y Facilitador <u>Perspectivas:</u> Pedagógica, Tecnológica, y Estratégica</li></ul>
          </div>
    </div>
    <footer class="d-flex justify-content-evenly p-3 bg-dark text-white">
      <p><img src="FullLogo.png" width="200" height="32" alt="eLearningQA"></p>

      <a>Manual de usuario</a>
      <a target="_blank" href="./about">Acerca de</a>
      <a href="mailto:rab1002@alu.ubu.es">Contacto</a>
    </footer>
</body>
</html>