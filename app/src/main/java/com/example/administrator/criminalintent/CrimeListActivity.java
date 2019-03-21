package com.example.administrator.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentacActivity{

    @Override
    protected Fragment createFragment() {
        /**
         * 在这里创建真正要显示的 fragment 对象
         *
         */
        return new CrimeListFragment();//创建实际的Fragment对象，以供显式！！
    }

}
