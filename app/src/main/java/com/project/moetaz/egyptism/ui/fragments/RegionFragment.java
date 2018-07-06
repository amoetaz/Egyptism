package com.project.moetaz.egyptism.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.adapters.CustomAdapter;
import com.project.moetaz.egyptism.utilies.Utilities;
import com.project.moetaz.egyptism.interfaces.SiteListener;
import com.project.moetaz.egyptism.models.Site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.DESC_STRING;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.ENGLISHVERSION_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.IMAGE_URL_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.LATLONG_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.NAME_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.ROOT_NODE;
import static com.project.moetaz.egyptism.utilies.FirebaseNodes.SITES_NODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegionFragment extends Fragment {
    private static final int ROW_COUNT = 1;
    @BindView(R.id.region_detail_list)
    RecyclerView recyclerView;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    private GridLayoutManager gridLayoutManager;
    private Firebase mRef;
    private CustomAdapter customAdapter;
    private List<Site> list = new ArrayList<>();
    String regionName;
    SiteListener siteListener;
    private Unbinder unbinder;
    private final String LIST_KEY = "sList";

    public RegionFragment() {
        // Required empty public constructor
    }

    public static RegionFragment newInstance() {
        RegionFragment f = new RegionFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            siteListener = (SiteListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        siteListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(getString(R.string.region_key))) {
            regionName = intent.getStringExtra(getString(R.string.region_key));

        }
        getActivity().setTitle(regionName);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LIST_KEY, (Serializable) list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_region, container, false);

        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mRef = new Firebase(ROOT_NODE + "/" + ENGLISHVERSION_NODE + "/" + regionName + "/"+SITES_NODE);
        recyclerView.setHasFixedSize(true);
        setGridManager();
        setHasOptionsMenu(true);
        list.clear();

        if (savedInstanceState == null) {
            getData();
        } else {
            list = (List<Site>) savedInstanceState.getSerializable(LIST_KEY);
            customAdapter = new CustomAdapter(getContext(), list, siteListener);
            recyclerView.setAdapter(customAdapter);
        }

        return view;
    }

    private void getData() {
        customAdapter = new CustomAdapter(getContext(), list, siteListener);
        recyclerView.setAdapter(customAdapter);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Site site = new Site();
                site.setName(dataSnapshot.child(NAME_NODE).getValue(String.class));
                site.setImage(dataSnapshot.child(IMAGE_URL_NODE).getValue(String.class));
                site.setDesc(dataSnapshot.child(DESC_STRING).getValue(String.class));
                site.setLatLong(dataSnapshot.child(LATLONG_NODE).getValue(String.class));
                list.add(site);
                customAdapter.notifyItemInserted(list.size() - 1);
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
        gridLayoutManager = new GridLayoutManager(getActivity(), ROW_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Utilities.message(getContext(),getString(R.string.error_msg));
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
