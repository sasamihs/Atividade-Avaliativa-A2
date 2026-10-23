import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Equipamento> equipamentos = new ArrayList<>();
    private static final List<Emprestimo> emprestimosAtivos = new ArrayList<>();

    public static void main(String[] args) {
        carregarDadosIniciais();
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        System.out.println("=== SISTEMA DE EMPRÉSTIMOS DA ATLÉTICA ===");

        while (opcao != 0) {
            exibirMenu();
            try {
                System.out.print("Escolha uma opção: ");
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> listarEquipamentos();
                    case 2 -> registrarEmprestimo(scanner);
                    case 3 -> registrarDevolucao(scanner);
                    case 4 -> verHistoricoEquipamento(scanner);
                    case 5 -> cadastrarUsuario(scanner);
                    case 0 -> System.out.println("\nEncerrando o sistema. Até mais!");
                    default -> System.out.println("\n[Erro] Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[Erro] Digite apenas números inteiros.");
            } catch (Exception e) {
                System.out.println("\n[Erro Inesperado] " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.print("""
            -----------------------------------------
            1. Consultar Equipamentos (Disponibilidade)
            2. Registrar Empréstimo
            3. Registrar Devolução
            4. Ver Histórico de um Equipamento
            5. Cadastrar Novo Aluno ou Servidor
            0. Sair
            -----------------------------------------
            """);
    }

    private static void carregarDadosIniciais() {
        usuarios.add(new Aluno("Lucas Kimura", "2046601"));
        usuarios.add(new Aluno("Samih de Oliveira", "2270862"));
        usuarios.add(new Servidor("Prof. Luiz Rodrigues", "12345"));

        equipamentos.add(new EquipamentoComum(101, "Bola de Futsal"));
        equipamentos.add(new EquipamentoComum(102, "Bola de Volei"));
        equipamentos.add(new EquipamentoComum(103, "Bola de Basquete"));
        equipamentos.add(new EquipamentoComum(150, "Colete1"));
        equipamentos.add(new Raquete(201, "Raquete"));
        equipamentos.add(new Bicicleta(301, "Bicicleta"));
    }

    private static void listarEquipamentos() {
        System.out.println("\n--- ESTADO DOS EQUIPAMENTOS ---");
        for (Equipamento e : equipamentos) {
            System.out.println(e + " | Status: " + (e.isDisponivel() ? "DISPONÍVEL" : "EMPRESTADO"));
        }
    }

    private static void registrarEmprestimo(Scanner scanner) {
        System.out.print("\nDigite a matrícula do usuário: ");
        Usuario usuario = buscarUsuario(scanner.nextLine());

        if (usuario == null) {
            System.out.println("[Erro] Usuário não encontrado.");
            return;
        }
        if (!usuario.podePegarEmprestado()) {
            System.out.println("[Bloqueio/Limite] Usuário atingiu limite ou está bloqueado.");
            return;
        }

        listarEquipamentos();
        System.out.print("\nDigite o ID do equipamento: ");
        Equipamento equipamento = buscarEquipamento(Integer.parseInt(scanner.nextLine()));

        if (equipamento == null || !equipamento.isDisponivel()) {
            System.out.println("[Erro] Equipamento não encontrado ou já emprestado.");
            return;
        }

        Emprestimo emp = new Emprestimo(usuario, equipamento);
        System.out.println("\n[Sucesso] Empréstimo realizado! Devolução até: " + emp.getDataDevolucaoPrevista());
    }

    private static void registrarDevolucao(Scanner scanner) {
        listarEquipamentos();
        System.out.print("\nDigite o ID do equipamento sendo devolvido: ");
        Emprestimo emp = buscarEmprestimoAtivo(Integer.parseInt(scanner.nextLine()));

        if (emp == null) {
            System.out.println("[Erro] Nenhum empréstimo ativo encontrado para este ID.");
            return;
        }

        Equipamento eq = emp.getEquipamento();
        Usuario us = emp.getUsuario();

        if (eq instanceof Bicicleta) {
            System.out.println("\n--- VISTORIA OBRIGATÓRIA DE BICICLETA ---");
            System.out.print("Pneus OK? (S/N): "); String pneu = scanner.nextLine();
            System.out.print("Freios OK? (S/N): "); String freio = scanner.nextLine();
            System.out.print("Corrente OK? (S/N): "); String corrente = scanner.nextLine();
            
            if (pneu.equalsIgnoreCase("N") || freio.equalsIgnoreCase("N") || corrente.equalsIgnoreCase("N")) {
                System.out.println("[Alerta] Encaminhar para manutenção!");
                eq.adicionarAoHistorico("⚠️ Manutenção (Pneu=" + pneu + ", Freio=" + freio + ", Corrente=" + corrente + ")");
            }
        }

        System.out.print("A devolução está atrasada? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.print("Dias de atraso: ");
            int dias = Integer.parseInt(scanner.nextLine());
            us.setBloqueadoAte(LocalDate.now().plusDays(dias * 2L));
            System.out.println("[Aviso] Bloqueado até: " + us.getBloqueadoAte());
        }

        eq.setDisponivel(true);
        us.getItensEmprestados().remove(eq);
        eq.adicionarAoHistorico("Devolvido por: " + us.getNome() + " em " + LocalDate.now());
        emprestimosAtivos.remove(emp);
        System.out.println("\n[Sucesso] Devolução processada!");
    }

    private static void verHistoricoEquipamento(Scanner scanner) {
        System.out.print("\nDigite o ID do equipamento: ");
        Equipamento eq = buscarEquipamento(Integer.parseInt(scanner.nextLine()));

        if (eq == null) {
            System.out.println("[Erro] Equipamento não encontrado.");
            return;
        }

        System.out.println("\n--- HISTÓRICO DE " + eq.getNome().toUpperCase() + " ---");
        if (eq.getHistorico().isEmpty()) System.out.println("Nenhum registro encontrado.");
        else eq.getHistorico().forEach(registro -> System.out.println("- " + registro));
    }

    private static void cadastrarUsuario(Scanner scanner) {
        System.out.print("\nNome do usuário: "); String nome = scanner.nextLine();
        System.out.print("Matrícula: "); String mat = scanner.nextLine();
        System.out.print("Tipo (1 - Aluno | 2 - Servidor): ");
        int tipo = Integer.parseInt(scanner.nextLine());

        if (tipo == 1) usuarios.add(new Aluno(nome, mat));
        else if (tipo == 2) usuarios.add(new Servidor(nome, mat));
        else { System.out.println("[Erro] Tipo inválido."); return; }
        
        System.out.println("[Sucesso] Cadastrado com êxito!");
    }

    private static Usuario buscarUsuario(String mat) {
        return usuarios.stream().filter(u -> u.getMatricula().equals(mat)).findFirst().orElse(null);
    }

    private static Equipamento buscarEquipamento(int id) {
        return equipamentos.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    private static Emprestimo buscarEmprestimoAtivo(int id) {
        return emprestimosAtivos.stream().filter(emp -> emp.getEquipamento().getId() == id).findFirst().orElse(null);
    }
}
