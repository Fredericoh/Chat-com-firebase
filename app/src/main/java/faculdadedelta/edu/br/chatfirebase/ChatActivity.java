package faculdadedelta.edu.br.chatfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

import faculdadedelta.edu.br.chatfirebase.br.edu.faculdadedelta.modelo.Mensagem;
import faculdadedelta.edu.br.chatfirebase.br.edu.faculdadedelta.modelo.Usuario;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private Usuario usuario;
    private Usuario eu;
    private EditText etChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usuario = getIntent().getExtras().getParcelable("usuario");
        getSupportActionBar().setTitle(usuario.getNomeUsuario());


        RecyclerView recyclerView = findViewById(R.id.rvChat);
        etChat = findViewById(R.id.etChat);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem();
            }
        });

        adapter = new GroupAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("/usuarios")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        eu = documentSnapshot.toObject(Usuario.class);
                        procurarMensagem();
                    }
                });



    }

    private void procurarMensagem() {

        if (eu != null){

            String idRecebido = eu.getUuid();
            String idEnviado = usuario.getUuid();

            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(idRecebido)
                    .collection(idEnviado)
                    .orderBy("tempo", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                            if (documentChanges != null){

                                for (DocumentChange doc :
                                     documentChanges) {
                                    if (doc.getType() == DocumentChange.Type.ADDED){
                                        Mensagem mensagem = doc.getDocument().toObject(Mensagem.class);
                                        adapter.add(new ItemMensagem(mensagem));
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void enviarMensagem() {

        String texto = etChat.getText().toString();

        etChat.setText(null);

        String idRecebido = FirebaseAuth.getInstance().getUid();
        String idEnviado = usuario.getUuid();
        long tempo = System.currentTimeMillis();

        Mensagem mensagem = new Mensagem();
        mensagem.setIdRecebido(idEnviado);
        mensagem.setIdEnviado(idRecebido);
        mensagem.setTempo(tempo);
        mensagem.setTexto(texto);

        if (!mensagem.getTexto().isEmpty()){
            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(idRecebido)
                    .collection(idEnviado)
                    .add(mensagem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste", e.getMessage(), e);
                        }
                    });

            FirebaseFirestore.getInstance().collection("/conversas")
                    .document(idEnviado)
                    .collection(idRecebido)
                    .add(mensagem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste", documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste", e.getMessage(), e);
                        }
                    });
        }
    }

    private class ItemMensagem extends Item<ViewHolder>{

        private Mensagem mensagem;

        private ItemMensagem(Mensagem mensagem) {
            this.mensagem = mensagem;
        }


        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            TextView tvMensagem = viewHolder.itemView.findViewById(R.id.tvMensagemRecebida);
            ImageView ivMensagem = viewHolder.itemView.findViewById(R.id.ivMensagemRecebida);

            tvMensagem.setText(mensagem.getTexto());
            Picasso.get()
                    .load(mensagem.getIdRecebido().equals(FirebaseAuth.getInstance().getUid())
                    ? eu.getProFileUrl()
                    : usuario.getProFileUrl())
                    .into(ivMensagem);
        }

        @Override
        public int getLayout() {
            return mensagem.getIdEnviado().equals(FirebaseAuth.getInstance().getUid())
                    ? R.layout.item_mensagem_recebida
                    : R.layout.item_mensagem_enviada;
        }
    }
}
