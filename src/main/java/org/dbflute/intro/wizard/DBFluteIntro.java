package org.dbflute.intro.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dbflute.emecha.eclipse.plugin.core.meta.website.EmMetaFromWebSite;
import org.dbflute.emecha.eclipse.plugin.core.util.io.EmFileUtil;
import org.dbflute.emecha.eclipse.plugin.core.util.net.EmURLUtil;
import org.dbflute.emecha.eclipse.plugin.core.util.util.zip.EmZipInputStreamUtil;
import org.dbflute.emecha.eclipse.plugin.wizards.client.DBFluteNewClientPageResult;
import org.dbflute.intro.util.Monitor;

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
    protected static final String BASIC_DIR_PATH = "./";

    protected static final String INI_FILE_PATH = BASIC_DIR_PATH + "/dbflute-intro.ini";

    private EmMetaFromWebSite site;

    protected EmMetaFromWebSite getEmMetaFromWebSite() {

        if (this.site == null) {
            EmMetaFromWebSite site = new EmMetaFromWebSite();

            site.loadMeta();

            this.site = site;
        }

        return this.site;
    }

    protected Properties getProperties() {

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

    protected void loadProxy() {

        Properties properties = getProperties();

        String proxyHost = properties.getProperty("proxyHost");
        String proxyPort = properties.getProperty("proxyPort");

        if (proxyHost != null && !proxyHost.equals("")) {
            System.setProperty("proxySet", "true");
            System.setProperty("proxyHost", properties.getProperty("proxyHost"));
        }

        if (proxyPort != null && !proxyPort.equals("")) {
            System.setProperty("proxyPort", properties.getProperty("proxyPort"));
        }

    }

    protected void downloadDBFlute(String dbfluteVersion) {

        final String downloadVersion = dbfluteVersion;
        if (downloadVersion == null || downloadVersion.trim().length() == 0) {
            return;
        }

        final File mydbflutePureFile;
        {
            mydbflutePureFile = new File(BASIC_DIR_PATH, "/mydbflute");
            mydbflutePureFile.mkdirs();
        }

        final String downloadUrl;
        {
            final EmMetaFromWebSite meta = new EmMetaFromWebSite();
            meta.loadMeta();
            downloadUrl = meta.buildDownloadUrlDBFlute(downloadVersion);
        }
        final String dbfluteVersionExpression = "dbflute-" + downloadVersion;

        Monitor.output("...Downloading DBFlute to " + mydbflutePureFile.getAbsolutePath() + dbfluteVersionExpression);

        final String zipFilename;
        {
            zipFilename = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression + ".zip";
            final URL url = EmURLUtil.createURL(downloadUrl);

            EmURLUtil.makeFileAndClose(url, zipFilename);
        }

        final ZipInputStream zipIn = EmZipInputStreamUtil.createZipInputStream(zipFilename);
        final String extractDirectoryBase = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression;
        EmZipInputStreamUtil.extractAndClose(zipIn, extractDirectoryBase);

        EmFileUtil.deleteFile(zipFilename); // After Care!

        final String templateZipFilename = extractDirectoryBase + "/etc/client-template/dbflute_dfclient.zip";
        final ZipInputStream templateZipIn = EmZipInputStreamUtil.createZipInputStream(templateZipFilename);
        final String templateExtractDirectoryBase = extractDirectoryBase + "/client-template/dbflute_dfclient.zip";
        EmZipInputStreamUtil.extractAndClose(templateZipIn, templateExtractDirectoryBase);
    }

    protected void createNewClient(DBFluteNewClientPageResult result) {

        final String dbfluteVersionExpression = "dbflute-" + result.getVersionInfoDBFlute();

        final File mydbflutePureFile = new File(BASIC_DIR_PATH, "/mydbflute");
        final String extractDirectoryBase = mydbflutePureFile.getAbsolutePath() + "/" + dbfluteVersionExpression;

        final String templateZipFileName = extractDirectoryBase + "/etc/client-template/dbflute_dfclient.zip";
        final ZipInputStream templateZipIn = EmZipInputStreamUtil.createZipInputStream(templateZipFileName);
        EmZipInputStreamUtil.extractAndClose(templateZipIn, BASIC_DIR_PATH);
        final File dbfluteClientDirTemp = new File(BASIC_DIR_PATH, "dbflute_dfclient");
        final File dbfluteClientDir = new File(BASIC_DIR_PATH, "dbflute_" + result.getProject());

        dbfluteClientDirTemp.renameTo(dbfluteClientDir);

        Map<File, Map<String, String>> fileMap = new LinkedHashMap<File, Map<String, String>>();

        Map<String, String> replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + result.getProject());
        fileMap.put(new File(dbfluteClientDir, "/_project.bat"), replaceMap);

        replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + result.getProject());
        fileMap.put(new File(dbfluteClientDir, "/_project.sh"), replaceMap);

        replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("torque.project = dfclient", "torque.project = " + result.getProject());
        fileMap.put(new File(dbfluteClientDir, "/build.properties"), replaceMap);

        replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("; database = h2", "; database = " + result.getDatabase());
        fileMap.put(new File(dbfluteClientDir, "/dfprop/basicInfoMap.dfprop"), replaceMap);

        replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("; driver   = {Please write your setting! at './dfprop/databaseInfoMap.dfprop'}",
                "; driver   = " + result.getDatabaseInfoDriver());
        replaceMap.put("; url      = ...", "; url      = " + result.getDatabaseInfoUrl());
        replaceMap.put("; schema   = ...", "; schema   = " + result.getDatabaseInfoSchema());
        replaceMap.put("; user     = ...", "; user     = " + result.getDatabaseInfoUser());
        replaceMap.put("; password = ...", "; password = " + result.getDatabaseInfoPassword());
        replaceMap.put("#; includeSynonyms=true", "; includeSynonyms=true");
        fileMap.put(new File(dbfluteClientDir, "/dfprop/databaseInfoMap.dfprop"), replaceMap);

        replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("#; aliasDelimiterInDbComment = :", "; aliasDelimiterInDbComment = :");
        replaceMap.put("#; isDbCommentOnAliasBasis = false", "; isDbCommentOnAliasBasis = true");
        replaceMap.put("#; isCheckColumnDefOrderDiff = false", "; isCheckColumnDefOrderDiff = true");
        replaceMap.put("#; isCheckDbCommentDiff = false", "; isCheckDbCommentDiff = true");
        replaceMap.put("#; isCheckProcedureDiff = false", "; isCheckProcedureDiff = true");
        fileMap.put(new File(dbfluteClientDir, "/dfprop/documentDefinitionMap.dfprop"), replaceMap);

        replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("; isGenerateProcedureParameterBean = false", "; isGenerateProcedureParameterBean = true");
        replaceMap.put("#; procedureSynonymHandlingType = NONE", "; procedureSynonymHandlingType = INCLUDE");
        fileMap.put(new File(dbfluteClientDir, "/dfprop/outsideSqlDefinitionMap.dfprop"), replaceMap);

        replaceFile(fileMap);

        if (result.getJdbcDriverJarPath() != null && !result.getJdbcDriverJarPath().equals("")) {

            File extLibDir = new File(dbfluteClientDir, "extlib");

            try {
                FileUtils.copyFileToDirectory(new File(result.getJdbcDriverJarPath()), extLibDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void createSchemaSyncCheck(String env, DBFluteNewClientPageResult result) {

        final File dbfluteClientDir = new File(BASIC_DIR_PATH, "dbflute_" + result.getProject());
        final File dfpropEnvDir = new File(dbfluteClientDir, "dfprop/schemaSyncCheck_" + env);
        dfpropEnvDir.mkdir();

        URL documentDefinitionMapURL = ClassLoader.getSystemResource("documentDefinitionMap+.dfprop");

        File documentDefinitionMapFile = new File(dfpropEnvDir, "documentDefinitionMap+.dfprop");
        try {
            FileUtils.copyURLToFile(documentDefinitionMapURL, documentDefinitionMapFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<File, Map<String, String>> fileMap = new LinkedHashMap<File, Map<String, String>>();

        Map<String, String> replaceMap = new LinkedHashMap<String, String>();
        replaceMap.put("; url = jdbc:...", "; url = " + result.getDatabaseInfoUrl());
        replaceMap.put("; schema = EXAMPLEDB", "; schema = " + result.getDatabaseInfoSchema());
        replaceMap.put("; user = exampuser", "; user = " + result.getDatabaseInfoUser());
        replaceMap.put("; password = exampword", "; password = " + result.getDatabaseInfoPassword());
        replaceMap.put("${env}", env);
        fileMap.put(documentDefinitionMapFile, replaceMap);

        replaceFile(fileMap);
    }

    private void replaceFile(Map<File, Map<String, String>> fileMap) {
        try {
            for (Entry<File, Map<String, String>> entry : fileMap.entrySet()) {

                String text = null;

                text = FileUtils.readFileToString(entry.getKey(), Charsets.UTF_8);

                for (Entry<String, String> replaceEntry : entry.getValue().entrySet()) {
                    text = text.replace(replaceEntry.getKey(), replaceEntry.getValue());
                }

                FileUtils.write(entry.getKey(), text, Charsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void upgradeClient(DBFluteNewClientPageResult result) {

    }
}
