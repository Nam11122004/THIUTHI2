package com.example.demo_thithuand;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo_thithuand.Model.Response_Model;
import com.example.demo_thithuand.Model.Teacher;
import com.example.demo_thithuand.sevices.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update extends AppCompatActivity {
    private ImageView imgUpdate;
    private EditText edtName, edtQue, edtLuong,edtChuyen;
    private Button btnUpdate;

    private File file;
    private String id;
    private HttpRequest httpRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        imgUpdate = findViewById(R.id.img_update_sv);
        edtName = findViewById(R.id.edt_name_gv_u);
        edtQue = findViewById(R.id.edt_que_u);
        edtLuong = findViewById(R.id.edt_luong_u);
        edtChuyen = findViewById(R.id.edt_cn_u);
        btnUpdate = findViewById(R.id.btn_update_sv);
        httpRequest = new HttpRequest();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String que = intent.getStringExtra("que");
        double luong = intent.getDoubleExtra("luong",0);
        String chuyen = intent.getStringExtra("chuyen");
        String anh = intent.getStringExtra("anh");
        id = intent.getStringExtra("id");
        edtName.setText(name);
        edtQue.setText(que);
        edtLuong.setText(String.valueOf(luong));
        edtChuyen.setText(chuyen);
        Glide.with(this).load(anh).into(imgUpdate);

        imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = edtName.getText().toString();
                String queQuan = edtQue.getText().toString();
                String luong = edtLuong.getText().toString();
                String chuyenNganh = edtChuyen.getText().toString();

                // Kiểm tra xem có thay đổi trong dữ liệu không
                if (TextUtils.isEmpty(_name) || TextUtils.isEmpty(queQuan) || TextUtils.isEmpty(luong) || TextUtils.isEmpty(chuyenNganh)) {
                    Toast.makeText(Update.this, "Vui lòng nhập lại dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                double luongTeacher;
                try {
                    luongTeacher = Double.parseDouble(luong);
                } catch (NumberFormatException e) {
                    Toast.makeText(Update.this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, RequestBody> bodyMap = new HashMap<>();
                //Put request body
                bodyMap.put("hoten_ph36893", getRequestBody(_name));
                bodyMap.put("quequan_ph36893", getRequestBody(queQuan));
                bodyMap.put("luong_ph36893", getRequestBody(String.valueOf(luong)));
                bodyMap.put("chuyennganh_ph36893", getRequestBody(chuyenNganh));

                MultipartBody.Part muPart = null;
                // Kiểm tra xem người dùng đã chọn ảnh mới hay chưa
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    muPart = MultipartBody.Part.createFormData("hinhanh_ph36893", file.getName(), requestFile);
                }else {
                    muPart = null;
                }

                // Gửi yêu cầu cập nhật lên server
                httpRequest.CallApi().updateTC(id,bodyMap, muPart).enqueue(responseApi);
            }
        });
    }

    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }



    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent data = o.getData();
                Uri imagePath = data.getData();

                file = createFileFromUri(imagePath, "hinhanh_ph36893");
                //gilde để load hinh
                Glide.with(Update.this).load(file)
                        .thumbnail(Glide.with(Update.this).load(R.mipmap.ic_launcher))
                        .centerCrop()
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgUpdate);
            }
        }
    });

    private File createFileFromUri(Uri uri, String name) {
        File file = new File(getCacheDir(), name + ".png");
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    Callback<Response_Model<Teacher>> responseApi = new Callback<Response_Model<Teacher>>() {
        @Override
        public void onResponse(Call<Response_Model<Teacher>> call, Response<Response_Model<Teacher>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(Update.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Update.this, MainActivity.class));
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response_Model<Teacher>> call, Throwable t) {
            Toast.makeText(Update.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
            Log.d("AddSinhVien", "onFailure: " + t.getMessage());
        }
    };
}