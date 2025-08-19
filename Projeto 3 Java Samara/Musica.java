public class Musica extends Midia {
    public Musica(String titulo, String artista, double duracao, Genero genero) {
        super(titulo, artista, duracao, genero);
    }

    @Override
    public String toString() {
        return "Música - Título: " + getTitulo() + " / Artista: " + getArtista() + " / Gênero: (" + getGenero() + ") " + "/ Duração: "  + getDuracao() + " Minutos";
    }
}
