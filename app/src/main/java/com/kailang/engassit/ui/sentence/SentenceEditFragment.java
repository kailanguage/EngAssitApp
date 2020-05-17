package com.kailang.engassit.ui.sentence;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.kailang.engassit.R;
import com.kailang.engassit.data.entity.Sentence;


public class SentenceEditFragment extends Fragment {
    private TextView tv_sdetail_userno, tv_detail_sno, tv_sdetail_cn, tv_sdetail_en;
    private Button bt_sdetail_delete, bt_sdetail_edit;
    private SentenceViewModel sentenceViewModel;
    private Sentence sentence;
    private AddSentencePopWin addSentencePopWin;

    public SentenceEditFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sentence_detail, container, false);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        tv_sdetail_userno = root.findViewById(R.id.tv_sdetail_userno);
        tv_detail_sno = root.findViewById(R.id.tv_detail_sno);
        tv_sdetail_en = root.findViewById(R.id.tv_sdetail_en);
        tv_sdetail_cn = root.findViewById(R.id.tv_sdetail_cn);

        bt_sdetail_delete = root.findViewById(R.id.bt_sdetail_delete);
        bt_sdetail_edit = root.findViewById(R.id.bt_sdetail_edit);
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sentenceViewModel = new ViewModelProvider(this).get(SentenceViewModel.class);
        Gson gson = new Gson();
        String wordJson = getArguments().getString("EditSentence");
        if (wordJson != null) {
            sentence = gson.fromJson(wordJson, Sentence.class);
            tv_sdetail_userno.setText(sentence.getUserno() + "");
            tv_detail_sno.setText(sentence.getSno() + "");
            tv_sdetail_en.setText(sentence.getEn());
            tv_sdetail_cn.setText(sentence.getCn());
        }
        bt_sdetail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentenceViewModel.deleteSentence(sentence);
            }
        });

        //add new sentence
        addSentencePopWin = new AddSentencePopWin(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_save_sentence:
                        String en = addSentencePopWin.tv_addSentence_en.getText().toString().trim();
                        String cn = addSentencePopWin.tv_addSentence_cn.getText().toString().trim();
                        if (!en.isEmpty() && !cn.isEmpty()) {
                            Sentence s = new Sentence();
                            s.setEn(en);
                            s.setCn(cn);
                            s.setUserno(sentence.getUserno());
                            s.setSno(sentence.getSno());
                            sentenceViewModel.updateSentence(s);
                            addSentencePopWin.dismiss();
                        } else {
                            Toast.makeText(getContext(), "参数不完整", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }, false, sentence);

        bt_sdetail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSentencePopWin.showAtLocation(getActivity().findViewById(R.id.sentence_detail_layout), Gravity.CENTER, 0, 0);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addSentencePopWin.dismiss();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}
