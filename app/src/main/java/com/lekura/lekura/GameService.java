package com.lekura.lekura;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameService {
    public static void findGame(Callback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.Lekura_Base_Url).newBuilder();
        String url = urlBuilder.build().toString();

        Request request= new Request.Builder()
            .url(url)
            .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public ArrayList<Game> processResults(Response response){
        ArrayList<Game> games = new ArrayList<>();

        try{
            String json1Data= response.body().string();
            Log.v("ConfirmThisForMe", json1Data);
            if (response.isSuccessful()){
                JSONArray lekuraObject = new JSONArray(json1Data);
                for (int i=0; i <lekuraObject.length(); i++){
                    JSONObject gameObject= lekuraObject.getJSONObject(i);
                    int id= gameObject.getInt("id");
                    String name = gameObject.getString("name");
                    String description = gameObject.getString("description");
                    String download = gameObject.getString("download");
                    String operating_system = gameObject.getString("operating_system");
                    String CPU = gameObject.getString("CPU");
                    String RAM = gameObject.getString("RAM");
                    String HDDspace = gameObject.getString("HDDspace");
                    String language = gameObject.getString("language");
                    String screenshot1 = gameObject.getString("screenshot1");
                    String screenshot2 = gameObject.getString("screenshot2");
                    String screenshot3 = gameObject.getString("screenshot3");
                    String game_cover = gameObject.getString("game_cover");
                    String trailer = gameObject.getString("trailer");
                    String post_date = gameObject.getString("post_date");
                    String payment = gameObject.getString("payment");
                    Integer developer = gameObject.getInt("developer");
                    ArrayList<String> categories = new ArrayList<>();
                    JSONArray categoriesJSON = gameObject.getJSONArray("categories");
                    for (int y=0; y < categoriesJSON.length(); y++){
                        categories.add(categoriesJSON.get(y).toString());
                    }
                    ArrayList<String> platforms= new ArrayList<>();
                    JSONArray platformsJSON = gameObject.getJSONArray("platforms");
                    

                    Log.v("testApi", name);

                    Game game= new Game(name, description, download, game_cover);
                    games.add(game);
                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return games;
    }

}
