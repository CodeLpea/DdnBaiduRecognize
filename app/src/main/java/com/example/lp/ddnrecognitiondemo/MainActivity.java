package com.example.lp.ddnrecognitiondemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lp.ddnrecognitiondemo.Json.ResultBean;
import com.example.lp.ddnrecognitiondemo.Json.reulstTitleBean;
import com.google.gson.Gson;
import com.isseiaoki.simplecropview.FreeCropImageView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.lp.ddnrecognitiondemo.Utils.FileUtils.file2String;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    private static final int REQUEST=1002;
    private static  String LocalTestPath="/storage/emulated/0/test.jpg";
    private static final String tokenUrl="https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=dieoDD6ARUlGTmRCBmc4xGet&client_secret=ZrsFr7gvZe7b2eNEDWcOXWGuoKWAGT9S";
    String access_token=null;
    TextView tv_time;
    LinearLayout linearLayoutLeft;
    LinearLayout linearLayouRight;
    CheckBox checkBox;
    private long lastProcessingTimeMs;
    private long startTime;
    ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Auth();
    }

    private void init() {
        findViewById(R.id.btn_takePhoto).setOnClickListener(listener);
        findViewById(R.id.btn_previewPhoto).setOnClickListener(listener);
        tv_time=findViewById(R.id.tv_time);
        checkBox=findViewById(R.id.cb_chooseModle);
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        checkBox.setChecked(true);
        linearLayoutLeft =findViewById(R.id.ll_left);
        linearLayouRight =findViewById(R.id.ll_right);
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

    CompoundButton.OnCheckedChangeListener checkedChangeListener=new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                initPicker(true);
            }else {
                initPicker(false);
                //不允许裁剪
            }
        }
    };
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_previewPhoto:
                    Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
                    startActivityForResult(intent, REQUEST);
                    linearLayoutLeft.removeAllViews();
                    linearLayouRight.removeAllViews();
                    break;
                case R.id.btn_takePhoto:
                    Intent intent2 = new Intent(MainActivity.this, ImageGridActivity.class);
                    intent2.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                    startActivityForResult(intent2, REQUEST);
                    linearLayoutLeft.removeAllViews();
                    linearLayouRight.removeAllViews();
                    break;
            }



        }
    };

    public void upImage(final String path) {
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
                    mreulstTitleBean=(reulstTitleBean)handleReulstResponse(response.body().string());
                    Log.i(TAG, "mreulstTitleBean.log_id: "+mreulstTitleBean.log_id);
                    Log.i(TAG, "mreulstTitleBean.result_num: "+mreulstTitleBean.result_num);
                    Log.i(TAG, "mreulstTitleBean.log_id: "+mreulstTitleBean.log_id);
                    final List<ResultBean> resultBeanList=mreulstTitleBean.result;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutLeft.removeAllViews();
                            linearLayouRight.removeAllViews();
                            for(int i=0;i<resultBeanList.size();i++){
                                Log.i(TAG, "resultBeanList.get(i).keyword: "+ resultBeanList.get(i).keyword);
                                Log.i(TAG, "resultBeanList.get(i).root: "+ resultBeanList.get(i).root);
                                Log.i(TAG, "resultBeanList.get(i).score: "+ resultBeanList.get(i).score);
                                TextView textView = new TextView(MainActivity.this);
                                textView.setText(
                                        "关键字："+resultBeanList.get(i).keyword+"\r\n"+
                                        "root："+resultBeanList.get(i).root+"\r\n"+
                                        "置信值："+resultBeanList.get(i).score);
                                linearLayoutLeft.addView(textView, i);

                            }
                            ImageView imageView=new ImageView(MainActivity.this);
                            imageView.setImageBitmap(BitmapFactory.decodeFile(path));//展示图片
                            linearLayouRight.addView(imageView);

                            lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                            tv_time.setText("用时： "+String.valueOf(lastProcessingTimeMs)+"ms");
                            Log.i(TAG, "lastProcessingTimeMs: "+lastProcessingTimeMs);
                        }
                    });

                   // Log.i(TAG, "onResponse: "+response.body().string());
                }
            });//回调方法的使用与get异步请求相同，此时略。

        }




    /**
     * 解析JSON数据
     * */
    public Object handleReulstResponse(String response){
            return  new Gson().fromJson(response,reulstTitleBean.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                upImage(images.get(0).path);
                startTime=SystemClock.uptimeMillis();
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * @param b 是否允许裁剪 true是，false否
     * */
    private void initPicker(boolean b) {
        imagePicker= ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setMultiMode(false);//单选照片
        imagePicker.setCrop(b);        //允许裁剪（单选才有效）
        imagePicker.setFreeCrop(b, FreeCropImageView.CropMode.FREE);//新版添加,自由裁剪，优先于setCrop
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

}


