package org.seasar.dbflute.emecha.eclipse.plugin.wizards.client.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jflute
 * @since 0.1.0 (2007/08/12 Sunday)
 */
public class DatabaseInfoDef {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected static List<DatabaseInfoDef> TARGET_DATABASE_INFO_LIST = new ArrayList<DatabaseInfoDef>();
    static {
        DatabaseInfoDef o = null;
        {
            o = info("h2", "org.h2.Driver", "jdbc:h2:file:xxx");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
        {
            o = info("derby", "org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:xxx;create=true");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
        {
            o = info("mysql", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/xxx");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
        {
            o = info("postgresql", "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/xxx");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
        //{
        //    o = info("firebird", "org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:localhost/3050:xxx");
        //    TARGET_DATABASE_INFO_LIST.add(o);
        //}
        {
            o = info("oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@localhost:1521:xxx");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
        {
            o = info("db2", "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://localhost:50000/xxx");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
        {
            o = info("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                    "jdbc:sqlserver://localhost:1433;DatabaseName=xxx;");
            TARGET_DATABASE_INFO_LIST.add(o);
        }
    }
    protected static java.util.Map<String, DatabaseInfoDef> TARGET_DATABASE_INFO_MAP = new java.util.LinkedHashMap<String, DatabaseInfoDef>();
    static {
        final List<DatabaseInfoDef> list = TARGET_DATABASE_INFO_LIST;
        for (DatabaseInfoDef info : list) {
            TARGET_DATABASE_INFO_MAP.put(info.getDatabaseName(), info);
        }
    }

    protected static DatabaseInfoDef info(String dbname, String driver, String urlTemplate) {
        return new DatabaseInfoDef(dbname, driver, urlTemplate);
    }

    // ===================================================================================
    //                                                                 Definition Accessor
    //                                                                 ===================
    public static DatabaseInfoDef findDatabaseInfo(String database) {
        return TARGET_DATABASE_INFO_MAP.get(database);
    }

    public static int findIndex(String databaseName) {
        final List<String> databaseList = extractDatabaseList();
        int index = 0;
        for (String database : databaseList) {
            if (database.equals(databaseName)) {
                return index;
            }
            ++index;
        }
        throw new IllegalStateException("Not found database: " + databaseName);
    }

    public static List<String> extractDatabaseList() {
        return new ArrayList<String>(TARGET_DATABASE_INFO_MAP.keySet());
    }

    public static List<String> extractDriverList() {
        final List<String> driverList = new ArrayList<String>();
        final Set<String> keySet = TARGET_DATABASE_INFO_MAP.keySet();
        for (String key : keySet) {
            final DatabaseInfoDef databaseInfo = TARGET_DATABASE_INFO_MAP.get(key);
            final String driverName = databaseInfo.getDriverName();
            driverList.add(driverName);
        }
        return driverList;
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String databaseName;
    protected String driverName;
    protected String urlTemplate;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseInfoDef(String databaseName, String driverName, String urlTemplate) {
        this.databaseName = databaseName;
        this.driverName = driverName;
        this.urlTemplate = urlTemplate;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }
}
