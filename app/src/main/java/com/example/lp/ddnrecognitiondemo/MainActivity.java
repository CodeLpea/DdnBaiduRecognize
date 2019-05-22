package com.example.lp.ddnrecognitiondemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lp.ddnrecognitiondemo.Json.ResultBean;
import com.example.lp.ddnrecognitiondemo.Json.TokenBean;
import com.example.lp.ddnrecognitiondemo.Json.reulstTitleBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private static  String LocalTestPath="/storage/emulated/0/test.jpg";
    private static final String tokenUrl="https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=dieoDD6ARUlGTmRCBmc4xGet&client_secret=ZrsFr7gvZe7b2eNEDWcOXWGuoKWAGT9S";
    String access_token=null;
    TextView result;
    ImageView iv;
    ScrollView scrollView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_test).setOnClickListener(listener);
        result = (TextView) findViewById(R.id.textView);
        iv = (ImageView) findViewById(R.id.iv);
        linearLayout=findViewById(R.id.ll_sl);
        Auth();
    }

    private void Auth() {
        Log.i(TAG, "Auth: ");
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。

        Request request = new Request.Builder()//创建Request 对象。
                .url(tokenUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String getResponse=response.body().string();
                    Log.i(TAG, "getResponse: "+getResponse);
                    if(!TextUtils.isEmpty(getResponse)) {
                        JSONObject tokenObject = new JSONObject(getResponse);
                        access_token = tokenObject.getString("access_token");
                    }
                    Log.i(TAG, "access_token: "+access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });//回调方法的使用与get异步请求相同，此时略。

    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: ");
            iv.setImageBitmap(BitmapFactory.decodeFile(LocalTestPath));//展示图片
            upImage(LocalTestPath);
        }
    };

    public void upImage(String path) {
        Log.i(TAG, "upImage: ");
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general?access_token=" + access_token;
        String imgStr = file2String(new File(path));

           OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。

            FormBody formBody = new FormBody.Builder()
                //add要拼接的form表单
                .add("image",imgStr)//传递键值对参数
                  .build();
            Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/x-www-form-urlencode")
                .post(formBody)
                .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: "+call.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    reulstTitleBean mreulstTitleBean=new reulstTitleBean();
                    mreulstTitleBean=handleReulstResponse(response.body().string());
                    Log.i(TAG, "mreulstTitleBean.log_id: "+mreulstTitleBean.log_id);
                    Log.i(TAG, "mreulstTitleBean.result_num: "+mreulstTitleBean.result_num);
                    Log.i(TAG, "mreulstTitleBean.log_id: "+mreulstTitleBean.log_id);
                    final List<ResultBean> resultBeanList=mreulstTitleBean.result;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout.removeAllViews();
                            for(int i=0;i<resultBeanList.size();i++){
                                Log.i(TAG, "resultBeanList.get(i).keyword: "+ resultBeanList.get(i).keyword);
                                Log.i(TAG, "resultBeanList.get(i).root: "+ resultBeanList.get(i).root);
                                Log.i(TAG, "resultBeanList.get(i).score: "+ resultBeanList.get(i).score);
                                TextView textView = new TextView(MainActivity.this);
                                textView.setText(
                                        "关键字："+resultBeanList.get(i).keyword+"\r\n"+
                                        "root："+resultBeanList.get(i).root+"\r\n"+
                                        "置信值："+resultBeanList.get(i).score);
                                linearLayout.addView(textView, i);

                            }
                        }
                    });


                   // Log.i(TAG, "onResponse: "+response.body().string());

                }
            });//回调方法的使用与get异步请求相同，此时略。

        }


    private String file2String(File file) {
        byte[] buffer = new byte[0];
        try {
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 解析JSON数据
     * */
    public reulstTitleBean handleReulstResponse(String response){
            return  new Gson().fromJson(response,reulstTitleBean.class);
    }

}
