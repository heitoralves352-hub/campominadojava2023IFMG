package campominado;

// Grupo fixo de constantes que podem ser usadas em todo o projeto


public enum Dificuldade {

    
INICIANTE("Iniciante", 9, 9,10),
INTERMEDIARIO("Intermediario", 16, 16, 40),
AVANCADO("Avançado", 16, 30, 99);
    
    private final String nome;
    private final int linhas;
    private final int colunas;
    private final int minas;

    Dificuldade(String nome, int linhas, int colunas, int minas) {
        this.nome = nome;
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
    }

    public String getNome() { return nome; }
    public int getLinhas() { return linhas; }
    public int getColunas() { return colunas; }
    public int getMinas() { return minas; }
}

