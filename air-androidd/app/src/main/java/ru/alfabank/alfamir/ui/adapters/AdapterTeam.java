package ru.alfabank.alfamir.ui.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelFavorite;
import ru.alfabank.alfamir.data.source.repositories.old_trash.ProfilePhotoRepository;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.utility.callbacks.InflatingProfilePic;
import ru.alfabank.alfamir.utility.static_utilities.BackGroundGenerator;
import ru.alfabank.alfamir.utility.static_utilities.InitialsMaker;
import ru.alfabank.alfamir.utility.static_utilities.LinkHandler;

import java.util.List;

public class AdapterTeam extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<ModelFavorite> favoriteItems;
    private AdapterTeam.ViewHolder viewHolder;
    private static App app;
    private ProfilePhotoRepository profilePhotoRepository;


    public AdapterTeam(List<ModelFavorite> favoriteItems, App app){
        this.favoriteItems = favoriteItems;
        this.app = app;
        profilePhotoRepository = app.getProfilePhotoRepository();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0: {
                View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_boos_viewholder, parent, false);
                AdapterTeam.ViewHolder viewHolder = new AdapterTeam.ViewHolder(rowView);
                return viewHolder;
            }
            case 1: {
                View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_person_viewholder, parent, false);
                AdapterTeam.ViewHolder viewHolder = new AdapterTeam.ViewHolder(rowView);
                return viewHolder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(favoriteItems.get(position)!=null){
            viewHolder = (AdapterTeam.ViewHolder) holder;
            viewHolder.name.setText(favoriteItems.get(position).getFullname());
            viewHolder.title.setText(favoriteItems.get(position).getJobTitle());

            viewHolder.profileIcon.setImageDrawable(null);

            if (favoriteItems.get(position).getPhotobase64()!=null&&!favoriteItems.get(position).getPhotobase64().equals("")){
                viewHolder.tvInitials.setText(null);
                viewHolder.tvInitials.setVisibility(View.GONE);
                profilePhotoRepository.inflateProfileAnimated(LinkHandler.INSTANCE.getPhotoLink(favoriteItems.get(position).getPhotobase64()), viewHolder, position);
            } else {
                viewHolder.tvInitials.setVisibility(View.VISIBLE);
                String initials = InitialsMaker.formInitials(favoriteItems.get(position).getFullname());
                viewHolder.tvInitials.setText(initials);
                Drawable drawable = viewHolder.mainView.getContext().getResources().getDrawable(R.drawable.background_profile_empty);
                drawable.setColorFilter(Color.parseColor("#"+ BackGroundGenerator.getBackgroundAvatarColor(initials)),
                        PorterDuff.Mode.SRC_IN);
                viewHolder.profileIcon.setImageDrawable(drawable);

            }
        }
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements InflatingProfilePic {
        TextView name;
        TextView title;
        TextView tvInitials;
        ImageView profileIcon;
        View mainView;
        public ViewHolder(View v) {
            super(v);
            mainView = v;
            name = v.findViewById(R.id.item_favorites_name);
            title = v.findViewById(R.id.item_favorites_position);
            tvInitials = v.findViewById(R.id.item_favorites_tv_profile_initials);
            profileIcon = v.findViewById(R.id.item_favorites_image_profile_icon);
            setListeners();
        }

        private void setListeners(){
            mainView.setOnClickListener(v -> {
                Intent intent = new Intent(app, ProfileActivity.class);
                intent.putExtra("id", favoriteItems.get(getAdapterPosition()).getId());
                mainView.getContext().startActivity(intent);
            });
        }

        @Override
        public void inflateProfilePic(RoundedBitmapDrawable img, int position) {
            if(getAdapterPosition()==position) {
                profileIcon.setImageDrawable(img);
            }
        }

        @Override
        public void inflateProfilePicAnimated(RoundedBitmapDrawable img, int position) {
            if(getAdapterPosition()==position) {
                profileIcon.setImageDrawable(img);
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(mainView.getContext(), R.anim.fadein);
                profileIcon.startAnimation(myFadeInAnimation);
            }
        }
    }

}
