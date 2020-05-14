package com.kailang.engassit.ui.word;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.kailang.engassit.R;
import com.kailang.engassit.data.entity.Sentence;
import com.kailang.engassit.data.entity.Word;
import com.kailang.engassit.ui.sentence.SentenceViewModel;

import java.util.List;


public class WordEditFragment extends Fragment {
    private TextView tv_detail_userno, tv_detail_wno, tv_detail_cn, tv_detail_en, tv_detail_level,tv_detail_sentence;
    private Button bt_detail_delete, bt_detail_edit;
    private WordViewModel wordViewModel;
    private SentenceViewModel sentenceViewModel;
    private Word word;
    private List<Sentence> allSentences;
    private AddWordPopWin addWordPopWin;

    public WordEditFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_word_detail, container, false);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        tv_detail_userno = root.findViewById(R.id.tv_detail_userno);
        tv_detail_wno = root.findViewById(R.id.tv_detail_wno);
        tv_detail_en = root.findViewById(R.id.tv_detail_en);
        tv_detail_cn = root.findViewById(R.id.tv_detail_cn);
        tv_detail_level = root.findViewById(R.id.tv_detal_level);
        bt_detail_delete = root.findViewById(R.id.bt_detail_delete);
        bt_detail_edit = root.findViewById(R.id.bt_detail_edit);
        tv_detail_sentence=root.findViewById(R.id.tv_detail_sentence);
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        sentenceViewModel=new ViewModelProvider(this).get(SentenceViewModel.class);

        Gson gson = new Gson();
        String wordJson = getArguments().getString("EditWord");
        if (wordJson != null) {
            word = gson.fromJson(wordJson, Word.class);
            tv_detail_userno.setText(word.getUserno() + "");
            tv_detail_wno.setText(word.getWno() + "");
            tv_detail_en.setText(word.getEn());
            tv_detail_cn.setText(word.getCn());
            tv_detail_level.setText(word.getWlevel()+"");
            sentenceViewModel.findWordWithPattern(word.getEn()).observe(getViewLifecycleOwner(), new Observer<List<Sentence>>() {
                @Override
                public void onChanged(List<Sentence> sentences) {
                    StringBuilder stringBuilder=new StringBuilder();
                    for(Sentence s:sentences)
                        stringBuilder.append(s.getEn()+"\n"+s.getCn()+"\n");
                    tv_detail_sentence.setText(stringBuilder);
                }
            });

        }

        bt_detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordViewModel.deleteWord(word);
                Navigation.findNavController(v).navigateUp();
            }
        });


        addWordPopWin=new AddWordPopWin(getActivity(), word, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_save_word:
                        String en = addWordPopWin.tv_addWord_en.getText().toString().trim();
                        String cn = addWordPopWin.tv_addWord_cn.getText().toString().trim();
                        short level=addWordPopWin.getLevel();
                        Log.e("addWordPopWin:",en+" "+cn+" "+level);
                        if(!en.isEmpty()&&!cn.isEmpty()) {
                            Word w = new Word();
                            w.setEn(en);
                            w.setCn(cn);
                            w.setWlevel(level);
                            w.setUserno(word.getUserno());
                            w.setWno(word.getWno());
                            wordViewModel.updateWord(w);
                            addWordPopWin.dismiss();
                        }else{
                            Toast.makeText(getContext(),"参数不完整",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        bt_detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWordPopWin.showAtLocation(getActivity().findViewById(R.id.word_detail_layout), Gravity.CENTER,0,0);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addWordPopWin.dismiss();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}
