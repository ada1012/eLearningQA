package es.ubu.lsi.model;

public class QuizSummary {
    private int id;
    private String nombreCuestionario;
    private int totalAlumnos;
    private int alumnosExaminados;
    private int totalPreguntas;
    private double notaMaxima;
    private double notaMedia;
    private int maxIntentos;
    private double mediaIntentos;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombreCuestionario() {
		return nombreCuestionario;
	}
	public void setNombreCuestionario(String nombreCuestionario) {
		this.nombreCuestionario = nombreCuestionario;
	}
	public int getTotalAlumnos() {
		return totalAlumnos;
	}
	public void setTotalAlumnos(int totalAlumnos) {
		this.totalAlumnos = totalAlumnos;
	}
	public int getAlumnosExaminados() {
		return alumnosExaminados;
	}
	public void setAlumnosExaminados(int alumnosExaminados) {
		this.alumnosExaminados = alumnosExaminados;
	}
	public int getTotalPreguntas() {
		return totalPreguntas;
	}
	public void setTotalPreguntas(int totalPreguntas) {
		this.totalPreguntas = totalPreguntas;
	}
	public double getNotaMaxima() {
		return notaMaxima;
	}
	public void setNotaMaxima(double notaMaxima) {
		this.notaMaxima = notaMaxima;
	}
	public double getNotaMedia() {
		return notaMedia;
	}
	public void setNotaMedia(double notaMedia) {
		this.notaMedia = notaMedia;
	}
	public int getMaxIntentos() {
		return maxIntentos;
	}
	public void setMaxIntentos(int maxIntentos) {
		this.maxIntentos = maxIntentos;
	}
	public double getMediaIntentos() {
		return mediaIntentos;
	}
	public void setMediaIntentos(double mediaIntentos) {
		this.mediaIntentos = mediaIntentos;
	}


}
