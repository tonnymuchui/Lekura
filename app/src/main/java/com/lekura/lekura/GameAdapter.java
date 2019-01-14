package com.lekura.lekura;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    private Context mContext;
    private List<Game> gameList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title , count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view){
            super(view);
            title= (TextView) view.findViewById(R.id.title);
            count= (TextView) view.findViewById(R.id.count);
            thumbnail= (ImageView) view.findViewById(R.id.thumbnail);
            overflow= (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public GameAdapter (Context mContext, List<Game> gameList){
        this.mContext = mContext;
        this.gameList = gameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){
        Game game = gameList.get(position);
        holder.title.setText(game.getName());
        holder.count.setText(game.getPlatform());

        Glide.with(mContext).load(game.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_game, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    @Override
    public int getItemCount(){
        return gameList.size();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener(){}

        @Override
        public boolean onMenuItemClick(MenuItem menuItem){
            switch (menuItem.getItemId()){
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "downloading...", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Game info", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}
