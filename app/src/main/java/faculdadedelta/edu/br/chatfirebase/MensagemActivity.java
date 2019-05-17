package faculdadedelta.edu.br.chatfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
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

import faculdadedelta.edu.br.chatfirebase.br.edu.faculdadedelta.modelo.Contato;

public class MensagemActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        RecyclerView rvContatos = findViewById(R.id.rvContatos);
        rvContatos.setLayoutManager(new LinearLayoutManager(getBaseContext()));


        adapter = new GroupAdapter();
        rvContatos.setAdapter(adapter);


        verificarAutenticacao();

        buscarUltimaMensagem();
    }

    private void buscarUltimaMensagem() {

        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/ultimas-mensagens")
                .document(uid)
                .collection("/contatos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                        if (documentChanges != null){
                            for (DocumentChange doc : documentChanges) {
                                if (doc.getType() == DocumentChange.Type.ADDED){
                                    Contato contato = doc.getDocument().toObject(Contato.class);

                                    adapter.add(new ItemDeContato(contato));
                                }
                                
                            }
                        }
                    }
                });

    }

    private void verificarAutenticacao() {
        if (FirebaseAuth.getInstance().getUid() == null){

            Intent intent = new Intent(getBaseContext(), LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.contatos :

                Intent intent = new Intent(getBaseContext(), ContatosActivity.class);
                startActivity(intent);
                break;


            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                verificarAutenticacao();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ItemDeContato extends Item<ViewHolder>{

        Contato contato = new Contato();

        public ItemDeContato(Contato contato) {
            this.contato = contato;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            TextView nomeUsuario = viewHolder.itemView.findViewById(R.id.tvMensagem);
            TextView mensagem = viewHolder.itemView.findViewById(R.id.tvMensagem2);
            ImageView fotoUsuario = viewHolder.itemView.findViewById(R.id.imageView);

            nomeUsuario.setText(contato.getNomeUsuario());
            mensagem.setText(contato.getUltimaMensagem());
            Picasso.get()
                    .load(contato.getFotoUrl())
                    .into(fotoUsuario);
        }

        @Override
        public int getLayout() {
            return R.layout.item_usuario_mensagem;
        }
    }
}
