package diamond.com.comp.sam.diamondmanager.coordinator;

import android.net.Uri;

import com.parse.ParseFile;

import java.util.ArrayList;

/**
 * Created by shubh on 01-03-2017.
 */
public interface OnFileUploadListener {
    void onFileUploadFailed(Uri uri);

    void onUploadFinished(ArrayList<ParseFile> parseFiles);
}
