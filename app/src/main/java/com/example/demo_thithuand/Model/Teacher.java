package com.example.demo_thithuand.Model;

import com.google.gson.annotations.SerializedName;

public class Teacher {
    @SerializedName("_id")
    private String id;
    private String hoten_ph36893;
    private String quequan_ph36893;
    private double luong_ph36893;
    private String chuyennganh_ph36893;
    private String hinhanh_ph36893;

    public Teacher() {
    }

    public Teacher(String id, String hoten_ph36893, String quequan_ph36893, double luong_ph36893, String chuyennganh_ph36893, String hinhanh_ph36893) {
        this.id = id;
        this.hoten_ph36893 = hoten_ph36893;
        this.quequan_ph36893 = quequan_ph36893;
        this.luong_ph36893 = luong_ph36893;
        this.chuyennganh_ph36893 = chuyennganh_ph36893;
        this.hinhanh_ph36893 = hinhanh_ph36893;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoten_ph36893() {
        return hoten_ph36893;
    }

    public void setHoten_ph36893(String hoten_ph36893) {
        this.hoten_ph36893 = hoten_ph36893;
    }

    public String getQuequan_ph36893() {
        return quequan_ph36893;
    }

    public void setQuequan_ph36893(String quequan_ph36893) {
        this.quequan_ph36893 = quequan_ph36893;
    }

    public double getLuong_ph36893() {
        return luong_ph36893;
    }

    public void setLuong_ph36893(double luong_ph36893) {
        this.luong_ph36893 = luong_ph36893;
    }

    public String getChuyennganh_ph36893() {
        return chuyennganh_ph36893;
    }

    public void setChuyennganh_ph36893(String chuyennganh_ph36893) {
        this.chuyennganh_ph36893 = chuyennganh_ph36893;
    }

    public String getHinhanh_ph36893() {
        return hinhanh_ph36893;
    }

    public void setHinhanh_ph36893(String hinhanh_ph36893) {
        this.hinhanh_ph36893 = hinhanh_ph36893;
    }
}
