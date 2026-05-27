package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        // Parse argumentos: formato é "valor,coluna,linha;valor,coluna,linha;..."
        // Resultado: Map com chave "linha,coluna" e valor "valor,fixo"
        final var positions = new java.util.HashMap<String, String>();
        if (args.length > 0) {
            String allData = String.join("", args);
            String[] items = allData.split(";");
            for (String item : items) {
                if (!item.trim().isEmpty()) {
                    String[] parts = item.split(",");
                    if (parts.length == 3) {
                        int valor = Integer.parseInt(parts[0].trim());
                        int coluna = Integer.parseInt(parts[1].trim());
                        int linha = Integer.parseInt(parts[2].trim());
                        // Chave: "linha,coluna", Valor: "valor,fixo(true se valor>0, false se valor==0)"
                        positions.put(linha + "," + coluna, valor + "," + (valor > 0 ? "true" : "false"));
                    }
                }
            }
        }
        var option = -1;
        while (true) {
            System.out.println("Selecione uma das opções a seguir:");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            // ler opção do menu validando entrada não-numérica e intervalo
            option = runUntilGetValidNumber(1, 8);

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber(scanner);
                case 3 -> removeNumber(scanner);
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções");
            }
        }
    }

    private static void startGame(final Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionConfig = positions.get(i + "," + j);
                int expected = 0;
                boolean fixed = false;

                if (positionConfig != null) {
                    String[] parts = positionConfig.split(",");
                    expected = Integer.parseInt(parts[0]);
                    fixed = Boolean.parseBoolean(parts[1]);
                }

                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");
    }

    private static void inputNumber(Scanner scanner) {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUntilGetValidNumber(1, 9);
        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }

    private static void removeNumber(Scanner scanner) {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.println("Informe a coluna que em que o número será inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");
        var row = runUntilGetValidNumber(0, 8);
        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col: board.getSpaces()) {
                args[argPos ++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BoardTemplate.BOARD_TEMPLATE) + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.printf("O jogo atualmente se econtra no status %s\n", board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("O jogo não contém erros");
        } else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo o seu progresso?");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")) {
            System.out.println("Informe 'sim' ou 'não'");
            confirm = scanner.next();
        }

        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
        }
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns! Você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros, verifique e faça as correções");
        } else {
            System.out.println("Você ainda precisa preencher algum espaço.");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        // Lê tokens do scanner e tenta converter para inteiro.
        // Se o usuário digitar algo que não é inteiro, captura e solicita novamente.
        while (true) {
            var token = scanner.next();
            try {
                var current = Integer.parseInt(token);
                if (current < min || current > max) {
                    System.out.printf("Informe um número entre %s e %s\n", min, max);
                    continue;
                }
                return current;
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Informe um número inteiro.");
            }
        }
    }
}
