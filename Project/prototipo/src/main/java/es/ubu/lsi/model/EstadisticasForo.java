package es.ubu.lsi.model;

public class EstadisticasForo {
    private int idForo;
    private String asunto;
    private int numeroMensajes;
    private int usuariosUnicos;
    private double porcentajeParticipacion;

    public EstadisticasForo(int idForo, String asunto, int numeroMensajes, int usuariosUnicos, double porcentajeParticipacion) {
        this.idForo = idForo;
        this.asunto = asunto;
        this.numeroMensajes = numeroMensajes;
        this.usuariosUnicos = usuariosUnicos;
        this.porcentajeParticipacion = porcentajeParticipacion;
    }

    public EstadisticasForo() {
    }

    public int getIdForo() {
        return idForo;
    }

    public String getAsunto() {
        return asunto;
    }

    public int getNumeroMensajes() {
        return numeroMensajes;
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

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setNumeroMensajes(int numeroMensajes) {
        this.numeroMensajes = numeroMensajes;
    }

    public void setUsuariosUnicos(int usuariosUnicos) {
        this.usuariosUnicos = usuariosUnicos;
    }

    public void setPorcentajeParticipacion(double porcentajeParticipacion) {
        this.porcentajeParticipacion = porcentajeParticipacion;
    }
    
    @Override
    public String toString() {
        return "EstadisticasForo{" + "idForo=" + idForo + ", asunto=" + asunto + ", numeroMensajes=" + numeroMensajes + ", usuariosUnicos=" + usuariosUnicos + ", porcentajeParticipacion=" + porcentajeParticipacion + '}';
    }
}
