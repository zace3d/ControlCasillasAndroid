package mx.citydevs.denunciaelectoral;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/31/15.
 */
public class ComplaintPreviewActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = ComplaintsListActivity.class.getSimpleName();

    public static final String COMPLAINT = "complaint";
    public static final String IMAGE = "image";
    public static final String DESCRIPTION = "description";

    private ComplaintType complaint;
    private String description;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintpreview);

        setSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complaint = (ComplaintType) bundle.getSerializable(COMPLAINT);
            description = bundle.getString(DESCRIPTION);

            byte[] imageBytes = bundle.getByteArray(IMAGE);
            if (imageBytes != null) {
                bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            if (complaint != null && description != null && bitmap != null)
                initUI();
        }
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getString(R.string.complaint_preview_name));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initUI() {
        CustomTextView tvCategory = (CustomTextView) findViewById(R.id.complaintpreview_tv_category);
        ImageView ivLogo = (ImageView) findViewById(R.id.complaintpreview_iv_logo);

        if (complaint.getCategory() != null) {
            if (complaint.getCategory().getId() == CategoriesType.CIUDADANO_ID) {
                tvCategory.setText(getString(R.string.label_un_ciudadano));
                ivLogo.setImageResource(R.drawable.ic_ciudadano);

            } else if (complaint.getCategory().getId() == CategoriesType.FUNCIONARIO_ID) {
                tvCategory.setText(getString(R.string.label_un_funcionario));
                ivLogo.setImageResource(R.drawable.ic_funcionario);

            } else if (complaint.getCategory().getId() == CategoriesType.CANDIDATO_ID) {
                tvCategory.setText(getString(R.string.label_un_candidato));
                ivLogo.setImageResource(R.drawable.ic_candidato);
            }
        }

        CustomTextView tvTitle = (CustomTextView) findViewById(R.id.complaintpreview_tv_title);
        tvTitle.setText(complaint.getName());

        CustomTextView tvDescription = (CustomTextView) findViewById(R.id.complaintpreview_tv_description);
        tvDescription.setText(description);

        ImageView ivPhoto = (ImageView) findViewById(R.id.complaintpreview_iv_photo);
        ivPhoto.setImageBitmap(bitmap);

        findViewById(R.id.complaintpreview_btn_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complaintpreview_btn_send:
                sendComplaint();
                break;
            default:
                break;
        }
    }

    protected void sendComplaint() {

    }
}
