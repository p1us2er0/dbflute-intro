package org.seasar.dbflute.intro.wizard;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.seasar.dbflute.emecha.eclipse.plugin.core.exception.EmPluginException;
import org.seasar.dbflute.emecha.eclipse.plugin.core.meta.website.EmMetaFromWebSite;
import org.seasar.dbflute.emecha.eclipse.plugin.wizards.client.DBFluteNewClientPageResult;
import org.seasar.dbflute.emecha.eclipse.plugin.wizards.client.definition.DatabaseInfoDef;
import org.seasar.dbflute.intro.util.SwingUtil;

/**
 * @author ecode
 * @author jflute
 */
public class DBFluteIntroPage {

    private JFrame frame;
    private JPanel clientPanel;
    private JPanel newClientPanel;
    private JTabbedPane tabPanel;
    private JTabbedPane schemaSyncCheckTabPanel;

    private JTextField projectText;
    private JComboBox databaseCombo;
    private JTextField databaseInfoUrlText;
    private JTextField databaseInfoSchemaText;
    private JTextField databaseInfoUserText;
    private JPasswordField databaseInfoPasswordText;
    private JTextField jdbcDriverJarPathText;
    private JComboBox versionInfoDBFluteCombo;
    private JButton clientCreateButton;

    private JComboBox projectCombo;
    private JButton jdbcDocButton;
    private JButton schemaSyncCheckButton;

    private JDialog dialog;
    private JTextField proxyHostText;
    private JTextField proxyPortText;

    private final Action schemaSyncCheckAddAction = new SchemaSyncCheckAddAction();
    private final Action schemaSyncCheckRemoveAction = new SchemaSyncCheckRemoveAction();
    private final Action clientCreateAction = new ClientCreateAction();
    private final Action downloadAction = new DownloadAction();
    private final Action proxySettingsViewAction = new ProxySettingsViewAction();
    private final Action proxySettingsAction = new ProxySettingsAction();

    private final DBFluteIntro dbFluteNewClient = new DBFluteIntro();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DBFluteIntroPage window = new DBFluteIntroPage();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public DBFluteIntroPage() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        dbFluteNewClient.loadProxy();

        frame = new JFrame("DBFlute Intro");
        frame.setBounds(100, 100, 500, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout(0, 0));
        frame.setLocationRelativeTo(null);

        tabPanel = new JTabbedPane();
        frame.getContentPane().add(tabPanel, "name_5009361789717");

        // ***************************************************************
        clientPanel = new JPanel();
        clientPanel.setLayout(null);
        tabPanel.addTab("Client", clientPanel);

        JLabel projectLabel = new JLabel("DB名(*)");
        projectLabel.setBounds(10, 10, 150, 20);
        clientPanel.add(projectLabel);

        projectCombo = new JComboBox();
        projectCombo.setBounds(150, 10, 300, 20);
        fireProjectCombo(projectCombo);
        clientPanel.add(projectCombo);

        File schemaHTMLFile = new File(DBFluteIntro.BASIC_DIR_PATH
                + "/dbflute_${project}/output/doc/schema-${project}.html");
        String schemaHTMLFileLinkMessge = "<a href='" + schemaHTMLFile.toURI() + "'>テーブル定義を開く</a> (SchemaHTML)";

        JEditorPane schemaHTMLLink = new JEditorPane("text/html", schemaHTMLFileLinkMessge);
        schemaHTMLLink.setBounds(10, 35, 300, 20);
        schemaHTMLLink.setEditable(false);
        schemaHTMLLink.setOpaque(false);
        schemaHTMLLink.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        schemaHTMLLink.setFont(new JLabel().getFont());
        schemaHTMLLink.addHyperlinkListener(new HyperlinkHandler());
        clientPanel.add(schemaHTMLLink);

        File historyHTMLFile = new File(DBFluteIntro.BASIC_DIR_PATH
                + "/dbflute_${project}/output/doc/history-${project}.html");
        String historyHTMLFileLinkMessge = "<a href='" + historyHTMLFile.toURI() + "'>DB変更履歴を開く</a> (HistoryHTML)";

        JEditorPane historyHTMLLink = new JEditorPane("text/html", historyHTMLFileLinkMessge);
        historyHTMLLink.setBounds(10, 60, 300, 20);
        historyHTMLLink.setEditable(false);
        historyHTMLLink.setOpaque(false);
        historyHTMLLink.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        historyHTMLLink.setFont(new JLabel().getFont());
        historyHTMLLink.addHyperlinkListener(new HyperlinkHandler());
        clientPanel.add(historyHTMLLink);

        File syncCheckHTMLFile = new File(DBFluteIntro.BASIC_DIR_PATH
                + "/dbflute_${project}/output/doc/sync-check-result_${env}.html");
        String syncCheckHTMLFileLinkMessge = "<a href='" + syncCheckHTMLFile.toURI()
                + "'>差分チェック結果を開く</a> (SyncCheckHTML)";

        JEditorPane syncCheckHTMLLink = new JEditorPane("text/html", syncCheckHTMLFileLinkMessge);
        syncCheckHTMLLink.setBounds(10, 85, 300, 20);
        syncCheckHTMLLink.setEditable(false);
        syncCheckHTMLLink.setOpaque(false);
        syncCheckHTMLLink.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        syncCheckHTMLLink.setFont(new JLabel().getFont());
        syncCheckHTMLLink.addHyperlinkListener(new HyperlinkHandler());
        clientPanel.add(syncCheckHTMLLink);

        List<ProcessBuilder> jdbcDocList = new ArrayList<ProcessBuilder>();
        String onName = System.getProperty("os.name");
        if (onName != null && onName.startsWith("Windows")) {
            jdbcDocList.add(new ProcessBuilder("cmd", "/c", "jdbc.bat"));
            jdbcDocList.add(new ProcessBuilder("cmd", "/c", "doc.bat"));
        } else {
            jdbcDocList.add(new ProcessBuilder("sh", "jdbc.sh"));
            jdbcDocList.add(new ProcessBuilder("sh", "doc.sh"));
        }

        jdbcDocButton = new JButton(new TaskAction("ドキュメント生成", jdbcDocList));
        jdbcDocButton.setBounds(10, 220, 200, 20);
        clientPanel.add(jdbcDocButton);

        List<ProcessBuilder> schemaSyncCheckList = new ArrayList<ProcessBuilder>();
        if (onName != null && onName.startsWith("Windows")) {
            schemaSyncCheckList.add(new ProcessBuilder("cmd", "/c", "manage.bat", "schema-sync-check"));
        } else {
            schemaSyncCheckList.add(new ProcessBuilder("sh", "manage.sh", "schema-sync-check"));
        }

        schemaSyncCheckButton = new JButton(new TaskAction("スキーマの差分チェック", schemaSyncCheckList));
        schemaSyncCheckButton.setBounds(220, 220, 200, 20);
        clientPanel.add(schemaSyncCheckButton);

        // ***************************************************************
        newClientPanel = new JPanel();
        newClientPanel.setLayout(null);
        tabPanel.addTab("+", newClientPanel);

        JLabel project2Label = new JLabel("Client Project(*)");
        project2Label.setBounds(10, 10, 150, 20);
        newClientPanel.add(project2Label);

        projectText = new JTextField();
        projectText.setBounds(150, 10, 300, 20);
        projectText.setColumns(10);
        newClientPanel.add(projectText);

        JLabel databaseLabel = new JLabel("Database(*)");
        databaseLabel.setBounds(10, 35, 150, 20);
        newClientPanel.add(databaseLabel);

        databaseCombo = new JComboBox();
        databaseCombo.setBounds(150, 35, 300, 20);
        newClientPanel.add(databaseCombo);
        for (String database : DatabaseInfoDef.extractDatabaseList()) {
            databaseCombo.addItem(database);
        }
        databaseCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseInfoDef databaseInfoDef = DatabaseInfoDef.findDatabaseInfo(databaseCombo.getSelectedItem()
                        .toString());
                fireDatabaseInfoUrlText(databaseInfoDef);
                fireDatabaseInfoSchemaText(databaseInfoDef);
                fireJdbcDriverJarText(databaseInfoDef);
            }
        });

        DatabaseInfoDef databaseInfoDef = DatabaseInfoDef.findDatabaseInfo(databaseCombo.getSelectedItem().toString());

        JLabel databaseInfoUrlLabel = new JLabel("Url(*)");
        databaseInfoUrlLabel.setBounds(10, 60, 150, 20);
        newClientPanel.add(databaseInfoUrlLabel);

        databaseInfoUrlText = new JTextField();
        databaseInfoUrlText.setBounds(150, 60, 300, 20);
        databaseInfoUrlText.setColumns(10);
        databaseInfoUrlText.setText(databaseInfoDef.getUrlTemplate());
        fireDatabaseInfoUrlText(databaseInfoDef);
        newClientPanel.add(databaseInfoUrlText);

        JLabel databaseInfoSchemaLabel = new JLabel("Schema");
        databaseInfoSchemaLabel.setBounds(10, 85, 150, 20);
        newClientPanel.add(databaseInfoSchemaLabel);

        databaseInfoSchemaText = new JTextField();
        databaseInfoSchemaText.setBounds(150, 85, 300, 20);
        databaseInfoSchemaText.setColumns(10);
        fireDatabaseInfoSchemaText(databaseInfoDef);
        newClientPanel.add(databaseInfoSchemaText);

        JLabel databaseInfoUserLabel = new JLabel("User(*)");
        databaseInfoUserLabel.setBounds(10, 110, 150, 20);
        newClientPanel.add(databaseInfoUserLabel);

        databaseInfoUserText = new JTextField();
        databaseInfoUserText.setBounds(150, 110, 300, 20);
        databaseInfoUserText.setColumns(10);
        newClientPanel.add(databaseInfoUserText);

        JLabel databaseInfoPasswordLabel = new JLabel("Password");
        databaseInfoPasswordLabel.setBounds(10, 135, 150, 20);
        newClientPanel.add(databaseInfoPasswordLabel);

        databaseInfoPasswordText = new JPasswordField();
        databaseInfoPasswordText.setBounds(150, 135, 300, 20);
        databaseInfoPasswordText.setColumns(10);
        newClientPanel.add(databaseInfoPasswordText);

        JLabel jdbcDriverJarPathLabel = new JLabel("Jdbc Driver Jar Path");
        jdbcDriverJarPathLabel.setBounds(10, 160, 150, 20);
        newClientPanel.add(jdbcDriverJarPathLabel);

        jdbcDriverJarPathText = new JTextField();
        jdbcDriverJarPathText.setBounds(150, 160, 300, 20);
        jdbcDriverJarPathText.setColumns(10);

        List<String> needJdbcDriverJarMessage = new ArrayList<String>();
        for (String database : DatabaseInfoDef.extractDatabaseList()) {
            if (this.needJdbcDriverJar(DatabaseInfoDef.findDatabaseInfo(database))) {
                needJdbcDriverJarMessage.add(database);
            }
        }

        jdbcDriverJarPathText.setToolTipText("Please download Jdbc Driver Jar. And Drag & Drop. Database is "
                + needJdbcDriverJarMessage);
        fireJdbcDriverJarText(databaseInfoDef);
        newClientPanel.add(jdbcDriverJarPathText);

        JLabel versionInfoDBFluteLabel = new JLabel("DBFlute Version(*)");
        versionInfoDBFluteLabel.setBounds(10, 185, 150, 20);
        newClientPanel.add(versionInfoDBFluteLabel);

        versionInfoDBFluteCombo = new JComboBox();
        versionInfoDBFluteCombo.setBounds(150, 185, 300, 20);
        fireVersionInfoDBFlute(versionInfoDBFluteCombo);
        newClientPanel.add(versionInfoDBFluteCombo);

        JLabel schemaSyncCheckLabel = new JLabel("SchemaSyncCheck");
        schemaSyncCheckLabel.setBounds(10, 210, 180, 20);
        newClientPanel.add(schemaSyncCheckLabel);

        JButton schemaSyncCheckAddButton = new JButton(schemaSyncCheckAddAction);
        schemaSyncCheckAddButton.setBounds(150, 210, 40, 20);
        newClientPanel.add(schemaSyncCheckAddButton);

        JButton schemaSyncCheckRemoveButton = new JButton(schemaSyncCheckRemoveAction);
        schemaSyncCheckRemoveButton.setBounds(190, 210, 40, 20);
        newClientPanel.add(schemaSyncCheckRemoveButton);

        clientCreateButton = new JButton(clientCreateAction);
        clientCreateButton.setBounds(150, 390, 300, 20);
        clientCreateButton.setEnabled(versionInfoDBFluteCombo.getItemCount() > 0);
        newClientPanel.add(clientCreateButton);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem downloadMenuItem = new JMenuItem(downloadAction);
        menu.add(downloadMenuItem);

        JMenuItem proxySettingsMenuItem = new JMenuItem(proxySettingsViewAction);
        menu.add(proxySettingsMenuItem);

        JMenu programLanguageMenu = new JMenu("Program Language<TODO>");
        menu.add(programLanguageMenu);

        JRadioButtonMenuItem javaMenuItem = new JRadioButtonMenuItem("java");
        JRadioButtonMenuItem csharpMenuItem = new JRadioButtonMenuItem("csharp");
        JRadioButtonMenuItem otherMenuItem = new JRadioButtonMenuItem("other", true);

        ButtonGroup group = new ButtonGroup();
        group.add(javaMenuItem);
        group.add(csharpMenuItem);
        group.add(otherMenuItem);

        programLanguageMenu.add(javaMenuItem);
        programLanguageMenu.add(csharpMenuItem);
        programLanguageMenu.add(otherMenuItem);

        JMenu displayLanguageMenu = new JMenu("Display Language<TODO>");
        menu.add(displayLanguageMenu);

        JRadioButtonMenuItem englishMenuItem = new JRadioButtonMenuItem("English", true);
        JRadioButtonMenuItem japaneseMenuItem = new JRadioButtonMenuItem("Japanese");

        group = new ButtonGroup();
        group.add(englishMenuItem);
        group.add(japaneseMenuItem);

        displayLanguageMenu.add(englishMenuItem);
        displayLanguageMenu.add(japaneseMenuItem);

        SwingUtil.updateLookAndFeel(frame);

        if (versionInfoDBFluteCombo.getItemCount() == 0) {
            // TODO
            downloadAction.actionPerformed(null);
        }
    }

    public DBFluteNewClientPageResult asResult() {

        final String projectName = projectText.getText();
        final String database = databaseCombo.getSelectedItem().toString();
        final String databaseInfoDriver = DatabaseInfoDef.findDatabaseInfo(database).getDriverName();
        final String databaseInfoUrl = databaseInfoUrlText.getText();
        final String databaseInfoSchema = databaseInfoSchemaText.getText();
        final String databaseInfoUser = databaseInfoUserText.getText();
        final String databaseInfoPassword = databaseInfoPasswordText.getText();
        final String jdbcDriverJarPath = jdbcDriverJarPathText.getText();
        final String versionInfoDBFlute = versionInfoDBFluteCombo.getSelectedItem().toString();

        final DBFluteNewClientPageResult result = new DBFluteNewClientPageResult();
        result.setProject(projectName);
        result.setDatabase(database);
        result.setDatabaseInfoDriver(databaseInfoDriver);
        result.setDatabaseInfoUrl(databaseInfoUrl);
        result.setDatabaseInfoSchema(databaseInfoSchema);
        result.setDatabaseInfoUser(databaseInfoUser);
        result.setDatabaseInfoPassword(databaseInfoPassword);
        result.setJdbcDriverJarPath(jdbcDriverJarPath);
        result.setVersionInfoDBFlute(versionInfoDBFlute);

        return result;
    }

    private class TaskAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private List<ProcessBuilder> taskList;

        public TaskAction(String taskName, List<ProcessBuilder> taskList) {
            putValue(NAME, taskName);
            putValue(SHORT_DESCRIPTION, "Some short description");
            this.taskList = taskList;
        }

        public void actionPerformed(ActionEvent event) {

            String project = projectCombo.getSelectedItem().toString();

            String env = null;
            if (getValue(NAME).toString().equals("schemaSyncCheck")) {

                env = showSchemaSyncCheckEnvDialog(project);
                if (env == null) {
                    return;
                }
            }

            for (final ProcessBuilder processBuilder : taskList) {
                processBuilder.directory(new File(DBFluteIntro.BASIC_DIR_PATH, "dbflute_" + project));

                Map<String, String> environment = processBuilder.environment();
                environment.put("pause_at_end", "n");
                if (getValue(NAME).toString().equals("schemaSyncCheck")) {
                    environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + env);
                }

                processBuilder.redirectErrorStream(true);

                ProgressBarDialog progressBarDialog = new ProgressBarDialog() {

                    @Override
                    public void execute() {
                        try {
                            Process process = processBuilder.start();

                            InputStream is = process.getInputStream();
                            try {
                                while (is.read() >= 0)
                                    ;
                            } finally {
                                is.close();
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                };

                progressBarDialog.start(getValue(NAME).toString(), "Execute...");
            }

            JOptionPane.showMessageDialog(frame, "Finished. Log Path = " + DBFluteIntro.INI_FILE_PATH + "/dbflute_"
                    + project + "/log/dbflute.log");
        }
    }

    private class SchemaSyncCheckAddAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SchemaSyncCheckAddAction() {
            putValue(NAME, "+");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent event) {

            String env = JOptionPane.showInputDialog(frame, "Please input env.");

            if (env != null) {
                if (schemaSyncCheckTabPanel == null) {
                    schemaSyncCheckTabPanel = new JTabbedPane();
                    schemaSyncCheckTabPanel.setBounds(0, 230, 480, 160);
                    newClientPanel.add(schemaSyncCheckTabPanel);
                }

                schemaSyncCheckTabPanel.addTab(env, new SchemaSyncCheckPage());
                SwingUtil.updateLookAndFeel(frame);
            }
        }
    }

    private class SchemaSyncCheckRemoveAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SchemaSyncCheckRemoveAction() {
            putValue(NAME, "-");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent event) {

            int selectedIndex = schemaSyncCheckTabPanel.getSelectedIndex();
            if (selectedIndex != -1) {
                schemaSyncCheckTabPanel.remove(schemaSyncCheckTabPanel.getSelectedIndex());
            }
        }
    }

    private class ClientCreateAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ClientCreateAction() {
            putValue(NAME, "Client creation");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent event) {

            DBFluteNewClientPageResult result = asResult();

            Map<String, String> data = new LinkedHashMap<String, String>();
            data.put("Client Project", result.getProject());
            data.put("Database", result.getDatabase());
            data.put("Url", result.getDatabaseInfoUrl());
            if (needDatabaseInfoSchema(DatabaseInfoDef.findDatabaseInfo(result.getDatabase()))) {
                data.put("Schema", result.getDatabaseInfoSchema());
            }
            data.put("User", result.getDatabaseInfoUser());
            if (needJdbcDriverJar(DatabaseInfoDef.findDatabaseInfo(result.getDatabase()))) {
                data.put("JdbcDriverJarPath", result.getJdbcDriverJarPath());
            }
            data.put("DBFlute Version", result.getVersionInfoDBFlute());

            if (schemaSyncCheckTabPanel != null) {
                for (int i = 0; i < schemaSyncCheckTabPanel.getTabCount(); i++) {
                    Component tabComponent = schemaSyncCheckTabPanel.getComponent(i);
                    if (!(tabComponent instanceof SchemaSyncCheckPage)) {
                        continue;
                    }

                    SchemaSyncCheckPage schemaSyncCheckPage = (SchemaSyncCheckPage) tabComponent;
                    DBFluteNewClientPageResult dbFluteNewClientPageResult = schemaSyncCheckPage.asResult();

                    String env = schemaSyncCheckTabPanel.getTitleAt(i);
                    data.put(env + ".User", dbFluteNewClientPageResult.getDatabaseInfoUser());
                }
            }

            for (Entry<String, String> entry : data.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    JOptionPane.showMessageDialog(frame, "[" + entry.getKey() + "] is required.");
                    return;
                }
            }

            final File dbfluteClientDir = new File(DBFluteIntro.BASIC_DIR_PATH, "dbflute_" + result.getProject());

            if (dbfluteClientDir.exists()) {
                JOptionPane.showMessageDialog(frame, "Already exists DBFlute client(" + result.getProject() + ").");
                return;
            }

            dbFluteNewClient.createNewClient(result);

            if (schemaSyncCheckTabPanel != null) {
                for (int i = 0; i < schemaSyncCheckTabPanel.getTabCount(); i++) {
                    Component tabComponent = schemaSyncCheckTabPanel.getComponent(i);
                    if (!(tabComponent instanceof SchemaSyncCheckPage)) {
                        continue;
                    }

                    SchemaSyncCheckPage schemaSyncCheckPage = (SchemaSyncCheckPage) tabComponent;
                    DBFluteNewClientPageResult dbFluteNewClientPageResult = schemaSyncCheckPage.asResult();
                    // TODO
                    dbFluteNewClientPageResult.setProject(projectText.getText());

                    dbFluteNewClient.createSchemaSyncCheck(schemaSyncCheckTabPanel.getTitleAt(i),
                            dbFluteNewClientPageResult);
                }
            }

            fireProjectCombo(projectCombo);

            JOptionPane.showMessageDialog(frame, "Finished DBFlute client creation.");
        }
    }

    private class DownloadAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public DownloadAction() {
            putValue(NAME, "Download(&Upgrade)");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent event) {

            EmMetaFromWebSite site = null;

            try {
                site = dbFluteNewClient.getEmMetaFromWebSite();
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(frame, "Network error. Please set proxy.");
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Please input a version.");
            message.append("\nLatest Version : ").append(site.getLatestVersionDBFlute());
            message.append("\nLatest Snapshot Version : ").append(site.getLatestSnapshotVersionDBFlute());

            final String dbfluteVersion = JOptionPane.showInputDialog(frame, message, site.getLatestVersionDBFlute());

            if (dbfluteVersion == null) {
                JOptionPane.showMessageDialog(frame, "Canceled download.");
                return;
            }

            ProgressBarDialog progressBarDialog = new ProgressBarDialog() {

                @Override
                public void execute() {
                    try {
                        dbFluteNewClient.downloadDBFlute(dbfluteVersion);

                    } catch (EmPluginException e) {
                        JOptionPane.showMessageDialog(frame, "Download error.");
                        return;
                    }

                    fireVersionInfoDBFlute(versionInfoDBFluteCombo);
                    clientCreateButton.setEnabled(versionInfoDBFluteCombo.getItemCount() > 0);
                }
            };

            progressBarDialog.start("Download", "ver" + dbfluteVersion + " download...");
        }
    }

    private abstract class ProgressBarDialog {

        public void start(String title, String message) {
            final JDialog dialog = new JDialog(frame, title, true);
            dialog.setBounds(100, 100, 200, 100);
            dialog.getContentPane().setLayout(new CardLayout(0, 0));
            dialog.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            // TODO
            dialog.getContentPane().add(panel, "panel");
            panel.setLayout(null);

            JLabel downloadMessageLabel = new JLabel(message);
            downloadMessageLabel.setBounds(10, 10, 200, 20);
            panel.add(downloadMessageLabel);

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setBounds(10, 35, 180, 20);
            progressBar.setValue(0);
            progressBar.setIndeterminate(true);
            panel.add(progressBar);

            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        execute();
                    } finally {
                        dialog.setVisible(false);
                    }
                }
            };
            thread.start();

            dialog.setVisible(true);
        }

        public abstract void execute();
    }

    private class ProxySettingsViewAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ProxySettingsViewAction() {
            putValue(NAME, "Proxy Settings");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent event) {

            Properties properties = dbFluteNewClient.getProperties();
            String proxyHost = properties.getProperty("proxyHost");
            String proxyPort = properties.getProperty("proxyPort");

            dialog = new JDialog(frame, "Proxy Settings", true);
            dialog.setBounds(100, 100, 200, 150);
            dialog.getContentPane().setLayout(new CardLayout(0, 0));
            dialog.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            dialog.getContentPane().add(panel, "name_5009361789718");
            panel.setLayout(null);

            JLabel proxyHostLabel = new JLabel("Proxy Host");
            proxyHostLabel.setBounds(10, 10, 80, 20);
            panel.add(proxyHostLabel);

            proxyHostText = new JTextField();
            proxyHostText.setBounds(80, 10, 100, 20);
            proxyHostText.setColumns(10);
            proxyHostText.setText(proxyHost);
            panel.add(proxyHostText);

            JLabel proxyPortLabel = new JLabel("Proxy Port");
            proxyPortLabel.setBounds(10, 35, 80, 20);
            panel.add(proxyPortLabel);

            proxyPortText = new JTextField();
            proxyPortText.setBounds(80, 35, 100, 20);
            proxyPortText.setColumns(10);
            proxyPortText.setText(proxyPort);
            panel.add(proxyPortText);

            JButton proxySettingsButton = new JButton(proxySettingsAction);
            proxySettingsButton.setBounds(80, 60, 50, 20);
            panel.add(proxySettingsButton);

            dialog.setVisible(true);
        }
    }

    private class ProxySettingsAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ProxySettingsAction() {
            putValue(NAME, "Set");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent event) {

            Properties properties = dbFluteNewClient.getProperties();
            properties.put("proxyHost", proxyHostText.getText());
            properties.put("proxyPort", proxyPortText.getText());

            FileOutputStream stream = null;
            try {
                stream = FileUtils.openOutputStream(new File(DBFluteIntro.INI_FILE_PATH));
                properties.store(stream, "Proxy Setting");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(stream);
            }

            dbFluteNewClient.loadProxy();

            dialog.setVisible(false);
        }
    }

    private void fireProjectCombo(JComboBox projectCombo) {

        projectCombo.removeAllItems();
        final File projectDir = new File(DBFluteIntro.BASIC_DIR_PATH);
        if (projectDir.exists()) {
            for (File file : projectDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute_")) {
                    projectCombo.addItem(file.getName().substring(8));
                }
            }
        }

        tabPanel.remove(clientPanel);
        if (projectCombo.getItemCount() != 0) {
            tabPanel.insertTab("Client", null, clientPanel, null, 0);
            tabPanel.setSelectedComponent(clientPanel);
            if (projectText != null && projectText.getText() != null && !projectText.getText().equals("")) {
                projectCombo.setSelectedItem(projectText.getText());
            }
        }

        SwingUtil.updateLookAndFeel(frame);
    }

    private void fireVersionInfoDBFlute(JComboBox versionInfoDBFluteCombo) {

        versionInfoDBFluteCombo.removeAllItems();
        final File mydbfluteDir = new File(DBFluteIntro.BASIC_DIR_PATH + "/mydbflute");
        if (mydbfluteDir.exists()) {
            for (File file : mydbfluteDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute-")) {
                    versionInfoDBFluteCombo.addItem(file.getName().substring(8));
                }
            }

            versionInfoDBFluteCombo.setSelectedIndex(versionInfoDBFluteCombo.getItemCount() - 1);
        }

        versionInfoDBFluteCombo.setToolTipText("Please download DBFlute Module.(menu -> Download(&upgrade))");
    }

    private boolean needDatabaseInfoSchema(DatabaseInfoDef databaseInfoDef) {
        return Arrays.asList("mssql").contains(databaseInfoDef.getDatabaseName());
    }

    private boolean needJdbcDriverJar(DatabaseInfoDef databaseInfoDef) {
        return !Arrays.asList("h2", "mysql", "postgresql").contains(databaseInfoDef.getDatabaseName());
    }

    private void fireDatabaseInfoUrlText(DatabaseInfoDef databaseInfoDef) {
        databaseInfoUrlText.setText(databaseInfoDef.getUrlTemplate());
    }

    private void fireDatabaseInfoSchemaText(DatabaseInfoDef databaseInfoDef) {

        databaseInfoSchemaText.setText("");

        if (needDatabaseInfoSchema(databaseInfoDef)) {
            databaseInfoSchemaText.setText("dbo");
        }
    }

    private void fireJdbcDriverJarText(DatabaseInfoDef databaseInfoDef) {

        jdbcDriverJarPathText.setText("");

        if (this.needJdbcDriverJar(databaseInfoDef)) {
            jdbcDriverJarPathText.setEnabled(true);
            jdbcDriverJarPathText.setTransferHandler(new SwingUtil.FileTransferHandler(jdbcDriverJarPathText));
        } else {
            jdbcDriverJarPathText.setEnabled(false);
            jdbcDriverJarPathText.setTransferHandler(null);
        }
    }

    private String showSchemaSyncCheckEnvDialog(String project) {

        List<String> envList = new ArrayList<String>();
        File dfpropDir = new File(DBFluteIntro.BASIC_DIR_PATH, "dbflute_" + project + "/dfprop");
        for (File file : dfpropDir.listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("schemaSyncCheck_")) {
                envList.add(file.getName().substring("schemaSyncCheck_".length()));
            }
        }

        if (envList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Not found schemaSyncCheck schema.");
            return null;
        }

        Object obj = JOptionPane.showInputDialog(frame, "env", "schemaSyncCheck", JOptionPane.QUESTION_MESSAGE, null,
                envList.toArray(), null);
        if (obj == null) {
            JOptionPane.showMessageDialog(frame, "Canceled schemaSyncCheck.");
            return null;
        }

        return obj.toString();
    }

    class HyperlinkHandler implements HyperlinkListener {

        @Override
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == EventType.ACTIVATED) {

                URL url = event.getURL();

                String project = projectCombo.getSelectedItem().toString();

                if (url == null) {
                    JOptionPane.showMessageDialog(frame, "not found.");
                    return;
                }

                String decodePath = null;
                try {
                    decodePath = URLDecoder.decode(url.getFile(), System.getProperty("file.encoding"));
                } catch (UnsupportedEncodingException e1) {
                    JOptionPane.showMessageDialog(frame, "[" + url.getFile() + "] not found.");
                    return;
                }

                String env = null;
                if (decodePath.contains("sync-check-result_${env}.html")) {
                    env = showSchemaSyncCheckEnvDialog(project);
                    if (env == null) {
                        return;
                    }
                }

                File file = new File(decodePath.replaceAll("\\$\\{project\\}",
                        projectCombo.getSelectedItem().toString()).replaceAll("\\$\\{env\\}", env));

                try {
                    file = file.getCanonicalFile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "[" + file.getAbsolutePath() + "] not found.");
                    return;
                }

                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, "[" + file.getAbsolutePath() + "] not found.");
                    return;
                }

                Desktop desktop = Desktop.getDesktop();

                try {
                    file = new File(file.getCanonicalPath());

                    desktop.open(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, e);
                    return;
                }
            }
        }
    }
}
