package com.example.philong.banhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.philong.banhang.Activity.MainActivity;
import com.example.philong.banhang.Objects.Category;
import com.example.philong.banhang.Objects.Category;
import com.example.philong.banhang.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Spinner_Category extends BaseAdapter {
    TextView textView;

    Context context;
    int myLayout;
    ArrayList<Category> arrayList;



    public Adapter_Spinner_Category(Context context, int myLayout, ArrayList<Category> arrayList) {
        this.context = context;
        this.myLayout = myLayout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(myLayout,null);

        textView = convertView.findViewById(R.id.textview_item_spinner_ten);
        textView.setText(arrayList.get(position).getCatalog_name());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("intent_tendanhmuc");
                intent.putExtra("category_name", arrayList.get(position).getCatalog_name());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        return convertView;
    }
}

//public class Adapter_Spinner_Category extends RecyclerView.Adapter<Adapter_Spinner_Category.ViewHolder> {
//    ArrayList<Category> arrayListCategory;
//    Context context;
//    MainActivity mainActivity;
//
//    public Adapter_Spinner_Category(ArrayList<Category> arrayListCategory, Context context) {
//        this.arrayListCategory = arrayListCategory;
//        this.context = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
//        View itemView = layoutInflater.inflate(R.layout.item_view_row_category, parent, false);
//        ViewHolder viewHolder = new ViewHolder(itemView);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        holder.btnCategory.setText(arrayListCategory.get(position).getCatalog_name());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayListCategory.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        Button btnCategory;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            btnCategory = itemView.findViewById(R.id.btn_category);
//        }
//
//    }
//}
