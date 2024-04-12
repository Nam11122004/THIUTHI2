package com.example.demo_thithuand.sevices;


import com.example.demo_thithuand.Model.Response_Model;
import com.example.demo_thithuand.Model.Teacher;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

    public static String BASE_URL = "http://10.0.2.2:3000/api/";

    @Multipart
    @POST("add")
    Call<Response_Model<Teacher>> addTC(@PartMap Map<String, RequestBody> requestBodyMap,
                                               @Part MultipartBody.Part image);

    //Param url sẽ bỏ vào {}
    @DELETE("delete/{id}")
    Call<Response_Model<Teacher>> Delete(@Path("id") String id);

    @Multipart
    @PUT("update/{id}")
    Call<Response_Model<Teacher>> updateTC(@Path("id") String id, @PartMap Map<String,RequestBody> requestBodyMap,
                                                  @Part MultipartBody.Part image);

    @GET("get-list")
    Call<Response_Model<ArrayList<Teacher>>> getList ();

    @GET("search")
    Call<Response_Model<ArrayList<Teacher>>> search(@Query("key") String key);
}
