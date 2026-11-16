package campominado;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

// Um quadradinho do campo, desenhado "na mão" para ter um visual moderno
public class BotaoCelula extends JComponent {

    private Celula celula;
    private boolean jogoPerdido = false;
    private boolean jogoTerminado = false;
    private boolean mouseEmCima = false;

    public BotaoCelula(int tamanho) {
        setPreferredSize(new Dimension(tamanho, tamanho));

        // Efeito de "hover": a célula clareia quando o mouse passa por cima
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { mouseEmCima = true;  repaint(); }

            @Override
            public void mouseExited(MouseEvent e)  { mouseEmCima = false; repaint(); }
        });
    }

    public void atualizar(Celula celula, boolean jogoPerdido, boolean jogoTerminado) {
        this.celula = celula;
        this.jogoPerdido = jogoPerdido;
        this.jogoTerminado = jogoTerminado;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (celula == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        boolean mostrarMina = celula.temMina() && (celula.isAberta() || jogoPerdido);

        // 1) Fundo arredondado
        g2.setColor(corDeFundo(mostrarMina));
        g2.fillRoundRect(1, 1, w - 2, h - 2, 10, 10);

        // 2) Conteúdo
        if (mostrarMina && !celula.temBandeira()) {
            desenharMina(g2, w, h, celula.isAberta());
        } else if (celula.temBandeira()) {
            desenharBandeira(g2, w, h);
        } else if (celula.isAberta() && celula.getMinasVizinhas() > 0) {
            desenharNumero(g2, w, h, celula.getMinasVizinhas());
        }

        g2.dispose();
    }

    private Color corDeFundo(boolean mostrarMina) {
        if (mostrarMina && celula.isAberta()) return Tema.EXPLOSAO;   // a mina que explodiu
        if (celula.isAberta() || mostrarMina)  return Tema.CELULA_ABERTA;
        if (mouseEmCima && !jogoTerminado)     return Tema.CELULA_HOVER;
        return Tema.CELULA_FECHADA;
    }

    private void desenharNumero(Graphics2D g2, int w, int h, int numero) {
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int) (h * 0.5)));
        g2.setColor(Tema.corDoNumero(numero));

        String texto = String.valueOf(numero);
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(texto)) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(texto, x, y);
    }

    private void desenharBandeira(Graphics2D g2, int w, int h) {
        int mastroX = (int) (w * 0.42);
        int topo = (int) (h * 0.24);
        int base = (int) (h * 0.76);

        // mastro
        g2.setColor(Tema.TEXTO);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(mastroX, topo, mastroX, base);
        g2.drawLine((int) (w * 0.30), base, (int) (w * 0.62), base);

        // pano da bandeira (triângulo)
        g2.setColor(Tema.DESTAQUE);
        int[] xs = { mastroX, (int) (w * 0.74), mastroX };
        int[] ys = { topo, (int) (h * 0.37), (int) (h * 0.50) };
        g2.fillPolygon(xs, ys, 3);
    }

    private void desenharMina(Graphics2D g2, int w, int h, boolean explodiu) {
        Color cor = explodiu ? Tema.FUNDO : Tema.EXPLOSAO;
        int cx = w / 2;
        int cy = h / 2;
        int raio = (int) (w * 0.20);
        int ponta = (int) (w * 0.32);

        // espinhos
        g2.setColor(cor);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(cx - ponta, cy, cx + ponta, cy);
        g2.drawLine(cx, cy - ponta, cx, cy + ponta);
        int diag = (int) (ponta * 0.7);
        g2.drawLine(cx - diag, cy - diag, cx + diag, cy + diag);
        g2.drawLine(cx - diag, cy + diag, cx + diag, cy - diag);

        // corpo
        g2.fillOval(cx - raio, cy - raio, raio * 2, raio * 2);

        // brilho
        g2.setColor(new Color(255, 255, 255, 140));
        int brilho = Math.max(3, raio / 2);
        g2.fillOval(cx - raio / 2, cy - raio / 2, brilho, brilho);
    }
}