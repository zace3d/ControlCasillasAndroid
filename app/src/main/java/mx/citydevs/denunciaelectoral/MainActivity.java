package mx.citydevs.denunciaelectoral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;

import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.httpconnection.HttpConnection;
import mx.citydevs.denunciaelectoral.location.LocationClientListener;
import mx.citydevs.denunciaelectoral.parser.GsonParser;
import mx.citydevs.denunciaelectoral.utils.LocationUtils;
import mx.citydevs.denunciaelectoral.utils.NetworkUtils;
import mx.citydevs.denunciaelectoral.utils.PreferencesUtils;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/26/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG_CLASS = MainActivity.class.getSimpleName();

    private ArrayList<ComplaintType> listComplaintsTypes;

    private LocationClientListener clientListener;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar();

        loadComplaints();

        if (LocationUtils.isGpsOrNetworkProviderEnabled(getBaseContext())) {
            initLocationClientListener();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (clientListener != null && userLocation == null) {
            startLocationListener();
        }
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

    protected void initLocationClientListener() {
        clientListener = new LocationClientListener(this);
        clientListener.setOnLocationClientListener(new LocationClientListener.OnLocationClientListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = LocationUtils.getLatLngFromLocation(location);

                // Dialogues.Toast(getBaseContext(), "Location.", Toast.LENGTH_SHORT);

                PreferencesUtils.putStringPreference(getApplication(),
                        PreferencesUtils.LOCATION, userLocation.latitude + "," + userLocation.longitude);

                clientListener.stopLocationUpdates();
            }
        });
    }

    public void startLocationListener() {
        if (clientListener != null)
            clientListener.connect();
    }

    protected void loadComplaints() {
        if (findViewById(R.id.main_btn_refresh).getVisibility() != View.GONE)
            findViewById(R.id.main_btn_refresh).setVisibility(View.GONE);

        if (findViewById(R.id.container).getVisibility() != View.VISIBLE)
            findViewById(R.id.container).setVisibility(View.VISIBLE);

        if (NetworkUtils.isNetworkConnectionAvailable(getBaseContext())) {
            GetComplaintsTypesAsyncTask task = new GetComplaintsTypesAsyncTask();
            task.execute();
        } else {
            setErrorMessage();
            Dialogues.Toast(getBaseContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT);
        }
    }

    protected void setErrorMessage() {
        View view = findViewById(R.id.main_btn_refresh);
        view.setOnClickListener(this);

        if (view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);

        if (findViewById(R.id.container).getVisibility() != View.GONE)
            findViewById(R.id.container).setVisibility(View.GONE);
    }

    /*@Override
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
    }*/

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
            case R.id.main_btn_refresh:
                loadComplaints();
                break;
            default:
                break;
        }
    }

    protected void startListComplaintsIntent(int categoryId) {
        Intent intent = new Intent(getBaseContext(), ComplaintsListActivity.class);
        intent.putExtra(ComplaintsListActivity.COMPLAINTS_TYPES, getFilterListComplaints(categoryId));
        intent.putExtra(ComplaintsListActivity.CATEGORY_ID, categoryId);
        startActivity(intent);
    }

    protected ArrayList<ComplaintType> getFilterListComplaints(int categoryId) {
        ArrayList<ComplaintType> listAux = new ArrayList<>();

        for (ComplaintType complaintType : listComplaintsTypes) {
            if (complaintType.getCategory() != null) {
                if (complaintType.getCategory().getId() == categoryId) {
                    listAux.add(complaintType);
                }
            }
        }

        return listAux;
    }

    private class GetComplaintsTypesAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;

        public GetComplaintsTypesAsyncTask() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Obteniendo información...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpConnection.POST(HttpConnection.URL + HttpConnection.TYPES, null);
        }

        @Override
        protected void onPostExecute(String result) {
            // Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);

            if (result != null) {
                try {
                    listComplaintsTypes = (ArrayList<ComplaintType>) GsonParser.getListComplaintsTypesFromJSON(result);

                    if (listComplaintsTypes != null && listComplaintsTypes.size() > 0) {
                        // Dialogues.Toast(getBaseContext(), listComplaintsTypes.size() + " Result: " + result, Toast.LENGTH_LONG);
                        initUI();
                    } else {
                        setErrorMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrorMessage();
                }
            } else {
                setErrorMessage();
            }

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        }
    }
}
