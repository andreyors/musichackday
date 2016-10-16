package com.mio.musicitout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TrainingListAdapter extends RecyclerView.Adapter<TrainingListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;

    public TrainingListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trainings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Training Training = new TrainingData().trainingList().get(position);

        holder.trainingName.setText(Training.name);
        Picasso.with(mContext).load(Training.getImageResourceId(mContext)).into(holder.trainingImage);

        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), Training.getImageResourceId(mContext));

        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int mutedLight = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                holder.trainingNameHolder.setBackgroundColor(mutedLight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return new TrainingData().trainingList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout trainingHolder;
        public LinearLayout trainingNameHolder;
        public TextView trainingName;
        public ImageView trainingImage;

        public ViewHolder(View itemView) {
            super(itemView);
            trainingHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            trainingName = (TextView) itemView.findViewById(R.id.trainingName);
            trainingNameHolder = (LinearLayout) itemView.findViewById(R.id.trainingNameHolder);
            trainingImage = (ImageView) itemView.findViewById(R.id.trainingImage);
            trainingHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
