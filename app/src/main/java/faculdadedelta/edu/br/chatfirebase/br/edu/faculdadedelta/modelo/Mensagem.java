package faculdadedelta.edu.br.chatfirebase.br.edu.faculdadedelta.modelo;

public class Mensagem {

    private String texto;
    private long tempo;
    private String idRecebido;
    private String idEnviado;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public String getIdRecebido() {
        return idRecebido;
    }

    public void setIdRecebido(String idRecebido) {
        this.idRecebido = idRecebido;
    }

    public String getIdEnviado() {
        return idEnviado;
    }

    public void setIdEnviado(String idEnviado) {
        this.idEnviado = idEnviado;
    }
}
