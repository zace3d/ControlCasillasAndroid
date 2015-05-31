package mx.citydevs.denunciaelectoral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mx.citydevs.denunciaelectoral.utils.PreferencesUtils;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/30/15.
 */
public class AboutActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setSupportActionBar();

        boolean isFirstTime = PreferencesUtils.getBooleanPreference(getApplication(), PreferencesUtils.ABOUT);
        if (isFirstTime) {
            initUI();
        } else {
            startMainIntent();
        }
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getResources().getString(R.string.about_name));

        setSupportActionBar(mToolbar);
    }

    private void startMainIntent() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        // overridePendingTransition(0, 0);
        finish();
    }

    private void initUI() {
        findViewById(R.id.about_btn_skip).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_btn_skip:
                PreferencesUtils.putBooleanPreference(getApplication(), PreferencesUtils.ABOUT, false);
                startMainIntent();
                break;
        }
    }
}