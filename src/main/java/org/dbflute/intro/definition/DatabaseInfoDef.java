package org.dbflute.intro.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jflute
 * @author ecode
 * @since 0.1.0 (2007/08/12 Sunday)
 */
public enum DatabaseInfoDef {

    /** mysql. */
    MySQL("mysql", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/xxx", "", false, false),
    /** oracle. */
    Oracle("oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@localhost:1521:xxx", "xxx", true, true),
    /** db2. */
    DB2("db2", "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://localhost:50000/xxx", "xxx", true, true),
    /** mssql. */
    SQLServer("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "jdbc:sqlserver://localhost:1433;DatabaseName=xxx;", "dbo", true, true),
    /** postgresql. */
    PostgreSQL("postgresql", "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/xxx", "public", true, false),
    /** h2. */
    H2("h2", "org.h2.Driver", "jdbc:h2:file:xxx", "PUBLIC", true, false),
    /** derby. */
    Derby("derby", "org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:xxx;create=true", "xxx", true, true);

    // ===================================================================================
    //                                                                 Definition Accessor
    //                                                                 ===================

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private static final Map<String, DatabaseInfoDef> _codeValueMap = new HashMap<String, DatabaseInfoDef>();
    private String databaseName;
    private String driverName;
    private String urlTemplate;
    private String defultSchema;
    private boolean needSchema;
    private boolean needJdbcDriverJar;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    private DatabaseInfoDef(String databaseName, String driverName, String urlTemplate, String defultSchema,
            boolean needSchema, boolean needJdbcDriverJar) {
        this.databaseName = databaseName;
        this.driverName = driverName;
        this.urlTemplate = urlTemplate;
        this.defultSchema = defultSchema;
        this.needSchema = needSchema;
        this.needJdbcDriverJar = needJdbcDriverJar;
    }

    static {
        for (DatabaseInfoDef value : values()) {
            _codeValueMap.put(value.databaseName().toLowerCase(), value);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String databaseName() {
        return databaseName;
    }

    public String driverName() {
        return driverName;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public String getDefultSchema() {
        return defultSchema;
    }

    public boolean needJdbcDriverJar() {
        return needJdbcDriverJar;
    }

    public boolean needSchema() {
        return needSchema;
    }

    public static DatabaseInfoDef codeOf(Object code) {
        if (code == null) {
            return null;
        }
        if (code instanceof DatabaseInfoDef) {
            return (DatabaseInfoDef) code;
        }
        return _codeValueMap.get(code.toString().toLowerCase());
    }
}
