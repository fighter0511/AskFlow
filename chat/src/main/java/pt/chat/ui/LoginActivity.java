package pt.chat.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pt.chat.R;
import pt.chat.entities.Account;

public class LoginActivity extends AppCompatActivity {

    private Account mAccoutn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.login);
        EditText name = (EditText) findViewById(R.id.name);
        EditText pass = (EditText) findViewById(R.id.pass);


    }

}
