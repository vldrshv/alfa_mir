package ru.alfabank.alfamir.ui.adapters;

import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import ru.alfabank.alfamir.R;

import java.util.LinkedList;
import java.util.List;


public class AdapterUploadPicture extends RecyclerView.Adapter<AdapterUploadPicture.ViewHolder> {

    private List<Uri> uriList;
    private ViewHolder.UploadListener listener;

    public AdapterUploadPicture(ViewHolder.UploadListener listener) {
        this.uriList = new LinkedList<>();
        uriList.add(new Uri.Builder().build());
        this.listener = listener;
    }

    public void addPicture(Uri resultUri) {
        uriList.add(1, resultUri);
        notifyItemInserted(1);
    }

    public void deletePicture(int position) {
        uriList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_picture_viewholder, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position != 0) {
            holder.image.setImageURI(uriList.get(position));
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        UploadListener listener;
        ImageView image;
        ImageView delete;

        public ViewHolder(View itemView, UploadListener listener) {
            super(itemView);
            mainView = itemView;
            this.listener = listener;
            image = itemView.findViewById(R.id.activity_create_post_image_photo);
            delete = itemView.findViewById(R.id.rc_item_upload_photo_delete);
            setListeners();
        }

        private void setListeners() {
            mainView.setOnClickListener((View v) -> {
                if (getAdapterPosition() == 0) {
                    listener.uploadPictureItemClicked();
                }
            });
            delete.setOnClickListener((View v) -> {
                listener.deletePictureItemClicked(getAdapterPosition());
            });
        }

        public interface UploadListener {
            void uploadPictureItemClicked();

            void deletePictureItemClicked(int position);
        }
    }
}
