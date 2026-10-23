import java.time.LocalDate; // Importa a classe para manipulação de datas do Java.
import java.util.ArrayList; // Importa a implementação de lista dinâmica (ArrayList).
import java.util.List; // Importa a interface List para trabalhar com coleções de dados.
import java.util.Scanner; // Importa a classe Scanner para ler dados digitados no terminal.

public class Main {
    // Declara uma lista estática privada para armazenar todos os usuários do sistema.
    private static List<Usuario> usuarios = new ArrayList<>();
    // Declara uma lista estática privada para armazenar todos os equipamentos cadastrados.
    private static List<Equipamento> equipamentos = new ArrayList<>();
    // Declara uma lista estática privada para rastrear os empréstimos que estão em andamento.
    private static List<Emprestimo> emprestimosAtivos = new ArrayList<>();
    public static void main(String[] args) {
    carregarDadosIniciais(); // Chama o método que popula as listas com dados de teste ao iniciar.
    Scanner scanner = new Scanner(System.in); // Cria o objeto scanner para ler a entrada do teclado.
    int opcao = -1; // Inicializa a variável de opção do menu com um valor que ativa o laço.

    System.out.println("=== SISTEMA DE EMPRÉSTIMOS DA ATLÉTICA ===");

    while (opcao != 0) { // Executa o bloco repetidamente até que o usuário digite 0 (Sair).
        exibirMenu(); // Chama o método que exibe as opções textuais na tela.
        try {
            System.out.print("Escolha uma opção: ");
            // Lê a linha digitada pelo usuário e a converte textualmente em um número inteiro.
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) { // Avalia qual número foi digitado e direciona para a opção escolhida.
                case 1 -> listarEquipamentos(); // Direciona para a listagem se a opção for 1.
                case 2 -> registrarEmprestimo(scanner); // Passa o scanner e vai para empréstimo se for 2.
                case 3 -> registrarDevolucao(scanner); // Passa o scanner e vai para devolução se for 3.
                case 4 -> verHistoricoEquipamento(scanner); // Passa o scanner e vai para histórico se for 4.
                case 5 -> cadastrarUsuario(scanner); // Passa o scanner e vai para cadastro se for 5.
                case 0 -> System.out.println("\nEncerrando o sistema. Até mais!");
                default -> System.out.println("\n[Erro] Opção inválida. Digite um número do menu.");
            }
        } catch (NumberFormatException e) { // Captura o erro caso o usuário digite letras em vez de números.
            System.out.println("\n[Erro Entrada Inválida] Digite apenas números inteiros para navegar no menu.");
        } catch (Exception e) { // Captura qualquer outro tipo de falha inesperada no sistema.
            System.out.println("\n[Erro Inesperado] Ocorreu um problema: " + e.getMessage());
        }
    }
    scanner.close(); // Fecha o fluxo de leitura do scanner liberando os recursos de memória.
}

private static void exibirMenu() {
    System.out.println("\n-----------------------------------------");
    System.out.println("1. Consultar Equipamentos (Disponibilidade)");
    System.out.println("2. Registrar Empréstimo");
    System.out.println("3. Registrar Devolução");
    System.out.println("4. Ver Histórico de um Equipamento");
    System.out.println("5. Cadastrar Novo Aluno ou Servidor");
    System.out.println("0. Sair");
    System.out.println("-----------------------------------------");
}

private static void carregarDadosIniciais() {
    // Adiciona instâncias pré-definidas de alunos na lista global de usuários.
    usuarios.add(new Aluno("Lucas Kimura", "2046601"));
    usuarios.add(new Aluno("Samih de Oliveira", "2270862"));
    // Adiciona uma instância pré-definida de servidor na lista global de usuários.
    usuarios.add(new Servidor("Prof. Luiz Rodrigues", "12345"));

    // Adiciona instâncias de equipamentos comuns na lista global.
    equipamentos.add(new EquipamentoComum(101, "Bola de Futsal "));
    equipamentos.add(new EquipamentoComum(102, "Bola de Volei "));
    equipamentos.add(new EquipamentoComum(103, "Bola de Basquete "));
    equipamentos.add(new EquipamentoComum(150, "Colete1"));
    // Adiciona uma instância da subclasse Raquete na lista global.
    equipamentos.add(new Raquete(201, "Raquete"));
    // Adiciona uma instância da subclasse Bicicleta na lista global.
    equipamentos.add(new Bicicleta(301, "Bicicleta"));
}

private static void listarEquipamentos() {
    System.out.println("\n--- ESTADO DOS EQUIPAMENTOS ---");
    for (Equipamento e : equipamentos) { // Percorre cada objeto 'Equipamento' contido na lista 'equipamentos'.
        // Define o texto do status usando uma expressão ternária baseada no booleano de disponibilidade.
        String status = e.isDisponivel() ? "DISPONÍVEL" : "EMPRESTADO";
        System.out.println(e + " | Status: " + status);
    }
}

private static void registrarEmprestimo(Scanner scanner) {
    System.out.print("\nDigite a matrícula do usuário: ");
    String RA = scanner.nextLine(); // Captura o texto da matrícula informado pelo operador.
    Usuario usuario = buscarUsuario(RA); // Invoca a busca interna para encontrar o objeto do usuário.

    if (usuario == null) { // Valida se o usuário não foi localizado no sistema.
        System.out.println("[Erro] Usuário não encontrado.");
        return; // Interrompe imediatamente a execução deste método.
    }

    if (!usuario.podePegarEmprestado()) { // Executa a validação de regras de bloqueio ou limite do usuário.
        System.out.println("[Bloqueio/Limite] Usuário atingiu limite de itens ou está bloqueado por atraso.");
        return; // Interrompe imediatamente a execução deste método.
    }

    listarEquipamentos(); // Lista os itens para facilitar a escolha visual do ID.

    System.out.print("\nDigite o ID do equipamento: ");
    // Lê a linha digitada e a transforma em um número inteiro para representar o ID do equipamento.
    int id = Integer.parseInt(scanner.nextLine());
    Equipamento equipamento = buscarEquipamento(id); // Invoca a busca interna para encontrar o objeto do equipamento.

    if (equipamento == null) { // Valida se o equipamento não foi localizado no sistema.
        System.out.println("[Erro] Equipamento não encontrado.");
        return; // Interrompe imediatamente a execução deste método.
    }

    if (!equipamento.isDisponivel()) { // Verifica se o item escolhido já está na posse de outra pessoa.
        System.out.println("[Erro] Este equipamento já está emprestado no momento.");
        return; // Interrompe imediatamente a execução deste método.
    }

    Emprestimo emp = new Emprestimo(usuario, equipamento); // Instancia um novo objeto de controle de Empréstimo.
    emprestimosAtivos.add(emp); // Registra este novo empréstimo na lista geral de pendências ativas.
    usuario.getItensEmprestados().add(equipamento); // Associa o equipamento à lista interna de posses do usuário.
    equipamento.setDisponivel(false); // Modifica o estado do equipamento para indisponível (false).

    System.out.println("\n[Sucesso] Empréstimo realizado!");
    System.out.println("Prazo Limite para Devolução: " + emp.getDataDevolucaoPrevista());
}

private static void registrarDevolucao(Scanner scanner) {
    listarEquipamentos(); // Exibe os estados dos equipamentos para ajudar na identificação do ID.

    System.out.print("\nDigite o ID do equipamento que está sendo devolvido: ");
    // Captura e converte a entrada textual para o ID numérico correspondente.
    int id = Integer.parseInt(scanner.nextLine());
    Emprestimo emprestimo = buscarEmprestimoAtivo(id); // Procura a ficha de empréstimo vinculada ao ID do equipamento.

    if (emprestimo == null) { // Verifica se não há registros de que o item foi realmente emprestado.
        System.out.println("[Erro] Nenhum empréstimo ativo encontrado para este ID.");
        return; // Interrompe imediatamente a execução deste método.
    }

    Equipamento eq = emprestimo.getEquipamento(); // Extrai a referência do objeto equipamento de dentro do empréstimo.
    Usuario us = emprestimo.getUsuario(); // Extrai a referência do objeto usuário de dentro do empréstimo.

    if (eq instanceof Bicicleta) { // Avalia se o equipamento devolvido é especificamente do tipo Bicicleta.
        System.out.println("\n--- VISTORIA OBRIGATÓRIA DE BICICLETA ---");
        System.out.print("Pneus em bom estado? (S/N): ");
        String pneu = scanner.nextLine(); // Lê a resposta sobre a condição dos pneus.
        System.out.print("Freios funcionando? (S/N): ");
        String freio = scanner.nextLine(); // Lê a resposta sobre o funcionamento do freio.
        System.out.print("Corrente lubrificada/ajustada? (S/N): ");
        String corrente = scanner.nextLine(); // Lê a resposta sobre o estado da corrente.

        // Condicional que verifica se houve resposta negativa em qualquer um dos três itens vistoriados.
        if (pneu.equalsIgnoreCase("N") || freio.equalsIgnoreCase("N") || corrente.equalsIgnoreCase("N")) {
            System.out.println("[Alerta] Bicicleta recebida com avarias! Encaminhar para manutenção.");
            // Adiciona um alerta detalhado com as respostas ao histórico individual do item.
            eq.adicionarAoHistorico("Necessita manutenção (Vistoria: Pneu=" + pneu + ", Freio=" + freio + ", Corrente=" + corrente + ")");
        } else {System.out.println("[Vistoria Concluída e Registrada com Sucesso]");}
        }

        System.out.print("A devolução está atrasada? (S/N): ");
        String atrasou = scanner.nextLine(); // Lê a resposta do operador sobre a pontualidade da entrega.
        
        if (atrasou.equalsIgnoreCase("S")) { // Bloco executado caso a devolução tenha ocorrido com atraso.
            System.out.print("Quantos dias de atraso? ");
            // Converte o texto digitado em um número inteiro para calcular o período de penalidade.
            int dias = Integer.parseInt(scanner.nextLine());
            // Aplica a punição bloqueando o usuário pelo dobro de dias em atraso a partir da data atual.
            us.setBloqueadoAte(LocalDate.now().plusDays(dias * 2L));
            System.out.println("[Aviso] Usuário punido e bloqueado até: " + us.getBloqueadoAte());
        }

        eq.setDisponivel(true); // Altera o estado do equipamento de volta para disponível (true).
        us.getItensEmprestados().remove(eq); // Remove o equipamento da lista de posses atuais do usuário.
        // Adiciona um registro detalhado de quem devolveu e a data atual no histórico do item.
        eq.adicionarAoHistorico("Devolvido por: " + us.getNome() + " em " + LocalDate.now());
        emprestimosAtivos.remove(emprestimo); // Remove a ficha deste empréstimo da lista de pendências ativas.

        System.out.println("\n[Sucesso] Devolução processada com êxito!");
    }

    private static void verHistoricoEquipamento(Scanner scanner) {
        System.out.print("\nDigite o ID do equipamento para ver o histórico: ");
        // Lê a entrada do terminal e a converte para o identificador numérico do item.
        int id = Integer.parseInt(scanner.nextLine());
        Equipamento eq = buscarEquipamento(id); // Invoca o método de busca pelo ID informado.

        if (eq == null) { // Valida se o equipamento correspondente ao ID existe no sistema.
            System.out.println("[Erro] Equipamento não encontrado.");
            return; // Encerra o método imediatamente caso não encontre o item.
        }

        System.out.println("\n--- HISTÓRICO DE " + eq.getNome().toUpperCase() + " ---");
        if (eq.getHistorico().isEmpty()) { // Verifica se a lista de histórico do objeto está vazia.
            System.out.println("Nenhum registro de uso anterior encontrado.");
        } else {
            for (String registro : eq.getHistorico()) { // Percorre e exibe cada linha de texto gravada no histórico.
                System.out.println("- " + registro);
            }
        }
    }

    private static void cadastrarUsuario(Scanner scanner) {
        System.out.print("\nNome do usuário: ");
        String nome = scanner.nextLine(); // Captura o nome completo do novo usuário.
        System.out.print("Matrícula/Identificador único: ");
        String mat = scanner.nextLine(); // Captura o RA do aluno ou o SIAPE do servidor.
        System.out.print("Tipo (1 - Aluno | 2 - Servidor): ");
        int tipo = Integer.parseInt(scanner.nextLine()); // Lê a opção que define a categoria do perfil.

        if (tipo == 1) { // Caso a opção seja 1, instancia e armazena um objeto da subclasse Aluno.
            usuarios.add(new Aluno(nome, mat));
            System.out.println("[Sucesso] Aluno cadastrado!");
        } else if (tipo == 2) { // Caso a opção seja 2, instancia e armazena um objeto da subclasse Servidor.
            usuarios.add(new Servidor(nome, mat));
            System.out.println("[Sucesso] Servidor cadastrado!");
        } else { // Trata cenários onde o operador digita um número fora das opções 1 ou 2.
            System.out.println("[Erro] Tipo de cadastro inválido.");
        }
    }

    // Método utilitário interno que varre a lista de usuários procurando uma matrícula correspondente.
    private static Usuario buscarUsuario(String mat) {
        for (Usuario u : usuarios) { // Percorre cada objeto 'Usuario' dentro da lista 'usuarios'.
            if (u.getRA().equals(mat)) return u; // Retorna a referência do objeto caso encontre a matrícula.
        }
        return null; // Retorna nulo se o laço terminar sem encontrar nenhuma correspondência.
    }

    // Método utilitário interno que varre a lista de equipamentos cadastrados em busca de um ID específico.
    private static Equipamento buscarEquipamento(int id) {
        for (Equipamento e : equipamentos) { // Percorre cada objeto 'Equipamento' dentro da lista 'equipamentos'.
            if (e.getId() == id) return e; // Retorna a referência do objeto caso os IDs sejam iguais.
        }
        return null; // Retorna nulo se o equipamento com esse identificador não existir.
    }

    // Método utilitário interno que varre as pendências ativas para achar o empréstimo vinculado a um equipamento.
    private static Emprestimo buscarEmprestimoAtivo(int idEquipamento) {
        for (Emprestimo emp : emprestimosAtivos) { // Percorre a lista de registros de empréstimos em andamento.
            // Retorna a ficha do empréstimo se o ID do equipamento dentro dela coincidir com o buscado.
            if (emp.getEquipamento().getId() == idEquipamento) return emp;
        }
        return null; // Retorna nulo se não houver nenhum empréstimo ativo registrado para o ID informado.
    }
}
