package org.dbflute.intro.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.dbflute.emecha.eclipse.plugin.wizards.client.DBFluteNewClientPageResult;
import org.dbflute.emecha.eclipse.plugin.wizards.client.definition.DatabaseInfoDef;
import org.dbflute.intro.util.SwingUtil;

/**
 * @author ecode
 */
public class NewClientPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_REQUIRED = "(*)";
    protected static final String LABEL_PROJECT = "DB名";
    private static final String LABEL_DATABASE = "RDB";
    protected static final String LABEL_URL = "URL";
    protected static final String LABEL_SCHEMA = "スキーマ";
    protected static final String LABEL_USER = "ユーザ";
    protected static final String LABEL_PASSWORD = "パスワード";
    private static final String LABEL_JDBC_DRIVER_JAR_PATH = "Jdbcドライバのパス";
    private static final String LABEL_DBFLUTE_VERSION = "DBFluteバージョン";
    protected static final String LABEL_SCHEMA_SYNC_CHECK = "他のDB環境";
    private static final String LABEL_CLIENT_CREATE = "作成";
    protected static final String LABEL_PROJECT_TAB = "DB";

    private static final String DEF_PROJECT = "yourdb";

    private static final String MSG_JDBC_DRIVER_JAR_PATH = "jdbcドライバをダウンロードして、ドラッグ&ドロップしてください。. jdbcドライバが必要なRDBは、%1$sです。";
    private static final String MSG_SCHEMA_SYNC_CHECK_ENV = "DB環境を入力してください。";
    private static final String MSG_REQUIRED = "「%1$s」を入力してください。";
    private static final String MSG_EXIST_PROJECT = "DB名「%1$s」はすでに存在します。";
    private static final String MSG_CLIENT_CREATE_FINISHED = "作成しました。";
    private static final String MSG_DBFLUTE_VERSION = "DBFluteモジュールをダウンロードして下さい。(メニュー　→ ダウンロード(&アップグレード))";

    private JFrame frame;

    private JTextField projectText;
    private JComboBox databaseCombo;
    private JTextField databaseInfoUrlText;
    private JTextField databaseInfoSchemaText;
    private JTextField databaseInfoUserText;
    private JPasswordField databaseInfoPasswordText;
    private JTextField jdbcDriverJarPathText;
    private JComboBox versionInfoDBFluteCombo;

    private JTabbedPane schemaSyncCheckTabPanel;

    private JButton clientCreateButton;

    private final Action schemaSyncCheckAddAction = new SchemaSyncCheckAddAction();
    private final Action schemaSyncCheckRemoveAction = new SchemaSyncCheckRemoveAction();
    private final Action clientCreateAction = new ClientCreateAction();

    /**
     * Create the application.
     */
    public NewClientPanel(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    public void initialize() {

        this.setLayout(null);

        this.add(createLabale(LABEL_PROJECT + LABEL_REQUIRED, 10));
        this.add(createLabale(LABEL_DATABASE + LABEL_REQUIRED, 35));
        this.add(createLabale(LABEL_URL + LABEL_REQUIRED, 60));
        this.add(createLabale(LABEL_SCHEMA, 85));
        this.add(createLabale(LABEL_USER + LABEL_REQUIRED, 110));
        this.add(createLabale(LABEL_PASSWORD, 135));
        this.add(createLabale(LABEL_JDBC_DRIVER_JAR_PATH, 160));
        this.add(createLabale(LABEL_DBFLUTE_VERSION + LABEL_REQUIRED, 185));
        this.add(createLabale(LABEL_SCHEMA_SYNC_CHECK, 210));

        projectText = new JTextField(DEF_PROJECT);
        projectText.setBounds(150, 10, 300, 20);
        projectText.setColumns(10);
        this.add(projectText);

        databaseCombo = new JComboBox(DatabaseInfoDef.extractDatabaseList().toArray());
        databaseCombo.setBounds(150, 35, 300, 20);
        databaseCombo.setSelectedIndex(-1); // means no selection
        this.add(databaseCombo);

        databaseCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String atabaseInfo = databaseCombo.getSelectedItem().toString();
                DatabaseInfoDef databaseInfoDef = DatabaseInfoDef.findDatabaseInfo(atabaseInfo);
                fireDatabaseInfoUrlText(databaseInfoDef);
                fireDatabaseInfoSchemaText(databaseInfoDef);
                fireJdbcDriverJarText(databaseInfoDef);
            }
        });

        databaseInfoUrlText = new JTextField();
        databaseInfoUrlText.setBounds(150, 60, 300, 20);
        databaseInfoUrlText.setColumns(10);
        this.add(databaseInfoUrlText);

        databaseInfoSchemaText = new JTextField();
        databaseInfoSchemaText.setBounds(150, 85, 300, 20);
        databaseInfoSchemaText.setColumns(10);
        this.add(databaseInfoSchemaText);

        databaseInfoUserText = new JTextField();
        databaseInfoUserText.setBounds(150, 110, 300, 20);
        databaseInfoUserText.setColumns(10);
        this.add(databaseInfoUserText);

        databaseInfoPasswordText = new JPasswordField();
        databaseInfoPasswordText.setBounds(150, 135, 300, 20);
        databaseInfoPasswordText.setColumns(10);
        this.add(databaseInfoPasswordText);

        jdbcDriverJarPathText = new JTextField();
        jdbcDriverJarPathText.setBounds(150, 160, 300, 20);
        jdbcDriverJarPathText.setColumns(10);

        List<String> needJdbcDriverJarMessage = new ArrayList<String>();
        for (String database : DatabaseInfoDef.extractDatabaseList()) {
            if (DBFluteIntro.needJdbcDriverJar(DatabaseInfoDef.findDatabaseInfo(database))) {
                needJdbcDriverJarMessage.add(database);
            }
        }

        jdbcDriverJarPathText.setToolTipText(String.format(MSG_JDBC_DRIVER_JAR_PATH, needJdbcDriverJarMessage));
        this.add(jdbcDriverJarPathText);

        versionInfoDBFluteCombo = new JComboBox();
        versionInfoDBFluteCombo.setBounds(150, 185, 300, 20);
        this.add(versionInfoDBFluteCombo);

        JButton schemaSyncCheckAddButton = new JButton(schemaSyncCheckAddAction);
        schemaSyncCheckAddButton.setBounds(150, 210, 40, 20);
        this.add(schemaSyncCheckAddButton);

        JButton schemaSyncCheckRemoveButton = new JButton(schemaSyncCheckRemoveAction);
        schemaSyncCheckRemoveButton.setBounds(190, 210, 40, 20);
        this.add(schemaSyncCheckRemoveButton);

        clientCreateButton = new JButton(clientCreateAction);
        clientCreateButton.setBounds(150, 390, 300, 20);
        this.add(clientCreateButton);

        fireVersionInfoDBFlute();
    }

    private JLabel createLabale(String label, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(10, y, 150, 20);

        return jLabel;
    }

    private class SchemaSyncCheckAddAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SchemaSyncCheckAddAction() {
            putValue(NAME, "+");
        }

        public void actionPerformed(ActionEvent event) {

            String env = JOptionPane.showInputDialog(frame, MSG_SCHEMA_SYNC_CHECK_ENV);

            if (env != null) {
                if (schemaSyncCheckTabPanel == null) {
                    schemaSyncCheckTabPanel = new JTabbedPane();
                    schemaSyncCheckTabPanel.setBounds(0, 230, 480, 160);
                    add(schemaSyncCheckTabPanel);
                }

                schemaSyncCheckTabPanel.addTab(env, new SchemaSyncCheckPanal());
                SwingUtil.updateLookAndFeel(frame);
            }
        }
    }

    private class SchemaSyncCheckRemoveAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SchemaSyncCheckRemoveAction() {
            putValue(NAME, "-");
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
            putValue(NAME, LABEL_CLIENT_CREATE);
        }

        public void actionPerformed(ActionEvent event) {

            DBFluteNewClientPageResult result = asResult();

            Map<String, String> data = new LinkedHashMap<String, String>();
            data.put(LABEL_PROJECT, result.getProject());
            data.put(LABEL_DATABASE, result.getDatabase());
            data.put(LABEL_URL, result.getDatabaseInfoUrl());
            if (DBFluteIntro.needDatabaseInfoSchema(DatabaseInfoDef.findDatabaseInfo(result.getDatabase()))) {
                data.put(LABEL_SCHEMA, result.getDatabaseInfoSchema());
            }
            data.put(LABEL_USER, result.getDatabaseInfoUser());
            if (DBFluteIntro.needJdbcDriverJar(DatabaseInfoDef.findDatabaseInfo(result.getDatabase()))) {
                data.put(LABEL_JDBC_DRIVER_JAR_PATH, result.getJdbcDriverJarPath());
            }
            data.put(LABEL_DBFLUTE_VERSION, result.getVersionInfoDBFlute());

            if (schemaSyncCheckTabPanel != null) {
                for (int i = 0; i < schemaSyncCheckTabPanel.getTabCount(); i++) {
                    Component tabComponent = schemaSyncCheckTabPanel.getComponent(i);
                    if (!(tabComponent instanceof SchemaSyncCheckPanal)) {
                        continue;
                    }

                    SchemaSyncCheckPanal schemaSyncCheckPage = (SchemaSyncCheckPanal) tabComponent;
                    DBFluteNewClientPageResult dbFluteNewClientPageResult = schemaSyncCheckPage.asResult();

                    String env = schemaSyncCheckTabPanel.getTitleAt(i);
                    data.put(env + "." + LABEL_USER, dbFluteNewClientPageResult.getDatabaseInfoUser());
                }
            }

            for (Entry<String, String> entry : data.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_REQUIRED, entry.getKey()));
                    return;
                }
            }

            final File dbfluteClientDir = new File(DBFluteIntro.BASIC_DIR_PATH, "dbflute_" + result.getProject());

            if (dbfluteClientDir.exists()) {
                JOptionPane.showMessageDialog(frame, String.format(MSG_EXIST_PROJECT, result.getProject()));
                return;
            }

            DBFluteIntro dbFluteNewClient = new DBFluteIntro();
            dbFluteNewClient.createNewClient(result);

            if (schemaSyncCheckTabPanel != null) {
                for (int i = 0; i < schemaSyncCheckTabPanel.getTabCount(); i++) {
                    Component tabComponent = schemaSyncCheckTabPanel.getComponent(i);
                    if (!(tabComponent instanceof SchemaSyncCheckPanal)) {
                        continue;
                    }

                    SchemaSyncCheckPanal schemaSyncCheckPage = (SchemaSyncCheckPanal) tabComponent;
                    DBFluteNewClientPageResult dbFluteNewClientPageResult = schemaSyncCheckPage.asResult();
                    // TODO
                    dbFluteNewClientPageResult.setProject(projectText.getText());

                    dbFluteNewClient.createSchemaSyncCheck(schemaSyncCheckTabPanel.getTitleAt(i),
                            dbFluteNewClientPageResult);
                }
            }

            // TODO
            JTabbedPane tabPanel = (JTabbedPane) getParent();
            boolean flg = false;
            for (Component component : tabPanel.getComponents()) {
                if (component instanceof ClientPanel) {
                    flg = true;
                    ((ClientPanel) component).fireProjectCombo(projectText.getText());
                }
            }

            if (!flg) {
                ClientPanel clientPanel = new ClientPanel(frame);
                tabPanel.addTab(LABEL_PROJECT_TAB, clientPanel);
                clientPanel.fireProjectCombo(projectText.getText());
            }

            JOptionPane.showMessageDialog(frame, MSG_CLIENT_CREATE_FINISHED);
        }
    }

    public DBFluteNewClientPageResult asResult() {

        final String projectName = projectText.getText();
        Object selectedItem = databaseCombo.getSelectedItem();
        final String database = selectedItem != null ? selectedItem.toString() : null;
        DatabaseInfoDef databaseInfo = DatabaseInfoDef.findDatabaseInfo(database);
        final String databaseInfoDriver = databaseInfo != null ? databaseInfo.getDriverName() : null;
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

    protected void fireVersionInfoDBFlute() {

        versionInfoDBFluteCombo.removeAllItems();
        DBFluteIntro dbFluteIntro = new DBFluteIntro();
        List<String> existedDBFluteVersionList = dbFluteIntro.getExistedDBFluteVersionList();
        for (String version : existedDBFluteVersionList) {
            versionInfoDBFluteCombo.addItem(version);
        }

        versionInfoDBFluteCombo.setSelectedIndex(versionInfoDBFluteCombo.getItemCount() - 1);
        versionInfoDBFluteCombo.setToolTipText(MSG_DBFLUTE_VERSION);

        clientCreateButton.setEnabled(versionInfoDBFluteCombo.getItemCount() > 0);
    }

    private void fireDatabaseInfoUrlText(DatabaseInfoDef databaseInfoDef) {
        databaseInfoUrlText.setText(databaseInfoDef.getUrlTemplate());
    }

    private void fireDatabaseInfoSchemaText(DatabaseInfoDef databaseInfoDef) {

        databaseInfoSchemaText.setText("");

        if (DBFluteIntro.needDatabaseInfoSchema(databaseInfoDef)) {
            databaseInfoSchemaText.setText("dbo");
        }
    }

    private void fireJdbcDriverJarText(DatabaseInfoDef databaseInfoDef) {

        jdbcDriverJarPathText.setText("");

        if (DBFluteIntro.needJdbcDriverJar(databaseInfoDef)) {
            jdbcDriverJarPathText.setEnabled(true);
            jdbcDriverJarPathText.setTransferHandler(new SwingUtil.FileTransferHandler(jdbcDriverJarPathText));
        } else {
            jdbcDriverJarPathText.setEnabled(false);
            jdbcDriverJarPathText.setTransferHandler(null);
        }
    }
}
