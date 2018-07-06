package com.project.moetaz.egyptism.ui.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.adapters.CustomAdapter;
import com.project.moetaz.egyptism.dataStroage.DBAdpater;
import com.project.moetaz.egyptism.interfaces.SiteListener;
import com.project.moetaz.egyptism.models.Site;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.project.moetaz.egyptism.provider.SitesProviderConstants.CONTENT_URI_1;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.DESC;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.IMAGE;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.LATLONG;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.NAME;


public class FavFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.region_detail_list)
    RecyclerView recyclerView;
    private List<Site> list = new ArrayList<>();
    CustomAdapter customAdapter;
    SiteListener siteListener;
    private ContentResolver contentResolver;
    Unbinder unbinder;
    private boolean check1 = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", (Serializable) list);
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
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Favourite Sites");
        setGridManager();
        if (!check1) {
            check1 = true;
            getLoaderManager().initLoader(1, null, this);
        } else {
            getLoaderManager().restartLoader(1, null, this);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_region, container, false);
        unbinder= ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        list.clear();
        if (savedInstanceState != null) {
            list = (List<Site>) savedInstanceState.getSerializable("list");
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                String var = (String) viewHolder.itemView.getTag();
                String[] strs = var.split("&");
                int postion = Integer.parseInt(strs[1].trim());
                String name = strs[0].trim();
                contentResolver.delete(CONTENT_URI_1
                        , NAME + " = ?", new String[]{name});
                list.remove(postion);
                customAdapter.notifyItemRemoved(postion);
                customAdapter.notifyItemRangeChanged(postion, customAdapter.getItemCount() - postion);
                FancyToast.makeText(getContext(),getString(R.string.remove_msg_confirm),FancyToast.LENGTH_LONG
                        ,FancyToast.SUCCESS,false).show();


            }
        }).attachToRecyclerView(recyclerView);
        return view;
    }

    private void setGridManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1) {
            return new CursorLoader(getContext(), CONTENT_URI_1
                    , null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<Site> sites = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                Site site = new Site();
                site.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                site.setDesc(cursor.getString(cursor.getColumnIndex(DESC)));
                site.setImage(cursor.getString(cursor.getColumnIndex(IMAGE)));
                site.setLatLong(cursor.getString(cursor.getColumnIndex(LATLONG)));

                list.add(site);

            }
        }
        assert cursor != null;
        cursor.close();
        if (list.size() > 0) {

            FancyToast.makeText(getContext(),getString(R.string.remove_tip),FancyToast.LENGTH_LONG
                    ,FancyToast.WARNING,false).show();
        }
        customAdapter = new CustomAdapter(getContext(), list, siteListener);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
