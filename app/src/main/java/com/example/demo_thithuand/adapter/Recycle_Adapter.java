package com.example.demo_thithuand.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo_thithuand.Model.Teacher;
import com.example.demo_thithuand.R;
import com.example.demo_thithuand.Update;
import com.example.demo_thithuand.handle.Item_Teacher_Handle;

import java.util.ArrayList;

public class Recycle_Adapter extends  RecyclerView.Adapter<Recycle_Adapter.viewHolep>{
    private Context context;
    private ArrayList<Teacher> list;
    private Item_Teacher_Handle handleTeacher;

    public Recycle_Adapter(Context context, ArrayList<Teacher> list, Item_Teacher_Handle handleTeacher) {
        this.context = context;
        this.list = list;
        this.handleTeacher = handleTeacher;
    }

    @NonNull
    @Override
    public viewHolep onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle, parent, false);
        return new viewHolep(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolep holder, int position) {
        Teacher teacher = list.get(position);

        Glide.with(context).load(teacher.getHinhanh_ph36893()).into(holder.imgHinhAnh);
        holder.txtHoTen.setText(teacher.getHoten_ph36893());
        holder.txtQueQuan.setText(teacher.getQuequan_ph36893());
        holder.txtLuong.setText(String.valueOf(teacher.getLuong_ph36893()));
        holder.txtChuyenNganh.setText(teacher.getChuyennganh_ph36893());

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muốn xoá không")
                        .setCancelable(false)
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handleTeacher.Delete(teacher.getId());
                                Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), Update.class);
                intent.putExtra("name", teacher.getHoten_ph36893());
                intent.putExtra("que", teacher.getQuequan_ph36893());
                intent.putExtra("luong", teacher.getLuong_ph36893());
                intent.putExtra("chuyen", teacher.getChuyennganh_ph36893());
                intent.putExtra("id", teacher.getId());
                intent.putExtra("anh", teacher.getHinhanh_ph36893());

                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_details, null);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                TextView tvName = view.findViewById(R.id.tv_name_dg);
                TextView tvQue = view.findViewById(R.id.tv_que_dg);
                TextView tvLuong = view.findViewById(R.id.tv_luong_dg);
                TextView tvNganh = view.findViewById(R.id.tv_nganh_dg);
                ImageView imgAnh = view.findViewById(R.id.img_dg);

                tvName.setText(teacher.getHoten_ph36893());
                tvQue.setText(teacher.getQuequan_ph36893());
                tvLuong.setText(String.valueOf(teacher.getLuong_ph36893()));
                tvNganh.setText(teacher.getChuyennganh_ph36893());
                Glide.with(context).load(teacher.getHinhanh_ph36893()).into(imgAnh);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class viewHolep extends RecyclerView.ViewHolder{
        ImageView imgHinhAnh, imgDelete, imgUpdate;
        TextView txtHoTen, txtQueQuan, txtLuong,txtChuyenNganh;

        public viewHolep(@NonNull View itemView) {
            super(itemView);
            imgHinhAnh = itemView.findViewById(R.id.imgHinhAnh);
            imgUpdate = itemView.findViewById(R.id.img_update);
            imgDelete = itemView.findViewById(R.id.img_delete);
            txtHoTen = itemView.findViewById(R.id.txtHoten);
            txtQueQuan= itemView.findViewById(R.id.txtQueQuan);
            txtLuong = itemView.findViewById(R.id.txtLuong);
            txtChuyenNganh = itemView.findViewById(R.id.txtChuyenNganh);

        }
    }
}
