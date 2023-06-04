package es.ubu.lsi.model;

public class EstadisticasDiscusion {
    private int idDiscusion;
    private String asunto;
    private int numeroMensajes;

    public EstadisticasDiscusion(int idDiscusion, String asunto, int numeroMensajes) {
        this.idDiscusion = idDiscusion;
        this.asunto = asunto;
        this.numeroMensajes = numeroMensajes;
    }

    public EstadisticasDiscusion() {
    }

    public int getIdDiscusion() {
        return idDiscusion;
    }

    public String getAsunto() {
        return asunto;
    }

    public int getNumeroMensajes() {
        return numeroMensajes;
    }

    public void setIdDiscusion(int idDiscusion) {
        this.idDiscusion = idDiscusion;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setNumeroMensajes(int numeroMensajes) {
        this.numeroMensajes = numeroMensajes;
    }
}
