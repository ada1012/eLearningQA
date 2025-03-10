package es.ubu.lsi.model;

public class QuizSummary {
    private int id;
    private String nombreCuestionario;
	private String url;
    private int totalAlumnos;
    private int alumnosExaminados;
    private int totalPreguntas;
    private double notaMaxima;
    private double notaMediaMejorIntentoAlumnosConNota;
	private double notaMediaMejorIntentoTotalAlumnos;
	private double notaMediaUltimoIntentoAlumnosConNota;
	private double notaMediaUltimoIntentoTotalAlumnos;
    private int maxIntentos;
    private int totalIntentos;
	private double skewness;
	private double kurtosis;

	public QuizSummary(int id, String nombreCuestionario, String url, int totalAlumnos, int alumnosExaminados, int totalPreguntas, double notaMaxima, double notaMediaMejorIntentoAlumnosConNota, double notaMediaMejorIntentoTotalAlumnos, double notaMediaUltimoIntentoAlumnosConNota, double notaMediaUltimoIntentoTotalAlumnos, int maxIntentos, int totalIntentos, double skewness, double kurtosis) {
		this.id = id;
		this.nombreCuestionario = nombreCuestionario;
		this.url = url;
		this.totalAlumnos = totalAlumnos;
		this.alumnosExaminados = alumnosExaminados;
		this.totalPreguntas = totalPreguntas;
		this.notaMaxima = notaMaxima;
		this.notaMediaMejorIntentoAlumnosConNota = notaMediaMejorIntentoAlumnosConNota;
		this.notaMediaMejorIntentoTotalAlumnos = notaMediaMejorIntentoTotalAlumnos;
		this.notaMediaUltimoIntentoAlumnosConNota = notaMediaUltimoIntentoAlumnosConNota;
		this.notaMediaUltimoIntentoTotalAlumnos = notaMediaUltimoIntentoTotalAlumnos;
		this.maxIntentos = maxIntentos;
		this.totalIntentos = totalIntentos;
		this.skewness = skewness;
		this.kurtosis = kurtosis;
	}

	public QuizSummary() {
	}
    
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public int getTotalIntentos() {
		return totalIntentos;
	}
	public void setTotalIntentos(int totalIntentos) {
		this.totalIntentos = totalIntentos;
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
