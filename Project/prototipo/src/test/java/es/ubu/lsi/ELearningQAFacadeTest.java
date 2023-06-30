package es.ubu.lsi;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.ubu.lsi.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ELearningQAFacadeTest {

    public static ELearningQAFacade fachada;
    public static String token;
    public static List<Course> listaCursos;
    public static List<StatusList> listasEstados=new ArrayList<>();
    public static List<List<es.ubu.lsi.model.Module>> listasModulos=new ArrayList<>();
    private static List<List<Group>> listasGrupos=new ArrayList<>();
    private static List<List<Assignment>> listasTareas=new ArrayList<>();
    private static List<List<Table>> listasCalificadores=new ArrayList<>();
    private static List<List<Resource>> listasRecursos=new ArrayList<>();
    private static List<List<CourseModule>> listasModulosTareas=new ArrayList<>();
    private static List<List<User>> listasUsuarios=new ArrayList<>();
    private static List<List<Post>> listasPosts=new ArrayList<>();
    private static List<List<Assignment>> listasTareasConNotas=new ArrayList<>();
    private static List<List<ResponseAnalysis>> listasAnalisis=new ArrayList<>();
    private static List<List<Survey>> listasSurveys=new ArrayList<>();
    private static List<List<Quiz>> listasQuizes=new ArrayList<>();
    private static List<List<Forum>> listasForos=new ArrayList<>();
    private static AlertLog registro=new AlertLog();
    static long idCurso1=66;
    static long idCurso2=56;
    static long idCurso3=59;
    static long idCurso4=27;
    static long idCurso5=41;
    static long idCurso6=68;
    static List<Long> ids= Arrays.asList(new Long[]{idCurso1, idCurso2,idCurso3,idCurso4,idCurso5,idCurso6});



    @BeforeAll
    public static void BeforeClass() {
        fachada=new ELearningQAFacade("Docencia_reglada","https://school.moodledemo.net");
        token=fachada.conectarse("teacher","moodle");
        String sep= File.separator;
        String ruta="."+sep+"src"+sep+"main"+sep+"resources"+sep+"json"+sep;
        String extension=".json";
        ObjectMapper mapper=new ObjectMapper();
        try {
            listaCursos=Arrays.asList(mapper.readValue(new File(ruta+"Listacursos"+extension), Course[].class));
            for (Long id:ids) {
                listasEstados.add(mapper.readValue(new File(ruta+"Listaestados"+id+extension), StatusList.class));
                listasModulos.add(Arrays.asList(mapper.readValue(new File(ruta+"Listamodulos"+id+extension), es.ubu.lsi.model.Module[].class)));
                listasGrupos.add(Arrays.asList(mapper.readValue(new File(ruta+"Listagrupos"+id+extension), Group[].class)));
                listasTareas.add(Arrays.asList(mapper.readValue(new File(ruta+"Listatareas"+id+extension), Assignment[].class)));
                listasCalificadores.add(Arrays.asList(mapper.readValue(new File(ruta+"Listacalificadores"+id+extension), Table[].class)));
                listasRecursos.add(Arrays.asList(mapper.readValue(new File(ruta+"Listarecursos"+id+extension), Resource[].class)));
                listasModulosTareas.add(Arrays.asList(mapper.readValue(new File(ruta+"Listamodulostareas"+id+extension), CourseModule[].class)));
                listasUsuarios.add(Arrays.asList(mapper.readValue(new File(ruta+"Listausuarios"+id+extension), User[].class)));
                listasPosts.add(Arrays.asList(mapper.readValue(new File(ruta+"Listaposts"+id+extension), Post[].class)));
                listasTareasConNotas.add(Arrays.asList(mapper.readValue(new File(ruta+"Listatareasconnotas"+id+extension), Assignment[].class)));
                listasAnalisis.add(Arrays.asList(mapper.readValue(new File(ruta+"Listaanalisis"+id+extension), ResponseAnalysis[].class)));
                listasSurveys.add(Arrays.asList(mapper.readValue(new File(ruta+"Listasurveys"+id+extension), Survey[].class)));
                listasQuizes.add(Arrays.asList(mapper.readValue(new File(ruta+"Listaquizes"+id+extension), Quiz[].class)));
                listasForos.add(Arrays.asList(mapper.readValue(new File(ruta+"Listaforos"+id+extension), Forum[].class)));
            }
        } catch (Exception e) {
            Logger LOGGER = LogManager.getLogger();
            LOGGER.error("exception", e);
        }
    }

    @org.junit.jupiter.api.Test
    void getListaCursosTest() {
        List<Long> listaIds = new ArrayList<>();
        for (Course curso:listaCursos) {
            listaIds.add(curso.getId());
        }
        assertTrue(listaIds.contains(idCurso1));
        assertTrue(listaIds.contains(idCurso2));
        assertTrue(listaIds.contains(idCurso3));
        assertTrue(listaIds.contains(idCurso4));
        assertTrue(listaIds.contains(idCurso5));
        assertTrue(listaIds.contains(idCurso6));
    }


    @org.junit.jupiter.api.Test
    void isSonVisiblesCondicionesTest() {
        assertTrue(fachada.isSonVisiblesCondiciones(listaCursos.get(0), registro));
        assertTrue(fachada.isSonVisiblesCondiciones(listaCursos.get(4), registro));
        assertFalse(fachada.isSonVisiblesCondiciones(listaCursos.get(3), registro));
        assertFalse(fachada.isSonVisiblesCondiciones(listaCursos.get(8), registro));
        assertTrue(fachada.isSonVisiblesCondiciones(listaCursos.get(9), registro));
        assertTrue(fachada.isSonVisiblesCondiciones(listaCursos.get(2), registro));
    }

    @org.junit.jupiter.api.Test
    void isTieneGruposTest() {
        assertTrue(fachada.isTieneGrupos(listasGrupos.get(0), registro));
        assertFalse(fachada.isTieneGrupos(listasGrupos.get(1), registro));
        assertTrue(fachada.isTieneGrupos(listasGrupos.get(2), registro));
        assertFalse(fachada.isTieneGrupos(listasGrupos.get(3), registro));
        assertTrue(fachada.isTieneGrupos(listasGrupos.get(4), registro));
        assertTrue(fachada.isTieneGrupos(listasGrupos.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void getCursoPorIdTest() {
        assertEquals("Digital Literacy ",listaCursos.get(0).getFullname());
        assertEquals("History: Russia in Revolution",listaCursos.get(4).getFullname());
        assertEquals("Celebrating Cultures",listaCursos.get(3).getFullname());
        assertEquals("Parents and Citizens Council",listaCursos.get(8).getFullname());
        assertEquals("Staffroom",listaCursos.get(9).getFullname());
        assertEquals("Votes for Women!",listaCursos.get(2).getFullname());
    }

    @org.junit.jupiter.api.Test
    void porcentajeFraccionTest() {
        assertEquals(20,fachada.porcentajeFraccion(1,5 ));
        assertEquals(34,fachada.porcentajeFraccion(17, 50));
        assertEquals(55,fachada.porcentajeFraccion(11, 20));
        assertEquals(100,fachada.porcentajeFraccion(9, 9));
    }


    @org.junit.jupiter.api.Test
    void isEstaProgresoActivadoTest() {
        assertTrue(fachada.isestaProgresoActivado(listasEstados.get(0), registro));
        assertTrue(fachada.isestaProgresoActivado(listasEstados.get(1), registro));
        assertTrue(fachada.isestaProgresoActivado(listasEstados.get(2), registro));
        assertFalse(fachada.isestaProgresoActivado(listasEstados.get(3), registro));
        assertTrue(fachada.isestaProgresoActivado(listasEstados.get(4), registro));
        assertTrue(fachada.isestaProgresoActivado(listasEstados.get(5), registro));
    }


    @org.junit.jupiter.api.Test
    void isEstaCorregidoATiempoTest() {
        assertFalse(fachada.isEstaCorregidoATiempo(listasTareasConNotas.get(0),listasUsuarios.get(0), registro));
        assertFalse(fachada.isEstaCorregidoATiempo(listasTareasConNotas.get(1),listasUsuarios.get(1), registro));
        assertFalse(fachada.isEstaCorregidoATiempo(listasTareasConNotas.get(2),listasUsuarios.get(2), registro));
        assertFalse(fachada.isEstaCorregidoATiempo(listasTareasConNotas.get(3),listasUsuarios.get(3), registro));
        assertFalse(fachada.isEstaCorregidoATiempo(listasTareasConNotas.get(4),listasUsuarios.get(4), registro));
        assertFalse(fachada.isEstaCorregidoATiempo(listasTareasConNotas.get(5),listasUsuarios.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isRespondeATiempoTest() {
        assertTrue(fachada.isRespondeATiempo(listasUsuarios.get(0),listasPosts.get(0), registro));
        assertTrue(fachada.isRespondeATiempo(listasUsuarios.get(1),listasPosts.get(1), registro));
        assertTrue(fachada.isRespondeATiempo(listasUsuarios.get(2),listasPosts.get(2), registro));
        assertTrue(fachada.isRespondeATiempo(listasUsuarios.get(3),listasPosts.get(3), registro));
        assertFalse(fachada.isRespondeATiempo(listasUsuarios.get(4),listasPosts.get(4), registro));
        assertTrue(fachada.isRespondeATiempo(listasUsuarios.get(5),listasPosts.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isUsaSurveysTest() {
        assertTrue(fachada.isUsaSurveys(listasSurveys.get(0), registro));
        assertFalse(fachada.isUsaSurveys(listasSurveys.get(1), registro));
        assertFalse(fachada.isUsaSurveys(listasSurveys.get(2), registro));
        assertFalse(fachada.isUsaSurveys(listasSurveys.get(3), registro));
        assertFalse(fachada.isUsaSurveys(listasSurveys.get(4), registro));
        assertTrue(fachada.isUsaSurveys(listasSurveys.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isAlumnosEnGruposTest() {
        assertFalse(fachada.isAlumnosEnGrupos(listasUsuarios.get(0), registro));
        assertFalse(fachada.isAlumnosEnGrupos(listasUsuarios.get(1), registro));
        assertFalse(fachada.isAlumnosEnGrupos(listasUsuarios.get(2), registro));
        assertFalse(fachada.isAlumnosEnGrupos(listasUsuarios.get(3), registro));
        assertFalse(fachada.isAlumnosEnGrupos(listasUsuarios.get(4), registro));
        assertTrue(fachada.isAlumnosEnGrupos(listasUsuarios.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isAnidamientoCalificadorAceptableTest() {
        assertTrue(fachada.isAnidamientoCalificadorAceptable(listasCalificadores.get(0), registro));
        assertTrue(fachada.isAnidamientoCalificadorAceptable(listasCalificadores.get(1), registro));
        assertTrue(fachada.isAnidamientoCalificadorAceptable(listasCalificadores.get(2), registro));
        assertTrue(fachada.isAnidamientoCalificadorAceptable(listasCalificadores.get(3), registro));
        assertTrue(fachada.isAnidamientoCalificadorAceptable(listasCalificadores.get(4), registro));
        assertTrue(fachada.isAnidamientoCalificadorAceptable(listasCalificadores.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isCalificadorMuestraPonderacionTest() {
        assertTrue(fachada.isCalificadorMuestraPonderacion(listasCalificadores.get(0), registro));
        assertTrue(fachada.isCalificadorMuestraPonderacion(listasCalificadores.get(1), registro));
        assertTrue(fachada.isCalificadorMuestraPonderacion(listasCalificadores.get(2), registro));
        assertTrue(fachada.isCalificadorMuestraPonderacion(listasCalificadores.get(3), registro));
        assertTrue(fachada.isCalificadorMuestraPonderacion(listasCalificadores.get(4), registro));
        assertTrue(fachada.isCalificadorMuestraPonderacion(listasCalificadores.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isHayRetroalimentacionTest() {
        assertFalse(fachada.isHayRetroalimentacion(listasCalificadores.get(0), registro));
        assertTrue(fachada.isHayRetroalimentacion(listasCalificadores.get(1), registro));
        assertFalse(fachada.isHayRetroalimentacion(listasCalificadores.get(2), registro));
        assertFalse(fachada.isHayRetroalimentacion(listasCalificadores.get(3), registro));
        assertFalse(fachada.isHayRetroalimentacion(listasCalificadores.get(4), registro));
        assertFalse(fachada.isHayRetroalimentacion(listasCalificadores.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isEsNotaMaxConsistenteTest() {
        assertFalse(fachada.isEsNotaMaxConsistente(listasCalificadores.get(0), registro));
        assertFalse(fachada.isEsNotaMaxConsistente(listasCalificadores.get(1), registro));
        assertFalse(fachada.isEsNotaMaxConsistente(listasCalificadores.get(2), registro));
        assertTrue(fachada.isEsNotaMaxConsistente(listasCalificadores.get(3), registro));
        assertFalse(fachada.isEsNotaMaxConsistente(listasCalificadores.get(4), registro));
        assertFalse(fachada.isEsNotaMaxConsistente(listasCalificadores.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isEstanActualizadosRecursosTest() {
        assertTrue(fachada.isEstanActualizadosRecursos(WebServiceClient.obtenerRecursosDesfasados(listaCursos.get(0),listasRecursos.get(0)), registro));
        assertTrue(fachada.isEstanActualizadosRecursos(WebServiceClient.obtenerRecursosDesfasados(listaCursos.get(4),listasRecursos.get(1)), registro));
        assertTrue(fachada.isEstanActualizadosRecursos(WebServiceClient.obtenerRecursosDesfasados(listaCursos.get(3),listasRecursos.get(2)), registro));
        assertTrue(fachada.isEstanActualizadosRecursos(WebServiceClient.obtenerRecursosDesfasados(listaCursos.get(8),listasRecursos.get(3)), registro));
        assertTrue(fachada.isEstanActualizadosRecursos(WebServiceClient.obtenerRecursosDesfasados(listaCursos.get(9),listasRecursos.get(4)), registro));
        assertTrue(fachada.isEstanActualizadosRecursos(WebServiceClient.obtenerRecursosDesfasados(listaCursos.get(2),listasRecursos.get(5)), registro));
    }

    @org.junit.jupiter.api.Test
    void isSonFechasCorrectasTest() {
        assertTrue(fachada.isSonFechasCorrectas(WebServiceClient.obtenerModulosMalFechados(listaCursos.get(0),listasModulos.get(0)), registro));
        assertTrue(fachada.isSonFechasCorrectas(WebServiceClient.obtenerModulosMalFechados(listaCursos.get(4),listasModulos.get(1)), registro));
        assertTrue(fachada.isSonFechasCorrectas(WebServiceClient.obtenerModulosMalFechados(listaCursos.get(3),listasModulos.get(2)), registro));
        assertTrue(fachada.isSonFechasCorrectas(WebServiceClient.obtenerModulosMalFechados(listaCursos.get(8),listasModulos.get(3)), registro));
        assertTrue(fachada.isSonFechasCorrectas(WebServiceClient.obtenerModulosMalFechados(listaCursos.get(9),listasModulos.get(4)), registro));
        assertFalse(fachada.isSonFechasCorrectas(WebServiceClient.obtenerModulosMalFechados(listaCursos.get(2),listasModulos.get(5)), registro));
    }

    @org.junit.jupiter.api.Test
    void isRespondenFeedbacksTest() {
        assertFalse(fachada.isRespondenFeedbacks(listasAnalisis.get(0),listasUsuarios.get(0), registro));
        assertTrue(fachada.isRespondenFeedbacks(listasAnalisis.get(1),listasUsuarios.get(1), registro));
        assertTrue(fachada.isRespondenFeedbacks(listasAnalisis.get(2),listasUsuarios.get(2), registro));
        assertTrue(fachada.isRespondenFeedbacks(listasAnalisis.get(3),listasUsuarios.get(3), registro));
        assertTrue(fachada.isRespondenFeedbacks(listasAnalisis.get(4),listasUsuarios.get(4), registro));
        assertFalse(fachada.isRespondenFeedbacks(listasAnalisis.get(5),listasUsuarios.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isHayTareasGrupalesTest() {
        assertFalse(fachada.isHayTareasGrupales(listasTareas.get(0), registro));
        assertFalse(fachada.isHayTareasGrupales(listasTareas.get(1), registro));
        assertFalse(fachada.isHayTareasGrupales(listasTareas.get(2), registro));
        assertFalse(fachada.isHayTareasGrupales(listasTareas.get(3), registro));
        assertFalse(fachada.isHayTareasGrupales(listasTareas.get(4), registro));
        assertTrue(fachada.isHayTareasGrupales(listasTareas.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isMuestraCriteriosTest() {
        assertFalse(fachada.isMuestraCriterios(listasModulosTareas.get(0), registro));
        assertFalse(fachada.isMuestraCriterios(listasModulosTareas.get(1), registro));
        assertFalse(fachada.isMuestraCriterios(listasModulosTareas.get(2), registro));
        assertFalse(fachada.isMuestraCriterios(listasModulosTareas.get(3), registro));
        assertFalse(fachada.isMuestraCriterios(listasModulosTareas.get(4), registro));
        assertFalse(fachada.isMuestraCriterios(listasModulosTareas.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isHayVariedadFormatosTest() {
        assertTrue(fachada.isHayVariedadFormatos(listasModulos.get(0), registro));
        assertTrue(fachada.isHayVariedadFormatos(listasModulos.get(1), registro));
        assertFalse(fachada.isHayVariedadFormatos(listasModulos.get(2), registro));
        assertFalse(fachada.isHayVariedadFormatos(listasModulos.get(3), registro));
        assertFalse(fachada.isHayVariedadFormatos(listasModulos.get(4), registro));
        assertTrue(fachada.isHayVariedadFormatos(listasModulos.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isHayCuestionariosTest() {
        assertTrue(fachada.isHayCuestionarios(listasQuizes.get(0), registro));
        assertTrue(fachada.isHayCuestionarios(listasQuizes.get(1), registro));
        assertTrue(fachada.isHayCuestionarios(listasQuizes.get(2), registro));
        assertTrue(fachada.isHayCuestionarios(listasQuizes.get(3), registro));
        assertTrue(fachada.isHayCuestionarios(listasQuizes.get(4), registro));
        assertTrue(fachada.isHayCuestionarios(listasQuizes.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void isHayForosTest() {
        assertTrue(fachada.isHayForos(listasForos.get(0), registro));
        assertTrue(fachada.isHayForos(listasForos.get(1), registro));
        assertFalse(fachada.isHayForos(listasForos.get(2), registro));
        assertTrue(fachada.isHayForos(listasForos.get(3), registro));
        assertTrue(fachada.isHayForos(listasForos.get(4), registro));
        assertTrue(fachada.isHayForos(listasForos.get(5), registro));
    }

    @org.junit.jupiter.api.Test
    void generarCampoRelativoTest() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        char sep=symbols.getDecimalSeparator();
        assertEquals(fachada.generarCampoRelativo(-0.1f,100),"<td class=\"tg-pred\"><meter value=\"-0.1\" min=\"0\" max=\"100.0\"></meter>-0"+sep+"1%</td>");
        assertEquals(fachada.generarCampoRelativo(0,100),"<td class=\"tg-pred\"><meter value=\"0.0\" min=\"0\" max=\"100.0\"></meter>0"+sep+"0%</td>");
        assertEquals(fachada.generarCampoRelativo(19.9f,100),"<td class=\"tg-pred\"><meter value=\"19.9\" min=\"0\" max=\"100.0\"></meter>19"+sep+"9%</td>");
        assertEquals(fachada.generarCampoRelativo(20,100),"<td class=\"tg-oran\"><meter value=\"20.0\" min=\"0\" max=\"100.0\"></meter>20"+sep+"0%</td>");
        assertEquals(fachada.generarCampoRelativo(39.9f,100),"<td class=\"tg-oran\"><meter value=\"39.9\" min=\"0\" max=\"100.0\"></meter>39"+sep+"9%</td>");
        assertEquals(fachada.generarCampoRelativo(40.0f,100),"<td class=\"tg-yell\"><meter value=\"40.0\" min=\"0\" max=\"100.0\"></meter>40"+sep+"0%</td>");
        assertEquals(fachada.generarCampoRelativo(59.9f,100),"<td class=\"tg-yell\"><meter value=\"59.9\" min=\"0\" max=\"100.0\"></meter>59"+sep+"9%</td>");
        assertEquals(fachada.generarCampoRelativo(60,100),"<td class=\"tg-char\"><meter value=\"60.0\" min=\"0\" max=\"100.0\"></meter>60"+sep+"0%</td>");
        assertEquals(fachada.generarCampoRelativo(79.9f,100),"<td class=\"tg-char\"><meter value=\"79.9\" min=\"0\" max=\"100.0\"></meter>79"+sep+"9%</td>");
        assertEquals(fachada.generarCampoRelativo(80,100),"<td class=\"tg-pgre\"><meter value=\"80.0\" min=\"0\" max=\"100.0\"></meter>80"+sep+"0%</td>");
        assertEquals(fachada.generarCampoRelativo(100,100),"<td class=\"tg-pgre\"><meter value=\"100.0\" min=\"0\" max=\"100.0\"></meter>100"+sep+"0%</td>");
        assertEquals(fachada.generarCampoRelativo(100.1f,100),"<td class=\"tg-pgre\"><meter value=\"100.1\" min=\"0\" max=\"100.0\"></meter>100"+sep+"1%</td>");
    }
}