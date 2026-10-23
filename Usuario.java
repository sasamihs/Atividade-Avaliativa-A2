import java.time.LocalDate; // Importa o componente nativo de manipulação cronológica de datas.
import java.util.ArrayList; // Importa a estrutura de armazenamento para controlar os itens em posse.
import java.util.List; // Importa o contrato estrutural de coleções.

public abstract class Usuario {
    // Atributos encapsulados para armazenamento seguro das informações individuais dos usuários.
    private String nome; // Armazena o nome civil do estudante ou do colaborador.
    private String RA; // Funciona como chave única de identificação (Matrícula acadêmica ou SIAPE funcional).
    private List<Equipamento> itensEmprestados; // Lista contendo as instâncias de equipamentos sob posse atual do usuário.
    private LocalDate bloqueadoAte; // Guarda o teto temporal da penalidade caso o usuário atrase entregas.

    // Construtor principal responsável por parametrizar o perfil básico da conta no sistema.
    public Usuario(String nome, String RA) {
        this.nome = nome; // Define a identidade nominal da conta.
        this.RA = RA; // Armazena o código validador individual.
        this.itensEmprestados = new ArrayList<>(); // Instancia a lista de pendências vazia para novos cadastros.
        this.bloqueadoAte = LocalDate.MIN; // Inicializa com a menor data possível, indicando que a conta começa sem punições.
    }

    // Métodos seletores e modificadores padrões para as rotinas operacionais das outras classes.
    public String getNome() { return nome; } // Permite resgatar o nome do usuário nas rotinas de log.
    public String getRA() { return RA; } // Expõe a matrícula para loops de varredura ou filtragem.
    public List<Equipamento> getItensEmprestados() { return itensEmprestados; } // Retorna os itens sob responsabilidade deste perfil.
    public LocalDate getBloqueadoAte() { return bloqueadoAte; } // Mostra o limite final do gancho disciplinar.
    public void setBloqueadoAte(LocalDate bloqueadoAte) { this.bloqueadoAte = bloqueadoAte; } // Permite aplicar novas punições.

    // Valida se o dia de hoje se encontra posicionado cronologicamente antes da data de término da punição.
    public boolean estaBloqueado() {
        return LocalDate.now().isBefore(bloqueadoAte); // Retorna verdadeiro se a data atual for menor que o bloqueio ativo.
    }

    // Polimorfismo puro: Cada tipo de usuário dita seus tetos de limite de itens simultâneos.
    public abstract boolean podePegarEmprestado();
}

// Subclasse destinada aos Alunos da instituição, possuindo políticas de empréstimo mais restritivas.
class Aluno extends Usuario {
    public Aluno(String nome, String RA) {
        super(nome, RA); // Encaminha as strings para preencher os atributos protegidos na superclasse.
    }

    @Override
    public boolean podePegarEmprestado() {
        // Aluno pode retirar se: Não estiver sob efeito de punição E tiver menos de dois itens com ele no momento.
        return !estaBloqueado() && getItensEmprestados().size() < 2;
    }

    @Override
    public String toString() { // Personaliza os metadados textuais para as saídas operacionais no console.
        return "[Aluno] " + getNome() + " (RA: " + getRA() + ")";
    }
}

// Subclasse destinada aos Servidores, possuindo maior flexibilidade em cotas internas devido à atividade laboral.
class Servidor extends Usuario {
    public Servidor(String nome, String RA) {
        super(nome, RA); // Invoca a regra de instanciação superior compartilhada por todos os usuários.
    }

    @Override
    public boolean podePegarEmprestado() {
        // Servidor pode retirar se: Não estiver suspenso por atraso E tiver menos de quatro itens ativos na sua ficha.
        return !estaBloqueado() && getItensEmprestados().size() < 4;
    }

    @Override
    public String toString() { // Constrói o texto identificador no terminal simulando uma credencial de servidor.
        return "[Servidor] " + getNome() + " (RA: " + getRA() + ")";
    }
}
