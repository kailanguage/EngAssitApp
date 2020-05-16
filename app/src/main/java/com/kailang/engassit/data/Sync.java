package com.kailang.engassit.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kailang.engassit.MainActivity;
import com.kailang.engassit.data.entity.Sentence;
import com.kailang.engassit.data.entity.Stat;
import com.kailang.engassit.data.entity.User;
import com.kailang.engassit.data.entity.Word;
import com.kailang.engassit.data.entity.Wordlib;
import com.kailang.engassit.data.repository.SentenceRep;
import com.kailang.engassit.data.repository.StatRep;
import com.kailang.engassit.data.repository.WordRep;
import com.kailang.engassit.data.repository.WordlibRep;
import com.kailang.engassit.ui.login.LoginActivity;
import com.kailang.engassit.ui.login.LoginViewModel;
import com.kailang.engassit.ui.person.PersonViewModel;
import com.kailang.engassit.utils.OkHttpCallback;
import com.kailang.engassit.utils.OkHttpUtil;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Sync {
    public static final String HOST = "http://47.95.202.171:8080";
    private Context context;
    public static User currentUser;
    public Sync(Context context) {
        this.context = context;
    }

    /*
    注册
     */
    public void register(final Integer username, String password, String nicName, final LoginViewModel loginViewModel){
        //currentUser.setUserno(username);
        OkHttpUtil.get(HOST + "/user/register?userno=" + username + "&password=" + password+"&username="+nicName,
                new OkHttpCallback() {
                    @Override
                    public void onFinish(boolean status, String msg) {
                        super.onFinish(status, msg);
                        if (status) {
                            //解析数据
                            Gson gson = new Gson();
                            ServerResponse<User> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<User>>() {
                            }.getType());
                            int stat = serverResponse.getStatus();
                            if (stat == 0) {//注册成功
                                Looper.prepare();
                                Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
                                loginViewModel.setIsRegSuccess(Boolean.TRUE);
                                Looper.loop();

                            } else {
                                Looper.prepare();
                                Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }

                        }
                    }
                });

    }

    /*
    登录
     */
    private int reTimes=0;
    public void login(final Integer userno, final String password, final LoginViewModel loginViewModel, final boolean isFirstLogin){
        OkHttpUtil.get(HOST + "/user/login?userno=" + userno + "&password=" + password,
                new OkHttpCallback() {
                    @Override
                    public void onFinish(boolean status, String msg) {
                        super.onFinish(status, msg);
                        if (status) {
                            //解析数据
                            Gson gson = new Gson();
                            ServerResponse<User> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<User>>() {
                            }.getType());
                            int stat = serverResponse.getStatus();
                            if (stat == 0) {//登录成功
                                //保存用户信息
                                currentUser = serverResponse.getData();

                                if(isFirstLogin) {
                                    //保存账号密码
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("user",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putInt("userno",userno);
                                    editor.putString("upassword",password);
                                    editor.commit();
                                }
                                //Activity跳转
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);

                                if(isFirstLogin) {
                                    Looper.prepare();
                                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else {
                                    Looper.prepare();
                                    Toast.makeText(context, "自动登录成功！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                Looper.prepare();
                                Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }

                        }else{
                            if(++reTimes<5){
                                Looper.prepare();
                                Toast.makeText(context,"正在尝试登录："+reTimes+"次", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                login(userno, password, loginViewModel,isFirstLogin);
                            }
                        }
                    }
                });
    }
    /*
退出
 */
    public void logout() {
        OkHttpUtil.get(HOST + "/user/logout", new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {
                        Looper.prepare();
                        Toast.makeText(context, "退出成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            }
        });

    }

    /*
    下载所有的word
     */
    public void downAllWord() {
        OkHttpUtil.get(HOST + "/word/select",
                new OkHttpCallback() {
                    @Override
                    public void onFinish(boolean status, String msg) {
                        super.onFinish(status, msg);
                        if (status) {
                            //解析数据
                            Gson gson = new Gson();
                            ServerResponse<List<Word>> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<List<Word>>>() {
                            }.getType());
                            int stat = serverResponse.getStatus();
                            if (stat == 0) {
                                List<Word> list = serverResponse.getData();

                                Looper.prepare();
                                WordRep wordRep = new WordRep(context);
                                for (Word w : list) {
                                    wordRep.insertWord(w);
                                }
                                Toast.makeText(context, "正在同步句子", Toast.LENGTH_SHORT).show();
                                Looper.loop();


                            } else {
                                Looper.prepare();
                                Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }

                        }
                    }
                });
    }

    /*
下载所有的Sentence
 */
    public void downAllSentence() {
        OkHttpUtil.get(HOST + "/sentence/select",
                new OkHttpCallback() {
                    @Override
                    public void onFinish(boolean status, String msg) {
                        super.onFinish(status, msg);
                        if (status) {
                            //解析数据
                            Gson gson = new Gson();
                            ServerResponse<List<Sentence>> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<List<Sentence>>>() {
                            }.getType());
                            int stat = serverResponse.getStatus();
                            if (stat == 0) {
                                List<Sentence> list = serverResponse.getData();

                                //sentanceRep.deleteAllSentence();
                                Looper.prepare();
                                SentenceRep sentanceRep = new SentenceRep(context);
                                for (Sentence s : list)
                                    sentanceRep.insertSentence(s);
                                Toast.makeText(context, "正在同步例句", Toast.LENGTH_SHORT).show();
                                Looper.loop();

                            } else {
                                Looper.prepare();
                                Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }

                        }
                    }
                });
    }

    /*
    插入一个word
     */
    public void insertWord(Word word) {
        String url = HOST + "/word/insert?userno=" + word.getUserno() + "&wno=" + word.getWno() + "&en=" + word.getEn() + "&cn=" + word.getCn() + "&wlevel=" + word.getWlevel();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "插入成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }

            }
        });
    }

    /*
    删除一个word
    */
    public void deletetWord(Word word) {
        String url = HOST + "/word/delete/" + word.getWno();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    更新一个word
    */
    public void updateWord(Word word) {
        String url = HOST + "/word/update?userno=" + word.getUserno() + "&wno=" + word.getWno() + "&en=" + word.getEn() + "&cn=" + word.getCn() + "&wlevel=" + word.getWlevel();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    插入一个sentence
    */
    public void inserSentence(Sentence sentence) {
        String url = HOST + "/sentence/insert?userno=" + sentence.getUserno() + "&sno=" + sentence.getSno() + "&en=" + sentence.getEn() + "&cn=" + sentence.getCn();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "插入成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    插入一个sentence
    */
    public void deleteSentence(Sentence sentence) {
        String url = HOST + "/sentence/delete/" + sentence.getSno();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    插入一个sentence
    */
    public void updateSentence(Sentence sentence) {
        String url = HOST + "/sentence/update?userno=" + sentence.getUserno() + "&sno=" + sentence.getSno() + "&en=" + sentence.getEn() + "&cn=" + sentence.getCn();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    获取stat
     */
    public void getStat() {
        String url = HOST + "/stat/select";
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse<Stat> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<Stat>>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功

                        //statRep.deleteAllStat();

                        Looper.prepare();
                        StatRep statRep = new StatRep(context);
                        statRep.insertStat(serverResponse.getData());
                        Toast.makeText(context, "获取stat成功", Toast.LENGTH_LONG).show();
                        Looper.loop();


                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    更新stat
     */
    public void updateStat(Stat stat) {
        String url = HOST + "/stat/update?userno=" + stat.getUserno() + "&today=" + stat.getToday() + "&total=" + stat.getTotal() + "&actday=" + stat.getActday();
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
        StatRep statRep = new StatRep(context);
        statRep.updateStat(stat);
    }

    /*
    下载所有的wordlib
     */
    public void downAllWordlib() {
        String url = HOST + "/wordlib/select/all";
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse<List<Wordlib>> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<List<Wordlib>>>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        WordlibRep wordlibRep = new WordlibRep(context);
                        //wordlibRep.deleteAllStat();
                        List<Wordlib> list = serverResponse.getData();
                        for (Wordlib w : list)
                            wordlibRep.insetWordlib(w);
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }

            }
        });
    }

    /*
    根据等级下载wordlib
     */
    public void downWordlibByLevel(Short level) {
        String url = HOST + "/wordlib/select/" + level;
        OkHttpUtil.get(url, new OkHttpCallback() {
            @Override
            public void onFinish(boolean status, String msg) {
                super.onFinish(status, msg);
                if (status) {//连接成功
                    //解析数据
                    Gson gson = new Gson();
                    ServerResponse<List<Wordlib>> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<List<Wordlib>>>() {
                    }.getType());
                    int stat = serverResponse.getStatus();
                    if (stat == 0) {//操作成功
                        Looper.prepare();
                        WordlibRep wordlibRep = new WordlibRep(context);
                        List<Wordlib> list = serverResponse.getData();
                        for (Wordlib w : list)
                            wordlibRep.insetWordlib(w);
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(context, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                }
            }
        });
    }

}
