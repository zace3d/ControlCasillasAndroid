package mx.citydevs.denunciaelectoral;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.views.CustomEditText;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/31/15.
 */
public class ComplaintActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = ComplaintsListActivity.class.getSimpleName();

    public static final int REQUEST_CODE_PICK_IMAGE = 5678;
    public static final int REQUEST_CODE_COMPLAINT = 1234;

    public static final String COMPLAINT = "complaint";
    private ComplaintType complaint;
    private String description;
    private Bitmap bitmap;
    private CustomEditText etDescription;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        setSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complaint = (ComplaintType) bundle.getSerializable(COMPLAINT);

            if (complaint != null)
                initUI();
        }
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getResources().getString(R.string.complaint_name));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initUI() {
        CustomTextView tvCategory = (CustomTextView) findViewById(R.id.complaint_tv_category);
        if (complaint.getCategory() != null) {
            if (complaint.getCategory().getId() == CategoriesType.CIUDADANO_ID) {
                tvCategory.setText(getString(R.string.label_un_ciudadano));

            } else if (complaint.getCategory().getId() == CategoriesType.FUNCIONARIO_ID) {
                tvCategory.setText(getString(R.string.label_un_funcionario));

            } else if (complaint.getCategory().getId() == CategoriesType.CANDIDATO_ID) {
                tvCategory.setText(getString(R.string.label_un_candidato));
            }
        }

        CustomTextView tvDescription = (CustomTextView) findViewById(R.id.complaint_tv_description);
        tvDescription.setText(complaint.getName());

        findViewById(R.id.complaint_btn_moreinfo).setOnClickListener(this);
        findViewById(R.id.complaint_btn_camera).setOnClickListener(this);
        findViewById(R.id.complaint_btn_continue).setOnClickListener(this);

        findViewById(R.id.complaint_iv_clear).setOnClickListener(this);
        findViewById(R.id.complaint_iv_image).setOnClickListener(this);

        etDescription = (CustomEditText) findViewById(R.id.complaint_et_description);
        //etDescription.setRegexType(RegularExpressions.KEY_IS_TEXT);
    }

    private void validateInformation() {
        boolean hasError = false;

        if (etDescription.hasSyntaxError() || etDescription.isEmpty()) {
            hasError = true;
        }

        if (hasError) {
            // etDescription.setErrorMessage(getString(R.string.wrong_information));
            Dialogues.Toast(getBaseContext(), getString(R.string.edittext_wrong_info), Toast.LENGTH_SHORT);
        } else {
            description = etDescription.getText().toString();

            if (bitmap != null)
                startComplaintPreviewIntent();
            else
                Dialogues.Toast(getBaseContext(), getString(R.string.image_field_empty), Toast.LENGTH_SHORT);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complaint_btn_moreinfo:
                showDialogMoreInfo(complaint.getDescription());
                break;
            case R.id.complaint_btn_camera:
                startChooseImageIntent();
                break;
            case R.id.complaint_btn_continue:
                etDescription.closeKeyboard();
                validateInformation();
                break;
            case R.id.complaint_iv_clear:
                clearSelectedImage();
                break;
            case R.id.complaint_iv_image:
                startImageViewerIntent();
                break;
            default:
                break;
        }
    }

    protected void clearSelectedImage() {
        ImageView ivPhoto = (ImageView) findViewById(R.id.complaint_iv_image);
        ivPhoto.setImageBitmap(null);
        bitmap = null;

        if (findViewById(R.id.complaint_vg_image).getVisibility() != View.GONE)
            findViewById(R.id.complaint_vg_image).setVisibility(View.GONE);

        if (findViewById(R.id.complaint_btn_camera).getVisibility() != View.VISIBLE)
            findViewById(R.id.complaint_btn_camera).setVisibility(View.VISIBLE);
    }

    protected void setSelectedImage(Bitmap bitmap) {
        ImageView ivPhoto = (ImageView) findViewById(R.id.complaint_iv_image);
        ivPhoto.setImageBitmap(bitmap);

        if (findViewById(R.id.complaint_vg_image).getVisibility() != View.VISIBLE)
            findViewById(R.id.complaint_vg_image).setVisibility(View.VISIBLE);

        if (findViewById(R.id.complaint_btn_camera).getVisibility() != View.GONE)
            findViewById(R.id.complaint_btn_camera).setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null && data.hasExtra("image")) {
                bitmap = BitmapFactory.decodeByteArray(data.getByteArrayExtra("image"), 0, data.getByteArrayExtra("image").length);

                if (bitmap != null) {
                    setSelectedImage(bitmap);
                }
            } else {
                // Dialogues.Toast(getBaseContext(), "No hay imagen.", Toast.LENGTH_SHORT);
            }
        } else if (requestCode == REQUEST_CODE_COMPLAINT) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Dialogues.Toast(this, getString(R.string.label_onbackpressed), Toast.LENGTH_SHORT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    protected void showDialogMoreInfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void startChooseImageIntent() {
        Intent intent = new Intent(getBaseContext(), ChooseImageActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        overridePendingTransition(0, 0);
    }

    protected void startComplaintPreviewIntent() {
        Intent intent = new Intent(getBaseContext(), ComplaintPreviewActivity.class);
        intent.putExtra(ComplaintPreviewActivity.COMPLAINT, complaint);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra(ComplaintPreviewActivity.IMAGE, bs.toByteArray());
        intent.putExtra(ComplaintPreviewActivity.DESCRIPTION, description);
        startActivityForResult(intent, REQUEST_CODE_COMPLAINT);
    }

    protected void startImageViewerIntent() {
        Intent intent = new Intent(getBaseContext(), ImageViewerActivity.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra(ComplaintPreviewActivity.IMAGE, bs.toByteArray());
        startActivity(intent);
    }
}
