package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.DbfluteMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * データベース接続例外クラス。
 * @author p1us2er0
 */
public class DatabaseConnectionException extends LaApplicationException {

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     */
    public DatabaseConnectionException(String debugMsg) {
        super(debugMsg);
        saveMessage(DbfluteMessages.ERRORS_APP_DATABASE_CONNECTION, new Object[0]);
    }

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     */
    public DatabaseConnectionException(String debugMsg, Throwable cause) {
        super(debugMsg, cause);
        saveMessage(DbfluteMessages.ERRORS_APP_DATABASE_CONNECTION, new Object[0]);
    }
}
