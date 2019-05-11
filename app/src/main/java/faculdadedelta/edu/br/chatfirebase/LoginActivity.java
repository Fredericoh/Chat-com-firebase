package faculdadedelta.edu.br.chatfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
}
