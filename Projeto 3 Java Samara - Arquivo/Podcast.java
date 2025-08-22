public class Podcast extends Midia {
    public Podcast(String titulo, String artista, double duracao, Genero genero) {
        super(titulo, artista, duracao, genero);
    }

    @Override
    public String toString() {
        return "Podcast - Título: " + getTitulo() + " / Artista: " + getArtista() + " / Duração: "  + getDuracao() + " Minutos";
    }
}