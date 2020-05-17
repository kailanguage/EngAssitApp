package com.kailang.engassit.ui.sentence;

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
import com.kailang.engassit.data.entity.Sentence;

public class AddSentencePopWin extends PopupWindow {

    private Context mContext;

    private View view;

    private Button bt_save_sentence;

    public EditText tv_addSentence_en;

    public EditText tv_addSentence_cn;

    private TextView sentence_dialog_title;

    public AddSentencePopWin(Activity mContext, View.OnClickListener itemsOnClick, boolean isNew, Sentence s) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.add_sentence_dialog, null);

        tv_addSentence_en = view.findViewById(R.id.tv_addSentence_en);
        tv_addSentence_cn = view.findViewById(R.id.tv_addSentence_cn);
        bt_save_sentence = view.findViewById(R.id.bt_save_sentence);
        sentence_dialog_title=view.findViewById(R.id.sentence_dialog_title);

        // 设置按钮监听
        bt_save_sentence.setOnClickListener(itemsOnClick);

        if(!isNew){
            sentence_dialog_title.setText("修改例句");
            tv_addSentence_en.setText(s.getEn());
            tv_addSentence_cn.setText(s.getCn());
        }else {
            sentence_dialog_title.setText("添加新例句");
            tv_addSentence_en.setText("");
            tv_addSentence_cn.setText("");
        }


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

}
