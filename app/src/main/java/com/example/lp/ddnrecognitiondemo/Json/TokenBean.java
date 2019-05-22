package com.example.lp.ddnrecognitiondemo.Json;

public class TokenBean {
    /**
     * refresh_token : 25.b03e5df8e58fda6e07dfaafcc08a47a6.315360000.1873882921.282335-16315854
     * expires_in : 2592000
     * session_key : 9mzdCPWIJ45CYK5mB0o3OgCOeGKzzEuAMibvZ+5aEJV3dvlJ0V0SfbFlpeq88Z0wiC+FwHP/s2znSYvwl4NRU/U/dBAv+w==
     * access_token : 24.31bc113bae400630a9909ffbc2856e8d.2592000.1561114921.282335-16315854
     * scope : vis-faceverify_FACE_Police public vis-classify_dishes vis-classify_car brain_all_scope vis-classify_animal vis-classify_plant brain_object_detect brain_realtime_logo brain_dish_detect brain_car_detect brain_animal_classify brain_plant_classify brain_ingredient brain_advanced_general_classify brain_custom_dish brain_poi_recognize wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower lpq_开放 cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base iop_autocar oauth_tp_app smartapp_smart_game_openapi oauth_sessionkey smartapp_swanid_verify smartapp_opensource_openapi smartapp_opensource_recapi
     * session_secret : 5c802a4d6056423d02afe5d4548a3bec
     */
    private String refresh_token;
    private int expires_in;
    private String session_key;
    private String access_token;
    private String scope;
    private String session_secret;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }
}
