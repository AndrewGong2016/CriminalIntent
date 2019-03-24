package com.example.administrator.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private CrimeAdapter mCrimeAdapter;
    private RecyclerView mCrimeRecycleView;

    private Callbacks mCallbacks;
    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks= (Callbacks) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecycleView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));//Q：why need it？A：RecycleView需要布局管理器来支撑，布局管理器亲自将View对象摆放到屏幕上

        updateUI();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity
//                        .newIntent(getActivity(), crime.getId());
//                startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime);

            case R.id.show_subtitle:
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);//设置该应用的副标题，
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }

        updateSubtitle();

    }

    /*
    * Class name: CrimeHolder
    * Note：
    * 1 实际V iew 对象的持有者；
    * 2 可用于更新View对象的内容
    * 3 RecycleView 中将根据需要，创建和复用此对象
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title_text;
        TextView title_date;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        //CrimeHolder（ViewHolder的子类） 掌管着Item中的视图组件
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            //构造器需要一个 inflater 来创建每一个需要Hold住的View
            super(inflater.inflate(R.layout.list_item_crime,parent,false));//下述的将指向super（view）传入的view对象，因此可直接获取；

            //使用itemView获取自定义视图中的组件；
            title_text = (TextView)itemView.findViewById(R.id.crime_title);
            title_date = (TextView)itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        //更新内容
        public void bind(Crime crime){
            mCrime = crime;
            title_text.setText(crime.getTitle());
            title_date.setText(crime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved()? View.VISIBLE:View.GONE);
        }

        //每一个Viewholder都可以被点击
        @Override
        public void onClick(View v) {
            /**
             * 在这里处理每一项HoldView被点击之后的触发逻辑
             * 为保持 Fragment 构件的独立性，通过 Intent 来执行点击逻辑：
             * 1 ，在手机平台，跳转至CrimePagerActivity 去，只是与跳转的Activity强关联，与自生所依托的Activity并无关联/耦合；
             * 2，在平板平台，【未完待处理】。？疑问：平板平台上不适合另起一个Activity，而在手机平台很有必要，代码逻辑上该如何权衡？？
             */
//            Toast.makeText(getActivity(),"id == "+mCrime.getTitle()+" clicked",Toast.LENGTH_SHORT).show();
////            Intent intent =  CrimeActivity.newIntent(getActivity(),mCrime.getId());
//            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
//            startActivity(intent);
            mCallbacks.onCrimeSelected(mCrime);

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /*
    * Class name :  CrimAdapter
    * Note: 1，创建 ViewHolder 子类对象：Crimeholder（oncreateViewHoler()）；
    *       2，该类为 ViewHolder 提供要展示的 itme 对象的内容（模型层数据,getItemCount）。
     */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private  String TAG="CrimeAdapterGuan";
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            Log.d(TAG,"ionCreateViewHolder =="+i);
            return new CrimeHolder(inflater,viewGroup);//创建Holder对象以供RecycleView使用
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {
            /**
             * parameter CrimeHolder crimeHolder: Holder对象
             * parameter int     i：Holder的位置；
             *
             */
            Crime crime = mCrimes.get(i);
            if(crime != null){
                crimeHolder.bind(crime);
            }
        }


        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}
