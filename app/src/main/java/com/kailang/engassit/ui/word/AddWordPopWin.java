package com.kailang.engassit.ui.word;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kailang.engassit.R;
import com.kailang.engassit.data.entity.Word;

public class AddWordPopWin extends PopupWindow {

    private Context mContext;

    private View view;

    private Button bt_save_word;

    private TextView tv_word_title;

    public EditText tv_addWord_en;

    public EditText tv_addWord_cn;

    private RadioGroup radioGroup;

    private RadioButton rb_easy,rb_common,rb_hard;

    private short level=1;

    /*
    update word
     */

    public AddWordPopWin(Activity mContext, Word word,View.OnClickListener itemsOnClick){

        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.add_word_dialog, null);

        tv_addWord_en = view.findViewById(R.id.tv_addWord_en);
        tv_addWord_cn = view.findViewById(R.id.tv_addWord_cn);
        radioGroup=view.findViewById(R.id.radioGroup_addWord);
        rb_easy=view.findViewById(R.id.easy_word);
        rb_common=view.findViewById(R.id.common_word);
        rb_hard=view.findViewById(R.id.hard_word);
        bt_save_word = view.findViewById(R.id.bt_save_word);
        tv_word_title=view.findViewById(R.id.word_dialog_title);

        tv_addWord_cn.setText(word.getCn());
        tv_addWord_en.setText(word.getEn());
        bt_save_word.setText("更新");
        tv_word_title.setText("修改单词");

        radioGroup.clearCheck();
        switch (word.getWlevel()){
            case 1:
                rb_easy.setChecked(true);
                break;
            case 2:
                rb_common.setChecked(true);
                break;
            case 3:
                rb_hard.setChecked(true);
                break;
            default:
                break;
        }

        // 设置按钮监听
        bt_save_word.setOnClickListener(itemsOnClick);

        //word level
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group==radioGroup){
                    switch (checkedId){
                        case R.id.easy_word:
                            level=1;
                            break;
                        case R.id.common_word:
                            level=2;
                            break;
                        case R.id.hard_word:
                            level=3;
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);

        // 设置弹出窗体的宽和高
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));
        // 设置弹出窗体可点击
        this.setFocusable(true);
    }
    /*
    add new word
     */
    public AddWordPopWin(Activity mContext, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.add_word_dialog, null);

        tv_addWord_en = view.findViewById(R.id.tv_addWord_en);
        tv_addWord_cn = view.findViewById(R.id.tv_addWord_cn);
        radioGroup=view.findViewById(R.id.radioGroup_addWord);
        rb_easy=view.findViewById(R.id.easy_word);
        rb_common=view.findViewById(R.id.common_word);
        rb_hard=view.findViewById(R.id.hard_word);
        bt_save_word = view.findViewById(R.id.bt_save_word);

        tv_addWord_en.setText("");
        tv_addWord_cn.setText("");
        bt_save_word.setText("保存");
        //tv_word_title.setText("添加新单词");

        // 设置按钮监听
        bt_save_word.setOnClickListener(itemsOnClick);

        //word level
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group==radioGroup){
                    switch (checkedId){
                        case R.id.easy_word:
                            level=1;
                            break;
                        case R.id.common_word:
                            level=2;
                            break;
                        case R.id.hard_word:
                            level=3;
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);

        // 设置弹出窗体的宽和高
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));
        // 设置弹出窗体可点击
        this.setFocusable(true);
    }

    public short getLevel(){
        return level;
    }
}
