package org.dbflute.intro.mylasta.action;

import org.dbflute.intro.mylasta.action.DbfluteMessages;
import org.dbflute.lastaflute.web.ruts.message.ActionMessage;

/**
 * The keys for message.
 * @author FreeGen
 */
public class DbfluteIntroMessages extends DbfluteMessages {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /** The key of the message: e.g. 290-9753 */
    public static final String MESSAGES_ZIP_CODE_INPUT_EXAMPLE = "messages.zipCode.input.example";

    /**
     * Add the created action message for the key 'errors.empty.login' with parameters.
     * <pre>
     * message: Mail Address and Password is very poor
     * comment: @Override ----------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public DbfluteIntroMessages addErrorsEmptyLogin(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_EMPTY_LOGIN, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.not.login' with parameters.
     * <pre>
     * message: Mail Address and Password is very fun
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public DbfluteIntroMessages addErrorsNotLogin(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_NOT_LOGIN, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'messages.zipCode.input.example' with parameters.
     * <pre>
     * message: e.g. 290-9753
     * comment: ----------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteIntroMessages addMessagesZipCodeInputExample(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(MESSAGES_ZIP_CODE_INPUT_EXAMPLE, (Object[])null));
        return this;
    }

    /**
     * The definition of keys for labels.
     * @author FreeGen
     */
    public static interface LabelKey extends DbfluteMessages.LabelKey {

        /** The key of the label: ZIP CODE */
        String LABELS_ZIP_CODE = "labels.zipCode";
    }
}
