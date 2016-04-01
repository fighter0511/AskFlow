package pt.askflow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import pt.askflow.util.Variables;


public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private XMPPConnection connection;
    private boolean checkLogin = false;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
    }

    private void initComponents() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createConnection();
            }
        });
//        SharedPreferences sp = getSharedPreferences(Variables.ACCOUNT, MODE_PRIVATE);
//        edtPassword.setText(sp.getString(Variables.PASSWORD, ""));
//        edtUsername.setText(sp.getString(Variables.USEER_NAME, ""));
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Checking ...");
    }

    private void createConnection() {
        pDialog.show();
        final ConnectionConfiguration config = new ConnectionConfiguration(Variables.HOST, Variables.PORT, Variables.SERVICE);
        connection = new XMPPConnection(config);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //tao ket noi voi server
                try {
                    connection.connect();
                } catch (XMPPException e) {
                    Log.d("xxx", "error connection");
                    e.printStackTrace();
                }
                final String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();
                try {
                    connection.login(username, password);

                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    checkLogin = true;
                } catch (XMPPException e) {
                    e.printStackTrace();
                    checkLogin = false;
                    Log.d("xxx", "login error");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (checkLogin == true) {
                            SharedPreferences sp = getSharedPreferences(Variables.ACCOUNT, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Variables.USEER_NAME, username);
                            editor.putString(Variables.PASSWORD, password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showDialog();
                        }
                    }
                });
            }
        });
        thread.start();

    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Không thể đăng nhập, vui lòng thử lại");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
