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
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.dbflute.intro.util.EmZipInputStreamUtil;
import org.dbflute.intro.wizard.DBFluteIntroPage;
import org.seasar.dbflute.helper.mapstring.MapListString;
import org.seasar.dbflute.infra.dfprop.DfPropFile;
import org.seasar.dbflute.util.DfReflectionUtil;

/**
 * @author ecode
 * @author jflute
 */
public class DBFluteIntro {

    /**
     * <pre>
     * e.g. "."
     *  dbflute-intro
     *   |-dbflute_exampledb // DBFlute client
     *   |-mydbflute         // DBFlute module
     *   |-dbflute-intro.jar
     * </pre>
     */
    public static final String BASE_DIR_PATH = ".";
    public static final String INI_FILE_PATH = BASE_DIR_PATH + "/dbflute-intro.ini";
    private static final String MY_DBFLUTE_PATH = BASE_DIR_PATH + "/mydbflute/dbflute-%1$s";

    private Properties publicProperties;

    public Properties getPublicProperties() {

        if (publicProperties != null) {
            return publicProperties;
        }

        publicProperties = new Properties();
        try {
            // TODO
            URL url = new URL("http://dbflute.org/meta/public.properties");
            publicProperties.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return publicProperties;
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
        final File mydbfluteDir = new File(DBFluteIntro.BASE_DIR_PATH, "mydbflute");
        if (mydbfluteDir.exists()) {
            for (File file : mydbfluteDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute-")) {
                    list.add(file.getName().substring(8));
                }
            }
        }

        return list;
    }

    protected static List<ProcessBuilder> getCommandList(List<List<String>> commandList) {

        List<ProcessBuilder> processBuilderList = new ArrayList<ProcessBuilder>();
        for (List<String> command : commandList) {

            if (command.isEmpty()) {
                continue;
            }

            String onName = System.getProperty("os.name").toLowerCase();
            List<String> list = new ArrayList<String>();
            if (onName.startsWith("windows")) {
                list.add("cmd");
                list.add("/c");
                list.add(command.get(0) + ".bat");
            } else {
                list.add("sh");
                list.add(command.get(0) + ".sh");
            }

            if (command.size() > 1) {
                list.addAll(command.subList(1, command.size()));
            }

            processBuilderList.add(new ProcessBuilder(list));
        }

        return processBuilderList;
    }

    public static List<ProcessBuilder> getJdbcDocCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "jdbc"));
        commandList.add(Arrays.asList("manage", "doc"));

        return getCommandList(commandList);
    }

    public static List<ProcessBuilder> getLoadDataReverseCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "jdbc"));
        commandList.add(Arrays.asList("manage", "load-data-reverse"));

        return getCommandList(commandList);
    }

    public static List<ProcessBuilder> getSchemaSyncCheckCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "schema-sync-check"));

        return getCommandList(commandList);
    }

    public static List<ProcessBuilder> getReplaceSchemaCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "replace-schema"));

        return getCommandList(commandList);
    }

    public static int executeCommand(ProcessBuilder processBuilder, OutputStream outputStream) {

        processBuilder.redirectErrorStream(true);

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        Process process;

        int result = 0;
        try {
            process = processBuilder.start();
            inputStream = process.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                IOUtils.write(line, outputStream);
                IOUtils.write(System.getProperty("line.separator"), outputStream);
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

        final String downloadUrl;
        {
            downloadUrl = getPublicProperties().getProperty("dbflute.engine.download.url").replace("$$version$$", downloadVersion);
        }

        final File mydbfluteDir = new File(String.format(MY_DBFLUTE_PATH, downloadVersion));
        mydbfluteDir.mkdirs();

        final String zipFilename;
        {
            zipFilename = mydbfluteDir.getAbsolutePath() + ".zip";
            try {
                FileUtils.copyURLToFile(new URL(downloadUrl), new File(zipFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        final ZipInputStream zipIn = EmZipInputStreamUtil.createZipInputStream(zipFilename);
        EmZipInputStreamUtil.extractAndClose(zipIn, mydbfluteDir.getAbsolutePath());

        FileUtils.deleteQuietly(new File(zipFilename));

        final String templateZipFileName = mydbfluteDir.getAbsolutePath() + "/etc/client-template/dbflute_dfclient.zip";
        final ZipInputStream templateZipIn = EmZipInputStreamUtil.createZipInputStream(templateZipFileName);
        final String templateExtractDirectoryBase = mydbfluteDir.getAbsolutePath()
                + "/client-template/dbflute_dfclient.zip";
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

            List<URL> urls = new ArrayList<URL>();
            if (clientDto.getJdbcDriverJarPath() == null || clientDto.getJdbcDriverJarPath().equals("")) {
                File mydbfluteDir = new File(String.format(MY_DBFLUTE_PATH, clientDto.getDbfluteVersion()), "lib");
                for (File file : FileUtils.listFiles(mydbfluteDir, FileFilterUtils.suffixFileFilter(".jar"), null)) {
                    urls.add(file.toURI().toURL());
                }
            } else {
                URL fileUrl = new File(clientDto.getJdbcDriverJarPath()).toURI().toURL();
                urls.add(fileUrl);
            }

            URLClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));

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
            replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + clientDto.getProject());
            fileMap.put(new File(dbfluteClientDir, "/_project.bat"), replaceMap);
            fileMap.put(new File(dbfluteClientDir, "/_project.sh"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("torque.project = dfclient", "torque.project = " + clientDto.getProject());
            fileMap.put(new File(dbfluteClientDir, "/build.properties"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@database@", clientDto.getDatabase());
            replaceMap.put("@targetLanguage@", clientDto.getTargetLanguage());
            replaceMap.put("@targetContainer@", clientDto.getTargetContainer());
            replaceMap.put("@packageBase@", clientDto.getPackageBase());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/basicInfoMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("{Please write your setting! at './dfprop/databaseInfoMap.dfprop'}", "");
            fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap.dfprop"), replaceMap);

            String[] schema = clientDto.getDatabaseDto().getSchema().split(",");
            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@driver@", escapeControlMark(clientDto.getJdbcDriver()));
            replaceMap.put("@url@", escapeControlMark(clientDto.getDatabaseDto().getUrl()));
            replaceMap.put("@schema@", escapeControlMark(schema[0].trim()));
            replaceMap.put("@user@", escapeControlMark(clientDto.getDatabaseDto().getUser()));
            replaceMap.put("@password@", escapeControlMark(clientDto.getDatabaseDto().getPassword()));
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < schema.length; i++) {
                builder.append("            ; " + escapeControlMark(schema[i].trim())
                        + " = map:{objectTypeTargetList=list:{TABLE; VIEW; SYNONYM}}\r\n");
            }

            replaceMap.put("@additionalSchema@", builder.toString().replaceAll("\r\n$", ""));
            fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap+.dfprop"), replaceMap);

            OptionDto optionDto = clientDto.getOptionDto();

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@isDbCommentOnAliasBasis@", optionDto.isDbCommentOnAliasBasis());
            replaceMap.put("@aliasDelimiterInDbComment@", optionDto.getAliasDelimiterInDbComment());
            replaceMap.put("@isCheckColumnDefOrderDiff@", optionDto.isCheckColumnDefOrderDiff());
            replaceMap.put("@isCheckDbCommentDiff@", optionDto.isCheckDbCommentDiff());
            replaceMap.put("@isCheckProcedureDiff@", optionDto.isCheckProcedureDiff());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/documentDefinitionMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@isGenerateProcedureParameterBean@", optionDto.isGenerateProcedureParameterBean());
            replaceMap.put("@procedureSynonymHandlingType@", optionDto.getProcedureSynonymHandlingType());
            fileMap.put(new File(dbfluteClientDir, "/dfprop/outsideSqlDefinitionMap+.dfprop"), replaceMap);

            replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("@driver@", escapeControlMark(clientDto.getJdbcDriver()));
            replaceMap.put("@url@", escapeControlMark(clientDto.getSystemUserDatabaseDto().getUrl()));
            replaceMap.put("@schema@", escapeControlMark(clientDto.getSystemUserDatabaseDto().getSchema()));
            replaceMap.put("@user@", escapeControlMark(clientDto.getSystemUserDatabaseDto().getUser()));
            replaceMap.put("@password@", escapeControlMark(clientDto.getSystemUserDatabaseDto().getPassword()));
            fileMap.put(new File(dbfluteClientDir, "/dfprop/replaceSchemaDefinitionMap+.dfprop"), replaceMap);

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
            replaceMap.put("@url@", escapeControlMark(entry.getValue().getUrl()));
            replaceMap.put("@schema@", escapeControlMark(entry.getValue().getSchema()));
            replaceMap.put("@user@", escapeControlMark(entry.getValue().getUser()));
            replaceMap.put("@password@", escapeControlMark(entry.getValue().getPassword()));
            replaceMap.put("@env@", escapeControlMark(entry.getKey()));
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
            FileUtils.copyURLToFile(new URL("http://ecode.github.io/dbflute-intro/download/" + jarPathFile.getName()),
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
            if (!file.getName().endsWith(".dfprop") || file.getName().startsWith("allClassCopyright")) {
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

        ClientDto clientDto = new ClientDto();
        clientDto.setProject(project);
        clientDto.setTargetLanguage((String) map.get("basicInfoMap.dfprop").get("targetLanguage"));
        clientDto.setTargetContainer((String) map.get("basicInfoMap.dfprop").get("targetContainer"));
        clientDto.setPackageBase((String) map.get("basicInfoMap.dfprop").get("packageBase"));
        clientDto.setDatabase((String) map.get("basicInfoMap.dfprop").get("database"));
        clientDto.setJdbcDriver((String) map.get("databaseInfoMap.dfprop").get("driver"));
        clientDto.getDatabaseDto().setUrl((String) map.get("databaseInfoMap.dfprop").get("url"));
        clientDto.getDatabaseDto().setSchema(schema);
        clientDto.getDatabaseDto().setUser((String) map.get("databaseInfoMap.dfprop").get("user"));
        clientDto.getDatabaseDto().setPassword((String) map.get("databaseInfoMap.dfprop").get("password"));
        @SuppressWarnings("unchecked")
        Map<String, Object> additionalUserMap = (Map<String, Object>) map.get("replaceSchemaDefinitionMap.dfprop").get("additionalUserMap");
        @SuppressWarnings("unchecked")
        Map<String, Object> systemUserDatabaseMap = (Map<String, Object>) additionalUserMap.get("system");
        clientDto.getSystemUserDatabaseDto().setUrl((String) systemUserDatabaseMap.get("url"));
        clientDto.getSystemUserDatabaseDto().setSchema((String) systemUserDatabaseMap.get("schema"));
        clientDto.getSystemUserDatabaseDto().setUser((String) systemUserDatabaseMap.get("user"));
        clientDto.getSystemUserDatabaseDto().setPassword((String) systemUserDatabaseMap.get("password"));
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

        OptionDto optionDto = clientDto.getOptionDto();
        optionDto.setDbCommentOnAliasBasis(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isDbCommentOnAliasBasis")));
        optionDto.setAliasDelimiterInDbComment((String) map.get("documentDefinitionMap.dfprop").get(
                "aliasDelimiterInDbComment"));
        optionDto.setCheckColumnDefOrderDiff(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isCheckColumnDefOrderDiff")));
        optionDto.setCheckDbCommentDiff(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isCheckDbCommentDiff")));
        optionDto.setCheckProcedureDiff(Boolean.toString(true).equals(
                map.get("documentDefinitionMap.dfprop").get("isCheckProcedureDiff")));
        optionDto.setGenerateProcedureParameterBean(Boolean.toString(true).equals(
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

            DfPropFile dfPropFile = new DfPropFile();
            Map<String, Object> readMap = dfPropFile.readMap(documentDefinitionMapFile.getAbsolutePath(), null);
            @SuppressWarnings("unchecked")
            Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");

            DatabaseDto databaseDto = new DatabaseDto();
            databaseDto.setUrl((String) schemaSyncCheckMap.get("url"));
            databaseDto.setSchema((String) schemaSyncCheckMap.get("schema"));
            databaseDto.setUser((String) schemaSyncCheckMap.get("user"));
            databaseDto.setPassword((String) schemaSyncCheckMap.get("password"));
            databaseDtoMap.put(file.getName().replace("schemaSyncCheck_", ""), databaseDto);
        }

        return databaseDtoMap;
    }

    // TODO
    private String escapeControlMark(Object value) {
        Method method = DfReflectionUtil.getAccessibleMethod(MapListString.class, "escapeControlMark", new Class[] {Object.class});
        Object result = DfReflectionUtil.invokeForcedly(method, new MapListString(), new Object[] {value});
        return String.valueOf(result);
    }
}
