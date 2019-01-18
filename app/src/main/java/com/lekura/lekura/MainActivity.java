package com.lekura.lekura;


import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private List<Game> gameList;
    public ArrayList<Game> mGame= new ArrayList<>();
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lekura.lekura.Chat.ChatActivity;
import com.lekura.lekura.Chat.Find_freindsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

//        gameList= new ArrayList<>();
//        adapter= new GameAdapter(this, gameList);

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);

//        prepareGames();
        getGames();

//        try {
//            Glide.with(this).load(R.drawable.fifa).into((ImageView) findViewById(R.id.backdrop));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    private void prepareGames() {
        int[] covers =new int[]{
            R.drawable.redd,
            R.drawable.fifa,
            R.drawable.battlefield,
            R.drawable.titan,
            R.drawable.blackops,
            R.drawable.nfs,
            R.drawable.mortalk,
            R.drawable.forza,
            R.drawable.destiny,
            R.drawable.fortnite,
            R.drawable.pubg
        };

//        Game a= new Game("Red dead", "android", covers[0]);
//        gameList.add(a);
//
//        a= new Game("Fifa19", "android", covers[1]);
//        gameList.add(a);
//
//        a= new Game("Battlefield V", "android", covers[2]);
//        gameList.add(a);
//
//        a= new Game("Titanfall 2", "android", covers[3]);
//        gameList.add(a);
//
//        a= new Game("Call of Duty", "android", covers[4]);
//        gameList.add(a);
//
//        a= new Game("Need for Speed", "android", covers[5]);
//        gameList.add(a);
//
//        a= new Game("Mortal Kombat", "android", covers[6]);
//        gameList.add(a);
//
//        a= new Game("Forza", "android", covers[7]);
//        gameList.add(a);
//
//        a= new Game("Destiny", "android", covers[8]);
//        gameList.add(a);
//
//        a= new Game("Fortnite", "android", covers[9]);
//        gameList.add(a);
//
//        a= new Game("Player Unknown (PUBG)", "android", covers[10]);
//        gameList.add(a);

        adapter.notifyDataSetChanged();
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar=
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange= -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0){
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow= true;
                } else if (isShow){
                    collapsingToolbar.setTitle(" ");
                    isShow= false;
                }
            }
        });
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getGames(){
        final GameService gameService= new GameService();
        gameService.findGame(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String jsonData = response.body().string();
//                    Log.v("The Games", jsonData);
                    mGame= gameService.processResults(response);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] gameNames = new String[mGame.size()];
                            for (int i=0; i< gameNames.length; i++){
                                gameNames[i] = mGame.get(i).getName();
                            }

                            GameAdapter adapter = new GameAdapter(getApplicationContext(), mGame);
                            recyclerView.setAdapter(adapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                        }
                    });
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
            }
        });
    }
}
