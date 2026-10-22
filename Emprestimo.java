import java.time.LocalDate;

public class Emprestimo {
    private Usuario usuario;
    private Equipamento equipamento;
    private LocalDate dataRetirada;
    private LocalDate dataDevolucaoPrevista;

    public Emprestimo(Usuario usuario, Equipamento equipamento) {
        this.usuario = usuario;
        this.equipamento = equipamento;
        this.dataRetirada = LocalDate.now();
        this.dataDevolucaoPrevista = dataRetirada.plusDays(equipamento.getPrazoDias());
    }

    public Usuario getUsuario() { return usuario; }
    public Equipamento getEquipamento() { return equipamento; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
}
