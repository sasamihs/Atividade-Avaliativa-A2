import java.time.LocalDate; // Importa a API de manipulação de datas do Java.

public class Emprestimo {
    // Declara os atributos que armazenam as referências dos objetos envolvidos e as datas de controle.
    private Usuario usuario; // Objeto do usuário que realizou a retirada.
    private Equipamento equipamento; // Objeto do equipamento que foi levado.
    private LocalDate dataRetirada; // Armazena o dia exato em que o empréstimo começou.
    private LocalDate dataDevolucaoPrevista; // Guarda a data limite calculada para a devolução.

    // Método construtor que inicializa o empréstimo vinculando o usuário ao equipamento desejado.
    public Emprestimo(Usuario usuario, Equipamento equipamento) {
        this.usuario = usuario; // Atribui o usuário passado por parâmetro ao atributo da classe.
        this.equipamento = equipamento; // Atribui o equipamento passado por parâmetro ao atributo da classe.
        this.dataRetirada = LocalDate.now(); // Grava automaticamente o dia atual como data de retirada.
        // Calcula a data prevista somando o prazo regulamentar específico daquele tipo de equipamento.
        this.dataDevolucaoPrevista = dataRetirada.plusDays(equipamento.getPrazoDias());
    }

    // Métodos seletores (getters) públicos para expor os dados encapsulados quando necessário.
    public Usuario getUsuario() { return usuario; } // Permite que outras classes descubram quem pegou o item.
    public Equipamento getEquipamento() { return equipamento; } // Permite descobrir qual é o item deste registro.
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; } // Expõe a data limite do empréstimo.
}
