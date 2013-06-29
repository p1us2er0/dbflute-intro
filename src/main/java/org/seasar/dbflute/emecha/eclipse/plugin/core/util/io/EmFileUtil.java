package org.seasar.dbflute.emecha.eclipse.plugin.core.util.io;

import java.io.File;

/**
 * @author jflute
 * @since 0.1.0 (2007/08/11 Saturday)
 */
public class EmFileUtil {

    public static String removeFirstFileSeparatorIfNeeds(String path, String fileSeparator) {
        if (path.startsWith(fileSeparator)) {
            path = path.substring(fileSeparator.length());
        }
        return path;
    }

    /**
     * Delete file.
     * @param filename File name. (NotNull)
     */
    public static void deleteFile(String filename) {
        new File(filename).delete();
    }
}
