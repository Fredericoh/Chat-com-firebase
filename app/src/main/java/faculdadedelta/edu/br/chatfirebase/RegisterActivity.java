package faculdadedelta.edu.br.chatfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEtUsuario;
    private EditText mEtEmail;
    private EditText mEtSenha;
    private Button mBtnCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtUsuario = findViewById(R.id.etUsuario);
        mEtEmail = findViewById(R.id.etEmail);
        mEtSenha = findViewById(R.id.etSenha);
        mBtnCadastrar = findViewById(R.id.btnCadastrar);

        mBtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                criarUsuario();

            }
        });


    }

    private void limparCampos(){
        mEtUsuario.setText("");
        mEtEmail.setText("");
        mEtSenha.setText("");
    }

    private void criarUsuario(){

        String email = mEtEmail.getText().toString();
        String senha = mEtSenha.getText().toString();

        if (email == null || email.isEmpty() || senha == null || senha.isEmpty()){

            Toast.makeText(getBaseContext(), "Os campos email e senha são obrigatorio. ", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Usuario cadastrado com sucesso. ", Toast.LENGTH_LONG).show();
            limparCampos();
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Log.i("Teste", task.getResult().getUser().getUid());
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
}
