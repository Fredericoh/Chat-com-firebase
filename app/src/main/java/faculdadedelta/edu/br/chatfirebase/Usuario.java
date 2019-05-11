package faculdadedelta.edu.br.chatfirebase;

public class Usuario {

    private String uuid;
    private String nomeUsuario;
    private String proFileUrl;

    public Usuario(String uuid, String nomeUsuario, String proFileUrl) {
        this.uuid = uuid;
        this.nomeUsuario = nomeUsuario;
        this.proFileUrl = proFileUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getProFileUrl() {
        return proFileUrl;
    }

    public void setProFileUrl(String proFileUrl) {
        this.proFileUrl = proFileUrl;
    }
}
