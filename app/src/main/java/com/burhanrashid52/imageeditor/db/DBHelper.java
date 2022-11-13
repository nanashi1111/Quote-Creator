package com.burhanrashid52.imageeditor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.burhanrashid52.imageeditor.models.Quote;
import com.burhanrashid52.imageeditor.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 8/27/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Quoter_DB";
    private static final int DB_VERSION = 11;
    private static final String TBL_QUOTE = "Quotes";
    private static final String CREATE_TABLE_QUOTE = "create table " + TBL_QUOTE + "(id integer primary key autoincrement not null, cate_name text, " +
            "content text, author_name text);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public long saveQuote(String content, String category, String author) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        contentValues.put("cate_name", category);
        contentValues.put("author_name", (author != null && !author.isEmpty()) ? author : "");
        long result = getWritableDatabase().insert(TBL_QUOTE, null, contentValues);
        Log.d("DBHelper", "Save " + content + " = " + result);
        return result;
    }

    public int getNumberQuoteCount(String category) {
        String[] columns = new String[]{"content", "cate_name"};
        Cursor result = getReadableDatabase().query(TBL_QUOTE, columns, "cate_name = ?", new String[]{category}, null, null, null);
        return result.getCount();
    }

    public boolean checkQuoteExist(Quote quote, String category) {
        String[] columns = new String[]{"content", "cate_name"};
        Cursor result = getReadableDatabase().query(TBL_QUOTE, columns, "content = ? and cate_name = ?", new String[]{quote.getContent(), category}, null, null, null);
        return result.getCount() > 0;
    }

    public List<Quote> getListQuote(String category) {
        ArrayList<Quote> listQuote = new ArrayList<>();
        String query = "select * from " + TBL_QUOTE + " where cate_name = \'" + category + "\'";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Quote quote = new Quote(cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("cate_name")),
                        cursor.getString(cursor.getColumnIndex("author_name")));
                listQuote.add(quote);
            } while (cursor.moveToNext());
        }
        return listQuote;
    }

    public String getQuoteOfCategory(String category) {
        String query = "select * from " + TBL_QUOTE + " where cate_name = \'" + category + "\' and length(content)<=40 order by random() limit 1";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        LogUtils.d("getQuoteOfCategory length = " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Quote quote = new Quote(cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("cate_name")),
                    cursor.getString(cursor.getColumnIndex("author_name")));
            return quote.getContent();
        }
        return "Quoter";
    }

    public int getQuoteCount() {
        return getReadableDatabase().rawQuery("select * from " + TBL_QUOTE, null).getCount();
    }

    public void clearCategory(String category) {
        String cmd = "delete from " + TBL_QUOTE + " where cate_name = \'" + category + "\'";
        getWritableDatabase().execSQL(cmd);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TBL_QUOTE);
        onCreate(db);
    }
}
