package com.example.administrator.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.criminalintent.database.CrimeBaseHelper;
import com.example.administrator.criminalintent.database.CrimeCursorWrapper;
import com.example.administrator.criminalintent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private SQLiteDatabase mDatabase;
    private Context mAppContext;


    //private私有构造方法，只能自己创建自己
    private CrimeLab(Context mcontext){
        mAppContext = mcontext.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mAppContext).getWritableDatabase();
    }

    //外界通过该public 方法来获取CrimeLab
    public static CrimeLab get(Context mContext){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(mContext);
        }
        return sCrimeLab;
    }

    private ContentValues getContentValues(Crime crime){

        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());//id 为String 类型
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());// getTime 返回long类型
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());// title 为 String 类型
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()? 1:0);//solved 值为 int 类型处理
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getSuspect());

        return  values;
    }

    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();
        //查询所有的Crimes；
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }


    public Crime getCrime(UUID uuid){

        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { uuid.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }


    }


    public void updateCrime( Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values,
                CrimeDbSchema.CrimeTable.Cols.UUID+" = ?",
                new String[]{uuidString});
    }

    public void addCrime(Crime crime){

        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {

        //1 查询数据库：
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        // 2 封装查询结果：
        return  new CrimeCursorWrapper(cursor);
    }





}
