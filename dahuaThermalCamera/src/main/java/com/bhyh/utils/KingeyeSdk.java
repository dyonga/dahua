package com.bhyh.utils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/**
 *    密匙
 * @author jinshan
 *
 */
public class KingeyeSdk {

    public static void main(String[] args) throws Exception {
    	String access_key = "ak from www.ksyun.com";
        String secret_key = "sk from www.ksyun.com";
        String postData = "{\n    \"guard_id\": \"1547778774476511751\",\n    \"image_url\": \"https://ks3-cn-beijing.ksyun.com/imgdb/chenshuibian.jpeg\"\n}";
        System.out.println(request("kir.api.ksyun.com", "POST", "kir",
                "ClassifyImageGuard", "2019-01-18", access_key, secret_key, postData));
    }
    public static String request(
            String host,
            String method,
            String service,
            String action,
            String version,
            String access_key,
             String secret_key,
             String postData) throws Exception {
        String contenttype = "application/json";
        String region = "cn-beijing-6";
        String endpoint = "http://" + host;
        String request_parameters = "Action=" + action + "&Version=" + version;

        Date t = new Date();
        SimpleDateFormat timeFormater = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        timeFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        String amzdate = timeFormater.format(t);
//        String amzdate = "20171211T123249Z";

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
        timeFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        String datestamp = dateFormater.format(t);
//        String datestamp = "20171211";

        String canonical_uri = "/";
        String canonical_querystring = request_parameters;
        String canonical_headers = "content-type:" + contenttype + "\n" + "host:" + host + "\n" + "x-amz-date:" + amzdate + "\n";
        String signed_headers = "content-type;host;x-amz-date";

        String payload_hash = toHex(hash(postData));

        String canonical_request = method + '\n' + canonical_uri + '\n' + canonical_querystring + '\n' + canonical_headers + '\n' + signed_headers + '\n' + payload_hash;

        String algorithm = "AWS4-HMAC-SHA256";
        String credential_scope = datestamp + '/' + region + '/' + service + '/' + "aws4_request";//20171129/cn-beijing-6/kir/aws4_request
        /*
         * AWS4-HMAC-SHA256
           20171129T100303Z
           20171129/cn-beijing-6/kir/aws4_request
           65e3a839cb411c645880bb07d3ff3e0a3a1ea4696d0c2e2b9de507bf63e79883
         * */
        String string_to_sign = algorithm + '\n' +  amzdate + '\n' +  credential_scope + '\n' +  toHex(hash(canonical_request));
       
        byte[] signing_key = getSignatureKey(secret_key, datestamp, region, service);
        String signature = toHex(hmacSHA256(string_to_sign, signing_key));
     
        String authorization_header = algorithm + ' ' + "Credential=" + access_key + '/' + credential_scope + ", " +  "SignedHeaders=" + signed_headers + ", " + "Signature=" + signature;

        String request_url = endpoint + '?' + canonical_querystring;
        HttpURLConnection connection = (HttpURLConnection)new URL(request_url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("x-amz-date", amzdate);
        connection.setRequestProperty("Authorization", authorization_header);
        connection.setRequestProperty("Content-Type", contenttype);
        connection.setDoOutput(true);

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(postData);
        wr.flush();

        try {
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                String content = "";
                String line = null;
                while ((line = reader.readLine()) != null) {
                    content += line;
                }
                reader.close();
                return content;
            } else {
                System.out.println(connection.getResponseMessage() + connection.getResponseCode());
            }
        } catch (Exception e) {

        }
        return null;
    }
    public static String toHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    static byte[] hash(String text) throws Exception {
        if (text == null)
            text = "";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("UTF8"));
        return md.digest();
    }

    static byte[] hmacSHA256(String data, byte[] key) throws Exception {
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = hmacSHA256(dateStamp, kSecret);
        byte[] kRegion = hmacSHA256(regionName, kDate);
        byte[] kService = hmacSHA256(serviceName, kRegion);
        byte[] kSigning = hmacSHA256("aws4_request", kService);
        return kSigning;
    }
}