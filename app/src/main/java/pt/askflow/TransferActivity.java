package pt.askflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.BytestreamsProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import java.io.File;

import pt.askflow.util.Variables;
import pt.askflow.xmpp.jid.IqPacket;
import pt.stickerlibrary.MessageItem;

public class TransferActivity extends AppCompatActivity {

    Button transferButton;
    XMPPConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
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
        transferButton = (Button) findViewById(R.id.send);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();

            }
        });
    }

    public void send()
    {

//        configureProviderManager(connection);
//        FileTransferNegotiator.IBB_ONLY = true;
//        FileTransferNegotiator.setServiceEnabled(connection, true);
//        FileTransferManager manager = new FileTransferManager(connection);
        //OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer("hariom@jabber.ccc.de/Smack");
//        String to = connection.getRoster().getPresence("huunc@anyquiz.info").getFrom();
//        OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(to);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                final ConnectionConfiguration config = new ConnectionConfiguration(Variables.HOST, Variables.PORT, Variables.SERVICE);
//                XMPPConnection connection = new XMPPConnection(config);
//                final IQ iq = new IQ() {
//                    @Override
//                    public String getChildElementXML() {
//                        return "<iq from=\"kyhoolee@anyquiz.info/mobile\" id=\"472kes5aul\" type=\"set\"><query xmlns=\"anyquiz:iq:quiz_ask\"><url>http://anyquiz.info:8444/cead2b72d36d414947976faf4ae1c94a771ac2a5/yUXcaL3Up2LZnvERTgcmk7p15qHDrxd03pzImrLf/croppedImage.jpg</url><quiz></quiz><qid>1459323748017</qid><topic>GMAT</topic></query></iq>";
//                    }
//                };
//                iq.setType(IQ.Type.SET);
//                connection.sendPacket(iq);
//                Log.d("xxx", iq.toString());
//            }
//        });
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
                final String username = "kyhoolee@anyquiz.info";
                final String password = "abc@123";
                try {
                    connection.login(username, password);

                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);

                } catch (XMPPException e) {
                    e.printStackTrace();
                    Log.d("xxx", "login error");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final IQ iq = new IQ() {
                            @Override
                            public String getChildElementXML() {

                                return "<iq from=\"ask1@anyquiz.info/mobile\" id=\"3p5pe9fqke\" to=\"upload.anyquiz.info\" type=\"get\">\n" +
                                        " <request xmlns=\"urn:xmpp:http:upload\">\n" +
                                        "   <filename>bai1.jpg</filename>\n" +
                                        "   <size>93682</size>\n" +
                                        "   <content-type>image/jpeg</content-type>\n" +
                                        " </request>\n" +
                                        "</iq>";
                            }
                        };

                        iq.setType(IQ.Type.SET);
                        connection.sendPacket(iq);
                        PacketFilter filter = new IQTypeFilter(IQ.Type.RESULT);
                        Log.d("filter", filter.toString());
                        connection.addPacketListener(new PacketListener() {
                            @Override
                            public void processPacket(Packet packet) {
                                Message message = (Message)packet;
                                Log.d("messs", message.toString());
                            }
                        }, filter);
                    }
                });
            }
        });
        thread.start();
//        File file = new File("/sdcard/flashCropped.png");
//        try {
//            Log.d("file sending",file.getAbsolutePath()+" "+file.getName());
//            configureProviderManager(connection);
//            transfer.sendFile(file, "test_file");
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        }
//
//        while(!transfer.isDone()) {
//            Log.d("status", transfer.getStatus().toString());
//            Log.d("percent", new Long(transfer.getBytesSent()).toString());
//            if (transfer.getStatus() == FileTransfer.Status.error) {
//                Log.e("percent", "Error " + new Long(transfer.getBytesSent()).toString() + " " + transfer.getError() + " " + transfer.getException());
//                transfer.cancel();
//
//            }
//
//            if(transfer.getStatus().equals(FileTransfer.Status.refused))
//                Log.d("xxx", transfer.getError() + "refused");
//            else if( transfer.getStatus().equals(FileTransfer.Status.error))
//                Log.d("xxx", transfer.getError() + "Error");
//            else if(transfer.getStatus().equals(FileTransfer.Status.cancelled))
//                Log.d("xxx", transfer.getError() + "Cancelled");
//            else
//                Log.d("xxx", "success");



//        }
    }





}
