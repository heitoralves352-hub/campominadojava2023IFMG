package campominado;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        // Tema escuro moderno (FlatLaf) para menus, botões e janelas
        FlatDarkLaf.setup();
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);

        SwingUtilities.invokeLater(() -> new JanelaJogo().setVisible(true));
    }
}