import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Equipamento> equipamentos = new ArrayList<>();
    private static List<Emprestimo> emprestimosAtivos = new ArrayList<>();

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
                    default -> System.out.println("\n[Erro] Opção inválida. Digite um número do menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[Erro Entrada Inválida] Digite apenas números inteiros para navegar no menu.");
            } catch (Exception e) {
                System.out.println("\n[Erro Inesperado] Ocorreu um problema: " + e.getMessage());
            }
        }
        scanner.close();
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
        usuarios.add(new Aluno("Lucas Kimura", "2046601"));
        usuarios.add(new Aluno("Samih de Oliveira", "2270862"));
        usuarios.add(new Servidor("Prof. Luiz Rodrigues", "12345"));

        equipamentos.add(new EquipamentoComum(101, "Bola de Futsal "));
        equipamentos.add(new EquipamentoComum(102, "Bola de Volei "));
        equipamentos.add(new EquipamentoComum(103, "Bola de Basquete "));
        equipamentos.add(new EquipamentoComum(150, "Colete1"));
        equipamentos.add(new Raquete(201, "Raquete"));
        equipamentos.add(new Bicicleta(301, "Bicicleta"));
    }

    private static void listarEquipamentos() {
        System.out.println("\n--- ESTADO DOS EQUIPAMENTOS ---");
        for (Equipamento e : equipamentos) {
            String status = e.isDisponivel() ? "DISPONÍVEL" : "EMPRESTADO";
            System.out.println(e + " | Status: " + status);
        }
    }

    private static void registrarEmprestimo(Scanner scanner) {
        System.out.print("\nDigite a matrícula do usuário: ");
        String matricula = scanner.nextLine();
        Usuario usuario = buscarUsuario(matricula);

        if (usuario == null) {
            System.out.println("[Erro] Usuário não encontrado.");
            return;
        }

        if (!usuario.podePegarEmprestado()) {
            System.out.println("[Bloqueio/Limite] Usuário atingiu limite de itens ou está bloqueado por atraso.");
            return;
        }

        // Mostra os equipamentos na tela para o usuário escolher o ID
        listarEquipamentos();

        System.out.print("\nDigite o ID do equipamento: ");
        int id = Integer.parseInt(scanner.nextLine());
        Equipamento equipamento = buscarEquipamento(id);

        if (equipamento == null) {
            System.out.println("[Erro] Equipamento não encontrado.");
            return;
        }

        if (!equipamento.isDisponivel()) {
            System.out.println("[Erro] Este equipamento já está emprestado no momento.");
            return;
        }

        Emprestimo emp = new Emprestimo(usuario, equipamento);
        emprestimosAtivos.add(emp);
        usuario.getItensEmprestados().add(equipamento);
        equipamento.setDisponivel(false);

        System.out.println("\n[Sucesso] Empréstimo realizado!");
        System.out.println("Prazo Limite para Devolução: " + emp.getDataDevolucaoPrevista());
    }

    private static void registrarDevolucao(Scanner scanner) {
        // Mostra a lista de equipamentos para saber qual id devolver
        listarEquipamentos();

        System.out.print("\nDigite o ID do equipamento que está sendo devolvido: ");
        int id = Integer.parseInt(scanner.nextLine());
        Emprestimo emprestimo = buscarEmprestimoAtivo(id);

        if (emprestimo == null) {
            System.out.println("[Erro] Nenhum empréstimo ativo encontrado para este ID.");
            return;
        }

        Equipamento eq = emprestimo.getEquipamento();
        Usuario us = emprestimo.getUsuario();

        if (eq instanceof Bicicleta) {
            System.out.println("\n--- VISTORIA OBRIGATÓRIA DE BICICLETA ---");
            System.out.print("Pneus em bom estado? (S/N): ");
            String pneu = scanner.nextLine();
            System.out.print("Freios funcionando? (S/N): ");
            String freio = scanner.nextLine();
            System.out.print("Corrente lubrificada/ajustada? (S/N): ");
            String corrente = scanner.nextLine();
            
            if (pneu.equalsIgnoreCase("N") || freio.equalsIgnoreCase("N") || corrente.equalsIgnoreCase("N")) {
                System.out.println("[Alerta] Bicicleta recebida com avarias! Encaminhar para manutenção.");
                eq.adicionarAoHistorico("⚠️ Necessita manutenção (Vistoria: Pneu=" + pneu + ", Freio=" + freio + ", Corrente=" + corrente + ")");
            } else {
                System.out.println("[Vistoria Concluída e Registrada com Sucesso]");
            }
        }

        System.out.print("A devolução está atrasada? (S/N): ");
        String atrasou = scanner.nextLine();
        if (atrasou.equalsIgnoreCase("S")) {
            System.out.print("Quantos dias de atraso? ");
            int dias = Integer.parseInt(scanner.nextLine());
            us.setBloqueadoAte(LocalDate.now().plusDays(dias * 2L));
            System.out.println("[Aviso] Usuário punido e bloqueado até: " + us.getBloqueadoAte());
        }

        eq.setDisponivel(true);
        us.getItensEmprestados().remove(eq);
        eq.adicionarAoHistorico("Devolvido por: " + us.getNome() + " em " + LocalDate.now());
        emprestimosAtivos.remove(emprestimo);

        System.out.println("\n[Sucesso] Devolução processada com êxito!");
    }

    private static void verHistoricoEquipamento(Scanner scanner) {
        System.out.print("\nDigite o ID do equipamento para ver o histórico: ");
        int id = Integer.parseInt(scanner.nextLine());
        Equipamento eq = buscarEquipamento(id);

        if (eq == null) {
            System.out.println("[Erro] Equipamento não encontrado.");
            return;
        }

        System.out.println("\n--- HISTÓRICO DE " + eq.getNome().toUpperCase() + " ---");
        if (eq.getHistorico().isEmpty()) {
            System.out.println("Nenhum registro de uso anterior encontrado.");
        } else {
            for (String registro : eq.getHistorico()) {
                System.out.println("- " + registro);
            }
        }
    }

    private static void cadastrarUsuario(Scanner scanner) {
        System.out.print("\nNome do usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula/Identificador único: ");
        String mat = scanner.nextLine();
        System.out.print("Tipo (1 - Aluno | 2 - Servidor): ");
        int tipo = Integer.parseInt(scanner.nextLine());

        if (tipo == 1) {
            usuarios.add(new Aluno(nome, mat));
            System.out.println("[Sucesso] Aluno cadastrado!");
        } else if (tipo == 2) {
            usuarios.add(new Servidor(nome, mat));
            System.out.println("[Sucesso] Servidor cadastrado!");
        } else {
            System.out.println("[Erro] Tipo de cadastro inválido.");
        }
    }

    private static Usuario buscarUsuario(String mat) {
        for (Usuario u : usuarios) {
            if (u.getMatricula().equals(mat)) return u;
        }
        return null;
    }

    private static Equipamento buscarEquipamento(int id) {
        for (Equipamento e : equipamentos) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    private static Emprestimo buscarEmprestimoAtivo(int idEquipamento) {
        for (Emprestimo emp : emprestimosAtivos) {
            if (emp.getEquipamento().getId() == idEquipamento) return emp;
        }
        return null;
    }
}
