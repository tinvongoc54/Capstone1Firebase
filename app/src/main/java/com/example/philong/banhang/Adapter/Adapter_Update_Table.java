package com.example.philong.banhang.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philong.banhang.Activity.Update_All_Table;
import com.example.philong.banhang.Activity.Update_All_Table_Update;
import com.example.philong.banhang.Activity.Update_All;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.Objects.Table;
import com.example.philong.banhang.R;

import java.util.ArrayList;

public class Adapter_Update_Table extends RecyclerView.Adapter<Adapter_Update_Table.ViewHolder>{
    ArrayList<Table> UpdatesTableArrayList;
    Context context;
    Update_All_Table updateTableClass;

    public Adapter_Update_Table(ArrayList<Table> updatesTableArrayList, Context context, Update_All_Table updateTableClass) {
        UpdatesTableArrayList = updatesTableArrayList;
        this.context = context;
        this.updateTableClass = updateTableClass;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_view_row_update_table, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.txtNameTable.setText(UpdatesTableArrayList.get(position).getTable_name());
        holder.imageViewEditUpdateTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("intent_bancansua");
                intent.putExtra("ban", UpdatesTableArrayList.get(position).getTable_name());
                intent.putExtra("mota", UpdatesTableArrayList.get(position).getTable_describe());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.imageViewEditDeleteTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XacNhanXoa(UpdatesTableArrayList.get(position).getTable_name(), position);
            }
        });
    }

    private void XacNhanXoa(final String name, final int vitrixoa){

        AlertDialog.Builder dialogxoa=new AlertDialog.Builder(updateTableClass);
        dialogxoa.setMessage("Ban có muốn xóa "+ name +" không?");
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("intent_bancanxoa");
                intent.putExtra("ban", name);
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

    @Override
    public int getItemCount() {
        return UpdatesTableArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameTable;
        ImageView imageViewEditUpdateTable,imageViewEditDeleteTable;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNameTable=itemView.findViewById(R.id.text_view_item_name_table_update);
            imageViewEditUpdateTable=itemView.findViewById(R.id.imageview_update_edit);
            imageViewEditDeleteTable=itemView.findViewById(R.id.imageview_update_delete);
        }

    }


}
