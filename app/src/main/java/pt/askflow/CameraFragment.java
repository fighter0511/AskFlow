package pt.askflow;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import pt.askflow.util.CameraView;

/**
 * Created by PhucThanh on 1/25/2016.
 */
public class CameraFragment extends Fragment {

    private ImageView resultImage;

    public CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, null);

        resultImage = (ImageView) view.findViewById(R.id.image);
        resultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultImage.setImageDrawable(null);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uriSavedImage = Uri.fromFile(new File("/sdcard/flashCropped.png"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(intent, Crop.REQUEST_PICK);

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("xxx", "activity result");
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            File file = new File("/sdcard/flashCropped.png");
            Uri uri = Uri.fromFile(file);
            beginCrop(uri);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Log.d("xxx", "begin crop");
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(getActivity());
        Log.d("xxx", "end crop");
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d("xxx", "handle");
            resultImage.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("xxx", "pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("xxx", "Resume");
    }
}
