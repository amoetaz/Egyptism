package com.project.moetaz.egyptism.ui.fragments;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.utilies.Utilities;
import com.project.moetaz.egyptism.interfaces.Action;
import com.project.moetaz.egyptism.ui.activities.MapActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.CONTENT_URI_1;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.CONTENT_URI_2;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.DESC;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.IMAGE;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.LATLONG;
import static com.project.moetaz.egyptism.provider.SitesProviderConstants.NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class SiteFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.co_sitefragment)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab)
    FloatingActionButton favFab;
    @BindView(R.id.site_desc)
    TextView desc;
    @BindView(R.id.site_image)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String imageUrlStr, descStr, siteStr,latLongStr;
    private Unbinder unbinder;
    private ContentResolver contentResolver;
    private boolean check1 = false;
    private boolean check2 = false;
    private final String DONE_MSG = "DONE";

    public SiteFragment() {
        // Required empty public constructor
    }

    public static SiteFragment newInstance() {
        SiteFragment f = new SiteFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();

            Bundle bundle = getArguments();
            imageUrlStr = bundle.getString(getString(R.string.image_envelope));
            descStr = bundle.getString(getString(R.string.desc_envelope));
            siteStr = bundle.getString(getString(R.string.name_envelope));
            latLongStr= bundle.getString(getString(R.string.latong_envelope));

    }

    private void saveToWidget(String descStr) {
        SharedPreferences.Editor editor = getActivity()
                .getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE).edit();
        editor.putString(getString(R.string.widget_desc), descStr);
        editor.putString(getString(R.string.widget_title), siteStr);
        editor.apply();


        Utilities.showSnack(coordinatorLayout,getString(R.string.widget_confirm_msg)
                ,DONE_MSG,new  Action() {
                    @Override
                    public void undo() {
                        undoSavingToWidget();
                    }
                });
    }

    private void undoSavingToWidget() {
        SharedPreferences.Editor editor = getActivity()
                .getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE).edit();
        editor.putString(getString(R.string.widget_desc), getString(R.string.no_value_msg));
        editor.putString(getString(R.string.widget_title), getString(R.string.no_value_msg));
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_site, container, false);
       unbinder =  ButterKnife.bind(this,view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(siteStr);
        setHasOptionsMenu(true);
        Picasso.get().load(imageUrlStr).into(imageView);
        desc.setText(descStr);

        favFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!check2) {
                    check2 = true;
                    getLoaderManager().initLoader(2, null, SiteFragment.this);
                } else {
                    getLoaderManager().restartLoader(2, null, SiteFragment.this);
                }

            }
        });


        return view;
    }

    private void unfavOperation() {
        favFab.setImageResource(R.drawable.unfav_ic);
        contentResolver.delete(CONTENT_URI_1
                , NAME + " = ?", new String[]{siteStr});

    }

    private void favOperation() {
        favFab.setImageResource(R.drawable.ic_fav);
        ContentValues cv = new ContentValues();
        cv.put(DESC, descStr);
        cv.put(NAME, siteStr);
        cv.put(IMAGE, imageUrlStr);
        cv.put(LATLONG, latLongStr);

        contentResolver.insert(CONTENT_URI_1, cv);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Utilities.message(getContext(),getString(R.string.error_msg));
        }
        if (!check1) {
            check1 = true;
            getLoaderManager().initLoader(1, null, this);
        } else {
            getLoaderManager().restartLoader(1, null, this);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new CursorLoader(getContext(), CONTENT_URI_2,
                    null, null, new String[]{siteStr}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        if (loader.getId() == 1) {

            if (cursor != null && cursor.getCount() > 0  ) {

                favFab.setImageResource(R.drawable.ic_fav);
            } else {
                favFab.setImageResource(R.drawable.unfav_ic);

            }
            cursor.close();
        } else if (loader.getId() == 2) {

            if (cursor != null && cursor.getCount() > 0 ) {
                unfavOperation();
                Utilities.showSnack(coordinatorLayout,getString(R.string.confirem_removing_msg)
                        ,DONE_MSG,new  Action() {
                    @Override
                    public void undo() {
                        favOperation();
                    }
                });
            } else {

                favOperation();
                Utilities.showSnack(coordinatorLayout,getString(R.string.confirm_adding_msg)
                        ,DONE_MSG,new Action() {
                    @Override
                    public void undo() {
                        unfavOperation();
                    }
                });
            }
            cursor.close();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.site_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.locate_on_map) {
            startActivity(new Intent(getActivity(), MapActivity.class)
                    .putExtra(getString(R.string.site_name_for_map), siteStr)
                    .putExtra(getString(R.string.latlong_string),latLongStr));
            return true;
        } else if (id == R.id.add_to_widget) {
            saveToWidget(descStr);
        }

        return super.onOptionsItemSelected(item);
    }

}
