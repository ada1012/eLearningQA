package es.ubu.lsi.model;

public class EstadisticaNotasPregunta {
    private int idPregunta;
    private int puntuacionMaxima;
    private double notaMediaPrgunta;

    public EstadisticaNotasPregunta(int idPregunta, int puntuacionMaxima, double notaMediaPrgunta) {
        this.idPregunta = idPregunta;
        this.puntuacionMaxima = puntuacionMaxima;
        this.notaMediaPrgunta = notaMediaPrgunta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public int getPuntuacionMaxima() {
        return puntuacionMaxima;
    }

    public double getNotaMediaPrgunta() {
        return notaMediaPrgunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public void setPuntuacionMaxima(int puntuacionMaxima) {
        this.puntuacionMaxima = puntuacionMaxima;
    }

    public void setNotaMediaPrgunta(double notaMediaPrgunta) {
        this.notaMediaPrgunta = notaMediaPrgunta;
    }
}
