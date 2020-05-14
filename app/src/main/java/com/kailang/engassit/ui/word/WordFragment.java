package com.kailang.engassit.ui.word;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kailang.engassit.R;
import com.kailang.engassit.adapter.WordAdapter;
import com.kailang.engassit.data.Sync;
import com.kailang.engassit.data.entity.Word;

import java.util.List;

import static com.kailang.engassit.data.Sync.currentUser;

public class WordFragment extends Fragment {

    private WordViewModel wordViewModel;
    private LiveData<List<Word>> allWordsLive;
    private List<Word> allWords;
    private WordAdapter wordAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AddWordPopWin addWordPopWin;
    private int maxWno;

    public WordFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.word_fragment_menu, menu);

        //查找功能
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(800);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String pattern = newText.trim();
                allWordsLive.removeObservers(getViewLifecycleOwner());
                allWordsLive = wordViewModel.findWordWithPattern(pattern);
                allWordsLive.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = wordAdapter.getItemCount();
                        wordAdapter.setAllWord(words);
                        if (temp != words.size()) {
                            wordAdapter.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_word, container, false);



        final FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton_word);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWordPopWin.showAtLocation(requireActivity().findViewById(R.id.swipeRefreshLayout_word), Gravity.CENTER, 0, 0);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = requireActivity().findViewById(R.id.recyclerView_word);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        wordAdapter = new WordAdapter(requireContext());
        recyclerView.setAdapter(wordAdapter);

        swipeRefreshLayout = requireActivity().findViewById(R.id.swipeRefreshLayout_word);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wordViewModel.deleteAllSync();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        allWordsLive = wordViewModel.getAllWords();
        allWordsLive.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                allWords = words;
                int tmp = wordAdapter.getItemCount();
                wordAdapter.setAllWord(words);
                if (tmp != words.size())
                    wordAdapter.notifyDataSetChanged();

                for(Word w:words)
                    if(w.getWno()>maxWno)maxWno=w.getWno();
            }
        });

        //add word
        addWordPopWin = new AddWordPopWin(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_save_word:
                        String en = addWordPopWin.tv_addWord_en.getText().toString().trim();
                        String cn = addWordPopWin.tv_addWord_cn.getText().toString().trim();
                        short level = addWordPopWin.getLevel();
                        if (!en.isEmpty() && !cn.isEmpty()) {
                            Log.e("addWordPopWin:", en + " " + cn + " " + level);
                            Word w = new Word();
                            w.setEn(en);
                            w.setCn(cn);
                            w.setWlevel(level);
                            w.setUserno(currentUser.getUserno());
                            w.setWno(++maxWno);
                            wordViewModel.insertWord(w);
                            addWordPopWin.dismiss();
                        } else {
                            Toast.makeText(getContext(), "参数不完整", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        //左右滑动启动删除功能
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //Item位置移动
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordToDelete = allWords.get(viewHolder.getAdapterPosition());
                wordViewModel.deleteWord(wordToDelete);
                Snackbar.make(requireActivity().findViewById(R.id.coordinatorLayout_word), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordViewModel.insertWord(wordToDelete);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        addWordPopWin.dismiss();
    }
}
