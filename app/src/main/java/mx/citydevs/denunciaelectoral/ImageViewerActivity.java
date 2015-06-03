package mx.citydevs.denunciaelectoral;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.views.CustomTextView;
import mx.citydevs.denunciaelectoral.views.TouchImageView;

/**
 * Created by zace3d on 6/2/15.
 */
public class ImageViewerActivity extends Activity {
    public static final String TAG_CLASS = MainActivity.class.getSimpleName();

    public static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] imageBytes = bundle.getByteArray(IMAGE);
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                if (bitmap != null)
                    initUI(bitmap);
                else
                    exitActivity();
            }
        } else {
            exitActivity();
        }
    }

    protected void exitActivity() {
        finish();
        Dialogues.Toast(getBaseContext(), "No se pudo obtener la imagen.", Toast.LENGTH_SHORT);
    }

    protected void initUI(Bitmap bitmap) {
        TouchImageView image = (TouchImageView) findViewById(R.id.imageviewer_iv_photo);
        image.setImageBitmap(bitmap);
    }
}
