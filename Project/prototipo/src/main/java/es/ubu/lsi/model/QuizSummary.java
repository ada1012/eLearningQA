package es.ubu.lsi.model;

public class QuizSummary {
    private int id;
    private String nombreCuestionario;
    private int totalAlumnos;
    private int alumnosExaminados;
    private int totalPreguntas;
    private double notaMaxima;
    private double notaMediaMejorIntentoAlumnosConNota;
	private double notaMediaMejorIntentoTotalAlumnos;
	private double notaMediaUltimoIntentoAlumnosConNota;
	private double notaMediaUltimoIntentoTotalAlumnos;
    private int maxIntentos;
    private double mediaIntentos;
	private double skewness;
	private double kurtosis;
    
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
	public double getNotaMediaMejorIntentoAlumnosConNota() {
		return notaMediaMejorIntentoAlumnosConNota;
	}
	public void setNotaMediaMejorIntentoAlumnosConNota(double notaMediaMejorIntentoAlumnosConNota) {
		this.notaMediaMejorIntentoAlumnosConNota = notaMediaMejorIntentoAlumnosConNota;
	}
	public double getNotaMediaMejorIntentoTotalAlumnos() {
		return notaMediaMejorIntentoTotalAlumnos;
	}
	public void setNotaMediaMejorIntentoTotalAlumnos(double notaMediaMejorIntentoTotalAlumnos) {
		this.notaMediaMejorIntentoTotalAlumnos = notaMediaMejorIntentoTotalAlumnos;
	}
	public double getNotaMediaUltimoIntentoAlumnosConNota() {
		return notaMediaUltimoIntentoAlumnosConNota;
	}
	public void setNotaMediaUltimoIntentoAlumnosConNota(double notaMediaUltimoIntentoAlumnosConNota) {
		this.notaMediaUltimoIntentoAlumnosConNota = notaMediaUltimoIntentoAlumnosConNota;
	}
	public double getNotaMediaUltimoIntentoTotalAlumnos() {
		return notaMediaUltimoIntentoTotalAlumnos;
	}
	public void setNotaMediaUltimoIntentoTotalAlumnos(double notaMediaUltimoIntentoTotalAlumnos) {
		this.notaMediaUltimoIntentoTotalAlumnos = notaMediaUltimoIntentoTotalAlumnos;
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

	public double getSkewness() {
		return skewness;
	}
	public void setSkewness(double skewness) {
		this.skewness = skewness;
	}
	public double getKurtosis() {
		return kurtosis;
	}
	public void setKurtosis(double kurtosis) {
		this.kurtosis = kurtosis;
	}

}
