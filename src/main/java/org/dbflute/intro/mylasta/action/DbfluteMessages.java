package org.dbflute.intro.mylasta.action;

import org.dbflute.lastaflute.web.ruts.message.ActionMessages;
import org.dbflute.lastaflute.web.ruts.message.ActionMessage;

/**
 * The keys for message.
 * @author FreeGen
 */
public class DbfluteMessages extends ActionMessages {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /** The key of the message: <font color="red"><ul> */
    public static final String ERRORS_HEADER = "errors.header";

    /** The key of the message: </ul></font> */
    public static final String ERRORS_FOOTER = "errors.footer";

    /** The key of the message: <li> */
    public static final String ERRORS_PREFIX = "errors.prefix";

    /** The key of the message: </li> */
    public static final String ERRORS_SUFFIX = "errors.suffix";

    /** The key of the message: 必須だってば */
    public static final String CONSTRAINTS_NotBlank_MESSAGE = "constraints.NotBlank.message";

    /** The key of the message: ちょっとありえなくない？ */
    public static final String CONSTRAINTS_Email_MESSAGE = "constraints.Email.message";

    /** The key of the message: でかいかちっちゃいか must be between {min} and {max} */
    public static final String CONSTRAINTS_Length_MESSAGE = "constraints.Length.message";

    /** The key of the message: {0}は必須です。 */
    public static final String ERRORS_REQUIRED = "errors.required";

    /** The key of the message: {0}の長さが最小値({1})未満です。 */
    public static final String ERRORS_MINLENGTH = "errors.minlength";

    /** The key of the message: {0}の長さが最大値({1})を超えています。 */
    public static final String ERRORS_MAXLENGTH = "errors.maxlength";

    /** The key of the message: {0}のバイト長が最小値({1})未満です。 */
    public static final String ERRORS_MINBYTELENGTH = "errors.minbytelength";

    /** The key of the message: {0}のバイト長が最大値({1})を超えています。 */
    public static final String ERRORS_MAXBYTELENGTH = "errors.maxbytelength";

    /** The key of the message: {0}が不正です。 */
    public static final String ERRORS_INVALID = "errors.invalid";

    /** The key of the message: {0}は{1}と{2}の間でなければいけません。 */
    public static final String ERRORS_RANGE = "errors.range";

    /** The key of the message: {0}はバイトでなければいけません。 */
    public static final String ERRORS_BYTE = "errors.byte";

    /** The key of the message: {0}は短整数でなければいけません。 */
    public static final String ERRORS_SHORT = "errors.short";

    /** The key of the message: {0}は整数でなければいけません。 */
    public static final String ERRORS_INTEGER = "errors.integer";

    /** The key of the message: {0}は長整数でなければいけません。 */
    public static final String ERRORS_LONG = "errors.long";

    /** The key of the message: {0}は単精度実数でなければいけません。 */
    public static final String ERRORS_FLOAT = "errors.float";

    /** The key of the message: {0}は倍精度実数でなければいけません。 */
    public static final String ERRORS_DOUBLE = "errors.double";

    /** The key of the message: {0}は日付でなければいけません */
    public static final String ERRORS_DATE = "errors.date";

    /** The key of the message: {0}はクレジットカード番号として不正です。 */
    public static final String ERRORS_CREDITCARD = "errors.creditcard";

    /** The key of the message: {0}はメールアドレスとして不正です。 */
    public static final String ERRORS_EMAIL = "errors.email";

    /** The key of the message: {0}はURLとして不正です。 */
    public static final String ERRORS_URL = "errors.url";

    /** The key of the message: {0}は数値を入力してください */
    public static final String ERRORS_NUMBER = "errors.number";

    /** The key of the message: {0}に同一の項目が選ばれています */
    public static final String ERRORS_SAME_VALUE = "errors.same.value";

    /** The key of the message: {0}は{1}より大きい数値を入力してください */
    public static final String ERRORS_GREATER_THAN = "errors.greater.than";

    /** The key of the message: {0}のいずれかを入力してください */
    public static final String ERRORS_REQUIRED_AT_LEAST_ONE = "errors.required.at.least.one";

    /** The key of the message: {0}と{1}のどちらかを入力してください */
    public static final String ERRORS_REQUIRED_OR = "errors.required.or";

    /** The key of the message: 上限が{1}バイトなのに実際は{0}バイトだったのでアップロードできませんでした。 */
    public static final String ERRORS_UPLOAD_SIZE = "errors.upload.size";

    /** The key of the message: メールアドレスまたはパスワードが未入力です */
    public static final String ERRORS_EMPTY_LOGIN = "errors.empty.login";

    /** The key of the message: メールアドレス又はパスワードが間違っています */
    public static final String ERRORS_LOGIN_FAILURE = "errors.login.failure";

    /** The key of the message: 既に登録済みのメールアドレスです */
    public static final String ERRORS_EMAIL_EXISTS = "errors.email.exists";

    /** The key of the message: {0}はすでに登録されている{1}です */
    public static final String ERRORS_ALREADY_REGISTERED = "errors.already.registered";

    /** The key of the message: 他の人が更新した可能性があります。再度やり直してください */
    public static final String ERRORS_APP_ALREADY_DELETED = "errors.app.already.deleted";

    /** The key of the message: 他の人が更新した可能性があります。再度やり直してください */
    public static final String ERRORS_APP_ALREADY_UPDATED = "errors.app.already.updated";

    /** The key of the message: 既に登録されているデータです。再度やり直してください */
    public static final String ERRORS_APP_ALREADY_EXISTS = "errors.app.already.exists";

    /** The key of the message: 不正なアクセスがされました。再度やり直してください */
    public static final String ERRORS_APP_ILLEGAL_TRANSITION = "errors.app.illegal.transition";

    /** The key of the message: 検索キーワードを入力 */
    public static final String MESSAGES_INPUT_NOTE_KEYWORD = "messages.input.note.keyword";

    /** The key of the message: メールアドレスを入力 */
    public static final String MESSAGES_INPUT_NOTE_EMAIL = "messages.input.note.email";

    /** The key of the message: Pixy って入れてー */
    public static final String MESSAGES_INPUT_NOTE_EMAIL_OR_ACCOUNT = "messages.input.note.emailOrAccount";

    /** The key of the message: sea って入れてー */
    public static final String MESSAGES_INPUT_NOTE_PASSWORD = "messages.input.note.password";

    /** The key of the message: 例: 153-0051 */
    public static final String MESSAGES_INPUT_NOTE_ZIP_CODE = "messages.input.note.zipCode";

    /**
     * Add the created action message for the key 'errors.header' with parameters.
     * <pre>
     * message: <font color="red"><ul>
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsHeader(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_HEADER, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.footer' with parameters.
     * <pre>
     * message: </ul></font>
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsFooter(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_FOOTER, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.prefix' with parameters.
     * <pre>
     * message: <li>
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsPrefix(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_PREFIX, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.suffix' with parameters.
     * <pre>
     * message: </li>
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsSuffix(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_SUFFIX, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotBlank.message' with parameters.
     * <pre>
     * message: 必須だってば
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsNotBlankMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_NotBlank_MESSAGE, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Email.message' with parameters.
     * <pre>
     * message: ちょっとありえなくない？
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsEmailMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Email_MESSAGE, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Length.message' with parameters.
     * <pre>
     * message: でかいかちっちゃいか must be between {min} and {max}
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsLengthMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Length_MESSAGE, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.required' with parameters.
     * <pre>
     * message: {0}は必須です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsRequired(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_REQUIRED, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.minlength' with parameters.
     * <pre>
     * message: {0}の長さが最小値({1})未満です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsMinlength(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_MINLENGTH, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.maxlength' with parameters.
     * <pre>
     * message: {0}の長さが最大値({1})を超えています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsMaxlength(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_MAXLENGTH, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.minbytelength' with parameters.
     * <pre>
     * message: {0}のバイト長が最小値({1})未満です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsMinbytelength(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_MINBYTELENGTH, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.maxbytelength' with parameters.
     * <pre>
     * message: {0}のバイト長が最大値({1})を超えています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsMaxbytelength(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_MAXBYTELENGTH, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.invalid' with parameters.
     * <pre>
     * message: {0}が不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsInvalid(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_INVALID, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.range' with parameters.
     * <pre>
     * message: {0}は{1}と{2}の間でなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @param arg2 The parameter 2 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsRange(String property, String arg0, String arg1, String arg2) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_RANGE, arg0, arg1, arg2));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.byte' with parameters.
     * <pre>
     * message: {0}はバイトでなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsByte(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_BYTE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.short' with parameters.
     * <pre>
     * message: {0}は短整数でなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsShort(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_SHORT, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.integer' with parameters.
     * <pre>
     * message: {0}は整数でなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsInteger(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_INTEGER, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.long' with parameters.
     * <pre>
     * message: {0}は長整数でなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsLong(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_LONG, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.float' with parameters.
     * <pre>
     * message: {0}は単精度実数でなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsFloat(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_FLOAT, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.double' with parameters.
     * <pre>
     * message: {0}は倍精度実数でなければいけません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsDouble(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_DOUBLE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.date' with parameters.
     * <pre>
     * message: {0}は日付でなければいけません
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsDate(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_DATE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.creditcard' with parameters.
     * <pre>
     * message: {0}はクレジットカード番号として不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsCreditcard(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_CREDITCARD, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.email' with parameters.
     * <pre>
     * message: {0}はメールアドレスとして不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsEmail(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_EMAIL, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.url' with parameters.
     * <pre>
     * message: {0}はURLとして不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsUrl(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_URL, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.number' with parameters.
     * <pre>
     * message: {0}は数値を入力してください
     * comment: -------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsNumber(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_NUMBER, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.same.value' with parameters.
     * <pre>
     * message: {0}に同一の項目が選ばれています
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsSameValue(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_SAME_VALUE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.greater.than' with parameters.
     * <pre>
     * message: {0}は{1}より大きい数値を入力してください
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsGreaterThan(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_GREATER_THAN, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.required.at.least.one' with parameters.
     * <pre>
     * message: {0}のいずれかを入力してください
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsRequiredAtLeastOne(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_REQUIRED_AT_LEAST_ONE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.required.or' with parameters.
     * <pre>
     * message: {0}と{1}のどちらかを入力してください
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsRequiredOr(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_REQUIRED_OR, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.upload.size' with parameters.
     * <pre>
     * message: 上限が{1}バイトなのに実際は{0}バイトだったのでアップロードできませんでした。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsUploadSize(String property, String arg1, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_UPLOAD_SIZE, arg1, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.empty.login' with parameters.
     * <pre>
     * message: メールアドレスまたはパスワードが未入力です
     * comment: ----------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsEmptyLogin(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_EMPTY_LOGIN, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.login.failure' with parameters.
     * <pre>
     * message: メールアドレス又はパスワードが間違っています
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsLoginFailure(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_LOGIN_FAILURE, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.email.exists' with parameters.
     * <pre>
     * message: 既に登録済みのメールアドレスです
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsEmailExists(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_EMAIL_EXISTS, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.already.registered' with parameters.
     * <pre>
     * message: {0}はすでに登録されている{1}です
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter 0 for message. (NotNull)
     * @param arg1 The parameter 1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAlreadyRegistered(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_ALREADY_REGISTERED, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.already.deleted' with parameters.
     * <pre>
     * message: 他の人が更新した可能性があります。再度やり直してください
     * comment: ---------------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppAlreadyDeleted(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_ALREADY_DELETED, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.already.updated' with parameters.
     * <pre>
     * message: 他の人が更新した可能性があります。再度やり直してください
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppAlreadyUpdated(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_ALREADY_UPDATED, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.already.exists' with parameters.
     * <pre>
     * message: 既に登録されているデータです。再度やり直してください
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppAlreadyExists(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_ALREADY_EXISTS, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.illegal.transition' with parameters.
     * <pre>
     * message: 不正なアクセスがされました。再度やり直してください
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppIllegalTransition(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_ILLEGAL_TRANSITION, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'messages.input.note.keyword' with parameters.
     * <pre>
     * message: 検索キーワードを入力
     * comment: ----------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addMessagesInputNoteKeyword(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(MESSAGES_INPUT_NOTE_KEYWORD, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'messages.input.note.email' with parameters.
     * <pre>
     * message: メールアドレスを入力
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addMessagesInputNoteEmail(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(MESSAGES_INPUT_NOTE_EMAIL, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'messages.input.note.emailOrAccount' with parameters.
     * <pre>
     * message: Pixy って入れてー
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addMessagesInputNoteEmailOrAccount(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(MESSAGES_INPUT_NOTE_EMAIL_OR_ACCOUNT, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'messages.input.note.password' with parameters.
     * <pre>
     * message: sea って入れてー
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addMessagesInputNotePassword(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(MESSAGES_INPUT_NOTE_PASSWORD, (Object[])null));
        return this;
    }

    /**
     * Add the created action message for the key 'messages.input.note.zipCode' with parameters.
     * <pre>
     * message: 例: 153-0051
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addMessagesInputNoteZipCode(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(MESSAGES_INPUT_NOTE_ZIP_CODE, (Object[])null));
        return this;
    }

    /**
     * Assert the property is not null.
     * @param property The value of the property. (NotNull)
     */
    protected void assertPropertyNotNull(String property) {
        if (property == null) {
            String msg = "The argument 'property' for message should not be null.";
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * The definition of keys for labels.
     * @author FreeGen
     */
    public static interface LabelKey {

        /** The key of the label: 会員 */
        String LABELS_MEMBER = "labels.member";

        /** The key of the label: 会員ID */
        String LABELS_MEMBER_ID = "labels.memberId";

        /** The key of the label: 会員名称 */
        String LABELS_MEMBER_NAME = "labels.memberName";

        /** The key of the label: メールアドレス */
        String LABELS_EMAIL = "labels.email";

        /** The key of the label: メアドもしくはアカウント */
        String LABELS_EMAIL_OR_ACCOUNT = "labels.emailOrAccount";

        /** The key of the label: パスワード */
        String LABELS_PASSWORD = "labels.password";

        /** The key of the label: バージョンNo */
        String LABELS_VERSION_NO = "labels.versionNo";

        /** The key of the label: 一覧 */
        String LABELS_LIST = "labels.list";

        /** The key of the label: 追加 */
        String LABELS_ADD = "labels.add";

        /** The key of the label: 編集 */
        String LABELS_EDIT = "labels.edit";

        /** The key of the label: 検索 */
        String LABELS_SEARCH = "labels.search";

        /** The key of the label: 登録 */
        String LABELS_REGISTER = "labels.register";

        /** The key of the label: 更新 */
        String LABELS_UPDATE = "labels.update";

        /** The key of the label: @[labels.member]@[labels.list] */
        String LABELS_MEMBER_LIST = "labels.member.list";

        /** The key of the label: @[labels.member]@[labels.add] */
        String LABELS_MEMBER_ADD = "labels.member.add";

        /** The key of the label: @[labels.member]@[labels.edit] */
        String LABELS_MEMBER_EDIT = "labels.member.edit";

        /** The key of the label: お知らせ */
        String LABELS_HEADER_TITLE_ERROR_MESSAGE = "labels.header.title.error.message";
    }
}
