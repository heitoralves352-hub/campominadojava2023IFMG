package campominado;


public class Celula {

    private boolean mina = false;        // tem mina nesta célula?
    private boolean aberta = false;      // o jogador já abriu?
    private boolean bandeira = false;    // o jogador marcou com bandeira?
    private int minasVizinhas = 0;       // quantas minas existem em volta

    public boolean temMina() { 
        return mina; 
    }
    public void colocarMina() { 
        this.mina = true; 
    }

    public boolean isAberta() { 
        return aberta; 
    }
    public void abrir() { 
        this.aberta = true; 
    }

    public boolean temBandeira() { 
        return bandeira; 
    }
    public void alternarBandeira() { 
        this.bandeira = !this.bandeira; 
    }

    public int getMinasVizinhas() { 
        return minasVizinhas; 
    }
    public void incrementarMinasVizinhas() { 
        this.minasVizinhas++; 
    }
}