/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.app.logic;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.dbflute.helper.mapstring.MapListString;
import org.dbflute.infra.dfprop.DfPropFile;
import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.bean.DatabaseInfoDefBean;
import org.dbflute.intro.app.bean.OptionBean;
import org.dbflute.intro.app.definition.DatabaseInfoDef;
import org.dbflute.intro.mylasta.direction.DbfluteConfig;
import org.dbflute.intro.mylasta.util.ZipUtil;
import org.dbflute.util.DfStringUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DbFluteClientLogic {

    @Resource
    private DbfluteConfig dbfluteConfig;

    public Map<String, Map<?, ?>> getClassificationMap() {
        Map<String, Map<?, ?>> classificationMap = new LinkedHashMap<String, Map<?, ?>>();

        Map<String, String> targetLanguageMap = Stream.of(dbfluteConfig.getTargetLanguage().split(",")).collect(
                Collectors.toMap(targetLanguage -> targetLanguage, targetLanguage -> targetLanguage, (u, v) -> v,
                        LinkedHashMap::new));
        classificationMap.put("targetLanguageMap", targetLanguageMap);

        Map<String, String> targetContainerMap = Stream.of(dbfluteConfig.getTargetContainer().split(",")).collect(
                Collectors.toMap(targetContainer -> targetContainer, targetContainer -> targetContainer, (u, v) -> v,
                        LinkedHashMap::new));
        classificationMap.put("targetContainerMap", targetContainerMap);

        Map<String, DatabaseInfoDefBean> databaseInfoDefMap = Stream.of(DatabaseInfoDef.values()).collect(
                Collectors.toMap(databaseInfoDef -> databaseInfoDef.getDatabaseName(),
                        databaseInfoDef -> new DatabaseInfoDefBean(databaseInfoDef), (u, v) -> v, LinkedHashMap::new));
        classificationMap.put("databaseInfoDefMap", databaseInfoDefMap);

        return classificationMap;
    }

    public List<String> getProjectList() {

        List<String> list = new ArrayList<String>();
        final File baseDir = new File(DbFluteIntroLogic.BASE_DIR_PATH);
        if (baseDir.exists()) {
            for (File file : baseDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute_")) {
                    list.add(file.getName().substring(8));
                }
            }
        }

        return list;
    }

    public List<String> getEnvList(String project) {
        List<String> envList = new ArrayList<String>();
        File dfpropDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        for (File file : dfpropDir.listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("schemaSyncCheck_")) {
                envList.add(file.getName().substring("schemaSyncCheck_".length()));
            }
        }

        return envList;
    }

    public boolean existReplaceSchemaFile(String project) {

        boolean exist = false;

        final File playsqlDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/playsql");
        for (File file : playsqlDir.listFiles()) {
            if (file.isFile() && file.getName().startsWith("replace-schema") && file.getName().endsWith(".sql")) {
                try {
                    if (FileUtils.readFileToString(file, Charsets.UTF_8).trim().length() > 0) {
                        exist = true;
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        }

        return exist;
    }

    public void testConnection(ClientBean clientBean) {

        ProxySelector proxySelector = ProxySelector.getDefault();
        ProxySelector.setDefault(null);
        Connection connection = null;

        try {
            Properties info = new Properties();
            info.put("user", clientBean.getDatabaseBean().getUser());
            String password = clientBean.getDatabaseBean().getPassword();
            if (DfStringUtil.is_NotNull_and_NotEmpty(password)) {
                info.put("password", password);
            }

            List<URL> urls = new ArrayList<URL>();
            if (DfStringUtil.is_Null_or_Empty(clientBean.getJdbcDriverJarPath())) {
                File mydbfluteDir = new File(String.format(DbFluteEngineLogic.MY_DBFLUTE_PATH, clientBean.getDbfluteVersion()), "lib");
                if (mydbfluteDir.isDirectory()) {
                    for (File file : FileUtils.listFiles(mydbfluteDir, FileFilterUtils.suffixFileFilter(".jar"), null)) {
                        urls.add(file.toURI().toURL());
                    }
                }
            } else {
                URL fileUrl = new File(clientBean.getJdbcDriverJarPath()).toURI().toURL();
                urls.add(fileUrl);
            }

            URLClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));

            @SuppressWarnings("unchecked")
            Class<Driver> driverClass = (Class<Driver>) loader.loadClass(clientBean.getJdbcDriver());
            Driver driver = driverClass.newInstance();

            connection = driver.connect(clientBean.getDatabaseBean().getUrl(), info);

        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException
                | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ProxySelector.setDefault(proxySelector);

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void createClient(ClientBean clientBean, boolean ignoreTestConnectionFail) {
        _createClient(clientBean, false, ignoreTestConnectionFail);
    }

    public void updateClient(ClientBean clientBean, boolean ignoreTestConnectionFail) {
        _createClient(clientBean, true, ignoreTestConnectionFail);
    }

    private void _createClient(ClientBean clientBean, boolean update, boolean ignoreTestConnectionFail) {
        final File dbfluteClientDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + clientBean.getProject());

        final String dbfluteVersionExpression = "dbflute-" + clientBean.getDbfluteVersion();

        final File mydbflutePureFile = new File(DbFluteIntroLogic.BASE_DIR_PATH, "/mydbflute");

        if (!dbfluteClientDir.exists()) {
            if (update) {
                throw new RuntimeException("更新するものがない");
            }
            final String extractDirectoryBase = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression;
            final String templateZipFileName = extractDirectoryBase + "/etc/client-template/dbflute_dfclient.zip";
            ZipUtil.decrypt(templateZipFileName, DbFluteIntroLogic.BASE_DIR_PATH);
            new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_dfclient").renameTo(dbfluteClientDir);
        } else {
            if (!update) {
                throw new RuntimeException("すでに登録されている");
            }
        }

        try {
            List<String> dfpropFileList = new ArrayList<String>();
            dfpropFileList.add("basicInfoMap+.dfprop");
            dfpropFileList.add("databaseInfoMap+.dfprop");
            dfpropFileList.add("documentDefinitionMap+.dfprop");
            dfpropFileList.add("littleAdjustmentMap+.dfprop");
            dfpropFileList.add("outsideSqlDefinitionMap+.dfprop");
            dfpropFileList.add("replaceSchemaDefinitionMap+.dfprop");

            try {
                for (String dfpropFile : dfpropFileList) {
                    URL url = ClassLoader.getSystemResource("dfprop/" + dfpropFile);
                    FileUtils.copyURLToFile(url, new File(dbfluteClientDir, "dfprop/" + dfpropFile));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Map<File, Map<String, Object>> fileMap = new LinkedHashMap<File, Map<String, Object>>();

            Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + clientBean.getProject());
            fileMap.put(new File(dbfluteClientDir, "/_project.bat"), replaceMap);
            fileMap.put(new File(dbfluteClientDir, "/_project.sh"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("torque.project = dfclient", "torque.project = " + clientBean.getProject());
            fileMap.put(new File(dbfluteClientDir, "/build.properties"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@database@", clientBean.getDatabase());
            replaceMap.put("@targetLanguage@", clientBean.getTargetLanguage());
            replaceMap.put("@targetContainer@", clientBean.getTargetContainer());
            replaceMap.put("@packageBase@", clientBean.getPackageBase());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/basicInfoMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("{Please write your setting! at './dfprop/databaseInfoMap.dfprop'}", "");
            fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap.dfprop"), replaceMap);

            String schema = clientBean.getDatabaseBean().getSchema();
            schema = schema == null ? "" : schema;
            String[] schemaList = schema.split(",");
            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@driver@", escapeControlMark(clientBean.getJdbcDriver()));
            replaceMap.put("@url@", escapeControlMark(clientBean.getDatabaseBean().getUrl()));
            replaceMap.put("@schema@", escapeControlMark(schemaList[0].trim()));
            replaceMap.put("@user@", escapeControlMark(clientBean.getDatabaseBean().getUser()));
            replaceMap.put("@password@", escapeControlMark(clientBean.getDatabaseBean().getPassword()));
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < schemaList.length; i++) {
                builder.append("            ; " + escapeControlMark(schemaList[i].trim())
                        + " = map:{objectTypeTargetList=list:{TABLE; VIEW; SYNONYM}}\r\n");
            }

            replaceMap.put("@additionalSchema@", builder.toString().replaceAll("\r\n$", ""));
            fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap+.dfprop"), replaceMap);

            OptionBean optionBean = clientBean.getOptionBean();

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@isDbCommentOnAliasBasis@", optionBean.isDbCommentOnAliasBasis());
            replaceMap.put("@aliasDelimiterInDbComment@", optionBean.getAliasDelimiterInDbComment());
            replaceMap.put("@isCheckColumnDefOrderDiff@", optionBean.isCheckColumnDefOrderDiff());
            replaceMap.put("@isCheckDbCommentDiff@", optionBean.isCheckDbCommentDiff());
            replaceMap.put("@isCheckProcedureDiff@", optionBean.isCheckProcedureDiff());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/documentDefinitionMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@isGenerateProcedureParameterBean@", optionBean.isGenerateProcedureParameterBean());
            replaceMap.put("@procedureSynonymHandlingType@", optionBean.getProcedureSynonymHandlingType());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/outsideSqlDefinitionMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@driver@", escapeControlMark(clientBean.getJdbcDriver()));
            replaceMap.put("@url@", escapeControlMark(clientBean.getSystemUserDatabaseBean().getUrl()));
            replaceMap.put("@schema@", escapeControlMark(clientBean.getSystemUserDatabaseBean().getSchema()));
            replaceMap.put("@user@", escapeControlMark(clientBean.getSystemUserDatabaseBean().getUser()));
            replaceMap.put("@password@", escapeControlMark(clientBean.getSystemUserDatabaseBean().getPassword()));
            fileMap.put(new File(dbfluteClientDir, "/dfprop/replaceSchemaDefinitionMap+.dfprop"), replaceMap);

            replaceFile(fileMap, false);

            fileMap = new LinkedHashMap<File, Map<String, Object>>();

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)", "$1" + clientBean.getDbfluteVersion());
            fileMap.put(new File(dbfluteClientDir, "/_project.bat"), replaceMap);
            fileMap.put(new File(dbfluteClientDir, "/_project.sh"), replaceMap);

            replaceFile(fileMap, true);

            if (clientBean.getJdbcDriverJarPath() != null && !clientBean.getJdbcDriverJarPath().equals("")) {

                File extLibDir = new File(dbfluteClientDir, "extlib");

                File jarFile = new File(clientBean.getJdbcDriverJarPath());
                File jarFileOld = new File(extLibDir, jarFile.getName());

                boolean flg = true;
                if (jarFileOld.exists()) {
                    if (jarFile.getCanonicalPath().equals(jarFileOld.getCanonicalPath())) {
                        flg = false;
                    }
                }

                if (flg) {
                    try {
                        for (File file : FileUtils.listFiles(extLibDir, new String[] { ".jar" }, false)) {
                            file.delete();
                        }

                        FileUtils.copyFileToDirectory(new File(clientBean.getJdbcDriverJarPath()), extLibDir);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            createSchemaSyncCheck(clientBean);

        } catch (Exception e) {
            try {
                FileUtils.deleteDirectory(dbfluteClientDir);
            } catch (IOException ignore) {
                // ignore
            }

            throw new RuntimeException(e);
        }
    }

    private void createSchemaSyncCheck(ClientBean clientBean) {

        final File dbfluteClientDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + clientBean.getProject());
        URL schemaSyncCheckURL = ClassLoader.getSystemResource("dfprop/documentDefinitionMap+schemaSyncCheck.dfprop");

        for (Entry<String, DatabaseBean> entry : clientBean.getSchemaSyncCheckMap().entrySet()) {
            final File dfpropEnvDir = new File(dbfluteClientDir, "dfprop/schemaSyncCheck_" + entry.getKey());
            dfpropEnvDir.mkdir();

            File documentDefinitionMapFile = new File(dfpropEnvDir, "documentDefinitionMap+.dfprop");
            try {
                FileUtils.copyURLToFile(schemaSyncCheckURL, documentDefinitionMapFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Map<File, Map<String, Object>> fileMap = new LinkedHashMap<File, Map<String, Object>>();

            Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@url@", escapeControlMark(entry.getValue().getUrl()));
            replaceMap.put("@schema@", escapeControlMark(entry.getValue().getSchema()));
            replaceMap.put("@user@", escapeControlMark(entry.getValue().getUser()));
            replaceMap.put("@password@", escapeControlMark(entry.getValue().getPassword()));
            replaceMap.put("@env@", escapeControlMark(entry.getKey()));
            fileMap.put(documentDefinitionMapFile, replaceMap);

            replaceFile(fileMap, false);
        }
    }

    public boolean deleteClient(String project) {

        if (project == null) {
            return false;
        }

        final File dbfluteClientDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project);
        try {
            FileUtils.deleteDirectory(dbfluteClientDir);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void replaceFile(Map<File, Map<String, Object>> fileMap, boolean regularExpression) {
        try {
            for (Entry<File, Map<String, Object>> entry : fileMap.entrySet()) {

                String text = FileUtils.readFileToString(entry.getKey(), Charsets.UTF_8);

                for (Entry<String, Object> replaceEntry : entry.getValue().entrySet()) {
                    Object value = replaceEntry.getValue();
                    value = value == null ? "" : value;
                    if (regularExpression) {
                        text = text.replaceAll(replaceEntry.getKey(), String.valueOf(value));
                    } else {
                        text = text.replace(replaceEntry.getKey(), String.valueOf(value));
                    }
                }

                FileUtils.write(entry.getKey(), text, Charsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientBean convClientBeanFromDfprop(String project) {

        Map<String, Map<String, Object>> map = new LinkedHashMap<String, Map<String, Object>>();
        File dfpropDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");

        for (File file : dfpropDir.listFiles()) {
            if (!file.getName().endsWith(".dfprop") || file.getName().startsWith("allClassCopyright")
                    || file.getName().startsWith("sourceCopyright")) {
                continue;
            }

            DfPropFile dfPropFile = new DfPropFile();
            map.put(file.getName(), dfPropFile.readMap(file.getAbsolutePath(), null));
        }

        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            if (!entry.getKey().endsWith("+.dfprop")) {
                continue;
            }

            String key = entry.getKey().replaceAll("\\+\\.dfprop", "\\.dfprop");
            map.get(key).putAll(entry.getValue());
        }

        String schema = (String) map.get("databaseInfoMap.dfprop").get("schema");

        @SuppressWarnings("unchecked")
        Map<String, Object> variousMap = ((Map<String, Object>) map.get("databaseInfoMap.dfprop").get("variousMap"));
        if (variousMap != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> additionalSchemaMap = ((Map<String, Object>) variousMap.get("additionalSchemaMap"));

            if (additionalSchemaMap != null) {
                Set<String> keySet = additionalSchemaMap.keySet();
                for (String additionalSchema : keySet) {
                    schema += "," + additionalSchema;
                }
            }
        }

        ClientBean clientBean = new ClientBean();
        clientBean.setProject(project);
        clientBean.setTargetLanguage((String) map.get("basicInfoMap.dfprop").get("targetLanguage"));
        clientBean.setTargetContainer((String) map.get("basicInfoMap.dfprop").get("targetContainer"));
        clientBean.setPackageBase((String) map.get("basicInfoMap.dfprop").get("packageBase"));
        clientBean.setDatabase((String) map.get("basicInfoMap.dfprop").get("database"));
        clientBean.setJdbcDriver((String) map.get("databaseInfoMap.dfprop").get("driver"));
        clientBean.getDatabaseBean().setUrl((String) map.get("databaseInfoMap.dfprop").get("url"));
        clientBean.getDatabaseBean().setSchema(schema);
        clientBean.getDatabaseBean().setUser((String) map.get("databaseInfoMap.dfprop").get("user"));
        clientBean.getDatabaseBean().setPassword((String) map.get("databaseInfoMap.dfprop").get("password"));
        if (map.get("replaceSchemaDefinitionMap.dfprop") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> additionalUserMap = (Map<String, Object>) map.get("replaceSchemaDefinitionMap.dfprop").get("additionalUserMap");
            @SuppressWarnings("unchecked")
            Map<String, Object> systemUserDatabaseMap = (Map<String, Object>) additionalUserMap.get("system");
            if (systemUserDatabaseMap != null) {
                clientBean.getSystemUserDatabaseBean().setUrl((String) systemUserDatabaseMap.get("url"));
                clientBean.getSystemUserDatabaseBean().setSchema((String) systemUserDatabaseMap.get("schema"));
                clientBean.getSystemUserDatabaseBean().setUser((String) systemUserDatabaseMap.get("user"));
                clientBean.getSystemUserDatabaseBean().setPassword((String) systemUserDatabaseMap.get("password"));
            }
        }
        File extlibDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/extlib");
        for (File file : extlibDir.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                clientBean.setJdbcDriverJarPath(file.getPath());
            }
        }

        File projectFile = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/_project.bat");
        String data = null;
        try {
            data = FileUtils.readFileToString(projectFile);
        } catch (IOException e) {
            new RuntimeException(e);
        }

        Pattern pattern = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            clientBean.setDbfluteVersion(matcher.group(2));
        }

        Map<String, Object> documentMap = map.get("documentDefinitionMap.dfprop");
        OptionBean optionBean = clientBean.getOptionBean();
        if (documentMap != null) {
            optionBean.setDbCommentOnAliasBasis(Boolean.parseBoolean((String) documentMap.get("isDbCommentOnAliasBasis")));
            optionBean.setAliasDelimiterInDbComment((String) documentMap.get("aliasDelimiterInDbComment"));
            optionBean.setCheckColumnDefOrderDiff(Boolean.parseBoolean((String) documentMap.get("isCheckColumnDefOrderDiff")));
            optionBean.setCheckDbCommentDiff(Boolean.parseBoolean((String) documentMap.get("isCheckDbCommentDiff")));
            optionBean.setCheckProcedureDiff(Boolean.parseBoolean((String) documentMap.get("isCheckProcedureDiff")));
        }

        Map<String, Object> outsideSqlMap = map.get("outsideSqlDefinitionMap.dfprop");
        if (outsideSqlMap != null) {
            optionBean.setGenerateProcedureParameterBean(Boolean.parseBoolean((String) outsideSqlMap.get("isGenerateProcedureParameterBean")));
        }

        Map<String, DatabaseBean> schemaSyncCheckMap = clientBean.getSchemaSyncCheckMap();
        schemaSyncCheckMap.putAll(convDatabaseBeanMapFromDfprop(project));

        return clientBean;
    }

    protected Map<String, DatabaseBean> convDatabaseBeanMapFromDfprop(String project) {

        Map<String, DatabaseBean> databaseBeanMap = new LinkedHashMap<String, DatabaseBean>();
        File dfpropDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        Stream.of(dfpropDir.listFiles()).forEach(file -> {
            if (!file.isDirectory() || !file.getName().startsWith("schemaSyncCheck_")) {
                return;
            }

            File documentDefinitionMapFile = new File(file, "documentDefinitionMap+.dfprop");
            if (!documentDefinitionMapFile.exists() || !documentDefinitionMapFile.isFile()) {
                return;
            }

            DfPropFile dfPropFile = new DfPropFile();
            Map<String, Object> readMap = dfPropFile.readMap(documentDefinitionMapFile.getAbsolutePath(), null);
            @SuppressWarnings("all")
            Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");

            DatabaseBean databaseBean = new DatabaseBean();
            databaseBean.setUrl((String) schemaSyncCheckMap.get("url"));
            databaseBean.setSchema((String) schemaSyncCheckMap.get("schema"));
            databaseBean.setUser((String) schemaSyncCheckMap.get("user"));
            databaseBean.setPassword((String) schemaSyncCheckMap.get("password"));
            databaseBeanMap.put(file.getName().replace("schemaSyncCheck_", ""), databaseBean);
        });

        return databaseBeanMap;
    }

    private String escapeControlMark(Object value) {
        return new MapListString().escapeControlMark(value);
    }
}
