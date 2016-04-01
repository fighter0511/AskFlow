package pt.askflow;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.soundcloud.android.crop.Crop;

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pt.askflow.util.Variables;
import pt.askflow.xml.Element;
import pt.askflow.xmpp.jid.DownloadableFile;
import pt.askflow.xmpp.jid.IqPacket;
import pt.askflow.xmpp.jid.Xmlns;

/**
 * Created by PhucThanh on 1/25/2016.
 */
public class CameraActivity extends AppCompatActivity {
    private ImageView resultView;
    private ImageButton btnCapture;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        resultView = (ImageView) findViewById(R.id.image);
        btnCapture = (ImageButton) findViewById(R.id.btn_capture);
        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uriSavedImage = Uri.fromFile(new File("/sdcard/flashCropped.png"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(intent, Crop.REQUEST_PICK);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        Log.d("xxx", "activity result");
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            final File file = new File("/sdcard/flashCropped.png");
            final Uri uri = Uri.fromFile(file);
            beginCrop(uri);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileExtension
                            = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                    String mimeType
                            = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    long imageSize = file.length();
                    sendIqPacket("flashCropped.png", mimeType, imageSize);
                }
            });
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Log.d("xxx", "begin crop");
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Log.d("xxx", "handle");
            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    XMPPConnection connection;
    public void sendIqPacket(final String fileName, final String mime, final long imageSize){
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
                                String host = "upload.anyquiz.info";
                                IqPacket packet = new IqPacket(IqPacket.TYPE.GET);
                                packet.setTo(host);
                                Element request = packet.addChild("request", Xmlns.HTTP_UPLOAD);
                                request.addChild("filename").setContent(fileName);
                                request.addChild("size").setContent(imageSize + "");
                                request.addChild("content-type").setContent(mime);
                                return request.toString();
                            }
                        };

                        iq.setType(IQ.Type.SET);
                        connection.sendPacket(iq);
                    }
                });
            }
        });
        thread.start();
    }
}
