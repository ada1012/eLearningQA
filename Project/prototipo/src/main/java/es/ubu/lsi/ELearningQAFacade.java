package es.ubu.lsi;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.ubu.lsi.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ELearningQAFacade {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CHECKS_DISENO =7;
    private static final int CHECKS_IMPLEMENTACION =5;
    private static final int CHECKS_REALIZACION =4;
    private static final int CHECKS_EVALUACION =3;
    private static final int CHECKS_TOTAL = CHECKS_DISENO + CHECKS_IMPLEMENTACION + CHECKS_REALIZACION + CHECKS_EVALUACION;
    protected static String[] camposInformeFases;
    private final FacadeConfig config;

    static {
        String sep= File.separator;
        String ruta="."+sep+"src"+sep+"main"+sep+"resources"+sep+"json"+sep+"informe"+sep;
        String extension=".json";
        ObjectMapper mapper=new ObjectMapper();
        try {
            camposInformeFases= mapper.readValue(new File(ruta+"CamposInformeFases"+extension), String[].class);
        } catch (Exception e) {
            LOGGER.error("exception", e);
        }
    }

    public ELearningQAFacade(String configFile, String host) {
        config=new FacadeConfig(configFile);
        config.setHost(host);
    }

    public String conectarse(String username, String password){
        return WebServiceClient.login(username, password, config.getHost());
    }

    public String generarListaCursos(String token){
        List<Course> listaCursos= getListaCursos(token);
        StringBuilder listaEnTabla= new StringBuilder("<table>");
        for (Course curso: listaCursos) {
            listaEnTabla.append("<tr><td><a target=\"_blank\" href=\"./informe?courseid=").append(curso.getId())
                    .append("\">").append(curso.getFullname())
                    .append(" ("+curso.getCoursecategory()+")").append("</a></td></tr>");
        }
        listaEnTabla.append("</table>");
        return listaEnTabla.toString();
    }

    public List<Course> getListaCursos(String token) {
        return WebServiceClient.obtenerCursos(token, config.getHost());
    }

    public String obtenerNombreCompleto(String token, String username) {
        return WebServiceClient.obtenerNombreCompleto(token, username, config.getHost());
    }

    public double[] realizarComprobaciones(String token, long courseid, AlertLog registro, Map<Integer, Double> estadisticasCuestionarios) {
        Course curso= getCursoPorId(token, courseid);
        List<User> listaUsuarios= WebServiceClient.obtenerUsuarios(token, courseid, config.getHost());
        StatusList listaEstados=WebServiceClient.obtenerListaEstados(token, courseid, listaUsuarios, config.getHost());
        List<es.ubu.lsi.model.Module> listaModulos=WebServiceClient.obtenerListaModulos(token, courseid, config.getHost());
        List<Group> listaGrupos=WebServiceClient.obtenerListaGrupos(token, courseid, config.getHost(), registro);
        List<Assignment> listaTareas=WebServiceClient.obtenerListaTareas(token, courseid, config.getHost());
        List<Table> listaCalificadores=WebServiceClient.obtenerCalificadores(token, courseid, config.getHost());
        
        // Obtener recursos no funciona para Pilgrimage
        List<Resource> listaRecursos=WebServiceClient.obtenerRecursos(token, courseid, config.getHost());
        List<CourseModule> listaModulosTareas=WebServiceClient.obtenerModulosTareas(token, listaTareas, config.getHost());
        List<Post> listaPosts=WebServiceClient.obtenerListaPosts(token, courseid, config.getHost());
        Map<Integer, Long> mapaFechasLimite=WebServiceClient.generarMapaFechasLimite(listaTareas);
        List<Assignment> tareasConNotas=WebServiceClient.obtenerTareasConNotas(token,mapaFechasLimite, config.getHost(), listaTareas);
        List<ResponseAnalysis> listaAnalisis=WebServiceClient.obtenerAnalisis(token, courseid, config.getHost());
        List<Survey> listaSurveys=WebServiceClient.obtenerSurveys(token, courseid, config.getHost());
        List<es.ubu.lsi.model.Module> modulosMalFechados=WebServiceClient.obtenerModulosMalFechados(curso, listaModulos);
        List<Resource> recursosDesfasados=WebServiceClient.obtenerRecursosDesfasados(curso, listaRecursos);
        
        // Cuestionarios
        List<Quiz> quizzes = WebServiceClient.getQuizzes(courseid, config.getHost(), token);
        // List<Double> estadisticas = new ArrayList<Double>();
        // for (Quiz quiz : quizzes) {
        // 	System.out.println("Quiz id: " + quiz.getId());
        // 	Map<Integer, Double> grades = WebServiceClient.getQuizGrades(token, courseid, config.getHost(), quiz.getId());
        // 	System.out.println("Grades: " + grades.size());
        // 	for (Integer key : grades.keySet()) {
        //         Double value = grades.get(key);
        //         
        //         System.out.println("Key: " + key + ", Value: " + value);
        //     }
        //     // double estadistica = WebServiceClient.obtenerEstadisticasCuestionario(token, courseid, config.getHost(), quiz.getId());
        //     // estadisticas.add(estadistica);
        // }

        // Calculamos los porcentajes
        double porcentaje;
        double sum = 0;
        if (estadisticasCuestionarios.isEmpty()) {
            porcentaje = 0;
        } else {
            for (Double estadistica : estadisticasCuestionarios.values()) {
                sum += estadistica;
            }
            porcentaje = sum / estadisticasCuestionarios.size();
        }
        
        double[] puntosComprobaciones = new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if(isestaProgresoActivado(listaEstados, registro)){puntosComprobaciones[0]++;}
        if(isHayVariedadFormatos(listaModulos, registro)){puntosComprobaciones[1]++;}
        if(isTieneGrupos(listaGrupos, registro)){puntosComprobaciones[2]++;}
        if(isHayTareasGrupales(listaTareas, registro)){puntosComprobaciones[3]++;}
        if(isSonVisiblesCondiciones(curso, registro)){puntosComprobaciones[4]++;}
        if(isEsNotaMaxConsistente(listaCalificadores, registro)){puntosComprobaciones[5]++;}
        if(isHayCuestionarios(quizzes, registro)){puntosComprobaciones[6]++;}
        if(isEstanActualizadosRecursos(recursosDesfasados, registro)){puntosComprobaciones[7]++;}
        if(isSonFechasCorrectas(modulosMalFechados, registro)){puntosComprobaciones[8]++;}
        if(isMuestraCriterios(listaModulosTareas, registro)){puntosComprobaciones[9]++;}
        if(isAnidamientoCalificadorAceptable(listaCalificadores, registro)){puntosComprobaciones[10]++;}
        if(isAlumnosEnGrupos(listaUsuarios, registro)){puntosComprobaciones[11]++;}
        if(isRespondeATiempo(listaUsuarios,listaPosts, registro)){puntosComprobaciones[12]++;}
        if(isHayRetroalimentacion(listaCalificadores, registro)){puntosComprobaciones[13]++;}
        if(isEstaCorregidoATiempo(tareasConNotas,listaUsuarios, registro)){puntosComprobaciones[14]++;}
        if(isCalificadorMuestraPonderacion(listaCalificadores, registro)){puntosComprobaciones[15]++;}
        if(isRespondenFeedbacks(listaAnalisis,listaUsuarios, registro)){puntosComprobaciones[16]++;}
        if(isUsaSurveys(listaSurveys, registro)){puntosComprobaciones[17]++;}
        puntosComprobaciones[18] = porcentajeCuestionariosContestados(quizzes, porcentaje, registro);

        return puntosComprobaciones;
    }

    public Map<Integer, Double> generarEstadisticasCuestionarios(String token, long courseid) {
        List<Quiz> quizzes = WebServiceClient.getQuizzes(courseid, config.getHost(), token);

        if (!quizzes.isEmpty()) {
            return WebServiceClient.obtenerEstadisticasCuestionarios(token, courseid, config.getHost(), quizzes);
        }
        
        return null;
    }

    public String generarInformeFases(double[] puntos, Map<Integer, Double> estadisticasCuestionarios, int nroCursos) {
        double contadorDiseno=puntos[0]+puntos[1]+puntos[2]+puntos[3]+puntos[4]+puntos[5]+puntos[6];
        double contadorImplementacion=puntos[7]+puntos[8]+puntos[9]+puntos[10]+puntos[11];
        double contadorRealizacion=puntos[12]+puntos[13]+puntos[14]+puntos[15];
        double contadorEvaluacion=puntos[16]+puntos[17]+puntos[18];
        double contadorTotal=contadorDiseno+contadorImplementacion+contadorRealizacion+contadorEvaluacion;
        String tabla = camposInformeFases[0]+generarCampoRelativo((float)contadorTotal/nroCursos, CHECKS_TOTAL) +
                camposInformeFases[1]+generarCampoRelativo((float)contadorDiseno/nroCursos, CHECKS_DISENO) +
                generarFilas(new int[]{2, 0}, 7, puntos, nroCursos)+
                camposInformeFases[9]+generarCampoRelativo((float)contadorImplementacion/nroCursos, CHECKS_IMPLEMENTACION) +
                generarFilas(new int[]{10, 7}, 5, puntos, nroCursos)+
                camposInformeFases[15]+generarCampoRelativo((float)contadorRealizacion/nroCursos, CHECKS_REALIZACION) +
                generarFilas(new int[]{16, 12}, 4, puntos, nroCursos)+
                camposInformeFases[20]+generarCampoRelativo((float)contadorEvaluacion/nroCursos, CHECKS_EVALUACION) +
                generarFilas(new int[]{21, 16}, 2, puntos, nroCursos);
        
        // Porcentaje de cuestionarios realizados
        tabla += "</tr><tr onclick=\"openInfo(event, 'estadisticquiz')\" data-bs-toggle=\"tooltip\" title=\"Se comprueba qué porcentaje de alumno realiza los respectivos cursos.\"> <td class=\"tg-ltgr\">Al menos un " + config.getMinQuizAnswerPercentage() * 100 + "% responde al cuestionario  <button onclick=\"toggleCuestionarios()\">Desplegar</button></td>" +
                generarCampoRelativo((float)puntos[18], 1);

        if(estadisticasCuestionarios != null && !estadisticasCuestionarios.isEmpty()){
            for (Map.Entry<Integer, Double> entry : estadisticasCuestionarios.entrySet()) {
                tabla += "</tr><tr class=\"toggle-cuestionarios\" data-bs-toggle=\"tooltip\"> <td class=\"tg-ltgr\">Cuestionario " + entry.getKey() + "  <button onclick=\"muestraCuestionario(" + entry.getKey() + ")\">Resumen</button></td>" + generarCampoRelativo((float)(entry.getValue()/100), 1);
                // tabla += camposInformeFases[24]+entry.getKey()+camposInformeFases[25]+entry.getValue();
            }
        }

        tabla += camposInformeFases[23];

        return tabla;
    }

    private String generarFilas(int[] posiciones, int cantidad, double[] puntos, int nroCursos){
        StringBuilder filas= new StringBuilder();
        if(nroCursos==1){
            for (int i = 0; i < cantidad; i++) {
                filas.append(camposInformeFases[posiciones[0] + i]).append(generarCampoAbsoluto(puntos[posiciones[1] + i]));
            }
        }else{
            for (int i = 0; i < cantidad; i++) {
                filas.append(camposInformeFases[posiciones[0] + i]).append(generarCampoRelativo((float)puntos[posiciones[1] + i],nroCursos));
            }
        }
        return filas.toString();
    }

    public Map<Integer, String> generarInformesCuestionarios(String token, long courseid) {
        // Obtenemos los cuestionarios
        List<Quiz> quizzes = WebServiceClient.getQuizzes(courseid, config.getHost(), token);
        Map<Integer, String> informes = new HashMap<>();
        
        // Por cada cuestionario, generamos su informe
        for (Quiz quiz : quizzes) {
            String informe = generarInformeCuestionario(token, courseid, quiz);
            // Creamos una relación entre el id del cuestionario y su informe
            informes.put(quiz.getId(), informe);
        }

        return informes;
    }

    public String generarInformeCuestionario(String token, long courseid, Quiz quiz) {
        QuizSummary quizSummary = WebServiceClient.obtenerResumenCuestionario(token, courseid, config.getHost(), quiz);
        String informe = "";

        if (quizSummary != null) {
            informe += "<div class=\"cuestionario\" id=\"" + quiz.getId() + "\">";
            informe += "<h1>Cuestionario " + quiz.getId() + " - " + quizSummary.getNombreCuestionario() + "</h1>";
            informe += "<p>Número de alumnos: " + quizSummary.getTotalAlumnos() + "</p>";
            informe += "<p>Número de alumnos examinados: " + quizSummary.getAlumnosExaminados() + "</p>";
            informe += "<p>Número de preguntas: " + quizSummary.getTotalPreguntas() + "</p>";
            informe += "<p>Nota máxima: " + quizSummary.getNotaMaxima() + "</p>";
            informe += "<p>Nota media: " + quizSummary.getNotaMedia() + "</p>";
            informe += "<p>Media de intentos: " + quizSummary.getMediaIntentos() + "</p>";
            informe += "</div>";

        }
        return informe;
    }


    public boolean isSonVisiblesCondiciones(Course curso, AlertLog registro) {
        return WebServiceClient.sonVisiblesCondiciones(curso, registro);
    }

    public boolean isTieneGrupos(List<Group> listaGrupos, AlertLog registro) {
        return WebServiceClient.tieneGrupos(listaGrupos, registro);
    }

    public Course getCursoPorId(String token, long courseid) {
        return WebServiceClient.obtenerCursoPorId(token, courseid, config.getHost());
    }



    public boolean isestaProgresoActivado(StatusList listaEstados, AlertLog registro) {
        return WebServiceClient.estaProgresoActivado(listaEstados, registro);
    }

    public boolean isUsaSurveys(List<Survey> listaEncuestas, AlertLog registro) {
        return WebServiceClient.usaSurveys(listaEncuestas, registro);
    }

    public boolean isAlumnosEnGrupos(List<User> listaUsuarios, AlertLog registro) {
        return WebServiceClient.alumnosEnGrupos(listaUsuarios, registro);
    }

    public boolean isEstaCorregidoATiempo(List<Assignment> tareasConNotas, List<User> listaUsuarios, AlertLog registro) {
        return WebServiceClient.estaCorregidoATiempo(tareasConNotas, listaUsuarios, registro, config);
    }

    public boolean isRespondeATiempo(List<User> listaUsuarios, List<Post> listaPostsCompleta, AlertLog registro) {
        return WebServiceClient.respondeATiempo(listaUsuarios, listaPostsCompleta, registro, config);
    }

    public boolean isAnidamientoCalificadorAceptable(List<Table> listaCalificadores, AlertLog registro) {
        return WebServiceClient.anidamientoCalificadorAceptable(listaCalificadores, registro, config);
    }

    public boolean isCalificadorMuestraPonderacion(List<Table> listaCalificadores, AlertLog registro) {
        return WebServiceClient.calificadorMuestraPonderacion(listaCalificadores, registro);
    }

    public boolean isHayRetroalimentacion(List<Table> listaCalificadores, AlertLog registro) {
        return WebServiceClient.hayRetroalimentacion(listaCalificadores, registro, config);
    }

    public boolean isEsNotaMaxConsistente(List<Table> listaCalificadores, AlertLog registro) {
        return WebServiceClient.esNotaMaxConsistente(listaCalificadores, registro);
    }

    // Devuelve true si hay algún cuestionario, sino se devuelve false y se añade un registro de alerta
    public boolean isHayCuestionarios(List<Quiz> quizzes, AlertLog registro) {
        return WebServiceClient.hayCuestionarios(quizzes, registro);
    }

    public boolean isEstanActualizadosRecursos(List<Resource> listaRecursosDesfasados, AlertLog registro) {
        return WebServiceClient.estanActualizadosRecursos(listaRecursosDesfasados, registro, config);
    }

    public boolean isSonFechasCorrectas(List<es.ubu.lsi.model.Module> listaModulosMalFechados, AlertLog registro) {
        return WebServiceClient.sonFechasCorrectas(listaModulosMalFechados, registro);
    }

    public boolean isRespondenFeedbacks(List<ResponseAnalysis> listaAnalisis, List<User> listaUsuarios, AlertLog registro) {
        return WebServiceClient.respondenFeedbacks(listaAnalisis,listaUsuarios, registro, config);
    }

    public boolean isHayTareasGrupales(List<Assignment> listaTareas, AlertLog registro) {
        return WebServiceClient.hayTareasGrupales(listaTareas, registro);
    }

    public boolean isMuestraCriterios(List<CourseModule> listaModulosTareas, AlertLog registro) {
        return WebServiceClient.muestraCriterios(listaModulosTareas, registro);
    }

    public boolean isHayVariedadFormatos(List<es.ubu.lsi.model.Module> listamodulos, AlertLog registro) {
        return WebServiceClient.hayVariedadFormatos(listamodulos, registro, config);
    }

    // Devuelve el porcentaje de cuestionarios contestados, sino se devuelve 0 y se añade un registro de alerta
    public double porcentajeCuestionariosContestados(List<Quiz> quizzes, double porcentaje, AlertLog registro) {
        return WebServiceClient.calculaPorcentajeCuestionarios(quizzes, porcentaje, registro, config);
    }

    public float porcentajeFraccion(float numerador, float denominador){
        return numerador/denominador*100;
    }

    public String generarCampoAbsoluto(double puntos){
        if (puntos==0){
            return "<td class=\"tg-pred\"><img src=\"Cross.png\" width=\"16\" height=\"16\" alt=\"No\"></td>";
        }else{
            return "<td class=\"tg-pgre\"><img src=\"Check.png\" width=\"16\" height=\"16\" alt=\"Sí." +
                    "\"></td>";
        }
    }

    public String generarCampoRelativo(float numerador, float denominador){
        float resultado= numerador/denominador;
        String campoAMedias="<meter value=\""+numerador+"\" min=\"0\" max=\""+denominador+"\"></meter>"+
                String.format("%.1f",porcentajeFraccion(numerador, denominador))+"%"+"</td>";
        if (resultado<0.2){return "<td class=\"tg-pred\">"+campoAMedias;}
        if (resultado<0.4){return "<td class=\"tg-oran\">"+campoAMedias;}
        if (resultado<0.6){return "<td class=\"tg-yell\">"+campoAMedias;}
        if (resultado<0.8){return "<td class=\"tg-char\">"+campoAMedias;}
        else{return "<td class=\"tg-pgre\">"+campoAMedias;}
    }

    public float[] calcularPorcentajesMatriz(double[] puntos,int numeroCursos){
        double[][] matrizPuntos=new double[][]{
                {3,1,0,0,0,0,0,0,0},
                {3,1,1,3,1,1,0,0,0},
                {3,1,1,0,0,0,0,0,0},
                {3,1,1,0,0,0,0,0,0},
                {3,1,1,0,0,0,0,0,0},
                {3,1,1,0,0,0,0,0,0},
                {3,1,1,3,1,1,0,0,0},
                {1,3,1,1,3,1,0,0,0},
                {3,1,1,3,1,1,0,0,0},
                {3,1,1,0,0,0,3,1,1},
                {0,0,0,1,0,3,1,0,3},
                {1,3,1,1,3,1,0,0,0},
                {1,3,1,1,3,1,0,0,0},
                {1,3,1,1,3,1,0,0,0},
                {1,3,1,1,3,1,0,0,0},
                {1,1,3,1,1,3,1,1,3},
                {1,1,3,1,1,3,1,1,3}
        };
        float[] porcentajes=new float[9];
        double[] puntuacionesMax=new double[]{34*numeroCursos,26*numeroCursos,19*numeroCursos,17*numeroCursos,20*numeroCursos,17*numeroCursos,6*numeroCursos,3*numeroCursos,10*numeroCursos};
        for(int i=0;i<matrizPuntos.length;i++){
            for(int j=0;j<porcentajes.length;j++){
                porcentajes[j]+= (float) (matrizPuntos[i][j] * puntos[i]) /puntuacionesMax[j];
            }
        }
        return porcentajes;
    }

    public String generarMatrizRolPerspectiva(float[] porcentajes){
        return "<table class=\"tg\"><tr><td class=\"tg-plgr\"></td><td class=\"tg-plgr\">Diseñador</td>"+
                "<td class=\"tg-plgr\">Facilitador</td><td class=\"tg-plgr\">Proveedor</td>"+
                "</tr><tr><td class=\"tg-plgr\">Pedagógica</td>"+generarCampoRelativo(porcentajes[0],1)+
                generarCampoRelativo(porcentajes[1],1)+generarCampoRelativo(porcentajes[2],1)+
                "</tr><tr><td class=\"tg-plgr\">Tecnológica</td>"+generarCampoRelativo(porcentajes[3],1)+
                generarCampoRelativo(porcentajes[4],1)+generarCampoRelativo(porcentajes[5],1)+
                "</tr><tr><td class=\"tg-plgr\">Estratégica</td>"+generarCampoRelativo(porcentajes[6],1)+
                generarCampoRelativo(porcentajes[7],1)+generarCampoRelativo(porcentajes[8],1)+
                "</tr></table>";
    }

}
