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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add extends AppCompatActivity {
    private ImageView imgAdd;
    private EditText edtName, edtQue, edtLuong, edtNganh;
    private Button btnAdd;

    private File file;

    private HttpRequest httpRequest;
    private ArrayList<Teacher> listTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        imgAdd = findViewById(R.id.img_add_teacher);
        edtName = findViewById(R.id.edt_name_teacher);
        edtQue = findViewById(R.id.edt_que_teacher);
        edtLuong = findViewById(R.id.edt_luong);
        edtNganh  = findViewById(R.id.edt_chuyenNganh);
        btnAdd = findViewById(R.id.btn_add_teacher);

        listTeacher = new ArrayList<>();
        httpRequest = new HttpRequest();

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = edtName.getText().toString();
                String queQuan = edtQue.getText().toString();
                String luong = edtLuong.getText().toString();
                String chuyenNganh = edtNganh.getText().toString();

                if (TextUtils.isEmpty(_name) || TextUtils.isEmpty(queQuan) || TextUtils.isEmpty(luong) || TextUtils.isEmpty(chuyenNganh)) {
                    Toast.makeText(Add.this, "Vui lòng nhập lại dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                double luongTeacher;
                try {
                    luongTeacher = Double.parseDouble(luong);
                } catch (NumberFormatException e) {
                    Toast.makeText(Add.this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, RequestBody> bodyMap = new HashMap<>();

                //Put request body
                bodyMap.put("hoten_ph36893", getRequestBody(_name));
                bodyMap.put("quequan_ph36893", getRequestBody(queQuan));
                bodyMap.put("luong_ph36893", getRequestBody(String.valueOf(luongTeacher)));
                bodyMap.put("chuyennganh_ph36893", getRequestBody(chuyenNganh));

                MultipartBody.Part muPart;
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    muPart = MultipartBody.Part.createFormData("hinhanh_ph36893", file.getName(), requestFile);
                    //"avatar" là cùng với key trong mutipart

                } else {
                    muPart = null;
                }
                httpRequest.CallApi().addTC(bodyMap, muPart).enqueue(responseAPI);
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

                file = createFileFromUri(imagePath, "hinhAnh_PH36760");
                //gilde để load hinh
                Glide.with(Add.this).load(file)
                        .thumbnail(Glide.with(Add.this).load(R.mipmap.ic_launcher))
                        .centerCrop()
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgAdd);
            }
        }
    });

    private File createFileFromUri(Uri path, String name) {
        File _file = new File(Add.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Add.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    Callback<Response_Model<Teacher>> responseAPI = new Callback<Response_Model<Teacher>>() {
        @Override
        public void onResponse(Call<Response_Model<Teacher>> call, Response<Response_Model<Teacher>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(Add.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Add.this, MainActivity.class));
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response_Model<Teacher>> call, Throwable t) {
            Toast.makeText(Add.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
            Log.d("AddSinhVien", "onFailure: " + t.getMessage());
        }
    };

}