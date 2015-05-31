package mx.citydevs.denunciaelectoral;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/31/15.
 */
public class ComplaintActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = ComplaintsListActivity.class.getSimpleName();

    public static final String COMPLAINT = "complaint";
    private ComplaintType complaint;

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
        actionbarTitle.setText(getResources().getString(R.string.app_name));

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

        findViewById(R.id.complaint_btn_moreinfo).setOnClickListener(this);
        findViewById(R.id.complaint_btn_camera).setOnClickListener(this);
        findViewById(R.id.complaint_btn_skip).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complaint_btn_moreinfo:
                showDialogMoreInfo(complaint.getDescription());
                break;
            case R.id.complaint_btn_camera:

                break;
            case R.id.complaint_btn_skip:

                break;
            default:
                break;
        }
    }

    protected void showDialogMoreInfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
