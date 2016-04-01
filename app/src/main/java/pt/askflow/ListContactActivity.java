package pt.askflow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.askflow.adapter.AdapterListContact;
import pt.askflow.util.Contact;


public class ListContactActivity extends AppCompatActivity {

    private ListView listContact;
    private FloatingActionButton fab;
    private List<Contact> contacts;
    private AdapterListContact adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);
        initComponents();
    }

    private void initComponents(){
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.icon);

        listContact = (ListView) findViewById(R.id.list_contact);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        contacts = new ArrayList<Contact>();
        contacts.add(new Contact(R.drawable.user_chat1, "kyhoolee@anyquiz.info"));
        contacts.add(new Contact(R.drawable.user_chat2, "huunc@anyquiz.info"));
        adapter = new AdapterListContact(this, contacts);
        listContact.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contacts.get(position);
                Intent intent = new Intent(ListContactActivity.this, ChatActivity.class);
                intent.putExtra("contact", contact.getName());
                startActivity(intent);
            }
        });
    }

    private void addContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm liên hệ");
        builder.setMessage("Nhập tên người dùng");
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        editText.setGravity(Gravity.CENTER);
        editText.setHint("username@chat.equiz.vn");
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int avatar = R.drawable.ic_username;
                String name = editText.getText().toString();
                contacts.add(new Contact(avatar, name));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
