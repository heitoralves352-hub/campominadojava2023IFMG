package campominado;


import java.util.Random;

public class Tabuleiro {

    private final int linhas;
    private final int colunas;
    private final int totalMinas;
    private final Celula[][] celulas;

    private boolean minasColocadas = false;  // as minas só são sorteadas no 1º clique
    private boolean perdeu = false;
    private boolean ganhou = false;
    private int celulasAbertas = 0;
    private int bandeiras = 0;

    public Tabuleiro(Dificuldade dificuldade) {
        this.linhas = dificuldade.getLinhas();
        this.colunas = dificuldade.getColunas();
        this.totalMinas = dificuldade.getMinas();

        celulas = new Celula[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                celulas[i][j] = new Celula();
            }
        }
    }

    // ===================== AÇÕES DO JOGADOR =====================

    public void abrir(int linha, int coluna) {
        if (isJogoTerminado() || !dentroDoCampo(linha, coluna)) return;

        Celula celula = celulas[linha][coluna];
        if (celula.isAberta() || celula.temBandeira()) return;

        // Primeiro clique: sorteia as minas longe dele (nunca perde no 1º clique)
        if (!minasColocadas) {
            colocarMinas(linha, coluna);
            minasColocadas = true;
        }

        if (celula.temMina()) {
            celula.abrir();
            perdeu = true;
            return;
        }

        abrirEmCascata(linha, coluna);

        if (celulasAbertas == (linhas * colunas) - totalMinas) {
            ganhou = true;
        }
    }

    public void alternarBandeira(int linha, int coluna) {
        if (isJogoTerminado() || !dentroDoCampo(linha, coluna)) return;

        Celula celula = celulas[linha][coluna];
        if (celula.isAberta()) return;

        celula.alternarBandeira();
        bandeiras += celula.temBandeira() ? 1 : -1;
    }

    // ===================== LÓGICA INTERNA =====================

    // Abre a célula e, se ela não tiver minas em volta, abre as vizinhas também (recursão)
    private void abrirEmCascata(int linha, int coluna) {
        if (!dentroDoCampo(linha, coluna)) return;

        Celula celula = celulas[linha][coluna];
        if (celula.isAberta() || celula.temBandeira() || celula.temMina()) return;

        celula.abrir();
        celulasAbertas++;

        if (celula.getMinasVizinhas() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        abrirEmCascata(linha + i, coluna + j);
                    }
                }
            }
        }
    }

    private void colocarMinas(int linhaSegura, int colunaSegura) {
        Random random = new Random();
        int colocadas = 0;

        while (colocadas < totalMinas) {
            int linha = random.nextInt(linhas);
            int coluna = random.nextInt(colunas);

            // não coloca mina no 1º clique nem em volta dele
            boolean pertoDoClique = Math.abs(linha - linhaSegura) <= 1
                                 && Math.abs(coluna - colunaSegura) <= 1;

            if (!celulas[linha][coluna].temMina() && !pertoDoClique) {
                celulas[linha][coluna].colocarMina();
                incrementarVizinhos(linha, coluna);
                colocadas++;
            }
        }
    }

    private void incrementarVizinhos(int linha, int coluna) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (dentroDoCampo(linha + i, coluna + j)) {
                    celulas[linha + i][coluna + j].incrementarMinasVizinhas();
                }
            }
        }
    }

    private boolean dentroDoCampo(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    // ===================== INFORMAÇÕES PARA A TELA =====================

    public Celula getCelula(int linha, int coluna) { return celulas[linha][coluna]; }
    public int getLinhas() { return linhas; }
    public int getColunas() { return colunas; }
    public int getMinasRestantes() { return totalMinas - bandeiras; }
    public boolean isPerdeu() { return perdeu; }
    public boolean isGanhou() { return ganhou; }
    public boolean isJogoTerminado() { return perdeu || ganhou; }
}