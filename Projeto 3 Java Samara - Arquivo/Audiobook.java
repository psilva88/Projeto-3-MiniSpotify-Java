public class Audiobook extends Midia {
    public Audiobook(String titulo, String artista, double duracao, Genero genero) {
        super(titulo, artista, duracao, genero);
    }

    @Override
    public String toString() {
        return "Audiobook - Título: " + getTitulo() + " / Artista: " + getArtista() + " / Duração: "  + getDuracao() + " Minutos";
    }
}