package com.project.moetaz.egyptism.ui.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.adapters.CustomAdapter;
import com.project.moetaz.egyptism.dataStroage.DBAdpater;
import com.project.moetaz.egyptism.models.Region;
import com.project.moetaz.egyptism.ui.activities.AboutActivity;
import com.project.moetaz.egyptism.ui.activities.FavActivity;
import com.project.moetaz.egyptism.utilies.Utilities;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.project.moetaz.egyptism.utilies.FirebaseNodes.ENGLISHVERSION_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.IMAGE_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.ROOT_NODE;


/**
 * A simple {@link Fragment} subclass.
 */

public class MainFragment extends Fragment {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav)
    NavigationView navigationView;
    @BindView(R.id.Recyclerview_main)
    RecyclerView recyclerView;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    private GridLayoutManager gridLayoutManager;
    private Firebase mRef;
    private CustomAdapter customAdapter;
    private List<Region> list = new ArrayList<>();
    private final String LIST_KEY = "rList";


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LIST_KEY, (Serializable) list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mRef = new Firebase(ROOT_NODE + "/" + ENGLISHVERSION_NODE);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open, R.string.close);
        setListnerToDrawer();
        setListnerToNavigationViewItems();
        recyclerView.setHasFixedSize(true);
        setGridManager();
        if (Utilities.isNetworkConnected(getContext())) {

            if (savedInstanceState == null) {
                getData(list);
            } else {
                list = (List<Region>) savedInstanceState.getSerializable(LIST_KEY);
                setCachedData(list);
            }
        } else {
            FancyToast.makeText(getContext(),getString(R.string.checking_internet_msg),FancyToast.LENGTH_LONG
                    ,FancyToast.ERROR,false).show();
        }

        return view;
    }

    private void setCachedData(List<Region> list) {
        customAdapter = new CustomAdapter(getContext(), list, null);
        recyclerView.setAdapter(customAdapter);
    }

    private void getData(List<Region> list) {

        customAdapter = new CustomAdapter(getContext(), list, null);
        recyclerView.setAdapter(customAdapter);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Region region = new Region();
                region.setImage(dataSnapshot.child(IMAGE_NODE).getValue(String.class));
                region.setName(dataSnapshot.getKey());
                MainFragment.this.list.add(region);
                customAdapter.notifyItemInserted(MainFragment.this.list.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void setGridManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void setListnerToDrawer() {
        if (Build.VERSION.SDK_INT >= 23)
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        else
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    private void setListnerToNavigationViewItems() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fav:
                        drawerLayout.closeDrawer(Gravity.START);
                        startActivity(new Intent(getActivity(), FavActivity.class));
                        break;


                    case R.id.about:
                        drawerLayout.closeDrawer(Gravity.START);
                        startActivity(new Intent(getContext(), AboutActivity.class));
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }


}
