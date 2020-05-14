package com.kailang.engassit.ui.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kailang.engassit.R;
import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Stat;
import com.kailang.engassit.ui.login.LoginActivity;

import java.util.List;

import static com.kailang.engassit.data.Sync.currentUser;

public class PersonFragment extends Fragment {

    private PersonViewModel personViewModel;
    private TextView tv_logout,tv_username,tv_today,tv_total,tv_actday,tv_sync;
    private Button bt_check;
    private Stat currentUserStat;

    public PersonFragment(){
        setHasOptionsMenu(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =new ViewModelProvider(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person, container, false);


        tv_logout=root.findViewById(R.id.tv_logout);
        tv_username=root.findViewById(R.id.tv_username);
        tv_today=root.findViewById(R.id.tv_today);
        tv_total=root.findViewById(R.id.tv_total);
        tv_actday=root.findViewById(R.id.tv_actday);
        bt_check=root.findViewById(R.id.bt_check);
        tv_sync=root.findViewById(R.id.textView_sync);

        if(currentUser!=null) {
            tv_username.setText(currentUser.getUname());
        }

        tv_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personViewModel.deleteAllStat();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //personViewModel.deleteAllStat();
        personViewModel.getStat().observe(getViewLifecycleOwner(), new Observer<List<Stat>>() {
            @Override
            public void onChanged(List<Stat> stats) {
//                if(stats==null||stats.isEmpty()){
//                    Stat s= new Stat();
//                    s.setActday(1);
//                    s.setToday((short) 0);
//                    s.setTotal(0);
//                    s.setUserno(currentUser.getUserno());
//                    personViewModel.insertStat(s);
//                }
                for(Stat s:stats){
                    if(currentUser!=null&&s.getUserno().equals(currentUser.getUserno())){
                        tv_actday.setText(s.getActday()+"");
                        tv_today.setText(s.getToday()+"");
                        tv_total.setText(s.getTotal()+"");
                        currentUserStat=s;
                    }
                }
            }
        });

        //签到
        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??
                currentUserStat.setActday(currentUserStat.getActday()+1);
                personViewModel.check(currentUserStat);
                bt_check.setText("已签到");
                bt_check.setClickable(false);
            }
        });


        //登出
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync sync = new Sync(getContext());
                sync.logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    User userT=personViewModel.getAllUser().get(0);
//                    if (userT!=null){
//                        tv_username.setText(userT.getUname());
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });


    }

}
