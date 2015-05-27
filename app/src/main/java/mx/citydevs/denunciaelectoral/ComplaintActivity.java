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
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.dialogues.Dialogues;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/27/15.
 */
public class ComplaintActivity extends ActionBarActivity {
    public static final String TAG_CLASS = ComplaintActivity.class.getSimpleName();

    public static final String COMPLAINTS_TYPES = "complaints_types";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        setSupportActionBar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<ComplaintType> listComplaintsTypes = (List<ComplaintType>) bundle.getSerializable(COMPLAINTS_TYPES);

            if (listComplaintsTypes != null && listComplaintsTypes.size() > 0)
                initUI(listComplaintsTypes);
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

    protected void initUI(List<ComplaintType> listComplaintsTypes) {
        RecyclerView mRecyclerList = (RecyclerView) findViewById(R.id.complaint_recycler);
        mRecyclerList.setHasFixedSize(true);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());

        RecyclerAdapter drawerAdapter = new RecyclerAdapter(listComplaintsTypes);
        drawerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Dialogues.Toast(getBaseContext(), "Position: " + position, Toast.LENGTH_SHORT);
            }
        });
        mRecyclerList.setAdapter(drawerAdapter);
    }
}
