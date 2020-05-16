package com.kailang.engassit.ui.test;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.kailang.engassit.R;
import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Word;
import com.kailang.engassit.data.entity.Wordlib;
import com.kailang.engassit.ui.word.WordViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TestGameFragment extends Fragment implements View.OnClickListener {

    private CardView cardView_a, cardView_b, cardView_c, cardView_d;
    private TextView tv_a, tv_b, tv_c, tv_d, tv_test_word,tv_testgame_total,tv_testgame_rightcount;
    private Button bt_testgame_exit,bt_testgame_nextone;
    private TestViewModel testViewModel;
    //private List<Wordlib> allWordlib,testWordlibs;
    private List<Word> allWords, testWords;
    private Short level;
    private int number = 0;
    private String rightKey;

    public static TestGameFragment newInstance() {
        return new TestGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_game, container, false);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        testViewModel = new ViewModelProvider(this).get(TestViewModel.class);
        level = getArguments().getShort("level");
        //testViewModel.downAllWordlib();
        Log.e("level", level + " ");

        cardView_a = root.findViewById(R.id.cv_testgame_a);
        cardView_b = root.findViewById(R.id.cv_testgame_b);
        cardView_c = root.findViewById(R.id.cv_testgame_c);
        cardView_d = root.findViewById(R.id.cv_testgame_d);
        tv_a = root.findViewById(R.id.tv_testgame_a);
        tv_b = root.findViewById(R.id.tv_testgame_b);
        tv_c = root.findViewById(R.id.tv_testgame_c);
        tv_d = root.findViewById(R.id.tv_testgame_d);
        tv_test_word = root.findViewById(R.id.tv_testgame_word);

        tv_testgame_rightcount=root.findViewById(R.id.tv_testgame_rightcount);
        tv_testgame_total=root.findViewById(R.id.tv_testgame_total);
        bt_testgame_exit=root.findViewById(R.id.bt_testgame_exit);
        bt_testgame_nextone=root.findViewById(R.id.bt_testgame_nextone);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        testWordlibs=new ArrayList<>();
//        testViewModel.getAllWordlib().observe(getViewLifecycleOwner(), new Observer<List<Wordlib>>() {
//            @Override
//            public void onChanged(List<Wordlib> wordlibs) {
//                allWordlib=wordlibs;
//                if(level!=0)
//                    for(Wordlib w:wordlibs){
//                        if(level==w.getWlevel())
//                            testWordlibs.add(w);
//                    }
//            }
//        });
        testWords = new ArrayList<>();
        testViewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                allWords = words;
                if (level != null)
                    for (Word w : words) {
                        if (w.getWlevel().equals(level)) {
                            testWords.add(w);
                        }
                    }
                initTestWord();
                number++;
            }
        });

        testViewModel.getRightCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tv_testgame_total.setText(number+"");
                tv_testgame_rightcount.setText(integer+"");
            }
        });

        cardView_a.setOnClickListener(this);
        cardView_b.setOnClickListener(this);
        cardView_c.setOnClickListener(this);
        cardView_d.setOnClickListener(this);
        tv_a.setOnClickListener(this);
        tv_b.setOnClickListener(this);
        tv_c.setOnClickListener(this);
        tv_d.setOnClickListener(this);
        bt_testgame_exit.setOnClickListener(this);
        bt_testgame_nextone.setOnClickListener(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_testgame_a:
                if(tv_a.getText().equals(rightKey)){
                    testViewModel.setRightCount(testViewModel.getRightCount().getValue()+1);
                    tv_a.setBackgroundResource(R.color.rightans);
                    delay(1);
                }else {
                    tv_a.setBackgroundResource(R.color.wrongans);
                    showRightAns();
                }
                break;
            case R.id.tv_testgame_b:
                if(tv_b.getText().equals(rightKey)){
                    testViewModel.setRightCount(testViewModel.getRightCount().getValue()+1);
                    tv_b.setBackgroundResource(R.color.rightans);
                    delay(1);
                }else{
                    tv_b.setBackgroundResource(R.color.wrongans);
                    showRightAns();
                }
                break;
            case R.id.tv_testgame_c:
                if(tv_c.getText().equals(rightKey)){
                    testViewModel.setRightCount(testViewModel.getRightCount().getValue()+1);
                    tv_c.setBackgroundResource(R.color.rightans);
                    delay(1);
                }else{
                    tv_c.setBackgroundResource(R.color.wrongans);
                    showRightAns();
                }
                break;
            case R.id.tv_testgame_d:
                if(tv_d.getText().equals(rightKey)){
                    testViewModel.setRightCount(testViewModel.getRightCount().getValue()+1);
                    tv_d.setBackgroundResource(R.color.rightans);
                    delay(1);
                }else{
                    tv_d.setBackgroundResource(R.color.wrongans);
                    showRightAns();
                }
                break;
            case R.id.bt_testgame_exit:
                Navigation.findNavController(v).navigateUp();
                break;
            case R.id.bt_testgame_nextone:
                initTestWord();
                number++;
                break;
            default:break;
        }
    }

    private void showRightAns() {
        if(tv_a.getText().toString().equals(rightKey))
            tv_a.setBackgroundResource(R.color.rightans);
        if(tv_b.getText().toString().equals(rightKey))
            tv_b.setBackgroundResource(R.color.rightans);
        if(tv_c.getText().toString().equals(rightKey))
            tv_c.setBackgroundResource(R.color.rightans);
        if(tv_d.getText().toString().equals(rightKey))
            tv_d.setBackgroundResource(R.color.rightans);
    }

    private void clearTvColor(){
        tv_a.setBackgroundResource(R.color.white);
        tv_b.setBackgroundResource(R.color.white);
        tv_c.setBackgroundResource(R.color.white);
        tv_d.setBackgroundResource(R.color.white);
    }

    private void delay(int second){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initTestWord();
                number++;
            }
        }, second*1000);//3秒后执行Runnable中的run方法



//        TimerTask task = new TimerTask(){
//            public void run(){
//                //TODO  todo somthing here
//                initTestWord();
//                number++;
//            }
//        };
//        Timer timer = new Timer();
//        //10秒后执行
//        timer.schedule(task, second * 1000);
    }
    private void initTestWord() {

        clearTvColor();
        Log.e("initTestWord", number + " ");
        HashSet<String> keySet = new HashSet<>();
        String[] cnKey = new String[4];
        if (testWords != null) {
            if (number < testWords.size()) {
                tv_test_word.setText(testWords.get(number).getEn());
                rightKey = testWords.get(number).getCn();
                keySet.add(rightKey);

                while (keySet.size() < 4&&testWords.size()>4) {
                    int indexRandom=(int) Math.round((1000) * Math.random()) % testWords.size();
                    keySet.add(testWords.get(indexRandom).getCn());
                    Log.e("initTestWord cn:",testWords.get(indexRandom).getCn()+"testWords.size()："+testWords.size());
                }
            }

            Iterator<String> it=keySet.iterator();
            int i=0;
            while(it.hasNext()) {
                if(i<4) {
                    cnKey[i++] = it.next();
                }
            }
            tv_a.setText(cnKey[0]);
            tv_b.setText(cnKey[1]);
            tv_c.setText(cnKey[2]);
            tv_d.setText(cnKey[3]);
    }
}

}
