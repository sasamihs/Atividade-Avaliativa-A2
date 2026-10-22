import java.util.ArrayList;
import java.util.List;

public abstract class Equipamento {
    private int id;
    private String nome;
    private boolean disponivel;
    private List<String> historico;

    public Equipamento(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.disponivel = true;
        this.historico = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public List<String> getHistorico() { return historico; }

    public void adicionarAoHistorico(String registro) {
        this.historico.add(registro);
    }

    // Abstração: Cada equipamento define seu prazo regulamentar
    public abstract int getPrazoDias();
}

class EquipamentoComum extends Equipamento { // Bolas e Coletes
    public EquipamentoComum(int id, String nome) {
        super(id, nome);
    }

    @Override
    public int getPrazoDias() { return 0; } // Até o fim do dia

    @Override
    public String toString() {
        return "[Comum/Bola/Colete] ID: " + getId() + " - " + getNome();
    }
}

class Raquete extends Equipamento {
    public Raquete(int id, String nome) {
        super(id, nome);
    }

    @Override
    public int getPrazoDias() { return 2; } // Até dois dias

    @Override
    public String toString() {
        return "[Raquete] ID: " + getId() + " - " + getNome();
    }
}

class Bicicleta extends Equipamento {
    public Bicicleta(int id, String nome) {
        super(id, nome);
    }

    @Override
    public int getPrazoDias() { return 7; } // Uma semana

    @Override
    public String toString() {
        return "[Bicicleta] ID: " + getId() + " - " + getNome();
    }
}
