package mx.citydevs.denunciaelectoral;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomplaints);

        setSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<ComplaintType> listComplaintsTypes = (List<ComplaintType>) bundle.getSerializable(COMPLAINTS_TYPES);
            int categoryId = bundle.getInt(CATEGORY_ID);

            if (listComplaintsTypes != null && listComplaintsTypes.size() > 0)
                initUI(listComplaintsTypes, categoryId);
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

    protected void initUI(List<ComplaintType> listComplaintsTypes, int categoryId) {
        RecyclerView mRecyclerList = (RecyclerView) findViewById(R.id.complaint_recycler);
        //mRecyclerList.setHasFixedSize(true);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mRecyclerList.addItemDecoration(new SimpleDividerItemDecoration(getResources()));

        RecyclerAdapter drawerAdapter = new RecyclerAdapter(getBaseContext(), listComplaintsTypes, categoryId);
        drawerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Dialogues.Toast(getBaseContext(), "Position: " + position, Toast.LENGTH_SHORT);
            }
        });
        mRecyclerList.setAdapter(drawerAdapter);
    }
}