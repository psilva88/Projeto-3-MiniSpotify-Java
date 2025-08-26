import java.util.*;

public class Catalago {
private List<Midia> midias = new ArrayList<>();
private List<Playlist> playLists = new ArrayList<>();
private Scanner scan = new Scanner(System.in);

//  1 - Listagem de mídias
    public void listarMidias() {
        if (midias.isEmpty()) {
            System.out.println("Erro! Nenhuma mídia cadastrada.");
        } else {
            System.out.println("Lista de mídias: ");
            for (Midia m : midias) {
                System.out.println(m.toString());
            }
        }
    } 
    
//  2 - Adicionar uma nova mídia
    public void adicionarMidia() {
        try {
            System.out.println("Qual tipo de mídia deseja adicionar?");
            System.out.println("1 - Música");
            System.out.println("2 - Podcast");
            System.out.println("3 - Audiobook");
            System.out.print("Opção: ");
            int tipo = Integer.parseInt(scan.nextLine());
            if (tipo < 1 || tipo > 3) {
                System.out.println("Tipo de mídia inválido.");
                return;
            }

            System.out.print("Título: ");
            String titulo = scan.nextLine();
            System.out.print("Artista: ");
            String artista = scan.nextLine();
            System.out.print("Duração (minutos): ");
            double duracao = Double.parseDouble(scan.nextLine());
            if (duracao <= 0) {
                System.out.println("Duração não pode ser zero.");
                return;
            }

            Midia novaMidia = null;

            switch (tipo) {
                case 1:
                    System.out.println("Selecione o gênero:");
                    int i = 1;
                    for (Genero g : Genero.values()){
                        System.out.println(i + ". " + g.getVariavelTemporaria());
                        i++;
                    }

                    Genero genero = selecionarGenero();
                    /* 
                    String selecaoGeneroInput = scan.nextLine();
                    if (selecaoGeneroInput.matches("\\d+")) { 
                        generoOpcao = Integer.parseInt(selecaoGeneroInput);
                        if (generoOpcao >= 1 && generoOpcao <= Genero.values().length) {
                            generoSelecionado = Genero.values()[generoOpcao - 1];
                            genero = generoSelecionado;
                        } else {
                             System.out.println("Opção de gênero inválida.");
                             return;
                        }
                    } else {
                        System.out.println("Opção de gênero inválida. Digite um número.");
                        return;
                    }*/
                    novaMidia = new Musica(titulo, artista, duracao, genero);
                    break;
                case 2:
                    novaMidia = new Podcast(titulo, artista, duracao, null);
                    break;
                case 3:
                    novaMidia = new Audiobook(titulo, artista, duracao, null);
                    break;
                default:
                    System.out.println("Tipo de mídia inválido.");
                    return;
            }

            midias.add(novaMidia);
            System.out.println("Mídia adicionada com sucesso!");

        } catch (NumberFormatException e ) {
            System.out.println("Erro! Formato de número inválido. Tente novamente.");
        }
    }

    public Genero selecionarGenero(){
        int generoOpcao = -1;
        Genero genero = null;
        Genero generoSelecionado = null;
        String selecaoGeneroInput = scan.nextLine();
        if (selecaoGeneroInput.matches("\\d+")) { 
            generoOpcao = Integer.parseInt(selecaoGeneroInput);
            if (generoOpcao >= 1 && generoOpcao <= Genero.values().length) {
                generoSelecionado = Genero.values()[generoOpcao - 1];
                genero = generoSelecionado;
            } else {
                    System.out.println("Opção de gênero inválida.");
                    return null;
            }
        } else {
            System.out.println("Opção de gênero inválida. Digite um número.");
            return null;
        }
        return genero;
    }

  
//  3 - Excluir uma mídia.   
    public void excluirMidia() {
        if (midias.isEmpty()) {
            System.out.println("Nenhuma mídia para excluir.");
            return;
        }
        System.out.println("Mídias cadastradas:");
        for (int i = 0; i < midias.size(); i++) {
            System.out.println(i+1 + " - " + midias.get(i).getTitulo());
        }
        System.out.print("Digite o número da mídia que deseja excluir: ");
        try {
            int index = (Integer.parseInt(scan.nextLine())-1);
            if (index >= 0 && index < midias.size()) {
                Midia midiaRemovida = midias.remove(index);
                System.out.println("Mídia '" + midiaRemovida.getTitulo() + "' removida com sucesso.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro! Entrada inválida. Digite um número.");
        }
    }


//  4 - Pesquisar uma mídia.
    public void pesquisarMidia()throws CodigoInvalidoException{
        System.out.println("Como deseja pesquisar a mídia?");
        System.out.println("1 - Por Título");
        System.out.println("2 - Por Artista");
        System.out.println("3 - Por Gênero");
        System.out.print("Opção: ");
        String opcao = scan.nextLine();
        List<Midia> resultados = new ArrayList<>();

        switch (opcao) {
            case "1":
                System.out.print("Digite o título: ");
                String titulo = scan.nextLine();
                for (Midia midia : midias) {
                    if (midia.getTitulo().equalsIgnoreCase(titulo)) {
                        resultados.add(midia);
                    }
                }
                break;
            case "2":
                System.out.print("Digite o nome do artista: ");
                String artista = scan.nextLine();
                for (Midia midia : midias) {
                    if (midia.getArtista().equalsIgnoreCase(artista)) {
                        resultados.add(midia);
                    }
                }
                break;
            case "3":
                System.out.print("Digite o gênero: ");
                String generoString = scan.nextLine();
                Genero genero = Genero.fromVariavelTemporaria(generoString);
                if (genero != null) {
                    for (Midia midia : midias) {
                        if (midia.getGenero() == genero) {
                            resultados.add(midia);
                        }
                    }
                } else {
                    System.out.println("Gênero inválido.");
                }
                break;
            default:
                System.out.println("Opção de pesquisa inválida.");
                return;
        }

        if (resultados == null || resultados.isEmpty()) {
            throw new CodigoInvalidoException("Código inválido! O código não pode ser encontrado ou estar vazio.");
        } else {
            System.out.println("Resultados da pesquisa:");
            for (Midia m : resultados) {
                System.out.println(m.toString());
            }
        }
    }

//  5 - Exibir quantidade total de mídias e playlists.
    public void quantidadeTotal() {
        System.out.println("Quantidade total de mídias: " + midias.size());
        System.out.println("Quantidade total de playlists: " + playLists.size());
    }
    
    /**
     * 6 - Criar uma playlist.
     * Cria uma nova playlist com um nome fornecido pelo usuário.
     */
    public void criarPlaylist() {
        System.out.print("Digite o nome da nova playlist: ");
        String nomePlaylist = scan.nextLine();
        if (nomePlaylist.isEmpty()) {
            System.out.println("Nome da playlist não pode ser vazio.");
            return;
        }
        for (Playlist p : playLists) {
            if (p.getNome().equalsIgnoreCase(nomePlaylist)) {
                System.out.println("Já existe uma playlist com esse nome.");
                return;
            }
        }
        Playlist novaPlaylist = new Playlist(nomePlaylist);
        playLists.add(novaPlaylist);
        System.out.println("Playlist '" + nomePlaylist + "' criada com sucesso.");
    }


//  7 - Excluir uma playlist.
    public void excluirPlaylist() {
        if (playLists.isEmpty()) {
            System.out.println("Nenhuma playlist para excluir.");
            return;
        }
        System.out.println("Playlists cadastradas:");
        for (int i = 0; i < playLists.size(); i++) {
            System.out.println(i+1 + " - " + playLists.get(i).getNome());
        }
        System.out.print("Digite o número da playlist que deseja excluir: ");
        try {
            int index = (Integer.parseInt(scan.nextLine())-1);
            if (index >= 0 && index < playLists.size()) {
                Playlist playlistRemovida = playLists.remove(index);
                System.out.println("Playlist '" + playlistRemovida.getNome() + "' removida com sucesso.");
            } else {
                System.out.println("Índice inválido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro! Entrada inválida. Digite um número.");
        }
    }


//  8 - Adicionar mídia a uma playlist.
    public void adicionarMidiaPlaylist() {
        if (midias.isEmpty() || playLists.isEmpty()) {
            System.out.println("É necessário ter mídias e playlists cadastradas.");
            return;
        }
        
        System.out.println("Selecione a playlist:");
        for (int i = 0; i < playLists.size(); i++) {
            System.out.println(i+1 + " - " + playLists.get(i).getNome());
        }
        System.out.print("Número da playlist: ");
        int indexPlaylist = (Integer.parseInt(scan.nextLine())-1);

        System.out.println("Selecione a mídia:");
        for (int i = 0; i < midias.size(); i++) {
            System.out.println(i+1 + " - " + midias.get(i).getTitulo());
        }
        System.out.print("Número da mídia: ");
        int indexMidia = (Integer.parseInt(scan.nextLine())-1);

        try {
            Playlist playlist = playLists.get(indexPlaylist);
            Midia midia = midias.get(indexMidia);
            if (playlist.getMidias().contains(midia)) {
                System.out.println("Mídia já está na playlist.");
                return;
            }
            else
            playlist.adicionarMidia(midia);
            System.out.println("Mídia '" + midia.getTitulo() + "' adicionada à playlist '" + playlist.getNome() + "' com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Não é permitido usar a mesma mídia em mais de uma playlist.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Índice de playlist ou mídia inválido.");
        }       
    }
    


//  9 - Excluir mídia de uma playlist.
    public void excluirMidiaPlaylist() {
        if (playLists.isEmpty()) {
            System.out.println("Nenhuma playlist para gerenciar.");
            return;
        }
        System.out.println("Selecione a playlist:");
        for (int i = 0; i < playLists.size(); i++) {
            System.out.println(i+1 + " - " + playLists.get(i).getNome());
        }
        System.out.print("Número da playlist: ");
        int indexPlaylist = (Integer.parseInt(scan.nextLine())-1);

        try {
            Playlist playlist = playLists.get(indexPlaylist);
            if (playlist.getMidias().isEmpty()) {
                System.out.println("Esta playlist não possui mídias.");
                return;
            }
            System.out.println("Selecione a mídia para remover:");
            for (int i = 0; i < playlist.getMidias().size(); i++) {
                System.out.println(i+1 + " - " + playlist.getMidias().get(i).getTitulo());
            }
            System.out.print("Número da mídia: ");
            int indexMidia = (Integer.parseInt(scan.nextLine())-1);

            Midia midiaRemover = playlist.getMidias().get(indexMidia);
            playlist.removerMidia(midiaRemover);
            System.out.println("Mídia '" + midiaRemover.getTitulo() + "' removida da playlist '" + playlist.getNome() + "' com sucesso.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Índice de playlist ou mídia inválido.");
        } catch (NumberFormatException e) {
            System.out.println("Erro! Entrada inválida.");
        }
    }
    

//  10 - Exibir playlists e mídias contidas.
    public void visualizarMidiasPlaylists() {
        if (playLists.isEmpty()) {
            System.out.println("Nenhuma playlist cadastrada.");
            return;
        }
        for (Playlist playlist : playLists) {
            System.out.println(playlist.toString());
            if (playlist.getMidias().isEmpty()) {
                System.out.println("  - Playlist vazia");
            } else {
                for (Midia midia : playlist.getMidias()) {
                    System.out.println("  - " + midia.toString());
                }
            }
        }
    }
}