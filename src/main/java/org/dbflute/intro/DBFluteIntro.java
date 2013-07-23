package org.dbflute.intro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dbflute.emecha.eclipse.plugin.core.meta.website.EmMetaFromWebSite;
import org.dbflute.intro.runtime.DfPropFile;
import org.dbflute.intro.util.EmZipInputStreamUtil;
import org.dbflute.intro.wizard.DBFluteIntroPage;

/**
 * @author ecode
 * @author jflute
 */
public class DBFluteIntro {

    /**
     * <pre>
     * e.g. "./"
     *  dbflute-intro
     *   |-dbflute_exampledb // DBFlute client
     *   |-mydbflute         // DBFlute module
     *   |-dbflute-intro.jar
     * </pre>
     */
    public static final String BASE_DIR_PATH = "./";

    public static final String INI_FILE_PATH = BASE_DIR_PATH + "/dbflute-intro.ini";

    private EmMetaFromWebSite site;

    public EmMetaFromWebSite getEmMetaFromWebSite() {

        if (this.site == null) {
            EmMetaFromWebSite site = new EmMetaFromWebSite();

            site.loadMeta();

            this.site = site;
        }

        return this.site;
    }

    public Properties getProperties() {

        File file = new File(DBFluteIntro.INI_FILE_PATH);
        Properties properties = new Properties();
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(fileInputStream);
            }
        }

        return properties;
    }

    public void loadProxy() {

        System.clearProperty("proxySet");
        System.clearProperty("proxyHost");
        System.clearProperty("proxyPort");

        Properties properties = getProperties();
        String proxyHost = properties.getProperty("proxyHost");
        String proxyPort = properties.getProperty("proxyPort");
        boolean useSystemProxies = Boolean.parseBoolean(properties.getProperty("java.net.useSystemProxies"));

        if (useSystemProxies) {
            System.setProperty("java.net.useSystemProxies", String.valueOf(useSystemProxies));
        } else {
            if (proxyHost != null && !proxyHost.equals("")) {
                System.setProperty("proxySet", "true");
                System.setProperty("proxyHost", proxyHost);
            }

            if (proxyPort != null && !proxyPort.equals("")) {
                System.setProperty("proxyPort", proxyPort);
            }
        }
    }

    public Map<String, Object> getManifestMap() {

        Map<String, Object> manifestMap = new LinkedHashMap<String, Object>();
        InputStream inputStream = null;
        try {

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = contextClassLoader.getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                inputStream = resources.nextElement().openStream();
                Manifest manifest = new Manifest(inputStream);

                for (Entry<Object, Object> entry : manifest.getMainAttributes().entrySet()) {
                    manifestMap.put(String.valueOf(entry.getKey()), entry.getValue());
                }

                if (DBFluteIntroPage.class.getName().equals(manifestMap.get("Main-Class"))) {
                    break;
                }

                manifestMap.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return manifestMap;
    }

    public String getVersion() {
        return String.valueOf(getManifestMap().get("Implementation-Version"));
    }

    public static List<String> getProjectList() {

        List<String> list = new ArrayList<String>();
        final File baseDir = new File(DBFluteIntro.BASE_DIR_PATH);
        if (baseDir.exists()) {
            for (File file : baseDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute_")) {
                    list.add(file.getName().substring(8));
                }
            }
        }

        return list;
    }

    public static List<String> getEnvList(String project) {
        List<String> envList = new ArrayList<String>();
        File dfpropDir = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        for (File file : dfpropDir.listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("schemaSyncCheck_")) {
                envList.add(file.getName().substring("schemaSyncCheck_".length()));
            }
        }

        return envList;
    }

    public static boolean existReplaceSchemaFile(String project) {

        boolean exist = false;

        final File playsqlDir = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + project + "/playsql");
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

    public List<String> getExistedDBFluteVersionList() {

        List<String> list = new ArrayList<String>();
        final File mydbfluteDir = new File(DBFluteIntro.BASE_DIR_PATH + "/mydbflute");
        if (mydbfluteDir.exists()) {
            for (File file : mydbfluteDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute-")) {
                    list.add(file.getName().substring(8));
                }
            }
        }

        return list;
    }

    public static List<ProcessBuilder> getJdbcDocCommondList() {
        List<ProcessBuilder> commondList = new ArrayList<ProcessBuilder>();
        String onName = System.getProperty("os.name");
        if (onName != null && onName.startsWith("Windows")) {
            commondList.add(new ProcessBuilder("cmd", "/c", "jdbc.bat"));
            commondList.add(new ProcessBuilder("cmd", "/c", "doc.bat"));
        } else {
            commondList.add(new ProcessBuilder("sh", "jdbc.sh"));
            commondList.add(new ProcessBuilder("sh", "doc.sh"));
        }

        return commondList;
    }

    public static List<ProcessBuilder> getLoadDataReverseCommondList() {
        List<ProcessBuilder> commondList = new ArrayList<ProcessBuilder>();
        String onName = System.getProperty("os.name");
        if (onName != null && onName.startsWith("Windows")) {
            commondList.add(new ProcessBuilder("cmd", "/c", "jdbc.bat"));
            commondList.add(new ProcessBuilder("cmd", "/c", "manage.bat", "load-data-reverse"));
        } else {
            commondList.add(new ProcessBuilder("sh", "jdbc.sh"));
            commondList.add(new ProcessBuilder("sh", "manage.sh", "load-data-reverse"));
        }

        return commondList;
    }

    public static List<ProcessBuilder> getSchemaSyncCheckCommondList() {
        List<ProcessBuilder> commondList = new ArrayList<ProcessBuilder>();
        String onName = System.getProperty("os.name");
        if (onName != null && onName.startsWith("Windows")) {
            commondList.add(new ProcessBuilder("cmd", "/c", "manage.bat", "schema-sync-check"));
        } else {
            commondList.add(new ProcessBuilder("sh", "manage.sh", "schema-sync-check"));
        }

        return commondList;
    }

    public static List<ProcessBuilder> getReplaceSchemaCommondList() {
        List<ProcessBuilder> commondList = new ArrayList<ProcessBuilder>();
        String onName = System.getProperty("os.name");
        if (onName != null && onName.startsWith("Windows")) {
            commondList.add(new ProcessBuilder("cmd", "/c", "replace-schema.bat"));
        } else {
            commondList.add(new ProcessBuilder("sh", "replace-schema.sh"));
        }

        return commondList;
    }

    public static int executeCommond(ProcessBuilder processBuilder, OutputStream outputStream) {

        processBuilder.redirectErrorStream(true);

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        Process process;

        int result = 0;
        try {
            process = processBuilder.start();
            inputStream = process.getInputStream();
            // TODO 文字コードの確認。
            inputStreamReader = new InputStreamReader(inputStream, "MS932");
            bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                IOUtils.write(line, outputStream, Charsets.UTF_8);
                IOUtils.write(System.getProperty("line.separator"), outputStream, Charsets.UTF_8);
                outputStream.flush();

                if (line.equals("BUILD FAILED")) {
                    result = 1;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(bufferedReader);
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(inputStream);
        }

        if (result == 0) {
            result = process.exitValue();
        }

        return result;
    }

    public void downloadDBFlute(String dbfluteVersion) {

        final String downloadVersion = dbfluteVersion;
        if (downloadVersion == null || downloadVersion.trim().length() == 0) {
            return;
        }

        final File mydbflutePureFile;
        {
            mydbflutePureFile = new File(BASE_DIR_PATH, "/mydbflute");
            mydbflutePureFile.mkdirs();
        }

        final String downloadUrl;
        {
            final EmMetaFromWebSite meta = new EmMetaFromWebSite();
            meta.loadMeta();
            downloadUrl = meta.buildDownloadUrlDBFlute(downloadVersion);
        }
        final String dbfluteVersionExpression = "dbflute-" + downloadVersion;

        final String zipFilename;
        {
            zipFilename = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression + ".zip";
            try {
                FileUtils.copyURLToFile(new URL(downloadUrl), new File(zipFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        final ZipInputStream zipIn = EmZipInputStreamUtil.createZipInputStream(zipFilename);
        final String extractDirectoryBase = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression;
        EmZipInputStreamUtil.extractAndClose(zipIn, extractDirectoryBase);

        FileUtils.deleteQuietly(new File(zipFilename));

        final String templateZipFilename = extractDirectoryBase + "/etc/client-template/dbflute_dfclient.zip";
        final ZipInputStream templateZipIn = EmZipInputStreamUtil.createZipInputStream(templateZipFilename);
        final String templateExtractDirectoryBase = extractDirectoryBase + "/client-template/dbflute_dfclient.zip";
        EmZipInputStreamUtil.extractAndClose(templateZipIn, templateExtractDirectoryBase);
    }

    public void testConnection(ClientDto clientDto, Map<String, DatabaseDto> schemaSyncCheckMap) {

        ProxySelector proxySelector = ProxySelector.getDefault();
        ProxySelector.setDefault(null);
        Connection connection = null;

        try {
            Properties info = new Properties();
            info.put("user", clientDto.getDatabaseDto().getUser());
            info.put("password", clientDto.getDatabaseDto().getPassword());

            URL fileUrl = new File(clientDto.getJdbcDriverJarPath()).toURI().toURL();
            URL[] urls = { fileUrl };
            URLClassLoader loader = URLClassLoader.newInstance(urls);

            @SuppressWarnings("unchecked")
            Class<Driver> driverClass = (Class<Driver>) loader.loadClass(clientDto.getJdbcDriver());
            Driver driver = driverClass.newInstance();

            connection = driver.connect(clientDto.getDatabaseDto().getUrl(), info);

        } catch (Exception e) {
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

    public void createNewClient(ClientDto clientDto, Map<String, DatabaseDto> schemaSyncCheckMap) {

        final File dbfluteClientDir = new File(BASE_DIR_PATH, "dbflute_" + clientDto.getProject());

        try {
            final String dbfluteVersionExpression = "dbflute-" + clientDto.getDbfluteVersion();

            final File mydbflutePureFile = new File(BASE_DIR_PATH, "/mydbflute");
            final String extractDirectoryBase = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression;

            if (!dbfluteClientDir.exists()) {
                final String templateZipFileName = extractDirectoryBase + "/etc/client-template/dbflute_dfclient.zip";
                final ZipInputStream templateZipIn = EmZipInputStreamUtil.createZipInputStream(templateZipFileName);
                EmZipInputStreamUtil.extractAndClose(templateZipIn, BASE_DIR_PATH);
                final File dbfluteClientDirTemp = new File(BASE_DIR_PATH, "dbflute_dfclient");

                dbfluteClientDirTemp.renameTo(dbfluteClientDir);
            }

            List<String> dfpropFileList = new ArrayList<String>();
            dfpropFileList.add("basicInfoMap+.dfprop");
            dfpropFileList.add("databaseInfoMap+.dfprop");
            dfpropFileList.add("documentDefinitionMap+.dfprop");
            dfpropFileList.add("littleAdjustmentMap+.dfprop");
            dfpropFileList.add("outsideSqlDefinitionMap+.dfprop");

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
            replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + clientDto.getProject());
            fileMap.put(new File(dbfluteClientDir, "/_project.bat"), replaceMap);
            fileMap.put(new File(dbfluteClientDir, "/_project.sh"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("torque.project = dfclient", "torque.project = " + clientDto.getProject());
            fileMap.put(new File(dbfluteClientDir, "/build.properties"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("${database}", clientDto.getDbms());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/basicInfoMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("{Please write your setting! at './dfprop/databaseInfoMap.dfprop'}", "");
            fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap.dfprop"), replaceMap);

            String[] schema = clientDto.getDatabaseDto().getSchema().split(",");
            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("${driver}", clientDto.getJdbcDriver());
            replaceMap.put("${url}", clientDto.getDatabaseDto().getUrl());
            replaceMap.put("${schema}", schema[0].trim());
            replaceMap.put("${user}", clientDto.getDatabaseDto().getUser());
            replaceMap.put("${password}", clientDto.getDatabaseDto().getPassword());
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < schema.length; i++) {
                builder.append("            ; " + schema[i].trim()
                        + " = map:{objectTypeTargetList=list:{TABLE; VIEW; SYNONYM}}\r\n");
            }

            replaceMap.put("${additionalSchema}", builder.toString().replaceAll("\r\n$", ""));
            fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("${aliasDelimiterInDbComment}", ":");
            replaceMap.put("${isDbCommentOnAliasBasis}", String.valueOf(clientDto.isDbCommentOnAliasBasis()));
            replaceMap.put("${isCheckColumnDefOrderDiff}", String.valueOf(clientDto.isCheckColumnDefOrderDiff()));
            replaceMap.put("${isCheckDbCommentDiff}", String.valueOf(clientDto.isCheckDbCommentDiff()));
            replaceMap.put("${isCheckProcedureDiff}", String.valueOf(clientDto.isCheckProcedureDiff()));
            fileMap.put(new File(dbfluteClientDir, "/dfprop/documentDefinitionMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("${isGenerateProcedureParameterBean}", clientDto.isGenerateProcedureParameterBean());
            replaceMap.put("${procedureSynonymHandlingType}", "INCLUDE");
            fileMap.put(new File(dbfluteClientDir, "/dfprop/outsideSqlDefinitionMap+.dfprop"), replaceMap);

            replaceFile(fileMap, false);

            fileMap = new LinkedHashMap<File, Map<String, Object>>();

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)", "$1" + clientDto.getDbfluteVersion());
            fileMap.put(new File(dbfluteClientDir, "/_project.bat"), replaceMap);
            fileMap.put(new File(dbfluteClientDir, "/_project.sh"), replaceMap);

            replaceFile(fileMap, true);

            if (clientDto.getJdbcDriverJarPath() != null && !clientDto.getJdbcDriverJarPath().equals("")) {

                File extLibDir = new File(dbfluteClientDir, "extlib");

                File jarFile = new File(clientDto.getJdbcDriverJarPath());
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

                        FileUtils.copyFileToDirectory(new File(clientDto.getJdbcDriverJarPath()), extLibDir);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            createSchemaSyncCheck(clientDto, schemaSyncCheckMap);

        } catch (Exception e) {
            try {
                FileUtils.deleteDirectory(dbfluteClientDir);
            } catch (IOException e2) {
                // ignore
            }

            throw new RuntimeException(e);
        }
    }

    private void createSchemaSyncCheck(ClientDto clientDto, Map<String, DatabaseDto> schemaSyncCheckMap) {

        final File dbfluteClientDir = new File(BASE_DIR_PATH, "dbflute_" + clientDto.getProject());
        URL schemaSyncCheckURL = ClassLoader.getSystemResource("dfprop/documentDefinitionMap+schemaSyncCheck.dfprop");

        for (Entry<String, DatabaseDto> entry : schemaSyncCheckMap.entrySet()) {
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
            replaceMap.put("${url}", entry.getValue().getUrl());
            replaceMap.put("${schema}", entry.getValue().getSchema());
            replaceMap.put("${user}", entry.getValue().getUser());
            replaceMap.put("${password}", entry.getValue().getPassword());
            replaceMap.put("${env}", entry.getKey());
            fileMap.put(documentDefinitionMapFile, replaceMap);

            replaceFile(fileMap, false);
        }
    }

    public static boolean deleteClient(String project) {

        if (project == null) {
            return false;
        }

        final File dbfluteClientDir = new File(BASE_DIR_PATH, "dbflute_" + project);
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
                    if (regularExpression) {
                        text = text.replaceAll(replaceEntry.getKey(), String.valueOf(replaceEntry.getValue()));
                    } else {
                        text = text.replace(replaceEntry.getKey(), String.valueOf(replaceEntry.getValue()));
                    }
                }

                FileUtils.write(entry.getKey(), text, Charsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean upgrade() {

        File jarPathFile = new File("./dbflute-intro.jar");

        Class<?> clazz = this.getClass();
        URL location = clazz.getResource("/" + clazz.getName().replaceAll("\\.", "/") + ".class");
        String path = location.getPath();

        if (path.lastIndexOf("!") != -1) {
            try {
                jarPathFile = new File(URLDecoder.decode(path.substring("file:/".length(), path.lastIndexOf("!")),
                        Charsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            FileUtils.copyURLToFile(
                    new URL("http://dbflute.seasar.org/download/helper/dbflute-intro/" + jarPathFile.getName()),
                    jarPathFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ClientDto convClientDtoFromDfprop(String project) {

        Map<String, Map<String, Object>> map = new LinkedHashMap<String, Map<String, Object>>();
        File dfpropDir = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        for (File file : dfpropDir.listFiles()) {
            if (!file.getName().endsWith(".dfprop")) {
                continue;
            }

            InputStream inputStream = null;
            try {
                inputStream = FileUtils.openInputStream(file);

                DfPropFile dfPropFile = new DfPropFile();
                map.put(file.getName(), dfPropFile.readMap(inputStream));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
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

        ClientDto clientDto = new ClientDto();
        clientDto.setProject(project);
        clientDto.setDbms((String) map.get("basicInfoMap.dfprop").get("database"));
        clientDto.setJdbcDriver((String) map.get("databaseInfoMap.dfprop").get("driver"));
        clientDto.getDatabaseDto().setUrl((String) map.get("databaseInfoMap.dfprop").get("url"));
        clientDto.getDatabaseDto().setSchema(schema);
        clientDto.getDatabaseDto().setUser((String) map.get("databaseInfoMap.dfprop").get("user"));
        clientDto.getDatabaseDto().setPassword((String) map.get("databaseInfoMap.dfprop").get("password"));
        File extlibDir = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + project + "/extlib");
        for (File file : extlibDir.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                clientDto.setJdbcDriverJarPath(file.getPath());
            }
        }

        File projectFile = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + project + "/_project.bat");
        String data = null;
        try {
            data = FileUtils.readFileToString(projectFile);
        } catch (IOException e) {
            new RuntimeException(e);
        }

        Pattern pattern = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            clientDto.setDbfluteVersion(matcher.group(2));
        }

        clientDto.setAliasDelimiterInDbComment((String) map.get("documentDefinitionMap.dfprop").get(
                "aliasDelimiterInDbComment"));
        clientDto.setDbCommentOnAliasBasis(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isDbCommentOnAliasBasis")));
        clientDto.setCheckColumnDefOrderDiff(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isCheckColumnDefOrderDiff")));
        clientDto.setCheckDbCommentDiff(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isCheckDbCommentDiff")));
        clientDto.setCheckProcedureDiff(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isCheckProcedureDiff")));
        clientDto.setGenerateProcedureParameterBean(Boolean.toString(true).equals(
                map.get("outsideSqlDefinitionMap.dfprop").get("isGenerateProcedureParameterBean")));

        return clientDto;
    }

    public static Map<String, DatabaseDto> convDatabaseDtoMapFromDfprop(String project) {

        Map<String, DatabaseDto> databaseDtoMap = new LinkedHashMap<String, DatabaseDto>();
        File dfpropDir = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        for (File file : dfpropDir.listFiles()) {
            if (!file.isDirectory() || !file.getName().startsWith("schemaSyncCheck_")) {
                continue;
            }

            File documentDefinitionMapFile = new File(file, "documentDefinitionMap+.dfprop");
            if (!documentDefinitionMapFile.exists() || !documentDefinitionMapFile.isFile()) {
                continue;
            }

            InputStream inputStream = null;
            try {
                inputStream = FileUtils.openInputStream(documentDefinitionMapFile);

                DfPropFile dfPropFile = new DfPropFile();
                Map<String, Object> readMap = dfPropFile.readMap(inputStream);
                @SuppressWarnings("unchecked")
                Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");

                DatabaseDto databaseDto = new DatabaseDto();
                databaseDto.setUrl((String) schemaSyncCheckMap.get("url"));
                databaseDto.setSchema((String) schemaSyncCheckMap.get("schema"));
                databaseDto.setUser((String) schemaSyncCheckMap.get("user"));
                databaseDto.setPassword((String) schemaSyncCheckMap.get("password"));
                databaseDtoMap.put(file.getName().replace("schemaSyncCheck_", ""), databaseDto);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }

        return databaseDtoMap;
    }
}
