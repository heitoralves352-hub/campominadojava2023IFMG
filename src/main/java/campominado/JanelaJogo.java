package campominado;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class JanelaJogo extends JFrame {

    private static final int TAMANHO_CELULA = 34;

    private Dificuldade dificuldade = Dificuldade.INICIANTE;
    private Tabuleiro tabuleiro;
    private BotaoCelula[][] botoes;

    private final JPanel painelCampo = new JPanel();
    private final JLabel lblMinas = criarNumeroGrande();
    private final JLabel lblTempo = criarNumeroGrande();
    private final JLabel lblStatus = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel lblRecorde = new JLabel(" ", SwingConstants.CENTER);

    // Cronômetro: soma 1 segundo a cada 1000 ms
    private int segundos = 0;
    private final Timer cronometro = new Timer(1000, e -> {
        segundos++;
        lblTempo.setText(formatar(segundos));
    });

    public JanelaJogo() {
        setTitle("Campo Minado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Tema.FUNDO);

        criarMenu();
        criarBarraSuperior();
        criarBarraInferior();

        painelCampo.setBackground(Tema.FUNDO);
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
        barra.setBackground(Tema.FUNDO);
        barra.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));

        JButton btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.addActionListener(e -> novoJogo(dificuldade));

        barra.add(criarIndicador("MINAS", lblMinas), BorderLayout.WEST);
        // GridBagLayout deixa o botão no centro, com o tamanho natural dele
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Tema.FUNDO);
        centro.add(btnReiniciar);
        barra.add(centro, BorderLayout.CENTER);
        barra.add(criarIndicador("TEMPO", lblTempo), BorderLayout.EAST);
        add(barra, BorderLayout.NORTH);
    }

    private void criarBarraInferior() {
        JPanel barra = new JPanel(new GridLayout(2, 1, 0, 2));
        barra.setBackground(Tema.FUNDO);
        barra.setBorder(BorderFactory.createEmptyBorder(4, 16, 14, 16));

        lblStatus.setForeground(Tema.TEXTO);
        lblStatus.setFont(lblStatus.getFont().deriveFont(Font.BOLD, 14f));
        lblRecorde.setForeground(Tema.TEXTO_SUAVE);
        lblRecorde.setFont(lblRecorde.getFont().deriveFont(12f));

        barra.add(lblStatus);
        barra.add(lblRecorde);
        add(barra, BorderLayout.SOUTH);
    }

    // Um bloquinho com um título pequeno em cima e um número grande embaixo
    private JPanel criarIndicador(String titulo, JLabel valor) {
        JPanel painel = new JPanel(new GridLayout(2, 1));
        painel.setBackground(Tema.FUNDO);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Tema.TEXTO_SUAVE);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 11f));

        painel.add(lblTitulo);
        painel.add(valor);
        return painel;
    }

    private static JLabel criarNumeroGrande() {
        JLabel label = new JLabel("000", SwingConstants.CENTER);
        label.setForeground(Tema.TEXTO);
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        return label;
    }

    // ===================== CONTROLE DO JOGO =====================

    private void novoJogo(Dificuldade novaDificuldade) {
        dificuldade = novaDificuldade;
        tabuleiro = new Tabuleiro(dificuldade);

        cronometro.stop();
        segundos = 0;
        lblTempo.setText(formatar(0));
        lblStatus.setForeground(Tema.TEXTO);
        lblStatus.setText("Clique em qualquer casa para começar");
        atualizarRecorde();

        int linhas = tabuleiro.getLinhas();
        int colunas = tabuleiro.getColunas();

        painelCampo.removeAll();
        painelCampo.setLayout(new GridLayout(linhas, colunas, 3, 3));
        painelCampo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        botoes = new BotaoCelula[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                botoes[i][j] = criarBotao(i, j);
                painelCampo.add(botoes[i][j]);
            }
        }

        atualizarTela();
        pack();
        setLocationRelativeTo(null);
    }

    private BotaoCelula criarBotao(int linha, int coluna) {
        BotaoCelula botao = new BotaoCelula(TAMANHO_CELULA);

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (tabuleiro.isJogoTerminado()) return;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    tabuleiro.abrir(linha, coluna);
                    if (!cronometro.isRunning()) {
                        cronometro.start();          // começa a contar no 1º clique
                        lblStatus.setText(" ");
                    }
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
        if (!tabuleiro.isJogoTerminado()) return;

        cronometro.stop();

        if (tabuleiro.isPerdeu()) {
            lblStatus.setForeground(Tema.EXPLOSAO);
            lblStatus.setText("Boom! Você pisou numa mina.");
        } else {
            boolean recorde = Recordes.salvarSeForMelhor(dificuldade, segundos);
            lblStatus.setForeground(Tema.DESTAQUE);
            lblStatus.setText(recorde
                    ? "Novo recorde! Você venceu em " + segundos + "s"
                    : "Você venceu em " + segundos + "s!");
            atualizarRecorde();
        }
    }

    // ===================== DESENHO =====================

    private void atualizarTela() {
        lblMinas.setText(formatar(tabuleiro.getMinasRestantes()));

        for (int i = 0; i < tabuleiro.getLinhas(); i++) {
            for (int j = 0; j < tabuleiro.getColunas(); j++) {
                botoes[i][j].atualizar(tabuleiro.getCelula(i, j),
                        tabuleiro.isPerdeu(), tabuleiro.isJogoTerminado());
            }
        }
    }

    private void atualizarRecorde() {
        int recorde = Recordes.obter(dificuldade);
        lblRecorde.setText(dificuldade.getNome() + "  •  Recorde: "
                + (recorde == -1 ? "--" : recorde + "s"));
    }

    // Mostra números sempre com 3 dígitos: 7 -> "007"
    private static String formatar(int valor) {
        return String.format("%03d", Math.max(valor, 0));
    }
}