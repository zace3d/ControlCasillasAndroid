package mx.citydevs.denunciaelectoral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import mx.citydevs.denunciaelectoral.adapters.RecyclerAdapter;
import mx.citydevs.denunciaelectoral.adapters.SimpleDividerItemDecoration;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/27/15.
 */
public class ComplaintsListActivity extends ActionBarActivity {
    public static final String TAG_CLASS = ComplaintsListActivity.class.getSimpleName();

    public static final String COMPLAINTS_TYPES = "complaints_types";
    public static final String CATEGORY_ID = "category_id";
    private List<ComplaintType> listComplaintsTypes;

    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomplaints);

        setSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            listComplaintsTypes = (List<ComplaintType>) bundle.getSerializable(COMPLAINTS_TYPES);
            int categoryId = bundle.getInt(CATEGORY_ID);

            if (listComplaintsTypes != null && listComplaintsTypes.size() > 0) {
                initUI(listComplaintsTypes, categoryId);
            } else {
                finish();
                Dialogues.Toast(getBaseContext(), "No se encontraron denuncias en esa categoría", Toast.LENGTH_SHORT);
            }
        }
    }

    protected void setSupportActionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mToolbar.setTitle("");
        mToolbar.getBackground().setAlpha(255);
        CustomTextView actionbarTitle = (CustomTextView) mToolbar.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getString(R.string.complaint_name));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initUI(List<ComplaintType> listComplaintsTypes, int categoryId) {
        RecyclerView mRecyclerList = (RecyclerView) findViewById(R.id.listcomplaints_recycler);
        //mRecyclerList.setHasFixedSize(true);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mRecyclerList.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        RecyclerAdapter drawerAdapter = new RecyclerAdapter(getBaseContext(), listComplaintsTypes, categoryId);
        drawerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startComplaintIntent(position);
            }
        });
        mRecyclerList.setAdapter(drawerAdapter);
    }

    protected void startComplaintIntent(int position) {
        Intent intent = new Intent(getBaseContext(), ComplaintActivity.class);
        intent.putExtra(ComplaintActivity.COMPLAINT, listComplaintsTypes.get(position));
        startActivity(intent);
    }
}
