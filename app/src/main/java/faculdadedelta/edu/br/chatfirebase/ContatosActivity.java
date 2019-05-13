package faculdadedelta.edu.br.chatfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

public class ContatosActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        RecyclerView recyclerView = findViewById(R.id.rvContatos);

        adapter = new GroupAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        buscarUsuarios();
    }

    private void buscarUsuarios() {

        FirebaseFirestore.getInstance().collection("/usuarios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null){
                            Log.e("Teste", e.getMessage(), e);
                            return;
                        }

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {

                            Usuario usuario = doc.toObject(Usuario.class);
                            adapter.add(new ItemUsuario(usuario));
                        }
                    }
                });

    }

    private class ItemUsuario extends Item<ViewHolder> {

        private Usuario usuario;

        public ItemUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            TextView txtUsuario = viewHolder.itemView.findViewById(R.id.tvMensagem);
            ImageView fotoUsuario = viewHolder.itemView.findViewById(R.id.imageView);

            txtUsuario.setText(usuario.getNomeUsuario());

            Picasso.get()
                    .load(usuario.getProFileUrl())
                    .into(fotoUsuario);

        }

        @Override
        public int getLayout() {
            return R.layout.item_usuario;
        }


    }
}
