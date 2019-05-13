package faculdadedelta.edu.br.chatfirebase;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Usuario usuario = getIntent().getExtras().getParcelable("usuario");
        getSupportActionBar().setTitle(usuario.getNomeUsuario());
        

        RecyclerView recyclerView = findViewById(R.id.rvChat);

        adapter = new GroupAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);

        adapter.add(new ItemMensagem(true));
        adapter.add(new ItemMensagem(true));
        adapter.add(new ItemMensagem(false));
        adapter.add(new ItemMensagem(false));
        adapter.add(new ItemMensagem(false));
        adapter.add(new ItemMensagem(true));
        adapter.add(new ItemMensagem(true));
        adapter.add(new ItemMensagem(true));
        adapter.add(new ItemMensagem(false));
        adapter.add(new ItemMensagem(true));


    }

    private class ItemMensagem extends Item<ViewHolder>{

        private boolean mensagemRecebida;

        private ItemMensagem(boolean mensagemRecebida) {
            this.mensagemRecebida = mensagemRecebida;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            return mensagemRecebida ? R.layout.item_mensagem_recebida
                    : R.layout.item_mensagem_enviada;
        }
    }
}
