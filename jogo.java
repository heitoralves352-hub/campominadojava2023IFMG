import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class jogo {

    int inicianteL = 9;
    int inicianteC = 9;
    int inicianteM = 10;

    int intermediarioL = 16;
    int intermediarioC = 16;
    int intermediarioM = 40;

    int avancadoL = 16;
    int avancadoC = 30;
    int avancadoM = 99;

    char OCULTO = '-';
    char mina = '*';
    

    char[][] campoMinado;
    char[][] campoVisivel;
    int linhas;
    int colunas;
    int minas;      // recebe o nivel
    int quadradosDescobertos;
    boolean jogoTerminado;


    public void definirDificuldade() {
        Scanner teclado = new Scanner(System.in);
        int opc;
        System.out.println("Qual dificuldade você deseja?");
        System.out.println("1 - Iniciante");
        System.out.println("2 - Intermediário");
        System.out.println("3 - Avançado");
        opc = teclado.nextInt();

        switch (opc) {
            case 1:
                linhas = inicianteL;
                colunas = inicianteC;
                minas = inicianteM;
                break;
            case 2:
                linhas = intermediarioL;
                colunas = intermediarioC;
                minas = intermediarioM;
                break;
            case 3:
                linhas = avancadoL;
                colunas = avancadoC;
                minas = avancadoM;
                break;
            default:
                System.out.println("Não existe essa opção. Escolha uma opção válida.");
                definirDificuldade();
                break;
        }
    }
     public void exibirCampoVisivel() {
        System.out.println("Campo Minado:");
        for (int i = 0; i < linhas; i++) {                  //Metodo criado para exibir o campo minado//
            for (int j = 0; j < colunas; j++) { 
                System.out.print(campoVisivel[i][j] + " ");
            }
            System.out.println();
        }
    }

      public void criarCampoMinado() {
        campoMinado = new char[linhas][colunas];       //campo original//
        campoVisivel = new char[linhas][colunas];      //campo que é mostrado para o usuario//
        for (int i = 0; i < linhas; i++) {
            Arrays.fill(campoMinado[i], ' ');       //  preenche todos os valores do array a partir do índice inicial a um índice final//
            Arrays.fill(campoVisivel[i], OCULTO);
        }

        Random random = new Random();

        for (int i = 0; i < minas; i++) {
            int linha, coluna;
            do {                                        // distribuição das minas no campo//
                linha = random.nextInt(linhas);
                coluna = random.nextInt(colunas);
            } while (campoMinado[linha][coluna] == mina);

            campoMinado[linha][coluna] = mina;
            incrementarNumerosVizinhos(linha, coluna);      // Chama a função para atualizar números vizinhos
        }
    }


   

    public void incrementarNumerosVizinhos(int linha, int coluna) {
        for (int i = -1; i <= 1; i++) {     // Loop para percorrer as células vizinhas na vertical
            for (int j = -1; j <= 1; j++) { // Loop para percorrer as células vizinhas na horizontal
                int linhaVizinha = linha + i;
                int colunaVizinha = coluna + j;

                // Verifica se a célula vizinha está dentro dos limites do tabuleiro e não contém uma mina
                if (linhaVizinha >= 0 && linhaVizinha < linhas && colunaVizinha >= 0 && colunaVizinha < colunas
                        && campoMinado[linhaVizinha][colunaVizinha] != mina) {

                    // Se a célula vizinha estiver vazia, incrementa para '1'
                    if (campoMinado[linhaVizinha][colunaVizinha] == ' ') {
                        campoMinado[linhaVizinha][colunaVizinha] = '1';
                    // Se a célula vizinha não estiver vazia e não for '9', incrementa seu valor
                    } else if (campoMinado[linhaVizinha][colunaVizinha] != '9') {
                        campoMinado[linhaVizinha][colunaVizinha]++;
                    }
                }
            }
        }
    }

  


    public void iniciarJogo() {
        Scanner teclado = new Scanner(System.in);
        definirDificuldade();
        criarCampoMinado();
        jogoTerminado = false;
        quadradosDescobertos = 0;                       // metodo que faz o jogo acontecer//
        int linha;
        int coluna;

        while (jogoTerminado != true) {
            exibirCampoVisivel();
            System.out.print("Digite a linha: ");
            linha = teclado.nextInt();
            System.out.print("Digite a coluna: ");
            coluna = teclado.nextInt();

            if (campoVisivel[linha][coluna] == OCULTO) {
                campoVisivel[linha][coluna] = campoMinado[linha][coluna];

                if (campoMinado[linha][coluna] == mina) {
                    exibirCampoVisivel();
                    System.out.println("Você perdeu o jogo!");
                    jogoTerminado = true;
                } else {
                    quadradosDescobertos++;
                    if (quadradosDescobertos == (linhas * colunas) - minas) {
                        exibirCampoVisivel();
                        System.out.println("Parabéns, você venceu o jogo!");

                    }
                }
            } else {
                System.out.println("Essa posição já foi selecionada. Escolha outra posição.");
            }
        }
    }

   

    public static void main(String args[]) {
        Scanner teclado = new Scanner(System.in);
        jogo xxx = new jogo();
        xxx.iniciarJogo();
    }

}