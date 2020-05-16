package com.kailang.engassit.ui.test;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kailang.engassit.R;

public class TestFragment extends Fragment {

    private TestViewModel mViewModel;
    private Button bt_test_easy,bt_test_common,bt_test_hard;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_test, container, false);

        bt_test_easy=root.findViewById(R.id.bt_test_easy);
        bt_test_common=root.findViewById(R.id.bt_test_common);
        bt_test_hard=root.findViewById(R.id.bt_test_hard);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bt_test_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putShort("level", (short) 1);
                Navigation.findNavController(v).navigate(R.id.action_navigation_test_to_testGameFragment,bundle);
            }
        });
        bt_test_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putShort("level", (short) 2);
                Navigation.findNavController(v).navigate(R.id.action_navigation_test_to_testGameFragment,bundle);
            }
        });
        bt_test_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putShort("level", (short) 3);
                Navigation.findNavController(v).navigate(R.id.action_navigation_test_to_testGameFragment,bundle);
            }
        });

    }
}
