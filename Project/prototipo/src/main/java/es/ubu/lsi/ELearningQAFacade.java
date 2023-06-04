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
    private static final int CHECKS_DISENO =8;
    private static final int CHECKS_IMPLEMENTACION =5;
    private static final int CHECKS_REALIZACION =5;
    private static final int CHECKS_EVALUACION =2;
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

    // Método para obtener la lista de cuesitonarios de un curso
    public List<Quiz> getQuizzes(String token, long courseid) {
        return WebServiceClient.getQuizzes(courseid, config.getHost(), token);
    }

    public double[] realizarComprobaciones(String token, long courseid, AlertLog registro, List<QuizSummary> estadisticasCuestionarios, List<Quiz> quizzes,
                                            List<User> listaUsuarios, List<Post> listaPosts, List<Forum> listaForos) {
        Course curso= getCursoPorId(token, courseid);
        StatusList listaEstados=WebServiceClient.obtenerListaEstados(token, courseid, listaUsuarios, config.getHost());
        List<es.ubu.lsi.model.Module> listaModulos=WebServiceClient.obtenerListaModulos(token, courseid, config.getHost());
        List<Group> listaGrupos=WebServiceClient.obtenerListaGrupos(token, courseid, config.getHost(), registro);
        List<Assignment> listaTareas=WebServiceClient.obtenerListaTareas(token, courseid, config.getHost());
        List<Table> listaCalificadores=WebServiceClient.obtenerCalificadores(token, courseid, config.getHost());
        List<Resource> listaRecursos=WebServiceClient.obtenerRecursos(token, courseid, config.getHost());
        List<CourseModule> listaModulosTareas=WebServiceClient.obtenerModulosTareas(token, listaTareas, config.getHost());
        Map<Integer, Long> mapaFechasLimite=WebServiceClient.generarMapaFechasLimite(listaTareas);
        List<Assignment> tareasConNotas=WebServiceClient.obtenerTareasConNotas(token,mapaFechasLimite, config.getHost(), listaTareas);
        List<ResponseAnalysis> listaAnalisis=WebServiceClient.obtenerAnalisis(token, courseid, config.getHost());
        List<Survey> listaSurveys=WebServiceClient.obtenerSurveys(token, courseid, config.getHost());
        List<es.ubu.lsi.model.Module> modulosMalFechados=WebServiceClient.obtenerModulosMalFechados(curso, listaModulos);
        List<Resource> recursosDesfasados=WebServiceClient.obtenerRecursosDesfasados(curso, listaRecursos);

        // Calculamos los porcentajes
        double porcentaje;
        double sum = 0;
        if (estadisticasCuestionarios == null || estadisticasCuestionarios.isEmpty()) {
            porcentaje = 0;
        } else {
            for (QuizSummary quizSummary : estadisticasCuestionarios) {
                if (quizSummary.getTotalAlumnos() > 0 && quizSummary.getAlumnosExaminados() > 0)
                    sum += ((double)(quizSummary.getAlumnosExaminados()*100)/quizSummary.getTotalAlumnos());
            }
            porcentaje = sum / estadisticasCuestionarios.size();
        }

        double[] puntosComprobaciones = asignarPuntosComprobaciones(curso, listaEstados, listaModulos, listaGrupos, listaTareas, listaCalificadores,
                quizzes, listaForos, listaPosts, recursosDesfasados, modulosMalFechados, listaModulosTareas, listaUsuarios, tareasConNotas, listaAnalisis,
                listaSurveys, registro, porcentaje);

        return puntosComprobaciones;
    }

    public double[] asignarPuntosComprobaciones(Course curso, StatusList listaEstados, List<es.ubu.lsi.model.Module> listaModulos,
                                                List<Group> listaGrupos, List<Assignment> listaTareas, List<Table> listaCalificadores,
                                                List<Quiz> quizzes, List<Forum> listaForos, List<Post> listaPosts, List<Resource> recursosDesfasados,
                                                List<es.ubu.lsi.model.Module> modulosMalFechados, List<CourseModule> listaModulosTareas,
                                                List<User> listaUsuarios, List<Assignment> tareasConNotas, List<ResponseAnalysis> listaAnalisis,
                                                List<Survey> listaSurveys, AlertLog registro, double porcentaje){
        double[] puntosComprobaciones = new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if(isestaProgresoActivado(listaEstados, registro)){puntosComprobaciones[0]++;}
        if(isHayVariedadFormatos(listaModulos, registro)){puntosComprobaciones[1]++;}
        if(isTieneGrupos(listaGrupos, registro)){puntosComprobaciones[2]++;}
        if(isHayTareasGrupales(listaTareas, registro)){puntosComprobaciones[3]++;}
        if(isSonVisiblesCondiciones(curso, registro)){puntosComprobaciones[4]++;}
        if(isEsNotaMaxConsistente(listaCalificadores, registro)){puntosComprobaciones[5]++;}
        if(isHayCuestionarios(quizzes, registro)){puntosComprobaciones[6]++;}
        if(isHayForos(listaForos, registro)){puntosComprobaciones[7]++;}
        if(isEstanActualizadosRecursos(recursosDesfasados, registro)){puntosComprobaciones[8]++;}
        if(isSonFechasCorrectas(modulosMalFechados, registro)){puntosComprobaciones[9]++;}
        if(isMuestraCriterios(listaModulosTareas, registro)){puntosComprobaciones[10]++;}
        if(isAnidamientoCalificadorAceptable(listaCalificadores, registro)){puntosComprobaciones[11]++;}
        if(isAlumnosEnGrupos(listaUsuarios, registro)){puntosComprobaciones[12]++;}
        if(isRespondeATiempo(listaUsuarios,listaPosts, registro)){puntosComprobaciones[13]++;}
        if(isHayRetroalimentacion(listaCalificadores, registro)){puntosComprobaciones[14]++;}
        if(isEstaCorregidoATiempo(tareasConNotas,listaUsuarios, registro)){puntosComprobaciones[15]++;}
        if(isCalificadorMuestraPonderacion(listaCalificadores, registro)){puntosComprobaciones[16]++;}
        if(isRespondenFeedbacks(listaAnalisis,listaUsuarios, registro)){puntosComprobaciones[17]++;}
        if(isUsaSurveys(listaSurveys, registro)){puntosComprobaciones[18]++;}
        puntosComprobaciones[19] = porcentajeCuestionariosContestados(quizzes, porcentaje, registro);

        return puntosComprobaciones;
    }

    public String generarInformeFases(String token, double[] puntos, AlertLog registro, List<QuizSummary> estadisticasCuestionarios, List<User> listaUsuarios,
                                    List<Post> listaPosts, List<Forum> listaForos, int nroCursos) {
        double contadorDiseno=puntos[0]+puntos[1]+puntos[2]+puntos[3]+puntos[4]+puntos[5]+puntos[6]+puntos[7];
        double contadorImplementacion=puntos[8]+puntos[9]+puntos[10]+puntos[11]+puntos[12];
        double contadorRealizacion=puntos[13]+puntos[14]+puntos[15]+puntos[16]+puntos[19];
        double contadorEvaluacion=puntos[17]+puntos[18];
        double contadorTotal=contadorDiseno+contadorImplementacion+contadorRealizacion+contadorEvaluacion;
        StringBuilder tabla= new StringBuilder();
        tabla.append(camposInformeFases[0]);
        tabla.append(generarCampoRelativo((float)contadorTotal/nroCursos, CHECKS_TOTAL));
        tabla.append(camposInformeFases[1]);
        tabla.append(generarCampoRelativo((float)contadorDiseno/nroCursos, CHECKS_DISENO));
        tabla.append(generarFilas(new int[]{2, 0}, 8, puntos, nroCursos));
        tabla.append(camposInformeFases[10]);
        tabla.append(generarCampoRelativo((float)contadorImplementacion/nroCursos, CHECKS_IMPLEMENTACION));
        tabla.append(generarFilas(new int[]{11, 8}, 5, puntos, nroCursos));
        tabla.append(camposInformeFases[16]);
        tabla.append(generarCampoRelativo((float)contadorRealizacion/nroCursos, CHECKS_REALIZACION));
        tabla.append(generarFilas(new int[]{17, 13}, 4, puntos, nroCursos));
        
        // Porcentaje de cuestionarios realizados
        tabla = generarInformeCuestionario(tabla, puntos, estadisticasCuestionarios); 

        // Porcentaje de alumnos que participan en los foros
        tabla = generarInformeForos(token, tabla, listaForos, listaUsuarios, registro);

        // Evaluación
        tabla.append(camposInformeFases[21]+generarCampoRelativo((float)contadorEvaluacion/nroCursos, CHECKS_EVALUACION));
        tabla.append(generarFilas(new int[]{22, 17}, 2, puntos, nroCursos));

        tabla.append(camposInformeFases[24]);

        return tabla.toString();
    }

    public StringBuilder generarInformeCuestionario(StringBuilder tabla, double[] puntos, List<QuizSummary> estadisticasCuestionarios) {
        tabla.append("</tr><tr onclick=\"openInfo(event, 'estadisticquiz')\" data-bs-toggle=\"tooltip\" title=\"Se comprueba qué porcentaje de alumnos participa en los cuestionarios.\"> <td class=\"tg-ltgr\">Al menos un " + (int) (config.getMinQuizAnswerPercentage() * 100) + "% de los alumnos responden a los cuestionarios  <button onclick=\"toggleCuestionarios()\">Desplegar</button></td>");
        tabla.append(generarCampoRelativoCuestionario((float)puntos[19], 1));

        if(estadisticasCuestionarios != null && !estadisticasCuestionarios.isEmpty()){
            for (QuizSummary quizSummary : estadisticasCuestionarios) {
                float porcentaje = 0;
                if (quizSummary.getTotalAlumnos() > 0 && quizSummary.getAlumnosExaminados() > 0)
                    porcentaje = ((float)(quizSummary.getAlumnosExaminados()*100)/quizSummary.getTotalAlumnos())/100;

                tabla.append("</tr><tr class=\"toggle-cuestionarios\" data-bs-toggle=\"tooltip\"> <td class=\"tg-ltgr\"onclick=\"muestraCuestionario(" + quizSummary.getId() + ")\">Cuestionario " + quizSummary.getNombreCuestionario() + " </td>" + generarCampoRelativoCuestionario(porcentaje, 1));
            }
        }

        return tabla;
    }

    public StringBuilder generarInformeForos(String token, StringBuilder tabla, List<Forum> listaForos, List<User> listaUsuarios, AlertLog registro) {
        List<EstadisticasForo> foros = porcentajeAlumnosForos(token, listaForos, listaUsuarios, registro);
        double porcentaje = 0;
        if (foros != null && !foros.isEmpty()) {
            for (EstadisticasForo estadisticasForo : foros) {
                porcentaje += estadisticasForo.getPorcentajeParticipacion();
            }
            porcentaje = porcentaje / foros.size();
        }
        tabla.append("</tr><tr onclick=\"openInfo(event, 'estadisticforum')\" data-bs-toggle=\"tooltip\" title=\"Se comprueba qué porcentaje de alumnos participa en los foros.\"> <td class=\"tg-ltgr\">Al menos un " + (int) (config.getMinQuizAnswerPercentage() * 100) + "% de los alumnos participa en los foros  <button onclick=\"toggleForos()\">Desplegar</button></td>");
        tabla.append(generarCampoRelativoCuestionario((float)porcentaje/100, 1));
        // Porcentaje de alumnos que participa en cada foro
        if (foros != null && !foros.isEmpty()) {
            for (EstadisticasForo estadisticasForo : foros) {
                tabla.append("</tr><tr class=\"toggle-foros\" data-bs-toggle=\"tooltip\"> <td class=\"tg-ltgr\">Foro " + estadisticasForo.getNombre() + " </td>" + generarCampoRelativoCuestionario((float)estadisticasForo.getPorcentajeParticipacion()/100, 1));
            }
        }

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

    public List<QuizSummary> generarListaCuestionarios(String token, long courseid, List<Quiz> quizzes) {
        ArrayList<QuizSummary> listaCuestionarios = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            if (quiz.isVisible()) {
                QuizSummary quizSummary = WebServiceClient.obtenerResumenCuestionario(token, courseid, config.getHost(), quiz);
                if (quizSummary != null) {
                    listaCuestionarios.add(quizSummary);
                }
            }
        }
        return listaCuestionarios;
    }

    // Obtener la lista de usuarios de un curso
    public List<User> getListaUsuarios(String token, long courseid) {
        return WebServiceClient.obtenerUsuarios(token, courseid, config.getHost());
    }

    // Obtener la lista de posts de un curso
    public List<Post> getListaPosts(String token, long courseid, List<Forum> listaForos) {
        return WebServiceClient.obtenerListaPosts(token, courseid, config.getHost(), listaForos);
    }

    public Map<Integer, String> generarInformesCuestionarios(String token, List<QuizSummary> quizzes) {
        Map<Integer, String> informes = new HashMap<>();
        
        // Por cada cuestionario, generamos su informe
        for (QuizSummary quiz : quizzes) {
            String informe = generarInformeCuestionario(quiz);
            // Creamos una relación entre el id del cuestionario y su informe
            informes.put(quiz.getId(), informe);
        }

        return informes;
    }

    public String generarInformeCuestionario(QuizSummary quizSummary) {
        String informe = "";

        if (quizSummary != null) {
            informe += "<div class=\"cuestionario\" id=\"" + quizSummary.getId() + "\">";
            informe += "<h1>Cuestionario " + quizSummary.getId() + " - " + quizSummary.getNombreCuestionario() + "</h1>";
            informe += "<p>Número de alumnos: " + quizSummary.getTotalAlumnos() + "</p>";
            informe += "<p>Número de alumnos examinados: " + quizSummary.getAlumnosExaminados() + "</p>";
            informe += "<p>Total de intentos: " + quizSummary.getTotalIntentos() + "</p>";
            informe += "<p>Número de preguntas: " + quizSummary.getTotalPreguntas() + "</p>";
            informe += "<p>Nota máxima: " + ((int)(quizSummary.getNotaMaxima() * 100)) / 100.00 + "</p>";
            informe += "<p>Nota media del mejor intento (Solo alumnos que han realizado el examen): " + ((int)(quizSummary.getNotaMediaMejorIntentoAlumnosConNota() * 100)) / 100.00 + "</p>";
            informe += "<p>Nota media del mejor intento (Todos los alumnos): " + ((int)(quizSummary.getNotaMediaMejorIntentoTotalAlumnos() * 100)) / 100.00 + "</p>";
            informe += "<p>Nota media del último intento (Solo alumnos que han realizado el examen): " + ((int)(quizSummary.getNotaMediaUltimoIntentoAlumnosConNota() * 100)) / 100.00 + "</p>";
            informe += "<p>Nota media del último intento (Todos los alumnos): " + ((int)(quizSummary.getNotaMediaUltimoIntentoTotalAlumnos() * 100)) / 100.00 + "</p>";
            if (quizSummary.getAlumnosExaminados() > 3) {
                informe += "<p>Skewness (para las mejores calificaciones): " + ((int)(quizSummary.getSkewness() * 100)) / 100.00 + "</p>";
                informe += "<p>Kurtosis (para las mejores calificaciones): " + ((int)(quizSummary.getKurtosis() * 100)) / 100.00 + "</p>";
            }
            informe += "</div>";

        }
        return informe;
    }

    // Método para obtener la relación entre el id de un cuestionario y las preguntas que lo componen
    public Map<Integer, int[]> generarGraficoPreguntas(String token, List<Quiz> quizzes) {
        List<EstadisticaNotasPregunta> estadisticas;
        Map<Integer, int[]> informes = new HashMap<>();

        for (Quiz quiz : quizzes) {
            if (quiz.isVisible()) {
                estadisticas = WebServiceClient.obtenerEstadisticasNotasPregunta(config.getHost(), token, quiz);
                int[] idPreguntas = new int[estadisticas.size()];
                int i = 0;
                for (EstadisticaNotasPregunta estadistica : estadisticas) {
                    idPreguntas[i] = estadistica.getIdPregunta();
                    i++;
                }
                informes.put(quiz.getId(), idPreguntas);
            }
        }

        return informes;
    }
    
    // Método para obtener la relación entre el id de un cuestionario y las notas medias por pregunta
    public Map<Integer, double[]> generarGraficoNotas(String token, List<Quiz> quizzes) {
        List<EstadisticaNotasPregunta> estadisticas;
        Map<Integer, double[]> informes = new HashMap<>();
        for (Quiz quiz : quizzes) {
            if (quiz.isVisible()) {
                estadisticas = WebServiceClient.obtenerEstadisticasNotasPregunta(config.getHost(), token, quiz);
                double[] notaMediaPreguntas = new double[estadisticas.size()];
                int i = 0;
                for (EstadisticaNotasPregunta estadistica : estadisticas) {
                    notaMediaPreguntas[i] = estadistica.getNotaMediaPregunta() * 100;
                    i++;
                }
                informes.put(quiz.getId(), notaMediaPreguntas);
            }
        }

        return informes;
    }

    public List<Forum> getListaForos(String token, long courseid) {
        return WebServiceClient.obtenerListaForos(token, courseid, config.getHost());
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

    // Devuelve true si hay algún foro, sino se devuelve false y se añade un registro de alerta
    public boolean isHayForos(List<Forum> listaForos, AlertLog registro) {
        return WebServiceClient.hayForos(listaForos, registro);
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

    // Devuelve el porcentaje de alumnos que participan en los foros, sino se devuelve 0 y se añade un registro de alerta
    public List<EstadisticasForo> porcentajeAlumnosForos(String token, List<Forum> listaForos, List<User> alumnos, AlertLog registro) {
        return WebServiceClient.calculaPorcentajeAlumnosForos(token, listaForos, alumnos, registro, config);
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

    public String generarCampoRelativoCuestionario(float numerador, float denominador){
        float resultado= numerador/denominador;
        String campoAMedias="<meter value=\""+numerador+"\" min=\"0\" max=\""+denominador+"\"></meter>"+
                String.format("%.1f",porcentajeFraccion(numerador, denominador))+"%"+"</td>";
        if (resultado<config.getMinQuizAnswerPercentage() * 100){return "<td class=\"tg-pred\">"+campoAMedias;}
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
