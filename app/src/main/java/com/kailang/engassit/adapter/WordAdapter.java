package com.kailang.engassit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.kailang.engassit.R;
import com.kailang.engassit.data.entity.Word;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.MyViewHolder>{
    private List<Word> allWasteBook = new ArrayList<>();
    private Context context;

    public WordAdapter(Context context) {
        this.context=context;
    }

    public void setAllWord(List<Word> allWasteBook) {
        this.allWasteBook = allWasteBook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.word_cell,parent,false);
        final MyViewHolder holder = new MyViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Word wasteBook = allWasteBook.get(position);
        holder.tv_cn.setText(wasteBook.getCn());
        holder.tv_en.setText(wasteBook.getEn());

        //点击
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String wordJson = gson.toJson(wasteBook, Word.class);
                    bundle.putString("EditWord", wordJson);
                    Navigation.findNavController(v).navigate(R.id.action_navigation_word_to_wordEditFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allWasteBook.size();
    }


    //自定义ViewHolder:内部类，static 防止内存泄露
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_en,tv_cn;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_en=itemView.findViewById(R.id.word_en);
            tv_cn=itemView.findViewById(R.id.word_cn);

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            clickListener.onItemClick(getAdapterPosition(),v);
//        }

    }

//    public void setOnItemClickListener(WasteBookClickListener clickListener){
//        this.clickListener=clickListener;
//    }
//
//    //点击事件接口
//    public interface WordClickListener {
//        void onItemClick(int position, View v);
//        void onItemLongClick(int position, View v);
//    }


}
