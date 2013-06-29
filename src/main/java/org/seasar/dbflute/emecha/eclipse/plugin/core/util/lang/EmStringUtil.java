package org.seasar.dbflute.emecha.eclipse.plugin.core.util.lang;

/**
 * 
 * @author jflute
 * @since 0.1.0 (2007/09/19 Wednesday)
 */
public class EmStringUtil {

    public static final String replace(String text, String fromText, String toText) {
        if (text == null || fromText == null || toText == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(100);
        int pos = 0;
        int pos2 = 0;
        while (true) {
            pos = text.indexOf(fromText, pos2);
            if (pos == 0) {
                buf.append(toText);
                pos2 = fromText.length();
            } else if (pos > 0) {
                buf.append(text.substring(pos2, pos));
                buf.append(toText);
                pos2 = pos + fromText.length();
            } else {
                buf.append(text.substring(pos2));
                break;
            }
        }
        return buf.toString();
    }
}
