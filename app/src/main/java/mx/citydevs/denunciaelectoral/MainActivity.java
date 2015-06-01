package mx.citydevs.denunciaelectoral;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.httpconnection.HttpConnection;
import mx.citydevs.denunciaelectoral.parser.GsonParser;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/26/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = MainActivity.class.getSimpleName();
    private ArrayList<ComplaintType> listComplaintsTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar();
        initUI();

        GetDenunciasTypesPublicationsAsyncTask task = new GetDenunciasTypesPublicationsAsyncTask();
        task.execute();
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getString(R.string.app_name));

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
                startListComplaintsIntent(CategoriesType.CIUDADANO_ID);
                break;
            case R.id.main_btn_funcionario:
                startListComplaintsIntent(CategoriesType.FUNCIONARIO_ID);
                break;
            case R.id.main_btn_candidato:
                startListComplaintsIntent(CategoriesType.CANDIDATO_ID);
                break;
            default:
        }
    }

    protected void startListComplaintsIntent(int categoryId) {
        Intent intent = new Intent(getBaseContext(), ComplaintsListActivity.class);
        intent.putExtra(ComplaintsListActivity.COMPLAINTS_TYPES, listComplaintsTypes);
        intent.putExtra(ComplaintsListActivity.CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    private class GetDenunciasTypesPublicationsAsyncTask extends AsyncTask<String, String, String> {
        public GetDenunciasTypesPublicationsAsyncTask() {}

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            return HttpConnection.POST(HttpConnection.URL + HttpConnection.TYPES);
        }

        @Override
        protected void onPostExecute(String result) {
            Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);

            if (result != null) {
                try {
                    listComplaintsTypes = (ArrayList<ComplaintType>) GsonParser.getListComplaintsTypesFromJSON(result);

                    if (listComplaintsTypes != null && listComplaintsTypes.size() > 0) {
                        Dialogues.Toast(getBaseContext(), listComplaintsTypes.size() + " Result: " + result, Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
