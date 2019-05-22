package com.example.lp.ddnrecognitiondemo.Json;

import java.util.List;

public class reulstTitleBean {

    /**
     * log_id : 1822404971859334102
     * result_num : 5
     * result : [{"score":0.862175,"root":"非自然图像-病理图","keyword":"手掌"},{"score":0.64284,"root":"人物-人物特写","keyword":"手"},{"score":0.3578,"root":"人物-人物特写","keyword":"手指"},{"score":0.176687,"root":"病理图-器官病理图","keyword":"荨麻疹"},{"score":1.09E-4,"root":"商品-首饰","keyword":"戒指"}]
     */

    public long log_id;
    public int result_num;
    public List<ResultBean> result;

}
