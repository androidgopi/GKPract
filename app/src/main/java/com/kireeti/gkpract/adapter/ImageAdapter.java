package com.kireeti.gkpract.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kireeti.gkpract.GatePassActivity;
import com.kireeti.gkpract.NotThereActivity;
import com.kireeti.gkpract.Quality_FindingActivity;
import com.kireeti.gkpract.R;
import com.kireeti.gkpract.Receiving_Photo_UploadActivity;
import com.kireeti.gkpract.Shipping_Photo_UploadActivity;
import com.kireeti.gkpract.Utilities;
import com.kireeti.gkpract.customClass.TouchImageView;
import com.kireeti.gkpract.models.ImageFile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context mContext;
    List<ImageFile> notesList;
    boolean fromResponse;
    private String note;
    int selectedPosition = -1;
    Receiving_Photo_UploadActivity callBack;
    Quality_FindingActivity quality_callBack;
    Shipping_Photo_UploadActivity shipping_callBack;
    private TouchImageView touchImage;
    private Dialog toucchimage_Dialog;
    private ImageView zoom_remove_photo;
    private TextView zoom_comment_text;
    private NotThereActivity notThere_callBack;
    private GatePassActivity getPass_callBack;


    public ImageAdapter(Context mContext, List<ImageFile> notesList, boolean fromResponse) {
        this.mContext = mContext;
        this.notesList = notesList;
        this.fromResponse = fromResponse;
    }

    @SuppressLint("NewApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_view, viewGroup, false);
        return new ViewHolder(view, this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ImageFile mModel = notesList.get(position);


        if (mModel.getFilePath() != null && !mModel.getFilePath().equalsIgnoreCase("")) {
            if (mModel.getFilePath().contains("http")) {

                if (position == notesList.size() - 1) {
                    try {
                        if (mModel.getFileName().contains("_D")) {

                            Utilities.Resend_button_count = Integer.valueOf(mModel.getFileName().split("D")[1].split("\\.")[0]);
                            Utilities.LastPhotoNumber = Integer.valueOf(mModel.getFileName().split("_D")[0].split("_")[2]);

                        } else {
                            Utilities.LastPhotoNumber = Integer.valueOf(mModel.getFileName().split("\\.")[0].split("_")[2]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Picasso.with(mContext)
                        .load(mModel.getFilePath())
                        .placeholder(R.drawable.ic_placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(500, 500)
                        .into(holder.adapter_image);

            } else {

                Uri url = Uri.fromFile(new File(mModel.getFilePath()));

                Picasso.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(200, 150)
                        .into(holder.adapter_image);

            }
        }
        holder.file_comment.setText(mModel.getComments());
        holder.remove_photo.setTag(mModel);
        holder.remove_photo.setContentDescription(String.valueOf(position));
        holder.file_name.setText(mModel.getFileName().replace("@@", " "));
        holder.remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Receiving_Photo_UploadActivity) {
                    callBack.deletePhoto(Integer.parseInt(view.getContentDescription().toString()), (ImageFile) view.getTag());
                } else if(mContext instanceof Quality_FindingActivity){
                    quality_callBack.deletePhoto(Integer.parseInt(view.getContentDescription().toString()), (ImageFile) view.getTag());
                }else if (mContext instanceof GatePassActivity){
                    getPass_callBack.deletePhoto(Integer.parseInt(view.getContentDescription().toString()), (ImageFile) view.getTag());
                }else if(mContext instanceof Shipping_Photo_UploadActivity) {
                    shipping_callBack.deletePhoto(Integer.parseInt(view.getContentDescription().toString()), (ImageFile) view.getTag());
                }else {
                    notThere_callBack.deletePhoto(Integer.parseInt(view.getContentDescription().toString()), (ImageFile) view.getTag());

                }
            }
        });
        holder.container.setTag(mModel);
        holder.container.setContentDescription(String.valueOf(position));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomPhoto(mModel.getFilePath(), mModel.getComments());
            }
        });
    }

    private void zoomPhoto(String image, String comments) {

        Context context = mContext.getApplicationContext();
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.imagefullscreen_qty_find, null);
        zoom_remove_photo = (ImageView) view.findViewById(R.id.zoom_remove_photo);
        zoom_comment_text = (TextView) view.findViewById(R.id.zoom_comment_text);
        touchImage = (TouchImageView) view.findViewById(R.id.touchImage);
        toucchimage_Dialog = new Dialog(mContext, R.style.animationdialog);
        toucchimage_Dialog.setContentView(view);
        toucchimage_Dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(toucchimage_Dialog.getWindow().getAttributes());
        lp.width = (int) (Utilities.screenWidth * 0.90);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//(int) (Utilities.screenHeight * 0.85);
        lp.gravity = Gravity.CENTER_VERTICAL;
        toucchimage_Dialog.getWindow().setAttributes(lp);

        if (comments != null) {
            zoom_comment_text.setVisibility(View.VISIBLE);
            zoom_comment_text.setText("Comment: " + comments);
        } else {
            zoom_comment_text.setVisibility(View.GONE);
        }

        if (image.contains("https")) {
            Picasso.with(mContext)
                    .load(image)
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(touchImage);
        } else {
            Picasso.with(mContext)
                    .load(new File(image))
                    .placeholder(R.drawable.ic_placeholder)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(touchImage);
        }

        zoom_remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toucchimage_Dialog.dismiss();
            }
        });

        toucchimage_Dialog.show();
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setCallBack(Receiving_Photo_UploadActivity callback) {
        this.callBack = callback;
    }

    public void quality_setCallBack(Quality_FindingActivity qualitycallback) {
        this.quality_callBack = qualitycallback;
    }

    public void setCallBack(NotThereActivity notThereActivity) {
        this.notThere_callBack=notThereActivity;
    }

    public void setCallBack(GatePassActivity callBack) {
        this.getPass_callBack = callBack;
    }

    public void setCallBack(Shipping_Photo_UploadActivity shipping_callBack) {
        this.shipping_callBack = shipping_callBack;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout container;
        private ImageAdapter parent;
        ImageView adapter_image, remove_photo;
        TextView file_name;
        TextView file_comment;

        public ViewHolder(View view, ImageAdapter parent) {
            super(view);
            this.parent = parent;
            container = (RelativeLayout) view.findViewById(R.id.container);
            adapter_image = (ImageView) view.findViewById(R.id.adapter_image);
            remove_photo = (ImageView) view.findViewById(R.id.remove_photo);
            file_name = (TextView) view.findViewById(R.id.file_name);
            file_comment = (TextView) view.findViewById(R.id.file_comment);
        }
    }

}
