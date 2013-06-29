package org.seasar.dbflute.emecha.eclipse.plugin.core.util.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.seasar.dbflute.emecha.eclipse.plugin.core.exception.EmExceptionHandler;
import org.seasar.dbflute.emecha.eclipse.plugin.core.util.io.EmInputStreamUtil;

/**
 * 
 * @author jflute
 * @since 0.1.0 (2007/09/19 Wednesday)
 */
public class EmURLUtil {

    public static URL createURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            String msg = "new URL(urlString) threw the IO exception: urlString=" + urlString;
            EmExceptionHandler.throwAsPluginException(msg, e);
            return null;
        }
    }

    public static InputStream openStream(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException e) {
            String msg = "It threw the IO exception: url=" + url;
            throw new IllegalStateException(msg, e);
        }
    }

    public static void makeFileAndClose(URL url, String outputFilename) {
        InputStream in;
        try {
            in = url.openStream();
        } catch (IOException e) {
            String msg = EmURLUtil.class.getSimpleName() + "#copy() threw the IO exception!";
            EmExceptionHandler.throwAsPluginException(msg, e);
            return;
        }
        EmInputStreamUtil.makeFileAndClose(in, outputFilename);
    }

    public static byte[] toBytes(InputStream in) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            copy(in, out);
        } catch (IOException e) {
            String msg = EmURLUtil.class.getSimpleName() + "#copy() threw the IO exception!";
            EmExceptionHandler.throwAsPluginException(msg, e);
            return null;
        }
        return out.toByteArray();
    }

    protected static void copy(InputStream in, OutputStream out) throws IOException {
        final byte[] buff = new byte[256];
        int len = in.read(buff);
        while (len != -1 && len != 0) {
            out.write(buff, 0, len);
            len = in.read(buff);
        }
    }
}
