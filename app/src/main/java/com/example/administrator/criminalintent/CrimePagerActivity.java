package com.example.administrator.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在Activity中布置一个ViewPager
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();

//        mViewPager.setAdapter(new FragmentStatePagerAdapter() {
//            @Override
//            public Fragment getItem(int i) {
//                return null;
//            }
//            @Override
//            public int getCount() {
//                return 0;
//            }
//        });

        //Adapter 需要一个fragmentManager 来把fragment 托管给activity
        /**
         * 上述描述有误，【ViewPager】 作为视图的容器，并不是只能装载Fragment，
         * Adapter为Fragment则转载fragment，其他亦可。
         *
         * ViewPager 只需要一个PagerAdapter对象，为其提供展示的内容；
         */
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });



        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }


    public static Intent newIntent(Context context,UUID uuid){

        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,uuid);

        return intent;
    }

}
