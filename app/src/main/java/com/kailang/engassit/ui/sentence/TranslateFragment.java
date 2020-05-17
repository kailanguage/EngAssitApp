package com.kailang.engassit.ui.sentence;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kailang.engassit.R;
import com.kailang.engassit.data.TranslateJson;
import com.kailang.engassit.utils.OkHttpCallback;
import com.kailang.engassit.utils.OkHttpUtil;

import java.util.List;


public class TranslateFragment extends Fragment {
    private EditText ev_in;
    private TextView tv_out;
    private Button bt_translate;
    private TranslateViewModel translateViewModel;
    public TranslateFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root =inflater.inflate(R.layout.fragment_translate, container, false);
        ev_in=root.findViewById(R.id.tv_translate_in);
        tv_out=root.findViewById(R.id.tv_translate_out);
        bt_translate=root.findViewById(R.id.bt_translate);
        translateViewModel =new ViewModelProvider(this).get(TranslateViewModel.class);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        translateViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv_out.setText(s);
            }
        });

        bt_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = ev_in.getText().toString().trim();
                if(!input.isEmpty()){

                    String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i="+input;
                    OkHttpUtil.get(url, new OkHttpCallback() {
                        @Override
                        public void onFinish(boolean status, String msg) {
                            super.onFinish(status, msg);
                            if (status) {//连接成功
                                //解析数据
                                Gson gson = new Gson();
                                 TranslateJson translateJson = gson.fromJson(msg,TranslateJson.class);
                                int stat = translateJson.getErrorCode();
                                if (stat == 0) {//操作成功
                                    List<List<TranslateJson.TranslateResultBean>> list = translateJson.getTranslateResult();
                                    StringBuilder temp=new StringBuilder();
                                    for(List<TranslateJson.TranslateResultBean> l:list)
                                        for(TranslateJson.TranslateResultBean ll:l)
                                            temp.append(ll.getTgt()+"\n");
                                        translateViewModel.setResult(temp.toString());
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}
