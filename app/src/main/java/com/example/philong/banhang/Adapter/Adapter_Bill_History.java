package com.example.philong.banhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.philong.banhang.Objects.Bill;
import com.example.philong.banhang.Objects.Bill_History;
import com.example.philong.banhang.Objects.Product_Bill;
import com.example.philong.banhang.R;

import java.util.ArrayList;

public class Adapter_Bill_History extends BaseAdapter {

    ArrayList<Bill_History> arrayListBillHistory;
    Context context;
    int layout;

    public Adapter_Bill_History(ArrayList<Bill_History> arrayListBillHistory, Context context, int layout) {
        this.arrayListBillHistory = arrayListBillHistory;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arrayListBillHistory.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout, null);

        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);

        TextView textViewHD = view.findViewById(R.id.textViewBillNumber);
        textViewHD.setText(arrayListBillHistory.get(i).getKey());
        TextView textViewNgay = view.findViewById(R.id.textViewDateCreate);
        textViewNgay.setText(arrayListBillHistory.get(i).getDate_create());
        TextView textViewKhachHang = view.findViewById(R.id.textViewKhachHang);
        textViewKhachHang.setText(arrayListBillHistory.get(i).getCustomer_name());
        TextView textViewSDT = view.findViewById(R.id.textViewSDT);
        textViewSDT.setText(arrayListBillHistory.get(i).getCustomer_phone());
        TextView textViewTrangThai = view.findViewById(R.id.textViewTrangThai);
        if (arrayListBillHistory.get(i).getStatus().equals("0")) {
            textViewTrangThai.setText("Đang chờ xác nhận");
        } else {
            textViewTrangThai.setText("Đã xác nhận");
        }
        TextView textViewNhanVien = view.findViewById(R.id.textViewNhanVien);
        textViewNhanVien.setText(arrayListBillHistory.get(i).getStaff_create());
        TextView textViewTongTien = view.findViewById(R.id.textViewTongTien);
        textViewTongTien.setText(arrayListBillHistory.get(i).getTotal_price_after_promotion());

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bill_History bill_history = arrayListBillHistory.get(i);

                String date = bill_history.getDate_create();
                String staff = bill_history.getStaff_create();
                String status = bill_history.getStatus();
                String total = bill_history.getTotal_price_after_promotion();
                String key = bill_history.getKey();
                String name = bill_history.getCustomer_name();
                String phone = bill_history.getCustomer_phone();

                Intent intent = new Intent("intent_billhistory");
                intent.putExtra("key", key);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        return view;
    }
}
