package com.hmct.refrigeratorpadpromusic.sql;



public class MusicPersonModel {

    private String name;
    private String imgurl;
    private String des;

    private String songid;
    private String songtitle;
    private String singer;
    private String songtime;
    private String number;


    public String getName() {
    return name;
}

    public void setName(String name) {
        this.name = name;
    }


    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSongtitle() {
        return songtitle;
    }

    public void setSongtitle(String songtitle) {
        this.songtitle = songtitle;
    }


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }


    public String getSongtime() {
        return songtime;
    }

    public void setSongtime(String songtime) {
        this.songtime = songtime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumbere(String number) {
        this.number = number;
    }


}

