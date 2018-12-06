package com.example.philong.banhang.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import com.example.philong.banhang.Activity.Update_All_Product_Update;
import com.example.philong.banhang.Activity.Update_All_Product;
import com.example.philong.banhang.Activity.Update_All_Product;
import com.example.philong.banhang.Activity.Update_All_Product_Update;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.R;

import java.util.ArrayList;

public class Adapter_Product_Update extends RecyclerView.Adapter<Adapter_Product_Update.ViewHolder>{

    ArrayList<Product> ProductUpdatesArrayList;
    Context context;
    Update_All_Product updateMenuClass;

    public Adapter_Product_Update(ArrayList<Product> productUpdatesArrayList, Context context, Update_All_Product updateMenuClass) {
        ProductUpdatesArrayList = productUpdatesArrayList;
        this.context = context;
        this.updateMenuClass = updateMenuClass;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_view_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    void XacNhanDelete(final String ten, final int vitrixoa){
        AlertDialog.Builder dialogxoa=new AlertDialog.Builder(updateMenuClass);
        dialogxoa.setMessage("Ban có muốn xóa món : "+ ten +" không?");
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("intent_tenmoncanxoa");
                intent.putExtra("tenmon", ten);
                intent.putExtra("vitrixoa", vitrixoa);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogxoa.show();
    }

    void EditProduct(final String ten, final String gia, final int vitrisua){
        Intent intent = new Intent("intent_tenmoncansua");
        intent.putExtra("tenmon", ten);
        intent.putExtra("gia", gia);
        intent.putExtra("vitrisua", vitrisua);
        Log.d("checkMes", ten);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtName.setText(ProductUpdatesArrayList.get(position).getProduct_name());
        holder.txtPrice.setText(String.valueOf(ProductUpdatesArrayList.get(position).getProduct_price()));

        holder.imagaViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("intent_tenmoncansua");
                intent.putExtra("tenmon", ProductUpdatesArrayList.get(position).getProduct_name());
                intent.putExtra("gia", ProductUpdatesArrayList.get(position).getProduct_price());
                Log.d("checkMes", ProductUpdatesArrayList.get(position).getProduct_name());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    //            EditProduct(ProductUpdatesArrayList.get(position).getProduct_name(), ProductUpdatesArrayList.get(position).getProduct_price(), position);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XacNhanDelete(ProductUpdatesArrayList.get(position).getProduct_name(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ProductUpdatesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice;
        ImageView imagaViewEdit,imageViewDelete;
        public ViewHolder(View itemView) {
            super(itemView);
        txtName=itemView.findViewById(R.id.text_view_item_name_menu);
        txtPrice=itemView.findViewById(R.id.text_view_item_price_menu);
        imagaViewEdit=itemView.findViewById(R.id.imageview_edit);
        imageViewDelete=itemView.findViewById(R.id.imageview_delete);
        }

}


}
