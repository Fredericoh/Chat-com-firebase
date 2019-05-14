package faculdadedelta.edu.br.chatfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import faculdadedelta.edu.br.chatfirebase.br.edu.faculdadedelta.modelo.Usuario;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEtUsuario; //= findViewById(R.id.etUsuario);
    private EditText mEtEmail;
    private EditText mEtSenha;
    private Button mBtnCadastrar;
    private Button mBtnImagemPerfil;
    private Uri mSelecionarUri;
    private ImageView mImgPerfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtUsuario = findViewById(R.id.etUsuario);
        mEtEmail = findViewById(R.id.etEmail);
        mEtSenha = findViewById(R.id.etSenha);
        mBtnCadastrar = findViewById(R.id.btnCadastrar);
        mBtnImagemPerfil = findViewById(R.id.btnImagemPerfil);
        mImgPerfil = findViewById(R.id.imgPerfil);


        mBtnImagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selecionarImagemPerfil();
            }
        });


        mBtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                criarUsuario();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0){

            mSelecionarUri = data.getData();

            Bitmap bitmap = null;

            try {
               bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelecionarUri);
               mImgPerfil.setImageDrawable(new BitmapDrawable(bitmap));
               mBtnImagemPerfil.setAlpha(0);
            } catch (IOException e) {
            }
        }
    }

    private void selecionarImagemPerfil() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void limparCampos(){
        mEtUsuario.setText("");
        mEtEmail.setText("");
        mEtSenha.setText("");
        mImgPerfil.setImageDrawable(null);
    }

    private void criarUsuario(){

        String nomeUsuario = mEtUsuario.getText().toString();
        String email = mEtEmail.getText().toString();
        String senha = mEtSenha.getText().toString();

        if ( mSelecionarUri == null || nomeUsuario.isEmpty() || email.isEmpty() || senha.isEmpty()){

            Toast.makeText(getBaseContext(), "Os campos imagem, usuario, email e senha são obrigatorio. ", Toast.LENGTH_LONG).show();
            return;
        }



        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Log.i("Teste", task.getResult().getUser().getUid());

                    salvarUsuarioNoFireBase();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.i("Teste", e.getMessage());
            }
        });


    }

    private void salvarUsuarioNoFireBase() {

        String nomeArquivo = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/images/" + nomeArquivo);
        reference.putFile(mSelecionarUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Log.i("Teste", uri.toString());

                                String uid = FirebaseAuth.getInstance().getUid();
                                String nomeUsuario = mEtUsuario.getText().toString();
                                String proFileUrl = uri.toString();

                                final Usuario usuario = new Usuario(uid, nomeUsuario, proFileUrl);

                                FirebaseFirestore.getInstance().collection("usuarios")
                                        .document(uid)
                                        .set(usuario)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent intent = new Intent(getBaseContext(), MensagemActivity.class);

                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                startActivity(intent);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("Error", e.getMessage());
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage(), e);
                    }
                });
    }
}
