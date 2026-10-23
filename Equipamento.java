mimport java.util.ArrayList; // Importa a estrutura de lista dinâmica para o histórico.
import java.util.List; // Importa a interface para tipagem da coleção.

public abstract class Equipamento {
    // Atributos privados para garantir o encapsulamento das informações internas do objeto.
    private int id; // Identificador numérico exclusivo de cada unidade física.
    private String nome; // Descrição ou nome do equipamento (ex: "Bola de Futsal").
    private boolean disponivel; // Flag booleana que controla se o item está livre para uso ou ocupado.
    private List<String> historico; // Lista de strings que funciona como log de eventos de uso do item.

    // Construtor da classe mãe para instanciar as informações básicas obrigatórias de qualquer equipamento.
    public Equipamento(int id, String nome) {
        this.id = id; // Define o identificador único fornecido.
        this.nome = nome; // Define a nomenclatura textual do item.
        this.disponivel = true; // Todo equipamento inicia o ciclo de vida como disponível no sistema.
        this.historico = new ArrayList<>(); // Inicializa a lista de strings pronta para receber registros.
    }

    // Métodos acessores públicos (getters e setters) para consultar e alterar os estados protegidos.
    public int getId() { return id; } // Expõe o ID único da unidade.
    public String getNome() { return nome; } // Retorna a descrição do equipamento.
    public boolean isDisponivel() { return disponivel; } // Retorna true se livre ou false se já emprestado.
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; } // Permite alterar a disponibilidade do item.
    public List<String> getHistorico() { return historico; } // Dá acesso à lista completa de logs do equipamento.

    // Adiciona uma nova linha de texto descritiva diretamente no histórico deste equipamento específico.
    public void adicionarAoHistorico(String registro) {
        this.historico.add(registro);
    }

    // Abstração pura: Força cada subclasse que herdar de Equipamento a criar sua própria regra de prazos.
    public abstract int getPrazoDias();
}

// Subclasse especializada em itens simples de uso imediato no complexo esportivo.
class EquipamentoComum extends Equipamento { // Representa Bolas e Coletes do almoxarifado.
    public EquipamentoComum(int id, String nome) {
        super(id, nome); // Repassa os valores recebidos para o construtor da classe base (Equipamento).
    }

    @Override
    public int getPrazoDias() { return 0; } // Sobrescreve o método: Itens comuns devem voltar no mesmo dia (zero dias úteis).

    @Override
    public String toString() { // Sobrescreve a formatação de texto para exibição organizada no terminal da Main.
        return "[Comum/Bola/Colete] ID: " + getId() + " - " + getNome();
    }
}

// Subclasse especializada em equipamentos de quadras de raquete (Tênis, Badminton, etc.).
class Raquete extends Equipamento {
    public Raquete(int id, String nome) {
        super(id, nome); // Aciona o construtor original herdado.
    }

    @Override
    public int getPrazoDias() { return 2; } // Sobrescreve o método: Raquetes concedem um prazo regulamentar de até dois dias.

    @Override
    public String toString() { // Sobrescreve a representação textual identificando a categoria específica.
        return "[Raquete] ID: " + getId() + " - " + getNome();
    }
}

// Subclasse especializada em equipamentos que exigem vistorias especificas no retorno.
class Bicicleta extends Equipamento {
    public Bicicleta(int id, String nome) {
        super(id, nome); // Invoca a lógica de inicialização de atributos da superclasse.
    }

    @Override
    public int getPrazoDias() { return 7; } // Sobrescreve o método: Bicicletas dão direito a uma semana inteira de uso.

    @Override
    public String toString() { // Sobrescreve para customizar o rótulo visual nas listagens operacionais do terminal.
        return "[Bicicleta] ID: " + getId() + " - " + getNome();
    }
}
