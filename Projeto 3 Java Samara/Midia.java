public abstract class Midia {
    private String titulo;
    private String artista;
    private double duracao;
    private Genero genero;

    public Midia(String titulo, String artista, double duracao, Genero genero) {
        this.titulo = titulo;
        this.artista = artista;
        this.duracao = duracao;
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public double getDuracao() {
        return duracao;
    }

    public Genero getGenero() {
        return genero;
    }

    @Override
    public String toString() {
        return titulo + " - " + artista + " (" + genero + ") " + duracao + " min";
    }
}
