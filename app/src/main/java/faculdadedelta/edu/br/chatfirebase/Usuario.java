package faculdadedelta.edu.br.chatfirebase;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String uuid;
    private String nomeUsuario;
    private String proFileUrl;

    public Usuario() {

    }

    public Usuario(String uuid, String nomeUsuario, String proFileUrl) {
        this.uuid = uuid;
        this.nomeUsuario = nomeUsuario;
        this.proFileUrl = proFileUrl;
    }

    protected Usuario(Parcel in) {
        uuid = in.readString();
        nomeUsuario = in.readString();
        proFileUrl = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nomeUsuario);
        dest.writeString(proFileUrl);
    }
}
