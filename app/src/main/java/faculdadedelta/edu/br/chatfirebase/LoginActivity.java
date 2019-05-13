package faculdadedelta.edu.br.chatfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mEtEmail;
    private EditText mEtSenha;
    private Button mBtnLogin;
    private TextView mTvCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtEmail = findViewById(R.id.etEmail);
        mEtSenha = findViewById(R.id.etSenha);
        mBtnLogin = findViewById(R.id.btnLogin);
        mTvCadastrar = findViewById(R.id.tvCadastrar);


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEtEmail.getText().toString();
                String senha = mEtSenha.getText().toString();

                Log.i("Teste", email);
                Log.i("Teste", senha);

                if (email == null || email.isEmpty() || senha == null || senha.isEmpty()){

                    Toast.makeText(getBaseContext(), "Os campos usuario, email e senha são obrigatorio. ", Toast.LENGTH_LONG).show();
                    limparCampos();
                    return;
                }


                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.i("Teste", task.getResult().getUser().getUid());

                                Intent intent = new Intent(getBaseContext(), MensagemActivity.class);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Teste", e.getMessage());
                            }
                        });

            }
        });

        mTvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void limparCampos() {

        mEtEmail.setText("");
        mEtSenha.setText("");
    }
}
