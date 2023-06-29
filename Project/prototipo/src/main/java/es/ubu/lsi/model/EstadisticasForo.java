package es.ubu.lsi.model;

import java.util.List;

public class EstadisticasForo {
    private int idForo;
    private String nombre;
    private String URL;
    private List<EstadisticasDiscusion> estadisticasDiscusiones;
    private int usuariosUnicos;
    private double porcentajeParticipacion;
    private String texto;

    public EstadisticasForo(int idForo, String nombre, String URL, List<EstadisticasDiscusion> estadisticasDiscusiones, int usuariosUnicos, double porcentajeParticipacion, String texto) {
        this.idForo = idForo;
        this.nombre = nombre;
        this.URL = URL;
        this.estadisticasDiscusiones = estadisticasDiscusiones;
        this.usuariosUnicos = usuariosUnicos;
        this.porcentajeParticipacion = porcentajeParticipacion;
        this.texto = texto;
    }

    public EstadisticasForo() {
    }

    public int getIdForo() {
        return idForo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    
    public List<EstadisticasDiscusion> getEstadisticasDiscusiones() {
        return estadisticasDiscusiones;
    }

    public int getUsuariosUnicos() {
        return usuariosUnicos;
    }

    public double getPorcentajeParticipacion() {
        return porcentajeParticipacion;
    }

    public void setIdForo(int idForo) {
        this.idForo = idForo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setEstadisticasDiscusiones(List<EstadisticasDiscusion> estadisticasDiscusiones) {
        this.estadisticasDiscusiones = estadisticasDiscusiones;
    }

    public void setUsuariosUnicos(int usuariosUnicos) {
        this.usuariosUnicos = usuariosUnicos;
    }

    public void setPorcentajeParticipacion(double porcentajeParticipacion) {
        this.porcentajeParticipacion = porcentajeParticipacion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
