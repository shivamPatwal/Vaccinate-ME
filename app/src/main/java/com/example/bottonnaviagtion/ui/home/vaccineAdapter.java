package com.example.bottonnaviagtion.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottonnaviagtion.R;

import java.util.ArrayList;

public class vaccineAdapter extends RecyclerView.Adapter<vaccineAdapter.vaccineHolder> {

    private Context context;
    private ArrayList<vaccine> arrayList = new ArrayList<vaccine>();

    public vaccineAdapter(Context context, ArrayList<vaccine> rrayList) {
        this.context = context;
        arrayList = rrayList;
    }

    @NonNull
    @Override
    public vaccineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home, parent, false);

        return new vaccineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vaccineHolder holder, int position) {
        vaccine vac = arrayList.get(position);
        holder.textView1.setText(vac.getCentreName());
        String d = Integer.toString(vac.getMinAge());
        holder.textView2.setText(d + "+");
        holder.textView3.setText(vac.getAddress());
        holder.textView4.setText(vac.getVaccine());
        holder.textView5.setText("Dose 1: " + Integer.toString(vac.getDose1()));
        holder.textView6.setText("Dose 2: " + Integer.toString(vac.getDose2()));
        holder.textView7.setText(vac.getPaid());
        if (vac.getavailDose1()) {
            holder.Image1.setImageResource(R.drawable.avail);
        } else {
            holder.Image1.setImageResource(R.drawable.notavail);

        }
        if (vac.getavailDose2()) {
            holder.Image2.setImageResource(R.drawable.avail);
        } else {
            holder.Image2.setImageResource(R.drawable.notavail);

        }
        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COWIN();
            }
        });
        holder.t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COWIN();
            }
        });
        holder.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COWIN();
            }
        });
        holder.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                COWIN();
            }
        });


    }

    public void COWIN() {
        String cowin = "https://selfregistration.cowin.gov.in/";
        Uri uriUrl = Uri.parse(cowin);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }


    public class vaccineHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
        ImageView Image1, Image2;
        AppCompatButton t1, t2, t3, t4;


        public vaccineHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            textView7 = itemView.findViewById(R.id.textView7);
            Image1 = itemView.findViewById(R.id.imageView7);
            Image2 = itemView.findViewById(R.id.imageView8);
            t1 = (AppCompatButton) itemView.findViewById(R.id.textView9);
            t2 = (AppCompatButton) itemView.findViewById(R.id.textView8);
            t3 = (AppCompatButton) itemView.findViewById(R.id.textView10);
            t4 = (AppCompatButton) itemView.findViewById(R.id.textView11);


        }
    }


}
