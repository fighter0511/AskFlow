package pt.askflow;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import pt.askflow.xml.Element;
import pt.askflow.xmpp.jid.IqPacket;
import pt.askflow.xmpp.jid.Xmlns;

public class Main2Activity extends AppCompatActivity {

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        result = (TextView) findViewById(R.id.result);
        String host = "upload.anyquiz.info";
        IqPacket packet = new IqPacket(IqPacket.TYPE.GET);
        packet.setTo(host);
        Element request = packet.addChild("request", Xmlns.HTTP_UPLOAD);
        request.addChild("filename").setContent("/sdcard/flashCropped.png");
        request.addChild("size").setContent("12345");
        request.addChild("content-type").setContent("image/jpeg");
        result.setText(request.toString());
    }

}
