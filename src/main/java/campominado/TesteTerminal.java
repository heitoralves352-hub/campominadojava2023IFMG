package src.main.java.campominado;



// Classe temporária, só para testar a lógica antes de criar a janela
public class TesteTerminal {

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(Dificuldade.INICIANTE);

        tabuleiro.abrir(4, 4);          // primeiro clique no meio do campo
        tabuleiro.alternarBandeira(0, 0);

        mostrar(tabuleiro);
        System.out.println("Minas restantes: " + tabuleiro.getMinasRestantes());
        System.out.println("Perdeu? " + tabuleiro.isPerdeu() + " | Ganhou? " + tabuleiro.isGanhou());
    }

    private static void mostrar(Tabuleiro t) {
        System.out.print("   ");
        for (int j = 0; j < t.getColunas(); j++) System.out.print(j + " ");
        System.out.println();

        for (int i = 0; i < t.getLinhas(); i++) {
            System.out.print(i + "  ");
            for (int j = 0; j < t.getColunas(); j++) {
                Celula c = t.getCelula(i, j);
                char simbolo;
                if (c.temBandeira())                simbolo = 'F';
                else if (!c.isAberta())             simbolo = '-';
                else if (c.temMina())               simbolo = '*';
                else if (c.getMinasVizinhas() == 0) simbolo = ' ';
                else simbolo = (char) ('0' + c.getMinasVizinhas());
                System.out.print(simbolo + " ");
            }
            System.out.println();
        }
    }
}
