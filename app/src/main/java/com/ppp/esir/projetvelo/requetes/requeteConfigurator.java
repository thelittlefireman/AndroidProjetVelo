package com.ppp.esir.projetvelo.requetes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by thoma on 02/05/2016.
 */
public class RequeteConfigurator {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String baseURL = "";

    public static String sendRequeteJson(RequeteConfigurator.ApiRequete apiRequete, String json) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(baseURL + apiRequete)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return "";
        }

    }

    public static String sendRequetePost(RequeteConfigurator.ApiRequete apiRequete, HashMap<String, String> postData) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        Iterator it = postData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            formBody.add((String) pair.getKey(), (String) pair.getValue());
        }

        RequestBody requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(baseURL + apiRequete)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return "";
        }
    }

    public enum ApiRequete {
        AUTHENTIFICATION("us"),
        sendSpeed("TWO");

        private final String text;

        /**
         * @param text
         */
        private ApiRequete(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
