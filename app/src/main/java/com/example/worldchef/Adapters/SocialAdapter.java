package com.example.worldchef.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worldchef.Models.User;
import com.example.worldchef.R;

import java.util.List;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.SocialViewHolder> {

    public List<User> users;

    public void setData(List<User> usersToAdapt) {
        this.users = usersToAdapt;

    }

    public static class SocialViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView mUsername;
        public TextView mPoints;

        public SocialViewHolder(View v) {
            super(v);
            view = v;
            mUsername = v.findViewById(R.id.user_name);
            mPoints = v.findViewById(R.id.user_points);

        }

    }

    @NonNull
    @Override
    public SocialAdapter.SocialViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user, parent, false);

        SocialAdapter.SocialViewHolder socialViewHolder = new SocialAdapter.SocialViewHolder(view);
        return socialViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SocialAdapter.SocialViewHolder holder, final int position) {

        final User currentUser = users.get(position);
        holder.mUsername.setText(currentUser.getUsername());
        holder.mPoints.setText(" " + currentUser.getPoints());


    }

    @Override
    public int getItemCount() { return users.size();}

}
