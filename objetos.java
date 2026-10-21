import   java.time.LocalDate;


enum tipo_Objeto{
    BOLA(0),
    COLETE(0),
    RAQUETE(2),
    BICICLETA(7);
    
    private final int prazo;
    //contrutor
    tipo_Objeto(int prazo){
        this.prazo = prazo;
    }

    //GETTER
    public int getPrazo(){
        return prazo;
    }
}

public class objetos {
    private string nome;
    private tipo_Objeto tipo;
    private int quantidade;
    private boolean quebrado;

    //Construtor
    objetos(string nome, tipo_Objeto tipo, int quantidade, boolean quebrado){
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.quebrado = quebrado;
    }
}