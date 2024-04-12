package com.example.demo_thithuand;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_thithuand.Model.Response_Model;
import com.example.demo_thithuand.Model.Teacher;
import com.example.demo_thithuand.adapter.Recycle_Adapter;
import com.example.demo_thithuand.handle.Item_Teacher_Handle;
import com.example.demo_thithuand.sevices.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Item_Teacher_Handle {

    private HttpRequest httpRequest;
    private RecyclerView rcvstudent;
    private Recycle_Adapter adapter;
    EditText txtsearch;
    private ArrayList<Teacher> list;
    FloatingActionButton fltadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rcvstudent=findViewById(R.id.rcv);
        txtsearch=findViewById(R.id.txtsearch);
        fltadd=findViewById(R.id.fltadd);
        list= new ArrayList<>();
        httpRequest = new HttpRequest();
        httpRequest.CallApi()
                .getList()
                .enqueue(getListAPI);

        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Add.class));
            }
        });
        txtsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //lấy từ khoá từ ô tìm kiếm
                    String key = txtsearch.getText().toString();

                    httpRequest.CallApi().search(key)//phương thức api cần thực thi
                            .enqueue(getListAPI);//xử lý bất đồng bộ
                    //vì giá trị trả về vẫ là một list distributor
                    //nên có thể sử dụng lại callback của getListDistributor
                    return true;
                }

                return false;
            }
        });

    }

    //getdata
    private void getData(ArrayList<Teacher> ds) {
        adapter = new Recycle_Adapter(this, ds,this);
        rcvstudent.setLayoutManager(new LinearLayoutManager(this));
        rcvstudent.setAdapter(adapter);
    }

    Callback<Response_Model<ArrayList<Teacher>>> getListAPI =new Callback<Response_Model<ArrayList<Teacher>>>() {
        @Override
        public void onResponse(Call<Response_Model<ArrayList<Teacher>>> call, Response<Response_Model<ArrayList<Teacher>>> response) {
            if (response.isSuccessful()) {
                // check status
                if (response.body().getStatus() == 200) {
                    // lấy data
                    ArrayList<Teacher> list = response.body().getData();
                    Log.d("List", "onResponse: " + list);
                    // set dữ liệu lên rcv
                    getData(list);
                    // Thông báo
//                    Toast.makeText(MainActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response_Model<ArrayList<Teacher>>> call, Throwable t) {
            Log.d(">>>GetListDistributor", "onFailure: " + t.getMessage());
        }
    };
    Callback<Response_Model<Teacher>> responseAPI=new Callback<Response_Model<Teacher>>() {
        @Override
        public void onResponse(Call<Response_Model<Teacher>> call, Response<Response_Model<Teacher>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    //gọi callback load lại dữ liệu
                    httpRequest.CallApi().getList().enqueue(getListAPI);
                }
            }
        }

        @Override
        public void onFailure(Call<Response_Model<Teacher>> call, Throwable t) {
            Log.d(">>>GetListDistributor", "onFailure: " + t.getMessage());
        }
    };
    @Override
    public void Delete(String id) {
        httpRequest.CallApi().Delete(id).enqueue(responseAPI);
    }

    @Override
    public void Update(String id, Teacher teacher) {

    }
}