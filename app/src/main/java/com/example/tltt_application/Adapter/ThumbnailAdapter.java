package com.example.tltt_application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tltt_application.R;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {

    private List<String> imageUrls;
    private int selectedPosition = 0;
    private OnThumbnailClickListener listener;

    public ThumbnailAdapter(List<String> imageUrls, OnThumbnailClickListener listener) {
        this.imageUrls = imageUrls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_image_car, parent, false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.rounded_image_background)
                .into(holder.thumbnailImage);

        // Hiển thị viền khi ảnh được chọn
        holder.thumbnailImage.setAlpha(position == selectedPosition ? 1.0f : 0.5f);

        // Xử lý nhấn vào ảnh bé
        holder.itemView.setOnClickListener(v -> {
            setSelectedPosition(position);
            listener.onThumbnailClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousPosition);
        notifyItemChanged(selectedPosition);
    }

    static class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImage;

        ThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.thumbnail_image);
        }
    }

    public interface OnThumbnailClickListener {
        void onThumbnailClick(int position);
    }
}