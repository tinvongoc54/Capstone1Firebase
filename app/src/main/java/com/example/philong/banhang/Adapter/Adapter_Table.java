package com.example.philong.banhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philong.banhang.Activity.MainActivity;
import com.example.philong.banhang.Objects.Product;
import com.example.philong.banhang.Objects.Table;
import com.example.philong.banhang.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter_Table extends RecyclerView.Adapter<Adapter_Table.ViewHolder>{
    ArrayList<Table> menuUpdatesTableArrayList;
    Context context;
    MainActivity mainActivityClass;
    TextView textViewNumberTable;

    public Adapter_Table(ArrayList<Table> menuUpdatesTableArrayList, Context context, MainActivity mainActivityClass, TextView textViewNumberTable) {
        this.menuUpdatesTableArrayList = menuUpdatesTableArrayList;
        this.context = context;
        this.mainActivityClass = mainActivityClass;
        this.textViewNumberTable = textViewNumberTable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_view_row_table, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }


    static int oldPosition = 0;
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Drawable notfree = ContextCompat.getDrawable(mainActivityClass,R.drawable.coffeetable);
        final Drawable free=ContextCompat.getDrawable(mainActivityClass,R.drawable.coffeetable1);
        holder.textViewTable.setText(menuUpdatesTableArrayList.get(position).getTable_name());

        //xu ly mau icon table
        holder.txtNameTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtNameTable.getBackground().equals(notfree)) {
                    holder.txtNameTable.setBackground(free);
                } else {
                    holder.txtNameTable.setBackground(notfree);
                    textViewNumberTable.setText(menuUpdatesTableArrayList.get(position).getTable_name());
                }



//                mainActivityClass.textViewNumberTable.setText(menuUpdatesTableArrayList.get(position).getName());
//                oldPosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuUpdatesTableArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button  txtNameTable;
        TextView textViewTable;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNameTable = itemView.findViewById(R.id.text_view_item_name_table);
            textViewTable = itemView.findViewById(R.id.nameTable);

        }

    }


}
