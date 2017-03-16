package diamond.com.comp.sam.diamondmanager.library;

import android.webkit.MimeTypeMap;

/**
 * Created by shubh on 26-02-2017.
 */

public class FileLibrary {

    public static String getMimeType(String filePath){
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        return myMime.getMimeTypeFromExtension(fileExt(filePath).substring(1));
    }

    private static String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

}
