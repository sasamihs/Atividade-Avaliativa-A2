import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    private String nome;
    private String matricula;
    private List<Equipamento> itensEmprestados;
    private LocalDate bloqueadoAte;

    public Usuario(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.itensEmprestados = new ArrayList<>();
        this.bloqueadoAte = LocalDate.MIN;
    }

    public String getNome() { return nome; }
    public String getMatricula() { return matricula; }
    public List<Equipamento> getItensEmprestados() { return itensEmprestados; }
    public LocalDate getBloqueadoAte() { return bloqueadoAte; }
    public void setBloqueadoAte(LocalDate bloqueadoAte) { this.bloqueadoAte = bloqueadoAte; }

    public boolean estaBloqueado() {
        return LocalDate.now().isBefore(bloqueadoAte);
    }

    // Polimorfismo: Limite máximo validado conforme subclasse
    public abstract boolean podePegarEmprestado();
}

class Aluno extends Usuario {
    public Aluno(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public boolean podePegarEmprestado() {
        return !estaBloqueado() && getItensEmprestados().size() < 2;
    }

    @Override
    public String toString() {
        return "[Aluno] " + getNome() + " (Matrícula: " + getMatricula() + ")";
    }
}

class Servidor extends Usuario {
    public Servidor(String nome, String matricula) {
        super(nome, matricula);
    }

    @Override
    public boolean podePegarEmprestado() {
        return !estaBloqueado() && getItensEmprestados().size() < 4;
    }

    @Override
    public String toString() {
        return "[Servidor] " + getNome() + " (SIAPE/Matrícula: " + getMatricula() + ")";
    }
}
