package com.project.moetaz.egyptism.ui.activities;


import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.project.moetaz.egyptism.R;
import com.project.moetaz.egyptism.ui.fragments.RegionFragment;
import com.project.moetaz.egyptism.utilies.Utilities;
import com.project.moetaz.egyptism.interfaces.SiteListener;
import com.project.moetaz.egyptism.ui.fragments.SiteFragment;

public class RegionActivity extends AppCompatActivity implements SiteListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fdetail,new RegionFragment())
                    .commit();
        }
    }



    @Override
    public void onSiteClickedListener(Bundle bundle, View view) {
        if (Utilities.isTablet(getApplicationContext())){
            SiteFragment siteFragment = new SiteFragment();
            siteFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fsite,siteFragment)
                    .commit();
        }else {

            SiteFragment siteFragment = SiteFragment.newInstance();
            siteFragment.setArguments(bundle);


            Slide slideTransition = null ;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                slideTransition = new Slide(Gravity.END);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

                ChangeBounds changeBoundsTransition = new ChangeBounds();
                changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

                siteFragment.setEnterTransition(slideTransition);
                siteFragment.setAllowEnterTransitionOverlap(false);
                siteFragment.setAllowReturnTransitionOverlap(false);
                siteFragment.setSharedElementEnterTransition(changeBoundsTransition);
            }


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fdetail,siteFragment,"fra2")
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                else
                    finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
