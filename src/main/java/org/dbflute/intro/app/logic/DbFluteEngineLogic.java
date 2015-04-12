package org.dbflute.intro.app.logic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.mylasta.util.ZipUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DbFluteEngineLogic {

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

    public void download(String dbfluteVersion) {

        final String downloadVersion = dbfluteVersion;
        if (downloadVersion == null || downloadVersion.trim().length() == 0) {
            return;
        }

        final String downloadUrl;
        {
            downloadUrl = getPublicProperties().getProperty("dbflute.engine.download.url").replace("$$version$$", downloadVersion);
        }

        final File mydbfluteDir = new File(String.format(DbFluteIntroLogic.MY_DBFLUTE_PATH, downloadVersion));
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

        ZipUtil.decrypt(zipFilename, mydbfluteDir.getAbsolutePath());

        FileUtils.deleteQuietly(new File(zipFilename));

        final String templateZipFileName = mydbfluteDir.getAbsolutePath() + "/etc/client-template/dbflute_dfclient.zip";
        final String templateExtractDirectoryBase = mydbfluteDir.getAbsolutePath() + "/client-template/dbflute_dfclient.zip";
        ZipUtil.decrypt(templateZipFileName, templateExtractDirectoryBase);
    }

    public List<String> getExistedVersionList() {

        List<String> list = new ArrayList<String>();
        final File mydbfluteDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "mydbflute");
        if (mydbfluteDir.exists()) {
            for (File file : mydbfluteDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute-")) {
                    list.add(file.getName().substring(8));
                }
            }
        }

        return list;
    }
}
