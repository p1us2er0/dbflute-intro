package org.seasar.dbflute.emecha.eclipse.plugin.core.util.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.seasar.dbflute.emecha.eclipse.plugin.core.exception.EmExceptionHandler;

/**
 * 
 * @author jflute
 * @since 0.1.0 (2007/09/19 Wednesday)
 */
public class EmZipInputStreamUtil {

    public static ZipInputStream createZipInputStream(String zipFilename) {
        try {
            return new ZipInputStream(new FileInputStream(zipFilename));
        } catch (FileNotFoundException e) {
            String msg = "new FileInputStream(zipFilename) threw the " + e.getClass().getSimpleName() + ":";
            msg = msg + " zipFilename=" + zipFilename;
            EmExceptionHandler.throwAsPluginException(msg, e);
            return null;
        }
    }

    /**
     * @param zipIn Zip input stream. (NotNull)
     * @param outputDirectory Output directory. (NotNull)
     */
    public static void extractAndClose(ZipInputStream zipIn, String outputDirectory) {
        makeDirectory(outputDirectory);
        ZipEntry entry = null;
        try {
            while ((entry = zipIn.getNextEntry()) != null) {
                final String entryName = entry.getName();
                if (entry.isDirectory()) {
                    makeDirectory(outputDirectory, entryName);
                } else {
                    final String parentEntryName = new File(entryName).getParent();
                    if (parentEntryName != null) {
                        makeDirectory(outputDirectory, parentEntryName);
                    }
                    final String filePath = buildDirectoryPath(outputDirectory, entryName);
                    final FileOutputStream out = new FileOutputStream(filePath);
                    final byte[] buf = new byte[1024];
                    int size = 0;
                    while ((size = zipIn.read(buf)) != -1) {
                        out.write(buf, 0, size);
                    }
                    out.close();
                }
                zipIn.closeEntry();
            }
            zipIn.close();
        } catch (IOException e) {
            String msg = EmZipInputStreamUtil.class.getSimpleName() + "#extractAndClose() threw the "
                    + e.getClass().getSimpleName() + ":";
            msg = msg + " outputDirectory=" + outputDirectory;
            EmExceptionHandler.throwAsPluginException(msg, e);
            return;
        }
    }

    private static void makeDirectory(String outputDirectory) {
        new File(outputDirectory).mkdirs();
    }

    private static void makeDirectory(String outputDirectory, String directoryName) {
        new File(buildDirectoryPath(outputDirectory, directoryName)).mkdirs();
    }

    private static String buildDirectoryPath(String outputDirectory, String name) {
        return outputDirectory + "/" + name;
    }
}
