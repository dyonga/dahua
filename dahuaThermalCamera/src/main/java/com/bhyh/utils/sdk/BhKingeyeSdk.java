package com.bhyh.utils.sdk;

import com.bhyh.utils.KingeyeSdk;

/**
 *    密匙
 * @author david_lin
 *
 */
public class BhKingeyeSdk extends KingeyeSdk{
	
    public static void main(String[] args) throws Exception {
    	String access_key = "AKLTTh-j56W0TyuYyvcrtj54hQ";
        String secret_key = "OASIKMtolLE74pr34/16MjTNTRSeLvxJBoImIlcvHn5NYEgxIQgAtwhsUmA93zhMRQ==";
        String postData = "{\n    \"guard_id\": \"1577327396146478400\",\n    \"image_url\": \"http://183.250.155.231:8099/20200110/a09ea8d0925f4a5ea1850e8048b13f8d.jpeg\"\n}";
        System.out.println(request("kir.api.ksyun.com", "POST", "kir",
                "ClassifyImageGuard", "2019-01-18", access_key, secret_key, postData));
    }
}

//
//http://183.250.155.231:8099/20200110/a09ea8d0925f4a5ea1850e8048b13f8d.jpeg