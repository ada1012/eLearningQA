package es.ubu.lsi;

import es.ubu.lsi.model.*;
import es.ubu.lsi.model.Date;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebServiceClient {


    private static final String COURSEID = "&courseid=";
    private static final String COURSEIDS_0 = "&courseids[0]=";
    private static final String MENOS_DE_UN = "Menos de un ";


    private WebServiceClient() {
        throw new IllegalStateException("Utility class");
    }

    public static String login(String username, String password, String host){
        RestTemplate restTemplate = new RestTemplate();
        String token=restTemplate.getForObject(host + "/login/token.php?username=" +username+"&password="+password+"&service=moodle_mobile_app", String.class);
        if (token==null){return "";}
        return token.split("\"", 0)[3];
    }

    public static List<User> obtenerUsuarios(String token, long courseid, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=core_enrol_get_enrolled_users&moodlewsrestformat=json&wstoken=" +token+ COURSEID +courseid;
        ArrayList<User> users = new ArrayList<>();
        User[] userArray=restTemplate.getForObject(url, User[].class);
        if(userArray!=null){
            users = new ArrayList<>(Arrays.asList(userArray));
        }
        return users;
    }

    public static String obtenerNombreCompleto(String token, String username, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=core_user_get_users_by_field&moodlewsrestformat=json&wstoken="+token+"&field=username&values[0]="+username;
        User[] usuarios=restTemplate.getForObject(url, User[].class);
        if (usuarios==null||usuarios[0]==null){return "John Doe";}
        return usuarios[0].getFullname();
    }

    public static boolean esProfesor(List<User> usuarios, long userid){
        for (User usuario:usuarios) {
            if(usuario.getId()==userid){
                for (Role rol: usuario.getRoles()) {
                    if(rol.getRoleid()==3||rol.getRoleid()==4){return true;}
                }
            }
        }
        return false;
    }

    public static boolean esAlumno(List<User> usuarios, long userid){
        for (User usuario:usuarios) {
            if(usuario.getId()==userid){
                for (Role rol: usuario.getRoles()) {
                    if(rol.getRoleid()==5){return true;}
                }
            }
        }
        return false;
    }

    public static List<Course> obtenerCursos(String token, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=core_course_get_enrolled_courses_by_timeline_classification&moodlewsrestformat=json&wstoken="+token+"&classification=all";
        CourseList listacursos=restTemplate.getForObject(url,CourseList.class);
        if (listacursos==null){return new ArrayList<>();}
        return listacursos.getCourses();
    }

    public static Course obtenerCursoPorId(String token, long courseid, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=core_course_get_courses_by_field&moodlewsrestformat=json&wstoken=" +token+"&field=id&value="+courseid;
        CourseList listacursos=restTemplate.getForObject(url,CourseList.class);
        if (listacursos==null){return new Course();}
        return listacursos.getCourses().get(0);
    }

    public static boolean tieneGrupos(List<Group> listaGrupos, AlertLog registro){
        if(listaGrupos.isEmpty()){
            registro.guardarAlerta("design hasgroups","El curso no tiene grupos");
            return false;
        }else{
            return true;
        }
    }

    public static List<Group> obtenerListaGrupos(String token, long courseid, String host, AlertLog registro) {
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=core_group_get_course_groups&moodlewsrestformat=json&wstoken=" + token + COURSEID + courseid;
        Group[] listaGrupos;
        try {
            listaGrupos= restTemplate.getForObject(url, Group[].class);
        } catch (Exception e) {
            registro.guardarAlerta("design hasgroups","No tienes permisos para ver los grupos, contacta al administrador si no debería ser así");
            listaGrupos=null;
        }
        if (listaGrupos==null){return new ArrayList<>();}
        return new ArrayList<>(Arrays.asList(listaGrupos));
    }

    public static boolean sonVisiblesCondiciones(Course curso, AlertLog registro){
        if(curso.getShowcompletionconditions()!=null){
            return curso.getShowcompletionconditions();
        }
        registro.guardarAlerta("design conditions","Las condiciones para completar las actividades no son visibles");
        return false;
    }

    public static StatusList obtenerListaEstados(String token, long courseid, List<User> listaUsuarios, String host) {
        RestTemplate restTemplate = new RestTemplate();
        if (listaUsuarios == null || listaUsuarios.isEmpty()){return new StatusList();}
        int idProfesor=listaUsuarios.get(0).getId();
        for (User usuario:listaUsuarios) {
            if(esProfesor(listaUsuarios,usuario.getId())){
                idProfesor=usuario.getId();
            }
        }
        String url = host + "/webservice/rest/server.php?wsfunction=core_completion_get_activities_completion_status&moodlewsrestformat=json&wstoken=" + token + COURSEID + courseid +"&userid="+idProfesor;
        StatusList listaEstados=restTemplate.getForObject(url,StatusList.class);
        if (listaEstados==null){return new StatusList();}
        return listaEstados;
    }

    public static boolean estaProgresoActivado(StatusList listaEstados, AlertLog registro) {
        List<Status> estados=listaEstados.getStatuses();
        if(estados==null||estados.isEmpty()){
            registro.guardarAlerta("design hasprogress","No hay actividades con las que medir el progreso");
            return false;
        }
        if(estados.get(0).isHascompletion()){
            return true;
        }else{
            registro.guardarAlerta("design hasprogress","Las opciones de progreso del estudiante están desactivadas");
            return false;
        }
    }

    public static boolean estaCorregidoATiempo(List<Assignment> tareasConNotas, List<User> listaUsuarios, AlertLog registro, FacadeConfig config) {
        StringBuilder detalles = new StringBuilder();
    
        if (tareasConNotas.isEmpty()) {
            registro.guardarAlerta("realization assignmentsgraded", "El curso no tiene tareas");
            return false;
        }
    
        for (Assignment tarea : tareasConNotas) {
            List<Grade> notas = tarea.getGrades();
    
            if (notas == null) {
                notas = new ArrayList<>();
            }
    
            for (Grade nota : notas) {
                if (Objects.equals(nota.getGradeValue(), "")) {
                    nota.setGradeValue("-1.00000");
                }

                generarDetalles(tarea, nota, config, detalles, listaUsuarios);
            }
        }
    
        if (detalles.length() > 0) {
            registro.guardarAlertaDesplegable("realization assignmentsgraded",
                    "Hay entregas sin corregir", "Entregas sin corregir <a href=\"" + config.getHost() + "/mod/assign/index.php?id=" + registro.getCourseid() + "\">(tareas)</a>", detalles.toString());
            return false;
        }
    
        return true;
    }

    public static StringBuilder generarDetalles(Assignment tarea, Grade nota, FacadeConfig config, StringBuilder detalles, List<User> listaUsuarios) {
        if (tieneRelevancia(tarea.getDuedate(), nota.getTimemodified(), config.getAssignmentGradingTime(), config.getAssignmentRelevancePeriod())
                || (System.currentTimeMillis() / 1000L) - tarea.getDuedate() > config.getAssignmentGradingTime()
                && Float.parseFloat(nota.getGradeValue()) < 0) {
            detalles.append("La entrega en ").append(tarea.getName()).append(" por ")
                    .append((obtenerUsuarioPorId(listaUsuarios, nota.getUserid()).getFullname() != null
                            ? obtenerUsuarioPorId(listaUsuarios, nota.getUserid()).getFullname()
                            : "Alumno desmatriculado"))
                    .append("<br>");
        }

        return detalles;
    }



    public static User obtenerUsuarioPorId(List<User> listaUsuarios, long id){
        for (User usuario:listaUsuarios) {
            if(usuario.getId()==id){
                return usuario;
            }
        }
        return new User();
    }

    public static List<Assignment> obtenerTareasConNotas(String token, Map<Integer, Long> mapaFechasLimite, String host, List<Assignment> listaTareas) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder url = new StringBuilder(host + "/webservice/rest/server.php?wsfunction=mod_assign_get_grades&moodlewsrestformat=json&wstoken=" + token);
        int contador=0;
        if (mapaFechasLimite.isEmpty()){return new ArrayList<>();}
        for (Integer id: mapaFechasLimite.keySet()) {
            url.append("&assignmentids[").append(contador++).append("]=").append(id);
        }
        AssignmentList tareasConNotas= restTemplate.getForObject(url.toString(),AssignmentList.class);
        if (tareasConNotas==null){return new ArrayList<>();}
        for (Assignment tarea: tareasConNotas.getAssignments()) {
            for (Assignment tareaConDatos:listaTareas) {
                if (tarea.getId()==tareaConDatos.getId()){
                    tareaConDatos.setGrades(tarea.getGrades());
                }
            }
        }
        return listaTareas;
    }

    public static Map<Integer, Long> generarMapaFechasLimite(List<Assignment> listaTareas) {
        Map<Integer, Long> mapaFechasLimite= new HashMap<>();
        for (Assignment tarea: listaTareas) {
            if(tarea.getDuedate()!=0){
                mapaFechasLimite.put(tarea.getId(),tarea.getDuedate());
            }
        }
        return mapaFechasLimite;
    }

    public static List<Assignment> obtenerListaTareas(String token, long courseid, String host) {
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=mod_assign_get_assignments&moodlewsrestformat=json&wstoken=" + token;
        CourseList listaCursos= restTemplate.getForObject(url,CourseList.class);
        List<Assignment> listaTareas= new ArrayList<>();
        if(listaCursos==null){return listaTareas;}
        for (Course curso:listaCursos.getCourses()) {
            if(curso.getId()== courseid) {
                listaTareas.addAll(curso.getAssignments());
            }
        }
        return listaTareas;
    }

    public static boolean respondeATiempo(List<User> listaUsuarios, List<Post> listaPostsCompleta, AlertLog registro, FacadeConfig config){
        StringBuilder detalles=new StringBuilder();
        Map<Integer,Post> dudasAlumnosSinRespuesta= new HashMap<>();
        List<Post> listaRespuestasProfesores= new ArrayList<>();
        for (Post comentario:listaPostsCompleta) {
            if (esAlumno(listaUsuarios,comentario.getAuthor().getId())&&comentario.getMessage().contains("?")){
                dudasAlumnosSinRespuesta.put(comentario.getId(),comentario);
            }
            if (esProfesor(listaUsuarios,comentario.getAuthor().getId())&&comentario.isHasparent()){
                listaRespuestasProfesores.add(comentario);
            }
        }
        for (Post respuestaProfe:listaRespuestasProfesores) {
            if(dudasAlumnosSinRespuesta.containsKey(respuestaProfe.getParentid())&&
                    !tieneRelevancia(dudasAlumnosSinRespuesta.get(respuestaProfe.getParentid()).getTimecreated(),
                            respuestaProfe.getTimecreated(),
                            config.getForumResponseTime(), config.getForumRelevancePeriod())){
                dudasAlumnosSinRespuesta.remove(respuestaProfe.getParentid());
            }
        }
        for (Post duda:dudasAlumnosSinRespuesta.values()) {
            detalles.append(duda.getSubject()+" por "+duda.getAuthor().getFullname()).append("<br>");
        }
        if (!dudasAlumnosSinRespuesta.isEmpty()){
            registro.guardarAlertaDesplegable("realization answersforums", "Hay dudas sin responder",
                    "Dudas sin responder", detalles.toString());
            return false;
        }
        return true;
    }

    public static boolean tieneRelevancia(long fechaOrigen, long fechaSolucion, int tiempoLimite, int tiempoRelevancia){
        return fechaSolucion-fechaOrigen>tiempoLimite&&fechaOrigen+tiempoRelevancia>Instant.now().getEpochSecond();
    }

    public static List<Post> obtenerListaPosts(String token, String host, List<Forum> listaForos) {
        List<Discussion> listaDebates;
        List<Discussion> listaDebatesCompleta= new ArrayList<>();
        for (Forum foro:listaForos) {
            listaDebates = obtenerListaDebates(token, foro, host);
            listaDebatesCompleta.addAll(listaDebates);
        }
        List<Post> listaPostsDebate;
        List<Post> listaPostsCompleta= new ArrayList<>();
        for (Discussion debate: listaDebatesCompleta) {
            listaPostsDebate = obtenerListaPostsPorDebate(token, debate, host);
            listaPostsCompleta.addAll(listaPostsDebate);
        }
        return listaPostsCompleta;
    }

    public static List<Post> obtenerListaPostsPorDebate(String token, Discussion debate, String host) {
        RestTemplate restTemplate = new RestTemplate();
        PostList listaPostsDebate;
        String url= host + "/webservice/rest/server.php?wsfunction=mod_forum_get_discussion_posts&moodlewsrestformat=json&wstoken=" + token +"&discussionid="+debate.getDiscussionNumber();
        listaPostsDebate=restTemplate.getForObject(url,PostList.class);
        if (listaPostsDebate==null){return new ArrayList<>();}
        return listaPostsDebate.getPosts();
    }

    public static List<Discussion> obtenerListaDebates(String token, Forum foro, String host) {
        RestTemplate restTemplate = new RestTemplate();
        DiscussionList listaDebates;
        String url= host + "/webservice/rest/server.php?wsfunction=mod_forum_get_forum_discussions&moodlewsrestformat=json&wstoken=" + token +"&forumid="+foro.getId();
        listaDebates= restTemplate.getForObject(url,DiscussionList.class);
        if (listaDebates==null){return new ArrayList<>();}
        return listaDebates.getDiscussions();
    }

    public static List<Forum> obtenerListaForos(String token, long courseid, String host) {
        RestTemplate restTemplate = new RestTemplate();
        String url= host + "/webservice/rest/server.php?wsfunction=mod_forum_get_forums_by_courses&moodlewsrestformat=json&wstoken=" + token + COURSEIDS_0 +courseid;
        Forum[] arrayForos= restTemplate.getForObject(url, Forum[].class);

        if (arrayForos==null){return new ArrayList<>();}
        List<Forum> listaForos= new ArrayList<>(Arrays.asList(arrayForos));
        // Eliminamos los foros que no son de tipo general
        listaForos.removeIf(foro -> foro.getType().equals("news"));

        return listaForos;
    }

    public static boolean usaSurveys(List<Survey> listaEncuestas, AlertLog registro){
        if(listaEncuestas.isEmpty()){
            registro.guardarAlerta("assessment survey","No se utilizan las encuestas de opinión");
            return false;
        }else{
            return true;
        }
    }

    public static List<Survey> obtenerSurveys(String token, long courseid, String host) {
        RestTemplate restTemplate = new RestTemplate();
        String url= host + "/webservice/rest/server.php?wsfunction=mod_survey_get_surveys_by_courses&moodlewsrestformat=json&wstoken=" + token + COURSEIDS_0 + courseid;
        SurveyList listaEncuestas = restTemplate.getForObject(url, SurveyList.class);
        if (listaEncuestas==null){return new ArrayList<>();}
        return listaEncuestas.getSurveys();
    }

    public static boolean alumnosEnGrupos(List<User> listaUsuarios, AlertLog registro){
        StringBuilder detalles=new StringBuilder();
        List<User> listaUsuariosHuerfanos = obtenerAlumnosSinGrupo(listaUsuarios);
        if(listaUsuariosHuerfanos.isEmpty()){
            return true;
        }else{
            for (User alumno:listaUsuariosHuerfanos) {
                detalles.append(alumno.getFullname()).append("<br>");
            }
            registro.guardarAlertaDesplegable("implementation studentsingroups", "Hay alumnos sin grupo en el curso", "Alumnos sin grupo", detalles.toString());
            return false;
        }
    }

    public static List<User> obtenerAlumnosSinGrupo(List<User> listaUsuarios) {
        List<User> listaUsuariosHuerfanos=new ArrayList<>();
        for (User usuario:listaUsuarios) {
            if (usuario.getGroups()!=null && usuario.getGroups().isEmpty() && esAlumno(listaUsuarios,usuario.getId())){
                listaUsuariosHuerfanos.add(usuario);
            }
        }
        return listaUsuariosHuerfanos;
    }


    public static List<Table> obtenerCalificadores(String token, long courseid, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url= host + "/webservice/rest/server.php?wsfunction=gradereport_user_get_grades_table&moodlewsrestformat=json&wstoken=" + token + COURSEID +courseid;
        TableList listaCalificadores = restTemplate.getForObject(url, TableList.class);
        if (listaCalificadores==null){return new ArrayList<>();}
        return listaCalificadores.getTables();
    }

    public static boolean anidamientoCalificadorAceptable(List<Table> listaCalificadores, AlertLog registro, FacadeConfig config){
        if (listaCalificadores == null || listaCalificadores.isEmpty()){
            registro.guardarAlerta("implementation nesting","No hay calificadores");
            return false;
        }
        if(listaCalificadores.get(0).getMaxdepth()< config.getExcessiveNesting()){
            return true;
        }else{
            registro.guardarAlerta("implementation nesting","Un anidamiento de "+listaCalificadores.get(0).getMaxdepth()+" es excesivo, manténgalo por debajo de "+config.getExcessiveNesting());
            return false;
        }
    }

    public static boolean calificadorMuestraPonderacion(List<Table> listaCalificadores, AlertLog registro){
        if (listaCalificadores == null || listaCalificadores.isEmpty()){
            registro.guardarAlerta("realization weightsshown","El calificador no muestra los pesos de los elementos calificables");
            return false;
        }
        for (Tabledata tabledata:listaCalificadores.get(0).getTabledata()) {
            if (tabledata.getWeight()!=null){
                return true;
            }
        }
        return false;
    }

    public static boolean hayRetroalimentacion(List<Table> listaCalificadores, AlertLog registro, FacadeConfig config) {
        int contadorRetroalimentacion = 0;
        int contadorTuplasComentables = 0;
        GradeTableField feedback;
        final String category = "realization assignmentfeedback";
    
        if (listaCalificadores == null || listaCalificadores.isEmpty()) {
            registro.guardarAlerta(category, "No hay calificadores que comprobar");
            return false;
        }
    
        for (Table calificador : listaCalificadores) {
            for (Tabledata tabledata : calificador.getTabledata()) {
                if (tabledata.getItemname() != null && tabledata.getItemname().getMyclass().contains("item ") &&
                        tabledata.getGrade() != null && !tabledata.getGrade().getContent().matches("[- ]")) {
                    contadorTuplasComentables++;
                    feedback = tabledata.getFeedback();
                    if (feedback != null && feedback.getContent().length() > 6) {
                        contadorRetroalimentacion++;
                    }
                }
            }
        }
    
        if (contadorTuplasComentables == 0) {
            registro.guardarAlerta(category, "No hay actividades que comentar");
            return false;
        }
    
        float percentage = (float) contadorRetroalimentacion / (float) contadorTuplasComentables;
        if (percentage > config.getMinCommentPercentage()) {
            return true;
        } else {
            registro.guardarAlerta(category, "No se hacen suficientes comentarios a las entregas de los alumnos");
            return false;
        }
    }

    public static boolean esNotaMaxConsistente(List<Table> listaCalificadores, AlertLog registro){
        String rango="";
        if (listaCalificadores == null || listaCalificadores.isEmpty()){
            registro.guardarAlerta("design consistentmaxgrade","Los calificadores están vacíos");
            return false;
        }
        for (Tabledata tabledata:listaCalificadores.get(0).getTabledata()) {
            if (tabledata.getRange()!=null){
                if (rango.equals("")){
                    rango=tabledata.getRange().getContent();
                }
                if (tabledata.getRange().getContent() != null && !rango.equals(tabledata.getRange().getContent())){
                    registro.guardarAlerta("design consistentmaxgrade","Las calificaciones máximas de las actividades y categorías son inconsistentes");
                    return false;
                }
            }
        }
        return true;
    }

    // Método que comprueba si el curso tiene cuestionarios
    // En este método no se comprueba ningún dato más del cuestionario
    public static boolean hayCuestionarios(List<Quiz> quizzes, AlertLog registro){
        if (quizzes != null && quizzes.isEmpty()){
            registro.guardarAlerta("design consistentminquiz","No hay cuestionarios en el curso");
            return false;
        }
        return true;
    }

    // Método que comprueba si el curso tiene al menos un foro
    // En este método no se comprueba ningún dato más del foro
    public static boolean hayForos(List<Forum> listaForos, AlertLog registro){
        if (listaForos != null && listaForos.isEmpty()){
            registro.guardarAlerta("design consistentminforum","No hay foros en el curso");
            return false;
        }
        return true;
    }

    public static List<Resource> obtenerRecursos(String token, long courseid, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=mod_resource_get_resources_by_courses&moodlewsrestformat=json&wstoken=" +token+ COURSEIDS_0 +courseid;
        ResourceList listaRecursos=restTemplate.getForObject(url, ResourceList.class);
        if (listaRecursos==null){return new ArrayList<>();}
        return listaRecursos.getResources();
    }

    public static boolean estanActualizadosRecursos(List<Resource> listaRecursosDesfasados, AlertLog registro, FacadeConfig config){
        StringBuilder detalles= new StringBuilder();
        List<Contentfile> archivos;
        if(listaRecursosDesfasados.isEmpty()){
            return true;
        }else{
            for (Resource recurso:listaRecursosDesfasados) {
                archivos=recurso.getContentfiles();
                for (Contentfile archivo:archivos) {
                    detalles.append(archivo.getFilename()).append(recurso.isVisible() ? "" : " <img src=\"eye-slash.png\" width=\"16\" height=\"16\" alt=\"No visible\"></td>").append("<br>");
                }
            }
            registro.guardarAlertaDesplegable("implementation resourcesuptodate", "El curso contiene archivos desfasados", "Archivos desfasados <a href=\""+config.getHost()+"/course/resources.php?id="+registro.getCourseid()+"\">(recursos)</a>", detalles.toString());
            return false;
        }
    }

    public static List<Resource> obtenerRecursosDesfasados(Course curso, List<Resource> listaRecursos) {
        List<Resource> listaRecursosDesfasados=new ArrayList<>();
        long fechaUmbral=curso.getStartdate()-15780000;
        for (Resource recurso:listaRecursos) {
            for (Contentfile archivo: recurso.getContentfiles()) {
                if(archivo.getTimemodified()<fechaUmbral){
                    listaRecursosDesfasados.add(recurso);
                }
            }
        }
        return listaRecursosDesfasados;
    }

    public static List<es.ubu.lsi.model.Module> obtenerListaModulos(String token, long courseid, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=core_course_get_contents&moodlewsrestformat=json&wstoken=" +token+ COURSEID +courseid;
        Section[] arraySecciones=restTemplate.getForObject(url, Section[].class);
        List<Section> listaSecciones;
        if (arraySecciones==null){
            listaSecciones=new ArrayList<>();
        }else{
            listaSecciones= new ArrayList<>(Arrays.asList(arraySecciones));
        }
        List<es.ubu.lsi.model.Module> listaModulos=new ArrayList<>();
        for (Section seccion:listaSecciones) {
            listaModulos.addAll(seccion.getModules());
        }
        return listaModulos;
    }

    public static boolean sonFechasCorrectas(List<es.ubu.lsi.model.Module> listaModulosMalFechados, AlertLog registro){
        StringBuilder detalles= new StringBuilder();
        if(listaModulosMalFechados.isEmpty()){
            return true;
        }else{
            for (es.ubu.lsi.model.Module modulo:listaModulosMalFechados) {
                detalles.append("<a href=\"").append(modulo.getUrl()).append("\">")
                        .append(modulo.getName())
                        .append(modulo.getVisible()==1?"":" <img src=\"eye-slash.png\" width=\"16\" height=\"16\" alt=\"No visible\"></td>")
                        .append("</a><br>");
            }
            registro.guardarAlertaDesplegable("implementation correctdates", "Hay módulos con fechas incorrectas", "Módulos mal fechados", detalles.toString());
            return false;
        }
    }

    public static List<es.ubu.lsi.model.Module> obtenerModulosMalFechados(Course curso, List<es.ubu.lsi.model.Module> listaModulos) {
        long startdate=curso.getStartdate();
        long enddate=curso.getEnddate();
        List<Date> dates;
        List<es.ubu.lsi.model.Module> listaModulosMalFechados=new ArrayList<>();
        long opendate;
        long duedate;
        if (listaModulos==null){
            return listaModulosMalFechados;
        }
        for (es.ubu.lsi.model.Module modulo:listaModulos) {
            opendate=0;
            duedate=0;
            dates=modulo.getDates();
            for (Date fecha:dates) {
                if (fecha.getLabel().contains("pened")){
                    opendate=fecha.getTimestamp();
                }else{
                    duedate=fecha.getTimestamp();
                }
            }
            if(!comprobarFechas(startdate, enddate, opendate, duedate)){
                listaModulosMalFechados.add(modulo);
            }
        }
        return listaModulosMalFechados;
    }

    public static boolean comprobarFechas(long startdate, long enddate, long opendate, long duedate){
        return (opendate==0||startdate<opendate&&(opendate<enddate||enddate==0))&&
                (duedate==0||startdate<duedate&&(duedate<enddate||enddate==0&&duedate>opendate));
    }

    public static int numeroAlumnos(List<User> usuarios){
        int contadorAlumnos=0;
        for (User usuario:usuarios) {
            for (Role rol: usuario.getRoles()) {
                if (rol.getRoleid() == 5) {
                    contadorAlumnos++;
                }
            }
        }
        return contadorAlumnos;
    }

    public static List<Feedback> obtenerFeedbacks(String token, long courseid, String host){
        RestTemplate restTemplate = new RestTemplate();
        String url= host + "/webservice/rest/server.php?wsfunction=mod_feedback_get_feedbacks_by_courses&moodlewsrestformat=json&wstoken=" +token+ COURSEIDS_0 +courseid;
        FeedbackList listaFeedbacks= restTemplate.getForObject(url, FeedbackList.class);
        if (listaFeedbacks==null){return new ArrayList<>();}
        return listaFeedbacks.getFeedbacks();
    }


    public static List<ResponseAnalysis> obtenerAnalisis(String token, long courseid, String host){
        List<Feedback> listaFeedbacks=obtenerFeedbacks(token, courseid, host);
        RestTemplate restTemplate = new RestTemplate();
        List<ResponseAnalysis> listaAnalisis= new ArrayList<>();
        String url= host + "/webservice/rest/server.php?wsfunction=mod_feedback_get_responses_analysis&moodlewsrestformat=json&wstoken=" +token+"&feedbackid=";
        for (Feedback feedback:listaFeedbacks) {
            listaAnalisis.add(restTemplate.getForObject(url+feedback.getId(),ResponseAnalysis.class));
        }
        return listaAnalisis;
    }





    public static boolean respondenFeedbacks(List<ResponseAnalysis> listaAnalisis, List<User> usuarios, AlertLog registro, FacadeConfig config){
        int nAlumnos=numeroAlumnos(usuarios);
        if(nAlumnos==0){return false;}
        for (ResponseAnalysis analisis:listaAnalisis) {
            if ((float)(analisis.getTotalattempts()+analisis.getTotalanonattempts())/(float) nAlumnos< config.getMinFeedbackAnswerPercentage()){
                registro.guardarAlerta("assessment mostrespond",
                        "La mayoría de alumnos no ha respondido algunas encuestas.");
                return false;
            }
        }
        return true;
    }

    public static boolean hayTareasGrupales(List<Assignment> listaTareas, AlertLog registro){
        for (Assignment tarea:listaTareas) {
            if(tarea.getTeamsubmission()==1){
                return true;
            }
        }
        registro.guardarAlerta("design groupactivities","El curso no contiene actividades grupales");
        return false;
    }



    public static List<CourseModule> obtenerModulosTareas(String token, List<Assignment> listaTareas, String host){
        RestTemplate restTemplate = new RestTemplate();
        List<CourseModule> listaModulosTareas=new ArrayList<>();
        CourseModuleWrapper wrapper;
        String url;
        for (Assignment tarea:listaTareas) {
            url = host + "/webservice/rest/server.php?wsfunction=core_course_get_course_module&moodlewsrestformat=json&wstoken=" +token+"&cmid="+tarea.getCmid();
            wrapper=restTemplate.getForObject(url,CourseModuleWrapper.class);
            if (wrapper!=null){
                listaModulosTareas.add(wrapper.getCm());
            }
        }
        return listaModulosTareas;
    }

    public static boolean muestraCriterios(List<CourseModule> listaModulosTareas, AlertLog registro){
        if (listaModulosTareas != null) {
            for (CourseModule modulo:listaModulosTareas) {
                if (modulo != null && modulo.getAdvancedgrading() != null) {
                    for (Advancedgrading metodoAvanzado: modulo.getAdvancedgrading()) {
                        if (metodoAvanzado != null && metodoAvanzado.getMethod()!=null){
                            return true;
                        }
                    }
                }
            }
        }
        registro.guardarAlerta("implementation rubrics","No hay métodos avanzados de calificación en ninguna de las actividades");
        return false;
    }

    public static boolean hayVariedadFormatos(List<es.ubu.lsi.model.Module> listaModulos, AlertLog registro, FacadeConfig config){
        List<String> formatosVistos=new ArrayList<>();
        for (es.ubu.lsi.model.Module modulo:listaModulos) {
            if ("book,resource,folder,imscp,label,page,url".contains(modulo.getModname())&&!formatosVistos.contains(modulo.getModname())){
                formatosVistos.add(modulo.getModname());
            }
        }
        if(formatosVistos.size()>= config.getFormatNumThreshold()){
            return true;
        }else{
            registro.guardarAlerta("design formats","Se usan menos formatos de los recomendados, hay "+formatosVistos.size()+" cuando debería haber un mínimo de "+config.getFormatNumThreshold());
            return false;
        }
    }

    // Método para calcular el porcentaje de alumnos que han respondido a un cuestionario
    public static double calculaPorcentajeCuestionarios(List<Quiz> quizzes, double porcentaje, AlertLog registro, FacadeConfig config){

        if (quizzes.isEmpty()) return 0;
        
        if  (porcentaje < (config.getMinQuizAnswerPercentage() * 100))
            registro.guardarAlerta("realization estadisticquiz",MENOS_DE_UN + (int) (config.getMinQuizAnswerPercentage() * 100) + "% de los alumnos no realiza los cuestionarios");
        
        return porcentaje/100;
    }
    
    // Método para obtener los cuestionarios de un curso
    public static List<Quiz> getQuizzes(long courseId, String host, String token) {
    	RestTemplate restTemplate = new RestTemplate();
        String url= host + "/webservice/rest/server.php?wsfunction=mod_quiz_get_quizzes_by_courses&moodlewsrestformat=json&wstoken="
        		+token+ COURSEIDS_0 +courseId;
        
        QuizList listaCuestionarios= restTemplate.getForObject(url, QuizList.class);
        if (listaCuestionarios==null){return new ArrayList<>();}
        return listaCuestionarios.getQuizzes();
    }
    
    // Método para obtener los intentos de un usuario para un cuestionario
    private static List<Attempt> getUserQuizAttempts(int quizId, int userId, String host, String token) {
        RestTemplate restTemplate = new RestTemplate();
    	String url = host + "/webservice/rest/server.php?wsfunction=mod_quiz_get_user_attempts&moodlewsrestformat=json&wstoken="
    			+ token + "&quizid=" + quizId + "&userid=" + userId;
        
    	AttemptList attemptList= restTemplate.getForObject(url, AttemptList.class);
        if (attemptList==null){return new ArrayList<>();}
        return attemptList.getAttempts();
    }
    
    // Obtener un intento en un cuestionario
    public static AttemptReviewList getQuizAttempt(int attemptId, String host, String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = host + "/webservice/rest/server.php?wsfunction=mod_quiz_get_attempt_review&moodlewsrestformat=json&wstoken="
        		+ token + "&attemptid=" + attemptId;
        
        AttemptReviewList attemptReviewList= restTemplate.getForObject(url, AttemptReviewList.class);
        if (attemptReviewList==null){return new AttemptReviewList();}

        return attemptReviewList;
    }

    // Método para obtener las estadísticas de participación en cuestionarios
    public static Map<Integer, Double> obtenerEstadisticasCuestionarios(String token, long courseId, String host, List<Quiz> quizzes) {
        List<User> usuarios = obtenerUsuarios(token, courseId, host);
        int totalAlumnos = usuarios.size();
        Map<Integer, Integer> alumnosPorCuestionario = new HashMap<>();
        List<Integer> quizIds = new ArrayList<>();
        
        for (Quiz quiz : quizzes) {
            quizIds.add(quiz.getId());
            alumnosPorCuestionario.put(quiz.getId(), 0);
        }
    
        for (User usuario : usuarios) {
            int userId = usuario.getId();
            List<Attempt> attempts = new ArrayList<>();
            for (int quizId : quizIds) {
                attempts.addAll(getUserQuizAttempts(quizId, userId, host, token));
                if (!attempts.isEmpty()) {
                    int contador = alumnosPorCuestionario.get(quizId);
                    alumnosPorCuestionario.put(quizId, contador + 1);
                    attempts.clear();
                }
            }
        }
    
        Map<Integer, Double> porcentajesRealizados = new HashMap<>();
        for (int quizId : quizIds) {
            int alumnosConIntento = alumnosPorCuestionario.get(quizId);
            double porcentajeRealizado = ((double) alumnosConIntento / totalAlumnos) * 100;
            porcentajesRealizados.put(quizId, porcentajeRealizado);
        }
    
        return porcentajesRealizados;
    }

    // Método para obtener los datos resumidos de un cuestionario
    public static QuizSummary obtenerResumenCuestionario(String token, long courseId, String host, Quiz quiz) {
        QuizSummary quizSummary = new QuizSummary();
        // Obtener los usuarios del curso y eliminar los que no son estudiantes
        List<User> alumnos = obtenerUsuarios(token, courseId, host);
        List<User> usuarios = new ArrayList<>();
        for (User alumno : alumnos) {
            boolean esEstudiante = false;
            for (Role role : alumno.getRoles()) {
                if (role.getShortname().equals("student")) {
                    esEstudiante = true;
                    break;
                }
            }
            if (esEstudiante) {
                usuarios.add(alumno);
            } 
        }

        int contadorIntentos = 0;
        int totalPreguntas = 0;
        double nota = 0;
        int intentos = 0;
        double notaIntento = 0;
        List<Double> notas = new ArrayList<>();
        List<Double> notasUltimoIntento = new ArrayList<>();
        int totalAlumnos = usuarios.size();
        
        // Obtener los alumnos que participan en el cuestionario
        for (User usuario : usuarios) {
            int userId = usuario.getId();
            List<Attempt> attempts = new ArrayList<>();
            attempts.addAll(getUserQuizAttempts(quiz.getId(), userId, host, token));

            for (Attempt attempt : attempts) {
                int attemptId = (int) attempt.getId();
                AttemptReviewList attemptReviewList = getQuizAttempt(attemptId, host, token);
                if (attemptReviewList != null){
                    totalPreguntas = attemptReviewList.getQuestions().size();
                    notaIntento = attemptReviewList.getGrade() != null ? Double.parseDouble(attemptReviewList.getGrade()) : 0;
                    if (notaIntento > nota) {
                        nota = notaIntento;
                    }
                }
                intentos++;
            }
            if (!attempts.isEmpty()) {
                attempts.clear();
                notasUltimoIntento.add(notaIntento);
                notas.add(nota);
                notaIntento = 0;
                nota = 0;
                contadorIntentos++;
            }
        }
    
        quizSummary.setId(quiz.getId());
        quizSummary.setNombreCuestionario(quiz.getName());
        quizSummary.setUrl(host + "/mod/quiz/view.php?id=" + quiz.getCoursemodule());
        quizSummary.setNotaMaxima(quiz.getGrade());
        quizSummary.setMaxIntentos(quiz.getAttempts());
        
        quizSummary.setTotalAlumnos(totalAlumnos);
        quizSummary.setAlumnosExaminados(contadorIntentos);
        quizSummary.setTotalPreguntas(totalPreguntas);
        quizSummary.setNotaMediaMejorIntentoAlumnosConNota(notas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        quizSummary.setNotaMediaUltimoIntentoAlumnosConNota(notasUltimoIntento.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        quizSummary.setNotaMediaMejorIntentoTotalAlumnos(notas.stream().mapToDouble(Double::doubleValue).sum() / totalAlumnos);
        quizSummary.setNotaMediaUltimoIntentoTotalAlumnos(notasUltimoIntento.stream().mapToDouble(Double::doubleValue).sum() / totalAlumnos);
        quizSummary.setTotalIntentos(intentos);
        if (contadorIntentos > 3) {
            quizSummary.setSkewness(calculaSkewness(notas));
            quizSummary.setKurtosis(calculaKurtosis(notas));
        }
    
        return quizSummary;
    }

    // Método para calcula la asimetría o skewness de un cuestionario
    public static double calculaSkewness(List<Double> notas) {
        int n = notas.size();
        double media = calculaMedia(notas);

        double sumaDiferenciam2 = 0.0;
        double sumaDiferenciam3 = 0.0;
        for (double nota : notas) {
            double diferencia = nota - media;
            sumaDiferenciam2 += Math.pow(diferencia, 2);
            sumaDiferenciam3 += Math.pow(diferencia, 3);
        }
        double m2 = sumaDiferenciam2 / n;
        double m3 = sumaDiferenciam3 / n;
        double k2 = (n * m2) / (n - 1);
        double k3 = (Math.pow(n, 2) * m3) / ((n - 1) * (n - 2));
        return k3 / Math.pow(k2, (float)3 / 2);
    }

    // Método para calcular la media de un cuestionario
    public static double calculaMedia(List<Double> notas) {
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        return suma / notas.size();
    }

    // Método para calcular la desviación estándar de un cuestionario
    public static double calculaDesviacionEstandar(List<Double> notas, double media) {
        double sumaDiferencia = 0.0;
        for (double nota : notas) {
            double diferencia = nota - media;
            sumaDiferencia += diferencia * diferencia;
        }
        return Math.sqrt(sumaDiferencia / (notas.size() - 1));
    }

    // Método para calcular la Kurtosis de un cuestionario
    public static double calculaKurtosis(List<Double> notas) {
        int n = notas.size();
        double media = calculaMedia(notas);

        double sumaDiferenciam2 = 0.0;
        double sumaDiferenciam4 = 0.0;
        for (double nota : notas) {
            double diferencia = nota - media;
            sumaDiferenciam2 += Math.pow(diferencia, 2);
            sumaDiferenciam4 += Math.pow(diferencia, 4);
        }
        double m2 = sumaDiferenciam2 / n;
        double m4 = sumaDiferenciam4 / n;
        double primerArgumento = Math.pow(n, 2) / ((n - 1) * (n - 2) * (n - 3));
        double segundoArgumento = (n + 1)*m4 - 3 * (n - 1) * Math.pow(m2, 2);
        double k2 = (n * m2) / (n - 1);
        double k4 = primerArgumento * segundoArgumento;
        return k4 / Math.pow(k2, 2);
    }

    // Método para rellenar los datos del gráfico notas/preguntas de un cuestionario
    public static List<EstadisticaNotasPregunta> obtenerEstadisticasNotasPregunta(String host, String token, Quiz quiz) {
        List<EstadisticaNotasPregunta> estadisticasNotasPregunta = new ArrayList<>();
        List<Attempt> attempts = new ArrayList<>();
        List<User> usuarios = obtenerUsuarios(token, quiz.getCourse(), host);
        int intentos = 0;

        Map<Integer, Double> notas = new HashMap<>();
        Map<Integer, Double> notasMaximas = new HashMap<>();

        for (User usuario : usuarios) {
            Map<Integer, Double> notasAuxiliar = new HashMap<>();
            int userId = usuario.getId();
            double nota = 0;
            attempts.addAll(getUserQuizAttempts(quiz.getId(), userId, host, token));
            // Se recorren los intentos de cada alumno
            for (Attempt attempt : attempts) {
                int attemptId = (int) attempt.getId();
                AttemptReviewList attemptReviewList = getQuizAttempt(attemptId, host, token);
                int idPregunta = 0;
                double puntuacionMaxima = 0;
                double notaMediaPregunta = 0;

                if (attempts.size() > 1) {
                    // Si el cuestionario tiene más de un intento, se almacena la nota del intento con mayor nota
                    double notaIntento = attemptReviewList.getGrade() != null ? Double.parseDouble(attemptReviewList.getGrade()) : 0;
                    // Si la nota del intento es mayor que la nota del intento anterior, se almacenan las notas
                    if (notaIntento > nota) {
                        nota = notaIntento;
                        for (Question question : attemptReviewList.getQuestions()) {
                            String mark = question.getMark();
                            idPregunta = question.getNumber();
                            puntuacionMaxima = question.getMaxmark();
                            if (!question.getMark().equals(""))
                                mark = question.getMark().replace(",", ".");
                            notaMediaPregunta = Double.parseDouble((!mark.equals("")) ? mark : "0");
                            notasAuxiliar.remove(idPregunta);
                            notasAuxiliar.put(idPregunta, notaMediaPregunta);
                            notasMaximas.put(idPregunta, puntuacionMaxima);
                        }
                    }
                } else {
                    // Si el cuestionario tiene un único intento, se almacenan las notas
                    for (Question question : attemptReviewList.getQuestions()) {
                        String mark = question.getMark();
                        idPregunta = question.getNumber();
                        puntuacionMaxima = question.getMaxmark();
                        if (!question.getMark().equals(""))
                            mark = question.getMark().replace(",", ".");
                        notaMediaPregunta = Double.parseDouble((!mark.equals("")) ? mark : "0");
                        notas.put(idPregunta, notaMediaPregunta + notas.getOrDefault(idPregunta, 0.0));
                        notasMaximas.put(idPregunta, puntuacionMaxima);
                    }
                    intentos++;
                }
            }
            if (notasAuxiliar.size() > 1) {
                // Se almacenan las notas del intento con mayor nota
                for (Map.Entry<Integer, Double> entry : notasAuxiliar.entrySet()) {
                    int idPregunta = entry.getKey();
                    notas.put(idPregunta, notasAuxiliar.get(idPregunta) + notas.getOrDefault(idPregunta, 0.0));
                }
                intentos++;
            }
            attempts.clear();
        }
        
        for (Map.Entry<Integer, Double> entry : notas.entrySet()) {
            int idPregunta = entry.getKey();
            double notaMediaPregunta = intentos > 0 ? notas.get(idPregunta) / intentos : 0;
            double puntuacionMaxima = notasMaximas.get(idPregunta);
            EstadisticaNotasPregunta estadisticaNotasPregunta = new EstadisticaNotasPregunta();
            estadisticaNotasPregunta.setIdPregunta(idPregunta);
            // Normalizar la nota media de la pregunta
            if (puntuacionMaxima != 0) {
                double notaNormalizada = notaMediaPregunta / puntuacionMaxima;
                estadisticaNotasPregunta.setNotaMediaPregunta(notaNormalizada);
            } else {
                // Manejar caso especial cuando puntuacionMaxima es 0 (evitar división por 0)
                estadisticaNotasPregunta.setNotaMediaPregunta(0);
            }
            estadisticaNotasPregunta.setPuntuacionMaxima(puntuacionMaxima);
            estadisticasNotasPregunta.add(estadisticaNotasPregunta);
        }
        
        return estadisticasNotasPregunta;
    }

    // Método que calcula el porcentaje de alumnos que han respondido a un foro
    public static List<EstadisticasForo> calculaPorcentajeAlumnosForos(String token, List<Forum> listaForos, List<User> alumnos, List<String>usuariosNoAlumnos, AlertLog registro, FacadeConfig config){
        List<EstadisticasForo> estadisticasForos = new ArrayList<>();
        List<EstadisticasDiscusion> estadisticasDiscusiones = new ArrayList<>();
        EstadisticasForo estadisticasForo = new EstadisticasForo();
        EstadisticasDiscusion estadisticasDiscusion = new EstadisticasDiscusion();
        List<Long> listaAlumnos = new ArrayList<>();
        int mensajes = 0;
        int usuariosUnicos = 0;
        double porcentaje = 0;

        // Si no hay foros no hay participación
        if (listaForos.isEmpty()) {
            registro.guardarAlerta("realization estadisticforum",MENOS_DE_UN + (int) (config.getMinQuizAnswerPercentage() * 100) + "% de los alumnos participa en los foros");
            return estadisticasForos;
        }

        // Se recorren los foros
        for (Forum foro : listaForos) {
            estadisticasForo.setIdForo(foro.getId());
            estadisticasForo.setNombre(foro.getName());
            estadisticasForo.setUrl(config.getHost() + "/mod/forum/view.php?id=" + foro.getCmid());

            // Mensajes del foro
            StringBuilder texto = new StringBuilder();

            List<Discussion> listaDiscusiones = obtenerListaDebates(token, foro, config.getHost());

            for (Discussion discusion : listaDiscusiones) {
                estadisticasDiscusion.setIdDiscusion(discusion.getId());
                estadisticasDiscusion.setAsunto(discusion.getName());

                List<Post> listaPostsDiscusion = obtenerListaPostsPorDebate(token, discusion, config.getHost());
                for (Post post : listaPostsDiscusion) {
                    mensajes++;
                    if (!listaAlumnos.contains(post.getAuthor().getId()) && !usuariosNoAlumnos.contains(post.getAuthor().getFullname())) {
                        listaAlumnos.add(post.getAuthor().getId());
                        usuariosUnicos++;
                    }
                    texto.append(post.getMessage()).append(" ");
                }
                estadisticasDiscusion.setNumeroMensajes(mensajes);
                estadisticasDiscusiones.add(estadisticasDiscusion);
                mensajes = 0;
                estadisticasDiscusion = new EstadisticasDiscusion();
            }

            String textoFinal = texto.toString();
            textoFinal = comprobarFormatoDiscusion(listaDiscusiones, textoFinal);

            estadisticasForo.setUsuariosUnicos(usuariosUnicos);
            estadisticasForo.setPorcentajeParticipacion(((double) usuariosUnicos / alumnos.size()) * 100);
            estadisticasForo.setEstadisticasDiscusiones(estadisticasDiscusiones);
            estadisticasForo.setTexto(textoFinal);
            estadisticasForos.add(estadisticasForo);

            usuariosUnicos = 0;
            porcentaje += estadisticasForo.getPorcentajeParticipacion();
            estadisticasForo = new EstadisticasForo();
            estadisticasDiscusiones = new ArrayList<>();
            listaAlumnos = new ArrayList<>();
        }

        // Si el porcentaje de alumnos que han participado en los foros es menor que el porcentaje mínimo, se guarda la alerta
        if ((porcentaje / listaForos.size()) < (config.getMinQuizAnswerPercentage() * 100))
            registro.guardarAlerta("realization estadisticforum",MENOS_DE_UN + (int) (config.getMinQuizAnswerPercentage() * 100) + "% de los alumnos participa en los foros");
        
        return estadisticasForos;
    }

    public static String comprobarFormatoDiscusion (List<Discussion> listaDiscusiones, String texto) {
        if (listaDiscusiones != null && !listaDiscusiones.isEmpty() && listaDiscusiones.get(0).getMessageformat() == 1) {
            return parseHtmlToString(texto);
        }

        return texto;
    }

    public static String parseHtmlToString (String html) {
        // Parsear el texto HTML
        Document doc = Jsoup.parse(html);
        
        // Obtener todos los elementos de texto
        Elements elementosTexto = doc.select(":not(*:has(*))");
        
        // Recorrer los elementos de texto y obtener su contenido
        StringBuilder textoCompleto = new StringBuilder();
        for (Element elemento : elementosTexto) {
            String texto = elemento.text();
            textoCompleto.append(texto).append(" ");
        }
        
        return textoCompleto.toString().trim();
    }
    
}
