import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Catalago catalago = new Catalago();

        // Cadastro inicial
            System.out.println("=== Bem-vindo ao Mini Spotify ===");
            System.out.print("Digite seu nome: ");
            String nome = scan.nextLine();
            System.out.print("Digite seu e-mail: ");
            String email = scan.nextLine();    
            if (nome.isEmpty() || email.isEmpty()) {
                System.out.println("Nome e e-mail são obrigatórios. Encerrando o sistema.");
                scan.close();
                return;
            }        

        int opcao;

        do {
            System.out.println("\nSelecione uma das opções abaixo:");
            System.out.println("1 - 🔍 Listagem de mídias");
            System.out.println("2 - ➕ Adicionar uma nova mídia");
            System.out.println("3 - ❌ Excluir uma mídia");
            System.out.println("4 - 🔎 Pesquisar uma mídia");
            System.out.println("5 - 📊 Exibir quantidade total de mídias e playlists");
            System.out.println("6 - ➕ Criar uma playlist");
            System.out.println("7 - ❌ Excluir uma playlist");
            System.out.println("8 - ➕ Adicionar mídia a uma playlist");
            System.out.println("9 - ❌ Excluir mídia de uma playlist");
            System.out.println("10 - 📊 Visualizar playlists e mídias contidas");
            System.out.println("0 - 🚪 Sair do sistema\n");

            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1:
                    // 1 - Listagem de mídias
                    catalago.listarMidias();
                    break;
                case 2:
                    // 2 - Adicionar uma nova mídia
                    catalago.adicionarMidia();
                    break;
                case 3:
                    // 3 - Excluir uma mídia
                    catalago.excluirMidia();
                    break;
                case 4:
                    // 4 - Pesquisar uma mídia
                    catalago.pesquisarMidia();
                    break;
                case 5:
                    // 5 - Exibir quantidade total de mídias e playlists
                    catalago.quantidadeTotal();
                    break;
                case 6:
                    // 6 - Criar uma playlist
                    catalago.criarPlaylist();
                    break;
                case 7:
                    // 7 - Excluir uma playlist
                    catalago.excluirPlaylist();
                    break;
                case 8:
                    // 8 - Adicionar mídia a uma playlist
                    catalago.adicionarMidiaPlaylist();
                case 9:
                    // 9 - Excluir mídia de uma playlist
                    catalago.excluirMidiaPlaylist();
                case 10:
                    // 10 - Exibir playlists e mídias contidas
                    System.out.println("Playlists de " + nome + ":");
                    catalago.visualizarMidiasPlaylists();
                case 0:
                    // 0 - Sair do sistema
                    System.out.println("Sistema encerrado com sucesso!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scan.close();
    }
}