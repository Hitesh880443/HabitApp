package com.android.habitapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.android.habitapp.addHabit.HabitSettingActivity;
import com.android.habitapp.habitstore.view.AllHabitFrag;
import com.android.habitapp.motive.view.MotiveFrag;
import com.android.habitapp.myhabit.view.MyHabitFrag;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    //region Variables
    private String habit_id, habit_name, habit_desciption, habit_users;
    private ArrayList<Fragment> fragList;
    private ArrayList<String> fragListName;
    //endregion

    //region Views
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    //endregion

    //region Lifecycle_and_Android_Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            fragList = new ArrayList<>();
            fragList.add(new AllHabitFrag());
            fragList.add(new MyHabitFrag());
            fragList.add(new MotiveFrag());

            fragListName = new ArrayList<>();
            fragListName.add("Habit Store");
            fragListName.add("My Habit");
            fragListName.add("Habit Wall");
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent addHabit = new Intent(MainActivity.this, HabitSettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("habitType", "own");
                    addHabit.putExtras(bundle);
                    startActivity(addHabit);
                }
            });

            mViewPager.setOffscreenPageLimit(fragList.size());
            mViewPager.setCurrentItem(1);
            fab.show();
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == 1)
                        fab.show();
                    else
                        fab.hide();
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    //endregion

    //region LocalMethods_and_classes

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return fragList.get(position);
          /*  if (position == 0)
                return new MotiveFrag();
            else
                return PlaceholderFragment.newInstance(position + 1);*/
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return fragList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragListName.get(position);
        }
    }
    //endregion
}
