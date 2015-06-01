package mx.citydevs.denunciaelectoral;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.utils.ImageUtils;

public class ChooseImageActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = ChooseImageActivity.class.getSimpleName();

	public static final int REQUEST_CODE_PHOTO = 1234;
	public static final int REQUEST_CODE_GALLERY = 4321;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseimage);

		initUI();
	}
	
	public void initUI() {
		findViewById(R.id.chooseimage_btn_gallery).setOnClickListener(this);
		findViewById(R.id.chooseimage_btn_camera).setOnClickListener(this);
		findViewById(R.id.container).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.chooseimage_btn_gallery:
				openGallery();
				break;

			case R.id.chooseimage_btn_camera:
				openCameraPhoto();
				break;

			case  R.id.container:
				finish();
				overridePendingTransition(0, 0);
				break;

			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == REQUEST_CODE_GALLERY) {
				try {
		        	Uri imageUri = data.getData();
					Bitmap bitmap = ImageUtils.resizeImageWithRatio(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
		        	
		        	if (bitmap != null) {
		        		setResult(bitmap);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_CODE_PHOTO) {
				getContentResolver().notifyChange(imageUri, null);

				try {
					Bitmap bitmap = ImageUtils.resizeImageWithRatio(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
					if (bitmap != null) {
						setResult(bitmap);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Escoge una opción"), REQUEST_CODE_GALLERY);
	}

	private Uri imageUri;
	public void openCameraPhoto() {
		// Check if there is a camera.
		Context context = getBaseContext();
		PackageManager packageManager = context.getPackageManager();
		if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Dialogues.Toast(getBaseContext(), "This device does not have a camera.", Toast.LENGTH_SHORT);
			return;
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(getPackageManager()) != null) {
			File imageFileCamera = new File(Environment.getExternalStorageDirectory(), "IMG_.jpg");
			imageUri = Uri.fromFile(imageFileCamera);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			//intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_PHOTO);
		}
	}

	public void setResult(Bitmap bitmap) {
		if(bitmap != null) {
			Intent data = new Intent();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
			data.putExtra("image", bs.toByteArray());
			
			setResult(ComplaintActivity.REQUEST_CODE_PICK_IMAGE, data);
			
			finish();
			overridePendingTransition(0, 0);
		} else {
			Dialogues.Toast(getApplicationContext(), "Debes seleccionar una imagen", Toast.LENGTH_SHORT);
		}
	}
}