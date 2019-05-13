package faculdadedelta.edu.br.chatfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MensagemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        verificarAutenticacao();
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
}
