package mx.citydevs.denunciaelectoral;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/26/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar();
        initUI();
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getResources().getString(R.string.app_name));

        setSupportActionBar(mToolbar);
    }

    protected void initUI() {
        findViewById(R.id.main_btn_ciudadano).setOnClickListener(this);
        findViewById(R.id.main_btn_funcionario).setOnClickListener(this);
        findViewById(R.id.main_btn_candidato).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_ciudadano:
                startCiudadanoIntent();
                break;
            case R.id.main_btn_funcionario:
                startCiudadanoIntent();
                break;
            case R.id.main_btn_candidato:
                startCiudadanoIntent();
                break;
            default:
        }
    }

    protected void startCiudadanoIntent() {
        Intent intent = new Intent();
        startActivity(intent);
    }
}
