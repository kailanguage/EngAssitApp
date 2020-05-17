package com.kailang.engassit.ui.sentence;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kailang.engassit.R;
import com.kailang.engassit.adapter.SentenceAdapter;
import com.kailang.engassit.adapter.WordAdapter;
import com.kailang.engassit.data.entity.Sentence;
import com.kailang.engassit.data.entity.Word;
import com.kailang.engassit.ui.word.WordViewModel;

import java.util.List;

import static com.kailang.engassit.data.Sync.currentUser;

public class SentenceFragment extends Fragment {

    private SentenceViewModel sentenceViewModel;

    private LiveData<List<Sentence>> allSentencesLive;
    private List<Sentence> allSentences;
    private SentenceAdapter sentenceAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AddSentencePopWin addSentencePopWin;
    private int maxSno;

    public SentenceFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.app_bar_translate){
            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_sentence_to_translateFragment);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sentence_fragment_menu, menu);

        //查找功能
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(780);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                final String pattern = newText.trim();
                //allSentencesLive.removeObservers(getViewLifecycleOwner());
                allSentencesLive=sentenceViewModel.findWordWithPattern(pattern);
                allSentencesLive.observe(getViewLifecycleOwner(), new Observer<List<Sentence>>() {
                    @Override
                    public void onChanged(List<Sentence> words) {
                        int temp=sentenceAdapter.getItemCount();
                        sentenceAdapter.setAllWord(words);
                        if(temp!=words.size()){
                            sentenceAdapter.notifyDataSetChanged();;
                        }
                    }
                });
                return true;
            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sentenceViewModel =new ViewModelProvider(this).get(SentenceViewModel.class);

        View root = inflater.inflate(R.layout.fragment_sentence, container, false);



        final FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton_sentence);

        //add new sentence
        addSentencePopWin=new AddSentencePopWin(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_save_sentence:
                        String en = addSentencePopWin.tv_addSentence_en.getText().toString().trim();
                        String cn = addSentencePopWin.tv_addSentence_cn.getText().toString().trim();
                        if(!en.isEmpty()&&!cn.isEmpty()) {
                            Sentence s = new Sentence();
                            s.setEn(en);
                            s.setCn(cn);
                            s.setUserno(currentUser.getUserno());
                            s.setSno(++maxSno);
                            sentenceViewModel.insertSentence(s);
                            Log.e("insertSentence",en+" "+cn);
                            addSentencePopWin.dismiss();
                        }else{
                        Toast.makeText(getContext(),"参数不完整",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        },true,null);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSentencePopWin.showAtLocation(requireActivity().findViewById(R.id.swipeRefreshLayout_sentence), Gravity.CENTER,0,0);

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = requireActivity().findViewById(R.id.recyclerView_sentence);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        sentenceAdapter = new SentenceAdapter(requireContext(), true);
        recyclerView.setAdapter(sentenceAdapter);

        swipeRefreshLayout = requireActivity().findViewById(R.id.swipeRefreshLayout_sentence);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sentenceViewModel.deleteAllSync();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        allSentencesLive=sentenceViewModel.getAllSentence();

        allSentencesLive.observe(getViewLifecycleOwner(), new Observer<List<Sentence>>() {
            @Override
            public void onChanged(List<Sentence> sentences) {
                allSentences = sentences;
                int tmp = sentenceAdapter.getItemCount();
                sentenceAdapter.setAllWord(sentences);
                if (tmp != sentences.size())
                    sentenceAdapter.notifyDataSetChanged();
                for(Sentence s:sentences)
                    if(s.getSno()>maxSno)maxSno=s.getSno();
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
                final Sentence wordToDelete = allSentences.get(viewHolder.getAdapterPosition());
                sentenceViewModel.deleteSentence(wordToDelete);
                Snackbar.make(requireActivity().findViewById(R.id.coordinatorLayout_sentence), "已删除一条记录", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sentenceViewModel.insertSentence(wordToDelete);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addSentencePopWin.dismiss();
    }
}
