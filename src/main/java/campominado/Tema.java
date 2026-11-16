package campominado;

import java.awt.Color;

// Todas as cores do jogo num lugar só (fica fácil trocar o visual depois)
public final class Tema {

    private Tema() { }

    public static final Color FUNDO          = new Color(0x1E1F26);
    public static final Color CELULA_FECHADA = new Color(0x464C62);
    public static final Color CELULA_HOVER   = new Color(0x585F7A);
    public static final Color CELULA_ABERTA  = new Color(0x272934);
    public static final Color EXPLOSAO       = new Color(0xE05561);
    public static final Color TEXTO          = new Color(0xE6E6E6);
    public static final Color TEXTO_SUAVE    = new Color(0x8A8FA3);
    public static final Color DESTAQUE       = new Color(0xFFB84D);

    // Cor de cada número (1 a 8)
    private static final Color[] CORES_NUMEROS = {
        null,
        new Color(0x5C9DFF), // 1 - azul
        new Color(0x4CC38A), // 2 - verde
        new Color(0xFF6B6B), // 3 - vermelho
        new Color(0xB18CFF), // 4 - roxo
        new Color(0xFFB84D), // 5 - laranja
        new Color(0x4DD0E1), // 6 - ciano
        new Color(0xE6E6E6), // 7 - branco
        new Color(0x9E9E9E)  // 8 - cinza
    };

    public static Color corDoNumero(int numero) {
        return CORES_NUMEROS[numero];
    }
}
