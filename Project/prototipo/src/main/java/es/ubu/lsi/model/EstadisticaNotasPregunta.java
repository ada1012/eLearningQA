package es.ubu.lsi.model;

public class EstadisticaNotasPregunta {
    private int idPregunta;
    private double puntuacionMaxima;
    private double notaMediaPregunta;

    public EstadisticaNotasPregunta(int idPregunta, double puntuacionMaxima, double notaMediaPregunta) {
        this.idPregunta = idPregunta;
        this.puntuacionMaxima = puntuacionMaxima;
        this.notaMediaPregunta = notaMediaPregunta;
    }

    public EstadisticaNotasPregunta() {
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public double getPuntuacionMaxima() {
        return puntuacionMaxima;
    }

    public double getNotaMediaPregunta() {
        return notaMediaPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public void setPuntuacionMaxima(double puntuacionMaxima) {
        this.puntuacionMaxima = puntuacionMaxima;
    }

    public void setNotaMediaPregunta(double notaMediaPregunta) {
        this.notaMediaPregunta = notaMediaPregunta;
    }
}
