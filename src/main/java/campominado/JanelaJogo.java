package campominado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

public class JanelaJogo extends JFrame {

    private static final int TAMANHO_CELULA = 32;

    private Dificuldade dificuldade = Dificuldade.INICIANTE;
    private Tabuleiro tabuleiro;
    private JButton[][] botoes;

    private final JPanel painelCampo = new JPanel();
    private final JLabel lblMinas = new JLabel();
    private final JButton btnReiniciar = new JButton("Novo jogo");

    public JanelaJogo() {
        setTitle("Campo Minado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        criarMenu();
        criarBarraSuperior();
        add(painelCampo, BorderLayout.CENTER);

        novoJogo(dificuldade);
    }

    // ===================== MONTAGEM DA TELA =====================

    private void criarMenu() {
        JMenuBar barraMenu = new JMenuBar();
        JMenu menuJogo = new JMenu("Jogo");

        JMenuItem itemNovo = new JMenuItem("Novo jogo");
        itemNovo.addActionListener(e -> novoJogo(dificuldade));
        menuJogo.add(itemNovo);
        menuJogo.addSeparator();

        // Um item de menu para cada dificuldade (só um pode ficar marcado)
        ButtonGroup grupo = new ButtonGroup();
        for (Dificuldade d : Dificuldade.values()) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(d.getNome());
            item.setSelected(d == dificuldade);
            item.addActionListener(e -> novoJogo(d));
            grupo.add(item);
            menuJogo.add(item);
        }

        menuJogo.addSeparator();
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuJogo.add(itemSair);

        barraMenu.add(menuJogo);
        setJMenuBar(barraMenu);
    }

    private void criarBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        lblMinas.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.addActionListener(e -> novoJogo(dificuldade));

        barra.add(lblMinas, BorderLayout.WEST);
        barra.add(btnReiniciar, BorderLayout.EAST);
        add(barra, BorderLayout.NORTH);
    }

    // ===================== CONTROLE DO JOGO =====================

    private void novoJogo(Dificuldade novaDificuldade) {
        dificuldade = novaDificuldade;
        tabuleiro = new Tabuleiro(dificuldade);

        int linhas = tabuleiro.getLinhas();
        int colunas = tabuleiro.getColunas();

        painelCampo.removeAll();
        painelCampo.setLayout(new GridLayout(linhas, colunas, 2, 2));
        painelCampo.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        botoes = new JButton[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                botoes[i][j] = criarBotao(i, j);
                painelCampo.add(botoes[i][j]);
            }
        }

        atualizarTela();
        pack();                          // ajusta o tamanho da janela ao campo
        setLocationRelativeTo(null);     // centraliza na tela
    }

    private JButton criarBotao(int linha, int coluna) {
        JButton botao = new JButton();
        botao.setPreferredSize(new Dimension(TAMANHO_CELULA, TAMANHO_CELULA));
        botao.setMargin(new Insets(0, 0, 0, 0));
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 15));

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    tabuleiro.abrir(linha, coluna);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    tabuleiro.alternarBandeira(linha, coluna);
                }
                atualizarTela();
                verificarFimDeJogo();
            }
        });
        return botao;
    }

    private void verificarFimDeJogo() {
        if (tabuleiro.isPerdeu()) {
            JOptionPane.showMessageDialog(this, "💥 Você pisou numa mina!", "Fim de jogo",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (tabuleiro.isGanhou()) {
            JOptionPane.showMessageDialog(this, "🏆 Parabéns, você venceu!", "Vitória",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===================== DESENHO DO CAMPO =====================

    private void atualizarTela() {
        lblMinas.setText("Minas: " + tabuleiro.getMinasRestantes());

        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                desenharCelula(botoes[i][j], tabuleiro.getCelula(i, j));
            }
        }
    }

    private void desenharCelula(JButton botao, Celula celula) {
        boolean mostrarMina = celula.temMina() && tabuleiro.isPerdeu();

        if (mostrarMina) {
            botao.setText("*");
            botao.setBackground(celula.isAberta() ? Color.RED : Color.LIGHT_GRAY);
        } else if (celula.temBandeira()) {
            botao.setText("F");
            botao.setBackground(null);
        } else if (celula.isAberta()) {
            int numero = celula.getMinasVizinhas();
            botao.setText(numero == 0 ? "" : String.valueOf(numero));
            botao.setBackground(Color.WHITE);
        } else {
            botao.setText("");
            botao.setBackground(null);
        }
    }
}