package ru.alfabank.alfamir.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.data.dto.old_trash.models.ModelCommunityType;

/**
 * Created by U_M0WY5 on 29.11.2017.
 */

public class AdapterSuggestions extends RecyclerView.Adapter<AdapterSuggestions.ViewHolder> {

    private List<ModelCommunityType> communitiesList;
    private SuggestionsListener suggestionsListener;

    public AdapterSuggestions (List<ModelCommunityType> communitiesList, SuggestionsListener suggestionsListener) {
        this.suggestionsListener = suggestionsListener;
        this.communitiesList = communitiesList;

    }

    @Override
    public AdapterSuggestions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publish_community_suggestion_viewholder, parent, false);
        return new ViewHolder(view, suggestionsListener);
    }

    @Override
    public void onBindViewHolder(AdapterSuggestions.ViewHolder holder, int position) {
        holder.tvName.setText(communitiesList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return communitiesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public ViewHolder(View itemView, SuggestionsListener suggestionsListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.rc_item_community_suggestion_name);
            itemView.setOnClickListener((View v) ->{
                suggestionsListener.onSuggestionsClicked(getAdapterPosition());
            });
        }
    }

    public interface SuggestionsListener {
        void onSuggestionsClicked(int position);
    }

}
