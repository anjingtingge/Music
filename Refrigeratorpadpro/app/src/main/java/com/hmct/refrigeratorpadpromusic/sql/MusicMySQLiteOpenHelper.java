package com.hmct.refrigeratorpadpromusic.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MusicMySQLiteOpenHelper extends SQLiteOpenHelper {



    private static Integer Version = 1;
    private List list;


    public MusicMySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                   int version) {

        super(context, name, factory, version);
    }


    public MusicMySQLiteOpenHelper(Context context, String name, int version)
    {
        this(context,name,null,version);
    }


    public MusicMySQLiteOpenHelper(Context context, String name)
    {
        this(context, name, Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("创建数据库和表");

        String sql = "create table listdetail(name varchar(200),number varchar(30),songtitle varchar(300),singer varchar(300),songtime varchar(200),songid varchar(200))";

        String sqldetail = "create table songlist(type varchar(40),name varchar(200),url varchar(300),des varchar(2000))";


        db.execSQL(sql);
        db.execSQL(sqldetail);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("更新数据库版本为:"+newVersion);
    }

    /**
     * 查询全部数据
     */
    public List<MusicPersonModel> queryAllPersonData(String tablename, String videoname) {

        //查询全部数据
        Cursor cursor =null;
        String rawQuerySql = null;




       List<MusicPersonModel> list = new ArrayList<>();

        if (tablename.equals("songlist")&&videoname==null){
            rawQuerySql = String.format("select * from songlist ORDER BY RANDOM()");

            cursor = getWritableDatabase().rawQuery(rawQuerySql,null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();


                for (int i = 0; i < cursor.getCount(); i++) {

                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));

                    String des = cursor.getString(cursor.getColumnIndex("des"));

                    MusicPersonModel model = new MusicPersonModel();
                    model.setName(name);
                    model.setImgurl(url);

                    model.setDes(des);

                    list.add(model);

                    cursor.moveToNext();
                }


            }

        }



        if (tablename.equals("songlist")&&videoname!=null){
            rawQuerySql = String.format("select * from songlist where name =? ");
            cursor = getWritableDatabase().rawQuery(rawQuerySql,new String[]{videoname});

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();


                for (int i = 0; i < cursor.getCount(); i++) {

                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String des = cursor.getString(cursor.getColumnIndex("des"));

                    MusicPersonModel model = new MusicPersonModel();
                    model.setName(name);
                    model.setImgurl(url);
                    model.setDes(des);

                    list.add(model);

                    cursor.moveToNext();
                }


            }

        }


        if (tablename.equals("listdetail")){

            rawQuerySql = String.format("select * from listdetail where name = ?");

            cursor = getWritableDatabase().rawQuery(rawQuerySql,new String[]{videoname});

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {

                    String songid = cursor.getString(cursor.getColumnIndex("songid"));
                    String songtitle = cursor.getString(cursor.getColumnIndex("songtitle"));
                    String singer = cursor.getString(cursor.getColumnIndex("singer"));
                    String songtime = cursor.getString(cursor.getColumnIndex("songtime"));
                    String number = cursor.getString(cursor.getColumnIndex("number"));

                    MusicPersonModel model = new MusicPersonModel();
                    model.setSongid(songid);
                    model.setSongtitle(songtitle);
                    model.setSinger(singer);
                    model.setSongtime(songtime);
                    model.setNumbere(number);

                    list.add(model);
                    //移动到下一位
                    cursor.moveToNext();
                }

            }

        }


        cursor.close();
        getWritableDatabase().close();

        return list;
    }

    public List<MusicPersonModel> rawQueryPersonData(String typename) {

        Cursor cursor = null;
        String rawQuerySql = null;
        List<MusicPersonModel> list = new ArrayList<>();


       rawQuerySql = "SELECT  * FROM "+"songlist" +" where "+"type"+" like '%"+typename+"%'"+"ORDER BY RANDOM()" ;




        cursor = getWritableDatabase().rawQuery(rawQuerySql,null);


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();


            for (int i = 0; i < cursor.getCount(); i++) {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                String url = cursor.getString(cursor.getColumnIndex("url"));

                String des = cursor.getString(cursor.getColumnIndex("des"));

                MusicPersonModel model = new MusicPersonModel();
                model.setName(name);
                model.setImgurl(url);

                model.setDes(des);

                list.add(model);

                cursor.moveToNext();
            }


        }


        return list;

    }




}

