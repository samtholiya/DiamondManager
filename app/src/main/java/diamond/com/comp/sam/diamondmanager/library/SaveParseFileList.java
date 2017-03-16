package diamond.com.comp.sam.diamondmanager.library;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.vlk.multimager.utils.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import diamond.com.comp.sam.diamondmanager.coordinator.OnFileUploadListener;

/**
 * Created by shubh on 01-03-2017.
 */

public class SaveParseFileList implements SaveCallback {

    private List<Image> mImages;
    private ArrayList<ParseFile> parseFiles;
    private int index;
    private OnFileUploadListener mOnFileUploadListener;

    public SaveParseFileList(@NonNull List<Image> fileList) {
        mImages = fileList;
        index = 0;
        parseFiles = new ArrayList<>();
    }

    public void upload(){
        this.done(null);
    }

    @Override
    public void done(ParseException e) {
        if (e != null) {
            Log.d(getClass().getName(), e.getLocalizedMessage());
            mOnFileUploadListener.onFileUploadFailed(mImages.get(index).uri);
            return;
        }
        if (index < mImages.size()) {

            ParseFile parseFile =  new ParseFile(new File((mImages.get(index++).imagePath)));
            parseFiles.add(parseFile);
            Log.d(getClass().getName(),String.valueOf(index) + "Uploaded");
            parseFile.saveInBackground(this);
        } else {
            mOnFileUploadListener.onUploadFinished(parseFiles);
        }
    }

    public void addOnFileUploadFailedListener(OnFileUploadListener onFileUploadListener) {
        mOnFileUploadListener = onFileUploadListener;
    }
}
