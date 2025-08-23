/*
 Mini Spotify — Solução ChatGPT (Projeto Console em Java)
 --------------------------------------------------------
 Como compilar e executar (sem pacotes):
 1) Salve este arquivo como Main.java
 2) Compile:  javac Main.java
 3) Execute:  java Main
 
 O que este projeto demonstra:
 - POO: encapsulamento, herança e polimorfismo
 - Estruturas: List, Set, Map (ArrayList, HashSet, HashMap)
 - Tratamento de exceções customizadas
 - Enum de gêneros musicais
 - Catálogo com busca por título, artista/autor/host ou gênero
 - Usuário pode criar/gerenciar playlists, adicionar/remover mídias e ver conteúdo

 Observação: Mantive tudo em um único arquivo (Main.java) para facilitar a correção.
 Somente a classe Main é pública; as demais são package-private.
*/

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/************************************
 * EXCEÇÕES ESPECÍFICAS DO DOMÍNIO  *
 ************************************/
class DomainException extends RuntimeException {
    public DomainException(String message) { super(message); }
}

class DuplicateMediaException extends DomainException {
    public DuplicateMediaException(String message) { super(message); }
}

class MediaNotFoundException extends DomainException {
    public MediaNotFoundException(String message) { super(message); }
}

class PlaylistNotFoundException extends DomainException {
    public PlaylistNotFoundException(String message) { super(message); }
}

class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) { super(message); }
}

/***********************
 * ENUM DE GÊNEROS     *
 ***********************/
enum Genero {
    ROCK, POP, MPB, JAZZ, CLASSICA, HIPHOP, ELETRONICA, SERTANEJO, FORRO, FUNK,
    METAL, BLUES, REGGAE, COUNTRY, INDIE, KPOP,

    // Para permitir buscas uniformes no catálogo, tratamos Podcast/Audiobook
    // como "gêneros" de mídia não-musical também:
    PODCAST, AUDIOBOOK
}

/********************************
 * MODELO PRINCIPAL DAS MÍDIAS  *
 ********************************/
abstract class Midia {
    private final String id;           // imutável, único
    private String titulo;             // encapsulado
    private String artistaOuAutor;     // pode ser artista (música), host (podcast) ou autor (audiobook)
    private int duracaoSegundos;       // duração total em segundos
    private Genero genero;

    protected Midia(String titulo, String artistaOuAutor, int duracaoSegundos, Genero genero) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título é obrigatório");
        if (artistaOuAutor == null || artistaOuAutor.isBlank()) throw new IllegalArgumentException("Artista/Autor/Host é obrigatório");
        if (duracaoSegundos <= 0) throw new IllegalArgumentException("Duração deve ser positiva (em segundos)");
        if (genero == null) throw new IllegalArgumentException("Gênero é obrigatório");
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo.trim();
        this.artistaOuAutor = artistaOuAutor.trim();
        this.duracaoSegundos = duracaoSegundos;
        this.genero = genero;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtistaOuAutor() { return artistaOuAutor; }
    public int getDuracaoSegundos() { return duracaoSegundos; }
    public Genero getGenero() { return genero; }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título inválido");
        this.titulo = titulo.trim();
    }

    public void setArtistaOuAutor(String artistaOuAutor) {
        if (artistaOuAutor == null || artistaOuAutor.isBlank()) throw new IllegalArgumentException("Artista/Autor/Host inválido");
        this.artistaOuAutor = artistaOuAutor.trim();
    }

    public void setDuracaoSegundos(int duracaoSegundos) {
        if (duracaoSegundos <= 0) throw new IllegalArgumentException("Duração deve ser positiva");
        this.duracaoSegundos = duracaoSegundos;
    }

    public void setGenero(Genero genero) {
        if (genero == null) throw new IllegalArgumentException("Gênero inválido");
        this.genero = genero;
    }

    // Polimorfismo: cada tipo de mídia pode customizar a forma de "toString"/detalhes
    public abstract String tipo();

    @Override
    public String toString() {
        return String.format("[%s] %s — %s (%s, %s)", tipo(), titulo, artistaOuAutor, formatarDuracao(duracaoSegundos), genero);
    }

    public static String formatarDuracao(int totalSegundos) {
        int horas = totalSegundos / 3600;
        int resto = totalSegundos % 3600;
        int minutos = resto / 60;
        int segundos = resto % 60;
        if (horas > 0) return String.format("%d:%02d:%02d", horas, minutos, segundos);
        return String.format("%d:%02d", minutos, segundos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Midia midia = (Midia) o;
        return Objects.equals(id, midia.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

class Musica extends Midia {
    public Musica(String titulo, String artista, int duracaoSegundos, Genero generoMusical) {
        super(titulo, artista, duracaoSegundos, generoMusical);
        if (generoMusical == Genero.PODCAST || generoMusical == Genero.AUDIOBOOK) {
            throw new IllegalArgumentException("Gênero de música não pode ser PODCAST/AUDIOBOOK");
        }
    }
    @Override public String tipo() { return "Música"; }
}

class Podcast extends Midia {
    private int episodios; // exemplo de campo específico
    public Podcast(String titulo, String host, int duracaoSegundos) {
        super(titulo, host, duracaoSegundos, Genero.PODCAST);
        this.episodios = 1;
    }
    public int getEpisodios() { return episodios; }
    public void setEpisodios(int episodios) { this.episodios = Math.max(1, episodios); }
    @Override public String tipo() { return "Podcast"; }
}

class Audiobook extends Midia {
    private String narrador; // campo específico
    public Audiobook(String titulo, String autor, String narrador, int duracaoSegundos) {
        super(titulo, autor, duracaoSegundos, Genero.AUDIOBOOK);
        this.narrador = (narrador == null || narrador.isBlank()) ? "Desconhecido" : narrador.trim();
    }
    public String getNarrador() { return narrador; }
    public void setNarrador(String narrador) { this.narrador = (narrador == null || narrador.isBlank()) ? this.narrador : narrador.trim(); }
    @Override public String tipo() { return "Audiobook"; }
}

/****************
 * CATÁLOGO     *
 ****************/
class Catalogo {
    // Armazenamento principal: id -> Mídia
    private final Map<String, Midia> porId = new HashMap<>();

    // Índices simples para busca rápida
    private final Map<String, Set<String>> idsPorTitulo = new HashMap<>();     // tituloLower -> ids
    private final Map<String, Set<String>> idsPorArtista = new HashMap<>();    // artistaLower -> ids
    private final Map<Genero, Set<String>> idsPorGenero = new HashMap<>();     // genero -> ids

    public void adicionar(Midia midia) {
        Objects.requireNonNull(midia, "Midia não pode ser nula");
        if (porId.containsKey(midia.getId())) {
            throw new DuplicateMediaException("Mídia já cadastrada no catálogo: " + midia.getTitulo());
        }
        porId.put(midia.getId(), midia);
        indexar(midia);
    }

    public Midia obterPorId(String id) {
        Midia m = porId.get(id);
        if (m == null) throw new MediaNotFoundException("Mídia não encontrada por id");
        return m;
    }

    public List<Midia> buscarPorTitulo(String termo) {
        if (termo == null) return List.of();
        String key = termo.toLowerCase();
        Set<String> ids = idsPorTitulo.getOrDefault(key, Set.of());
        return coletar(ids);
    }

    public List<Midia> buscarPorArtista(String termo) {
        if (termo == null) return List.of();
        String key = termo.toLowerCase();
        Set<String> ids = idsPorArtista.getOrDefault(key, Set.of());
        return coletar(ids);
    }

    public List<Midia> buscarPorGenero(Genero genero) {
        if (genero == null) return List.of();
        Set<String> ids = idsPorGenero.getOrDefault(genero, Set.of());
        return coletar(ids);
    }

    public List<Midia> listarTudo() { return new ArrayList<>(porId.values()); }

    private List<Midia> coletar(Set<String> ids) {
        List<Midia> lista = new ArrayList<>();
        for (String id : ids) {
            Midia m = porId.get(id);
            if (m != null) lista.add(m);
        }
        return lista;
    }

    private void indexar(Midia m) {
        idsPorTitulo.computeIfAbsent(m.getTitulo().toLowerCase(), k -> new HashSet<>()).add(m.getId());
        idsPorArtista.computeIfAbsent(m.getArtistaOuAutor().toLowerCase(), k -> new HashSet<>()).add(m.getId());
        idsPorGenero.computeIfAbsent(m.getGenero(), k -> new HashSet<>()).add(m.getId());
    }
}

/****************
 * PLAYLIST     *
 ****************/
class Playlist {
    private final String nome;
    private final List<Midia> itens = new ArrayList<>();

    public Playlist(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da playlist é obrigatório");
        this.nome = nome.trim();
    }

    public String getNome() { return nome; }
    public List<Midia> getItens() { return Collections.unmodifiableList(itens); }

    public void adicionar(Midia m) {
        Objects.requireNonNull(m, "Midia não pode ser nula");
        itens.add(m);
    }

    public boolean removerPrimeiraOcorrenciaPorId(String id) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId().equals(id)) {
                itens.remove(i);
                return true;
            }
        }
        return false;
    }

    public int duracaoTotalSegundos() {
        int total = 0;
        for (Midia m : itens) total += m.getDuracaoSegundos();
        return total;
    }

    public String resumo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist: ").append(nome)
          .append(" | Itens: ").append(itens.size())
          .append(" | Duração: ").append(Midia.formatarDuracao(duracaoTotalSegundos()))
          .append("\n");
        AtomicInteger idx = new AtomicInteger(1);
        itens.forEach(m -> sb.append(String.format("%02d. %s\n", idx.getAndIncrement(), m.toString())));
        return sb.toString();
    }
}

/************
 * USUÁRIO  *
 ************/
class Usuario {
    private final String nome;
    private final String email;
    private final Map<String, Playlist> playlists = new LinkedHashMap<>(); // preserva ordem de inserção

    public Usuario(String nome, String email) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório");
        if (email == null || email.isBlank() || !email.contains("@")) throw new IllegalArgumentException("E-mail inválido");
        this.nome = nome.trim();
        this.email = email.trim();
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public void criarPlaylist(String nome) {
        String key = nome.trim().toLowerCase();
        if (playlists.containsKey(key)) throw new DomainException("Já existe uma playlist com esse nome");
        playlists.put(key, new Playlist(nome));
    }

    public Playlist obterPlaylist(String nome) {
        Playlist p = playlists.get(nome.trim().toLowerCase());
        if (p == null) throw new PlaylistNotFoundException("Playlist não encontrada: " + nome);
        return p;
    }

    public void removerPlaylist(String nome) {
        if (playlists.remove(nome.trim().toLowerCase()) == null) throw new PlaylistNotFoundException("Playlist não encontrada");
    }

    public Collection<Playlist> listarPlaylists() { return Collections.unmodifiableCollection(playlists.values()); }
}

/************************
 * APLICAÇÃO (CLI)      *
 ************************/
public class Mini_Spotify_ChatGPT {
    private static final Scanner scan = new Scanner(System.in);
    private static final Catalogo catalogo = new Catalogo();
    private static Usuario usuarioAtual = null;

    public static void main(String[] args) {
        seedCatalogo(); // dados iniciais
        System.out.println("=== Bem-vindo ao Mini Spotify ===");
        boolean rodando = true;
        while (rodando) {
            try {
                mostrarMenu();
                String opc = scan.nextLine().trim();
                switch (opc) {
                    case "1" -> cadastrarUsuario();
                    case "2" -> criarPlaylist();
                    case "3" -> adicionarMidiaNaPlaylist();
                    case "4" -> removerMidiaDaPlaylist();
                    case "5" -> visualizarPlaylists();
                    case "6" -> buscarNoCatalogo();
                    case "7" -> listarCatalogo();
                    case "0" -> { System.out.println("Saindo. Até mais!"); rodando = false; }
                    default -> System.out.println("Opção inválida. Tente novamente.\n");
                }
            } catch (DomainException | IllegalArgumentException e) {
                System.out.println("[Erro] " + e.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("[Erro inesperado] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1) Cadastrar usuário");
        System.out.println("2) Criar playlist");
        System.out.println("3) Adicionar mídia em playlist");
        System.out.println("4) Remover mídia da playlist");
        System.out.println("5) Visualizar minhas playlists");
        System.out.println("6) Buscar no catálogo");
        System.out.println("7) Listar catálogo completo");
        System.out.println("0) Sair");
        System.out.print("Escolha: ");
    }

    private static void cadastrarUsuario() {
        System.out.println("=== Cadastro de Usuário ===");
        System.out.print("Digite seu nome: ");
        String nome = scan.nextLine();
        System.out.print("Digite seu e-mail: ");
        String email = scan.nextLine();
        usuarioAtual = new Usuario(nome, email);
        System.out.println("Usuário cadastrado: " + usuarioAtual.getNome() + " (" + usuarioAtual.getEmail() + ")\n");
    }

    private static void exigirUsuario() {
        if (usuarioAtual == null) throw new UserNotFoundException("Nenhum usuário logado. Cadastre-se primeiro (opção 1)");
    }

    private static void criarPlaylist() {
        exigirUsuario();
        System.out.print("Nome da nova playlist: ");
        String nome = scan.nextLine();
        usuarioAtual.criarPlaylist(nome);
        System.out.println("Playlist criada com sucesso!\n");
    }

    private static void adicionarMidiaNaPlaylist() {
        exigirUsuario();
        System.out.print("Nome da playlist destino: ");
        String nome = scan.nextLine();
        Playlist p = usuarioAtual.obterPlaylist(nome);
        System.out.print("Buscar mídia por (1) Título, (2) Artista/Autor/Host, (3) Gênero: ");
        String modo = scan.nextLine().trim();
        List<Midia> resultados = new ArrayList<>();
        switch (modo) {
            case "1" -> {
                System.out.print("Título exato: ");
                resultados = catalogo.buscarPorTitulo(scan.nextLine());
            }
            case "2" -> {
                System.out.print("Artista/Autor/Host exato: ");
                resultados = catalogo.buscarPorArtista(scan.nextLine());
            }
            case "3" -> {
                System.out.print("Gênero (ex: ROCK, POP, MPB, PODCAST, AUDIOBOOK): ");
                Genero g = Genero.valueOf(scan.nextLine().trim().toUpperCase());
                resultados = catalogo.buscarPorGenero(g);
            }
            default -> {
                System.out.println("Modo inválido");
                return;
            }
        }
        if (resultados.isEmpty()) {
            System.out.println("Nenhuma mídia encontrada.");
            return;
        }
        System.out.println("Resultados:");
        for (int i = 0; i < resultados.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, resultados.get(i));
        }
        System.out.print("Escolha o número para adicionar: ");
        int idx = Integer.parseInt(scan.nextLine().trim());
        if (idx < 1 || idx > resultados.size()) {
            System.out.println("Índice inválido");
            return;
        }
        p.adicionar(resultados.get(idx - 1));
        System.out.println("Mídia adicionada à playlist!\n");
    }

    private static void removerMidiaDaPlaylist() {
        exigirUsuario();
        System.out.print("Nome da playlist: ");
        String nome = scan.nextLine();
        Playlist p = usuarioAtual.obterPlaylist(nome);
        System.out.print("Informe (1) índice na playlist OU (2) id da mídia: ");
        String modo = scan.nextLine().trim();
        if (modo.equals("1")) {
            listarPlaylistDetalhada(p);
            System.out.print("Número do item para remover: ");
            try {
                int idx = Integer.parseInt(scan.nextLine().trim());
                String id = p.getItens().get(idx - 1).getId();
                boolean ok = p.removerPrimeiraOcorrenciaPorId(id);
                System.out.println(ok ? "Removido!" : "Não encontrado.");
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Índice inválido.");
            }
        } else if (modo.equals("2")) {
            System.out.print("Digite o id: ");
            String id = scan.nextLine().trim();
            boolean ok = p.removerPrimeiraOcorrenciaPorId(id);
            System.out.println(ok ? "Removido!" : "ID não encontrado na playlist.");
        } else {
            System.out.println("Modo inválido");
        }
    }

    private static void visualizarPlaylists() {
        exigirUsuario();
        if (usuarioAtual.listarPlaylists().isEmpty()) {
            System.out.println("Você ainda não tem playlists.");
            return;
        }
        for (Playlist p : usuarioAtual.listarPlaylists()) {
            System.out.println(p.resumo());
        }
    }

    private static void listarPlaylistDetalhada(Playlist p) {
        System.out.println(p.resumo());
    }

    private static void buscarNoCatalogo() {
        System.out.println("=== Busca no Catálogo ===");
        System.out.print("(1) Por título  (2) Por artista/autor/host  (3) Por gênero: ");
        String opc = scan.nextLine().trim();
        List<Midia> res = switch (opc) {
            case "1" -> { System.out.print("Título exato: "); yield catalogo.buscarPorTitulo(scan.nextLine()); }
            case "2" -> { System.out.print("Artista/Autor/Host exato: "); yield catalogo.buscarPorArtista(scan.nextLine()); }
            case "3" -> { System.out.print("Gênero (ex: ROCK, PODCAST): "); yield catalogo.buscarPorGenero(Genero.valueOf(scan.nextLine().trim().toUpperCase())); }
            default -> { System.out.println("Opção inválida"); yield List.of(); }
        };
        if (res.isEmpty()) System.out.println("Nenhum resultado.");
        else res.forEach(m -> System.out.println("- " + m));
    }

    private static void listarCatalogo() {
        System.out.println("=== Catálogo Completo ===");
        List<Midia> todos = catalogo.listarTudo();
        if (todos.isEmpty()) { System.out.println("(vazio)"); return; }
        todos.forEach(m -> System.out.println("- " + m));
    }

    // Dados iniciais para testes
    private static void seedCatalogo() {
        catalogo.adicionar(new Musica("Garota de Ipanema", "Tom Jobim", 330, Genero.MPB));
        catalogo.adicionar(new Musica("Bohemian Rhapsody", "Queen", 354, Genero.ROCK));
        catalogo.adicionar(new Musica("Take Five", "Dave Brubeck", 325, Genero.JAZZ));
        catalogo.adicionar(new Musica("Nocturne Op.9 No.2", "Chopin", 260, Genero.CLASSICA));
        catalogo.adicionar(new Musica("Rolling in the Deep", "Adele", 228, Genero.POP));
        catalogo.adicionar(new Podcast("Ciência Sem Filtro #42", "Dra. Ana Souza", 3600));
        catalogo.adicionar(new Podcast("TechWeekly: IA na prática", "Carlos Lima", 2400));
        catalogo.adicionar(new Audiobook("Dom Casmurro", "Machado de Assis", "João Silva", 28800));
        catalogo.adicionar(new Audiobook("O Alienista", "Machado de Assis", "Maria Rocha", 21600));
    }
}
