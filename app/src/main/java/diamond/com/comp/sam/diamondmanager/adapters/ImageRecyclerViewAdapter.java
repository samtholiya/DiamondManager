package diamond.com.comp.sam.diamondmanager.adapters;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.coordinator.OnImageListFragmentInteractionListener;
import diamond.com.comp.sam.diamondmanager.coordinator.OnListFragmentInteractionListener;

import static diamond.com.comp.sam.diamondmanager.App.HOST_ADDRESS;
import static diamond.com.comp.sam.diamondmanager.App.HOST_PATH;

/**
 * {@link RecyclerView.Adapter} that can display a Image and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    public final List<Uri> mValues;
    private final OnImageListFragmentInteractionListener mListener;
    private Fragment mFragment;

    public ImageRecyclerViewAdapter(Fragment fragment, OnImageListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mFragment = fragment;
        mListener = listener;
    }

    public void addImage(Uri imageUri, int position) {
        if (position < mValues.size()) {
            mValues.add(position, imageUri);
            notifyItemInserted(position);
        }
    }

    public void addImage(Uri image) {
        mValues.add(image);
        notifyItemChanged(mValues.size() - 1);
    }


    public void addImages(List<Uri> imagesUri) {
        int start = mValues.size();
        mValues.addAll(imagesUri);
        notifyItemRangeInserted(start, mValues.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ViewHolder(view);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //Load Image into view
        //holder.mImageView.setImageURI(holder.mItem);
        Log.d(getClass().getName(), "{Path} :" + holder.mItem.getPath());

        if (holder.mItem.getPath().contains(HOST_PATH)) {
            Log.d(getClass().getName(), "internet url");
            Glide
                    .with(mFragment)
                    .load(HOST_ADDRESS + holder.mItem.getPath())
                    .crossFade()
                    .into(holder.mImageView);
        } else {
            Glide
                    .with(mFragment)
                    .load(Uri.fromFile(new File(holder.mItem.getPath())))
                    .crossFade()
                    .into(holder.mImageView);
        }
        //holder.mImageView.setImageBitmap(decodeSampledBitmapFromFile(holder.mItem.getPath(), 100, 100));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public Uri mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_view);
        }

    }
}
