package com.kailang.engassit.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kailang.engassit.MainActivity;
import com.kailang.engassit.R;
import com.kailang.engassit.data.ServerResponse;
import com.kailang.engassit.data.Sync;


import java.util.ArrayList;
import java.util.List;

/**
 * 登录、注册
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText mAccountView;
    private EditText mPasswordView;
    private EditText mNameView;
    private ImageView mClearAccountView;
    private ImageView mClearPasswordView;
    private CheckBox mEyeView;
    private CheckBox mDropDownView;
    private Button mLoginView;
    private TextView mForgetPsdView;
    private TextView mRegisterView;
    private LinearLayout mTermsLayout;
    private TextView mTermsView;
    private RelativeLayout mPasswordLayout,mRegister;

    private List<View> mDropDownInvisibleViews;
    private LoginViewModel loginViewModel;
    private boolean isReg;
    private Sync sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewId();
        initDropDownGroup();

        mPasswordView.setLetterSpacing(0.2f);

        loginViewModel=new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.initAll();

        loginViewModel.getIsRegSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                    mLoginView.setText("登 录");
                    mRegisterView.setText("注册账号");
                    mRegister.setVisibility(View.GONE);
            }
        });
       sync = new Sync(this);
        //登录或登录
        mLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer username = Integer.parseInt(mAccountView.getText().toString().trim());
                String password = mPasswordView.getText().toString().trim();

                if(mLoginView.getText().equals("注 册")){
                    String nicName=mNameView.getText().toString().trim();
                   sync.register(username,password,nicName,loginViewModel);//注册
                }else {
                    sync.login(username,password,loginViewModel,true);//登录
                }
            }
        });

        //切换注册、登录
        mRegisterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRegisterView.getText().equals("注册账号")) {
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mLoginView.setText("注 册");
                    mRegisterView.setText("登录账号");
                    mRegister.setVisibility(View.VISIBLE);
                }else{
                    mLoginView.setText("登 录");
                    mRegisterView.setText("注册账号");
                    mRegister.setVisibility(View.GONE);
                }
            }
        });


        mClearAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountView.setText("");
            }
        });

        mClearPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordView.setText("");
            }
        });

        mEyeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        mAccountView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当账号栏正在输入状态时，密码栏的清除按钮和眼睛按钮都隐藏
                if (hasFocus) {
                    mClearPasswordView.setVisibility(View.INVISIBLE);
                    mEyeView.setVisibility(View.INVISIBLE);
                } else {
                    mClearPasswordView.setVisibility(View.VISIBLE);
                    mEyeView.setVisibility(View.VISIBLE);
                }
//                Log.i(TAG,"onFocusChange()::" + hasFocus);
            }
        });
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当密码栏为正在输入状态时，账号栏的清除按钮隐藏
                if (hasFocus)
                    mClearAccountView.setVisibility(View.INVISIBLE);
                else mClearAccountView.setVisibility(View.VISIBLE);
            }
        });

        mDropDownView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //下拉按钮点击的时候，密码栏、忘记密码、新用户注册、同意服务条款先全部隐藏
                    setDropDownVisible(View.INVISIBLE);
                    //下拉箭头变为上拉箭头
                    //弹出一个popupWindow
                    //showDropDownWindow();
                } else {
                    setDropDownVisible(View.VISIBLE);
                }
            }
        });
        mDropDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mForgetPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFindPsdDialog();
            }
        });

        mTermsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入服务条款界面
            }
        });
    }


    private void findViewId() {
        mAccountView = findViewById(R.id.et_input_account);
        mPasswordView = findViewById(R.id.et_input_password);
        mNameView = findViewById(R.id.et_input_name);
        mClearAccountView = findViewById(R.id.iv_clear_account);
        mClearPasswordView = findViewById(R.id.iv_clear_password);
        mEyeView = findViewById(R.id.iv_login_open_eye);
        mDropDownView = findViewById(R.id.cb_login_drop_down);
        mLoginView = findViewById(R.id.btn_login);
        mForgetPsdView = findViewById(R.id.tv_forget_password);
        mRegisterView = findViewById(R.id.tv_register_account);
        mTermsLayout = findViewById(R.id.ll_terms_of_service_layout);
        mTermsView = findViewById(R.id.tv_terms_of_service);
        mPasswordLayout = findViewById(R.id.rl_password_layout);
        mRegister=findViewById(R.id.layout_show_name);
    }

    private void initDropDownGroup() {
        mDropDownInvisibleViews = new ArrayList<>();
        mDropDownInvisibleViews.add(mPasswordView);
        mDropDownInvisibleViews.add(mForgetPsdView);
        mDropDownInvisibleViews.add(mRegisterView);
        mDropDownInvisibleViews.add(mPasswordLayout);
        mDropDownInvisibleViews.add(mLoginView);
        mDropDownInvisibleViews.add(mTermsLayout);
    }


    private void setDropDownVisible(int visible) {
        for (View view : mDropDownInvisibleViews) {
            view.setVisibility(visible);
        }
    }

//    private void showDropDownWindow() {
//        final PopupWindow window = new PopupWindow(this);
//        //下拉菜单里显示上次登录的账号，在这里先模拟获取有记录的用户列表
//        List<UserBean> userBeanList = new ArrayList<>();
//        userBeanList.add(new UserBean("12345678","123456789"));
//        userBeanList.add(new UserBean("22669988","22669988"));
//        //配置ListView的适配器
//        final UserBeanAdapter adapter = new UserBeanAdapter(this);
//        adapter.replaceData(userBeanList);
//        //初始化ListView
//        ListView userListView = (ListView) View.inflate(this,
//                R.layout.window_account_drop_down,null);
//        userListView.setAdapter(adapter);
//        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //当下拉列表条目被点击时，显示刚才被隐藏视图,下拉箭头变上拉箭头
//                //相当于mDropDownView取消选中
//                mDropDownView.setChecked(false);
//                //账号栏和密码栏文本更新
//                UserBean checkedUser = adapter.getItem(position);
//                mAccountView.setText(checkedUser.getAccount());
//                mPasswordView.setText(checkedUser.getPassword());
//                //关闭popupWindow
//                window.dismiss();
//            }
//        });
//        //添加一个看不见的FooterView，这样ListView就会自己在倒数第一个（FooterView）上边显示Divider，
//        //进而在UI上实现最后一行也显示分割线的效果了
//        userListView.addFooterView(new TextView(this));
//
//        //配置popupWindow并显示
//        window.setContentView(userListView);
//        window.setAnimationStyle(0);
//        window.setBackgroundDrawable(null);
//        window.setWidth(mPasswordLayout.getWidth());
//        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        window.setOutsideTouchable(true);
//        window.showAsDropDown(mAccountView);
//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                //当点击popupWindow之外区域导致window关闭时，显示刚才被隐藏视图，下拉箭头变上拉箭头
//                //相当于mDropDownView取消选中
//                mDropDownView.setChecked(false);
//            }
//        });
//
//    }

    private void showFindPsdDialog() {
        //有空了做下
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDropDownInvisibleViews.clear();
    }
}
