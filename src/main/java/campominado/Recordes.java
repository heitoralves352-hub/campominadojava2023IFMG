package campominado;

import java.util.prefs.Preferences;

// Guarda o melhor tempo de cada dificuldade no próprio computador
public final class Recordes {

    private static final Preferences PREFS = Preferences.userNodeForPackage(Recordes.class);

    private Recordes() { }

    // Retorna o recorde em segundos, ou -1 se ainda não existe
    public static int obter(Dificuldade dificuldade) {
        return PREFS.getInt(dificuldade.name(), -1);
    }

    // Salva o tempo se for melhor que o recorde atual. Retorna true se bateu o recorde.
    public static boolean salvarSeForMelhor(Dificuldade dificuldade, int segundos) {
        int atual = obter(dificuldade);
        if (atual == -1 || segundos < atual) {
            PREFS.putInt(dificuldade.name(), segundos);
            return true;
        }
        return false;
    }
}
