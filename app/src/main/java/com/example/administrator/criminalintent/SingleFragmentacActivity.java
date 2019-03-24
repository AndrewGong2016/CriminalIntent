package com.example.administrator.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentacActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    /**
     * 该抽象方法的资源id 作为承载Fragment的视图容器（且其中必须包含有ID== R.idfragment_container 的view container）
     * @return 资源id，用于setContentView(int resId)
     */
    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);//FrameLayout 作为一个fragment container
        if(fragment == null){
            fragment = createFragment();//子类中需实现这个方法
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }


}
