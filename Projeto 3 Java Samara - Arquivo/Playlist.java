import java.util.*;

public class Playlist {
    private String nome;
    private List<Midia> midias = new ArrayList<>();

    public Playlist(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Midia> getMidias() {
        return midias;
    }

    public void adicionarMidia(Midia midia) {
        midias.add(midia);
    }

    public void removerMidia(Midia midia) {
        midias.remove(midia);
    }

    @Override
    public String toString() {
        return "Playlist: " + nome;
    }
}