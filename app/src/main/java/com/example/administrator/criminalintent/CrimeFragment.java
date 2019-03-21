package com.example.administrator.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        * 直接从 fragment 附属的 Activity 中获取 Extra 将过渡依赖于 CrimeActivity，不如自己告诉 Activity 怎么创建我这个 fragment ——新增 public static newInstance() 方法；
        * 因此建议使用 fragment 的 argument 参数，将 fragment 最大限度独立于 activity，见下述的 getArguments 方法。
         */
//      UUID idForCrime = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID idForCrime = (UUID) getArguments().getSerializable(ARG_CRIME_ID);// CrimeActivity负责创建fragment(使用CrimeFragment.newInstance()来创建)，
                                                                                // CrimeFragment参数配置和获取，全在Fragment中。
        mCrime = CrimeLab.get(getActivity()).getCrime(idForCrime);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());//接受用户的title输入
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.d(CrimeActivity.TAG,"crime fragment date button getting...");
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(CrimeActivity.TAG,"onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    /*
    * Fragment实例创建在Activity中，由它通过提供所需的 uuid;
    *  startActivity 传递所需的uuid参数 ;
     */
    public static CrimeFragment newInstance(UUID uuid){
        CrimeFragment fragment = new CrimeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID,uuid);
        fragment.setArguments(bundle);
        return fragment;
    }

}
