package org.dbflute.intro.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;

import org.dbflute.intro.ClientDto;
import org.dbflute.intro.DBFluteIntro;
import org.dbflute.intro.DatabaseDto;
import org.dbflute.intro.definition.DatabaseInfoDef;
import org.dbflute.intro.util.SwingUtil;
import org.dbflute.intro.util.SwingUtil.ReflectionCaretListener;

/**
 * @author ecode
 */
public class NewClientPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_REQUIRED = "(*)";
    protected static final String LABEL_PROJECT = "DB名";
    private static final String LABEL_DATABASE = "DBMS";
    private static final String LABEL_TARGET_LANGUAGE = "言語";
    private static final String LABEL_TARGET_CONTAINER = "コンテナ";
    private static final String LABEL_PACKAGE_BASE = "パッケージベース";
    protected static final String LABEL_URL = "URL";
    protected static final String LABEL_SCHEMA = "スキーマ";
    protected static final String LABEL_USER = "ユーザ";
    protected static final String LABEL_PASSWORD = "パスワード";
    private static final String LABEL_JDBC_DRIVER_JAR_PATH = "JDBCドライバのパス";
    private static final String LABEL_File_CHOOSE = "ファイル選択";
    private static final String LABEL_DBFLUTE_VERSION = "DBFluteバージョン";
    private static final String LABEL_OPTION = "オプション";
    private static final String LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS = "DBコメントを別名基準で利用";
    private static final String LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF = "差分判定でカラム定義順をチェック";
    private static final String LABEL_IS_CHECK_DB_COMMENT_DIFF = "差分判定でDBコメントをチェック";
    private static final String LABEL_IS_CHECK_PROCEDURE_DIFF = "差分判定でプロシージャをチェック";
    private static final String LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN = "プロシージャ自動生成";
    protected static final String LABEL_SCHEMA_SYNC_CHECK = "他のDB環境";
    private static final String LABEL_CLIENT_CREATE = "作成";
    protected static final String LABEL_CLIENT_CHANGE = "変更";
    protected static final String LABEL_CLIENT_CANCEL = "キャンセル";
    protected static final String LABEL_PROJECT_TAB = "DB";

    private static final String DEF_PROJECT = "yourdb";

    private static final String MSG_SCHEMA_SYNC_CHECK_ENV = "DB環境を入力してください。";
    private static final String MSG_REQUIRED = "「%1$s」を入力してください。";
    private static final String MSG_INVALID = "%1$s「%2$s」が不正です。";
    private static final String MSG_EXIST_PROJECT = LABEL_PROJECT + "「%1$s」がすでに存在します。";
    private static final String MSG_DUPLICATE_CHEMA_SYNC_CHECK = LABEL_SCHEMA_SYNC_CHECK + "の名前が重複しています。";
    private static final String MSG_TEST_CONNECTION_ERROR = "DBに接続できませんでした。作成を続けますか。\r\n%1$s";
    private static final String MSG_CLIENT_CREATE_ERROR = "%1$sに失敗しました。";
    private static final String MSG_CLIENT_CREATE_FINISHED = "%1$sしました。";
    private static final String MSG_DBFLUTE_VERSION = "DBFluteモジュールをダウンロードして下さい。(" + DBFluteIntroPage.LABEL_SETTING
            + " → " + DBFluteIntroPage.LABEL_DOWNLOAD;

    private JFrame frame;

    private JLabel databaseSchemaLabel;
    private JLabel jdbcDriverJarPathLabel;

    private JTextField projectText;
    private JComboBox databaseCombo;
    private JComboBox targetLanguageCombo;
    private JComboBox targetContainerCombo;
    private JTextField packageBaseText;
    private JTextField databaseUrlText;
    private JTextField databaseSchemaText;
    private JTextField databaseUserText;
    private JPasswordField databasePasswordText;
    private JTextField jdbcDriverJarPathText;
    private JComboBox dbfluteVersionCombo;
    private Map<String, JCheckBox> optionMap = new LinkedHashMap<String, JCheckBox>();

    private JTabbedPane schemaSyncCheckTabPanel;

    private JButton clientCreateButton;

    private ReflectionCaretListener userReflectionCaretListener;

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

        this.add(createLabal(LABEL_PROJECT + LABEL_REQUIRED, 10));
        this.add(createLabal(LABEL_DATABASE + LABEL_REQUIRED, 35));
        this.add(createLabal(LABEL_TARGET_LANGUAGE + LABEL_REQUIRED, 60));
        this.add(createLabal(LABEL_TARGET_CONTAINER + LABEL_REQUIRED, 85));
        this.add(createLabal(LABEL_PACKAGE_BASE + LABEL_REQUIRED, 110));
        this.add(createLabal(LABEL_URL + LABEL_REQUIRED, 135));
        databaseSchemaLabel = createLabal(LABEL_SCHEMA, 160);
        this.add(databaseSchemaLabel);
        this.add(createLabal(LABEL_USER + LABEL_REQUIRED, 185));
        this.add(createLabal(LABEL_PASSWORD, 210));
        jdbcDriverJarPathLabel = createLabal(LABEL_JDBC_DRIVER_JAR_PATH, 235);
        this.add(jdbcDriverJarPathLabel);
        this.add(createLabal(LABEL_DBFLUTE_VERSION + LABEL_REQUIRED, 260));

        this.add(createLabal(LABEL_OPTION, 285));
        this.add(createOption(LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS, 285));
        this.add(createOption(LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF, 310));
        this.add(createOption(LABEL_IS_CHECK_DB_COMMENT_DIFF, 335));
        this.add(createOption(LABEL_IS_CHECK_PROCEDURE_DIFF, 360));
        this.add(createOption(LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN, 385));
        this.add(createLabal(LABEL_SCHEMA_SYNC_CHECK, 415));

        projectText = new JTextField(DEF_PROJECT);
        projectText.setBounds(150, 10, 300, 20);
        projectText.setColumns(10);
        this.add(projectText);

        databaseCombo = new JComboBox(DatabaseInfoDef.values());
        databaseCombo.setBounds(150, 35, 300, 20);
        databaseCombo.setSelectedIndex(-1); // means no selection
        this.add(databaseCombo);

        databaseCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseInfoDef databaseInfoDef = (DatabaseInfoDef) databaseCombo.getSelectedItem();
                fireDatabaseUrlText(databaseInfoDef);
                fireDatabaseSchemaLabel(databaseInfoDef);
                fireDatabaseSchemaText(databaseInfoDef);
                fireJdbcDriverJarPathLabel(databaseInfoDef);
                fireJdbcDriverJarPathText(databaseInfoDef);
            }
        });

        targetLanguageCombo = new JComboBox(new String[] {"java", "csharp", "scala"});
        targetLanguageCombo.setBounds(150, 60, 300, 20);
        targetLanguageCombo.setSelectedIndex(-1); // means no selection
        this.add(targetLanguageCombo);

        targetContainerCombo = new JComboBox(new String[] {"seasar", "spring", "guice", "cdi"});
        targetContainerCombo.setBounds(150, 85, 300, 20);
        targetContainerCombo.setSelectedIndex(-1); // means no selection
        this.add(targetContainerCombo);

        packageBaseText = new JTextField();
        packageBaseText.setBounds(150, 110, 300, 20);
        packageBaseText.setColumns(10);
        this.add(packageBaseText);

        databaseUrlText = new JTextField();
        databaseUrlText.setBounds(150, 135, 300, 20);
        databaseUrlText.setColumns(10);
        this.add(databaseUrlText);

        databaseSchemaText = new JTextField();
        databaseSchemaText.setBounds(150, 160, 300, 20);
        databaseSchemaText.setColumns(10);
        this.add(databaseSchemaText);

        databaseUserText = new JTextField();
        databaseUserText.setBounds(150, 185, 300, 20);
        databaseUserText.setColumns(10);
        this.add(databaseUserText);

        databasePasswordText = new JPasswordField();
        databasePasswordText.setBounds(150, 210, 300, 20);
        databasePasswordText.setColumns(10);
        this.add(databasePasswordText);

        jdbcDriverJarPathText = new JTextField();
        jdbcDriverJarPathText.setBounds(150, 235, 210, 20);
        jdbcDriverJarPathText.setColumns(10);
        this.add(jdbcDriverJarPathText);

        JButton jdbcDriverJarPathButton = new JButton(new JdbcDriverJarPathAction());
        jdbcDriverJarPathButton.setBounds(360, 235, 90, 20);
        this.add(jdbcDriverJarPathButton);

        dbfluteVersionCombo = new JComboBox();
        dbfluteVersionCombo.setBounds(150, 260, 300, 20);
        this.add(dbfluteVersionCombo);

        JButton schemaSyncCheckAddButton = new JButton(new SchemaSyncCheckAddAction());
        schemaSyncCheckAddButton.setBounds(150, 415, 40, 20);
        this.add(schemaSyncCheckAddButton);

        JButton schemaSyncCheckRemoveButton = new JButton(new SchemaSyncCheckRemoveAction());
        schemaSyncCheckRemoveButton.setBounds(190, 415, 40, 20);
        this.add(schemaSyncCheckRemoveButton);

        clientCreateButton = new JButton(new ClientCreateAction(LABEL_CLIENT_CREATE));
        clientCreateButton.setBounds(150, 595, 300, 20);
        this.add(clientCreateButton);

        fireVersionInfoDBFlute();
    }

    private JLabel createLabal(String label, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(10, y, 150, 20);

        return jLabel;
    }

    private JCheckBox createOption(String label, int y) {
        JCheckBox checkBox = new JCheckBox(label, true);
        checkBox.setBounds(150, y, 300, 20);
        optionMap.put(label, checkBox);

        return checkBox;
    }

    private class JdbcDriverJarPathAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public JdbcDriverJarPathAction() {
            putValue(NAME, LABEL_File_CHOOSE);
        }

        public void actionPerformed(ActionEvent event) {

            String jdbcDriverJarPath = jdbcDriverJarPathText.getText();

            JFileChooser fileChooser = new JFileChooser(new File(jdbcDriverJarPath).getParent());
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jar", "jar");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    jdbcDriverJarPathText.setText(fileChooser.getSelectedFile().getCanonicalPath());
                } catch (IOException e1) {
                    // ignore
                }
            }
        }
    }

    private class SchemaSyncCheckAddAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SchemaSyncCheckAddAction() {
            putValue(NAME, "+");
        }

        public void actionPerformed(ActionEvent event) {

            String env = JOptionPane.showInputDialog(frame, MSG_SCHEMA_SYNC_CHECK_ENV);

            if (env != null && !env.equals("")) {
                if (schemaSyncCheckTabPanel == null) {
                    schemaSyncCheckTabPanel = new JTabbedPane();
                    schemaSyncCheckTabPanel.setBounds(0, 435, 480, 160);
                    add(schemaSyncCheckTabPanel);
                }

                SchemaSyncCheckPanal schemaSyncCheckPanal = new SchemaSyncCheckPanal();
                schemaSyncCheckTabPanel.addTab(env, schemaSyncCheckPanal);
                schemaSyncCheckTabPanel.setSelectedComponent(schemaSyncCheckPanal);
                schemaSyncCheckTabPanel.setVisible(schemaSyncCheckTabPanel.getTabCount() != 0);
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

            schemaSyncCheckTabPanel.setVisible(schemaSyncCheckTabPanel.getTabCount() != 0);
        }
    }

    private class ClientCreateAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ClientCreateAction(String name) {
            putValue(NAME, name);
        }

        public void actionPerformed(ActionEvent event) {

            ClientDto clientDto = asResult();

            Map<String, String> data = new LinkedHashMap<String, String>();
            data.put(LABEL_PROJECT, clientDto.getProject());
            data.put(LABEL_DATABASE, clientDto.getDatabase());
            data.put(LABEL_TARGET_LANGUAGE, clientDto.getTargetLanguage());
            data.put(LABEL_TARGET_CONTAINER, clientDto.getTargetContainer());
            data.put(LABEL_PACKAGE_BASE, clientDto.getPackageBase());
            data.put(LABEL_URL, clientDto.getDatabaseDto().getUrl());
            DatabaseInfoDef databaseInfoDef = DatabaseInfoDef.codeOf(clientDto.getDatabase());
            if (databaseInfoDef != null && databaseInfoDef.needSchema()) {
                data.put(LABEL_SCHEMA, clientDto.getDatabaseDto().getSchema());
            }
            data.put(LABEL_USER, clientDto.getDatabaseDto().getUser());
            if (databaseInfoDef != null && databaseInfoDef.needJdbcDriverJar()) {
                data.put(LABEL_JDBC_DRIVER_JAR_PATH, clientDto.getJdbcDriverJarPath());
            }
            data.put(LABEL_DBFLUTE_VERSION, clientDto.getDbfluteVersion());

            Map<String, DatabaseDto> schemaSyncCheckMap = new LinkedHashMap<String, DatabaseDto>();
            int schemaSyncCheckCount = schemaSyncCheckTabPanel == null ? 0 : schemaSyncCheckTabPanel.getTabCount();
            for (int i = 0; i < schemaSyncCheckCount; i++) {
                Component tabComponent = schemaSyncCheckTabPanel.getComponent(i);
                if (!(tabComponent instanceof SchemaSyncCheckPanal)) {
                    continue;
                }

                SchemaSyncCheckPanal schemaSyncCheckPage = (SchemaSyncCheckPanal) tabComponent;
                DatabaseDto databaseDto = schemaSyncCheckPage.asResult();

                schemaSyncCheckMap.put(schemaSyncCheckTabPanel.getTitleAt(i), databaseDto);
                data.put(schemaSyncCheckTabPanel.getTitleAt(i) + "." + LABEL_USER, databaseDto.getUser());
            }

            for (Entry<String, String> entry : data.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_REQUIRED, entry.getKey()));
                    return;
                }
            }

            final File dbfluteClientDir = new File(DBFluteIntro.BASE_DIR_PATH, "dbflute_" + clientDto.getProject());
            if (projectText.isEnabled() && dbfluteClientDir.exists()) {
                JOptionPane.showMessageDialog(frame, String.format(MSG_EXIST_PROJECT, clientDto.getProject()));
                return;
            }

            if (clientDto.getJdbcDriverJarPath() != null && !clientDto.getJdbcDriverJarPath().equals("")) {
                final File jdbcDriverJarFile = new File(clientDto.getJdbcDriverJarPath());
                if (!jdbcDriverJarFile.exists() || !jdbcDriverJarFile.isFile()) {
                    JOptionPane.showMessageDialog(frame,
                            String.format(MSG_INVALID, LABEL_JDBC_DRIVER_JAR_PATH, clientDto.getJdbcDriverJarPath()));
                    return;
                }
            }

            if (schemaSyncCheckCount != schemaSyncCheckMap.size()) {
                JOptionPane.showMessageDialog(frame, MSG_DUPLICATE_CHEMA_SYNC_CHECK);
                return;
            }

            DBFluteIntro dbFluteIntro = new DBFluteIntro();
            try {
                dbFluteIntro.testConnection(clientDto, schemaSyncCheckMap);
            } catch (RuntimeException e) {
                int result = JOptionPane.showConfirmDialog(frame,
                        String.format(MSG_TEST_CONNECTION_ERROR, e.getMessage()), null, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try {
                dbFluteIntro.createNewClient(clientDto, schemaSyncCheckMap);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, String.format(MSG_CLIENT_CREATE_ERROR, getValue(NAME)));
                return;
            }

            // TODO
            JTabbedPane tabPanel = (JTabbedPane) getParent();
            ClientPanel clientPanel = null;
            for (Component component : tabPanel.getComponents()) {
                if (component instanceof ClientPanel) {
                    clientPanel = (ClientPanel) component;
                    break;
                }
            }

            if (clientPanel == null) {
                clientPanel = new ClientPanel(frame);
                tabPanel.addTab(LABEL_PROJECT_TAB, clientPanel);
            }

            // TODO 更新の場合の後処理
            if (!projectText.isEnabled()) {
                tabPanel.remove(NewClientPanel.this);
                tabPanel.addTab("+", new NewClientPanel(frame));
            }

            clientPanel.fireProjectCombo(projectText.getText());

            JOptionPane.showMessageDialog(frame, String.format(MSG_CLIENT_CREATE_FINISHED, getValue(NAME)));
        }
    }

    public ClientDto asResult() {

        final String projectName = projectText.getText();
        DatabaseInfoDef databaseInfo = (DatabaseInfoDef) databaseCombo.getSelectedItem();
        final String database = databaseInfo != null ? databaseInfo.databaseName() : null;
        final String targetLanguage = (String) targetLanguageCombo.getSelectedItem();
        final String targetContainer = (String) targetContainerCombo.getSelectedItem();
        final String packageBase = packageBaseText.getText();
        final String databaseInfoDriver = databaseInfo != null ? databaseInfo.driverName() : null;
        final String databaseInfoUrl = databaseUrlText.getText();
        final String databaseInfoSchema = databaseSchemaText.getText();
        final String databaseInfoUser = databaseUserText.getText();
        final String databaseInfoPassword = new String(databasePasswordText.getPassword());
        final String jdbcDriverJarPath = jdbcDriverJarPathText.getText();
        final String versionInfoDBFlute = dbfluteVersionCombo.getSelectedItem().toString();

        final ClientDto clientDto = new ClientDto();
        clientDto.setProject(projectName);
        clientDto.setDatabase(database);
        clientDto.setTargetLanguage(targetLanguage);
        clientDto.setTargetContainer(targetContainer);
        clientDto.setPackageBase(packageBase);
        clientDto.setJdbcDriver(databaseInfoDriver);
        clientDto.getDatabaseDto().setUrl(databaseInfoUrl);
        clientDto.getDatabaseDto().setSchema(databaseInfoSchema);
        clientDto.getDatabaseDto().setUser(databaseInfoUser);
        clientDto.getDatabaseDto().setPassword(databaseInfoPassword);
        clientDto.setJdbcDriverJarPath(jdbcDriverJarPath);
        clientDto.setDbfluteVersion(versionInfoDBFlute);

        clientDto.setDbCommentOnAliasBasis(optionMap.get(LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS).isSelected());
        clientDto.setCheckColumnDefOrderDiff(optionMap.get(LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF).isSelected());
        clientDto.setCheckDbCommentDiff(optionMap.get(LABEL_IS_CHECK_DB_COMMENT_DIFF).isSelected());
        clientDto.setCheckProcedureDiff(optionMap.get(LABEL_IS_CHECK_PROCEDURE_DIFF).isSelected());
        clientDto.setGenerateProcedureParameterBean(optionMap.get(LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN)
                .isSelected());

        return clientDto;
    }

    protected void fireVersionInfoDBFlute() {

        dbfluteVersionCombo.removeAllItems();
        DBFluteIntro dbFluteIntro = new DBFluteIntro();
        List<String> existedDBFluteVersionList = dbFluteIntro.getExistedDBFluteVersionList();
        for (String version : existedDBFluteVersionList) {
            dbfluteVersionCombo.addItem(version);
        }

        dbfluteVersionCombo.setSelectedIndex(dbfluteVersionCombo.getItemCount() - 1);
        dbfluteVersionCombo.setToolTipText(MSG_DBFLUTE_VERSION);

        clientCreateButton.setEnabled(dbfluteVersionCombo.getItemCount() > 0);
    }

    private void fireDatabaseUrlText(DatabaseInfoDef databaseInfoDef) {
        databaseUrlText.setText(databaseInfoDef.getUrlTemplate());
    }

    private void fireDatabaseSchemaLabel(DatabaseInfoDef databaseInfoDef) {
        boolean required = databaseInfoDef != null && databaseInfoDef.needSchema();
        databaseSchemaLabel.setText(LABEL_SCHEMA + (required ? LABEL_REQUIRED : ""));
    }

    private void fireDatabaseSchemaText(DatabaseInfoDef databaseInfoDef) {
        if (databaseInfoDef == null) {
            return;
        }

        if (EnumSet.of(DatabaseInfoDef.Oracle, DatabaseInfoDef.DB2).contains(databaseInfoDef)) {
            ((AbstractDocument) databaseSchemaText.getDocument()).setDocumentFilter(SwingUtil.UPPER_DOCUMENT_FILTER);
        } else {
            ((AbstractDocument) databaseSchemaText.getDocument()).setDocumentFilter(null);
        }

        // Oracleは、ユーザとスキーマが一致することが多いため、入力補助する
        databaseSchemaText.removeCaretListener(userReflectionCaretListener);
        if (DatabaseInfoDef.Oracle == databaseInfoDef) {
            if (userReflectionCaretListener == null) {
                userReflectionCaretListener = new ReflectionCaretListener(databaseUserText);
            }
            databaseSchemaText.addCaretListener(userReflectionCaretListener);
        }

        databaseSchemaText.setText(databaseInfoDef.getDefultSchema());
    }

    private void fireJdbcDriverJarPathLabel(DatabaseInfoDef databaseInfoDef) {
        boolean required = databaseInfoDef != null && databaseInfoDef.needJdbcDriverJar();
        jdbcDriverJarPathLabel.setText(LABEL_JDBC_DRIVER_JAR_PATH + (required ? LABEL_REQUIRED : ""));
    }

    private void fireJdbcDriverJarPathText(DatabaseInfoDef databaseInfoDef) {

        jdbcDriverJarPathText.setText("");

        if (databaseInfoDef != null && databaseInfoDef.needJdbcDriverJar()) {
            jdbcDriverJarPathText.setEnabled(true);
            jdbcDriverJarPathText.setTransferHandler(new SwingUtil.FileTransferHandler(jdbcDriverJarPathText));
        } else {
            jdbcDriverJarPathText.setEnabled(false);
            jdbcDriverJarPathText.setTransferHandler(null);
        }
    }

    protected void reflect(ClientDto clientDto, Map<String, DatabaseDto> envDatabaseDtoMap) {

        projectText.setText(clientDto.getProject());
        databaseCombo.setSelectedItem(DatabaseInfoDef.codeOf(clientDto.getDatabase()));
        targetLanguageCombo.setSelectedItem(clientDto.getTargetLanguage());
        targetContainerCombo.setSelectedItem(clientDto.getTargetContainer());
        packageBaseText.setText(clientDto.getPackageBase());
        jdbcDriverJarPathText.setText(clientDto.getJdbcDriverJarPath());
        databaseUrlText.setText(clientDto.getDatabaseDto().getUrl());
        databaseSchemaText.setText(clientDto.getDatabaseDto().getSchema());
        databaseUserText.setText(clientDto.getDatabaseDto().getUser());
        databasePasswordText.setText(clientDto.getDatabaseDto().getPassword());
        dbfluteVersionCombo.setSelectedItem(clientDto.getDbfluteVersion());

        optionMap.get(LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS).setSelected(clientDto.isDbCommentOnAliasBasis());
        optionMap.get(LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF).setSelected(clientDto.isCheckColumnDefOrderDiff());
        optionMap.get(LABEL_IS_CHECK_DB_COMMENT_DIFF).setSelected(clientDto.isCheckDbCommentDiff());
        optionMap.get(LABEL_IS_CHECK_PROCEDURE_DIFF).setSelected(clientDto.isCheckProcedureDiff());
        optionMap.get(LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN).setSelected(
                clientDto.isGenerateProcedureParameterBean());

        if (schemaSyncCheckTabPanel == null && !envDatabaseDtoMap.isEmpty()) {
            schemaSyncCheckTabPanel = new JTabbedPane();
            schemaSyncCheckTabPanel.setBounds(0, 360, 480, 160);
            add(schemaSyncCheckTabPanel);
        }

        for (Entry<String, DatabaseDto> entry : envDatabaseDtoMap.entrySet()) {

            SchemaSyncCheckPanal schemaSyncCheckPanal = new SchemaSyncCheckPanal();
            schemaSyncCheckPanal.reflect(entry.getValue());
            schemaSyncCheckTabPanel.addTab(entry.getKey(), schemaSyncCheckPanal);
            schemaSyncCheckTabPanel.setVisible(schemaSyncCheckTabPanel.getTabCount() != 0);
        }

        projectText.setEnabled(false);
        clientCreateButton.setVisible(false);

        JButton changeButton = new JButton(new ClientCreateAction(LABEL_CLIENT_CHANGE));
        changeButton.setBounds(150, 595, 130, 20);
        this.add(changeButton);

        JButton cancelButton = new JButton(new AbstractAction(LABEL_CLIENT_CANCEL) {

            @Override
            public void actionPerformed(ActionEvent e) {
                JTabbedPane tabPanel = (JTabbedPane) getParent();
                tabPanel.remove(NewClientPanel.this);
                tabPanel.addTab("+", new NewClientPanel(frame));
                ClientPanel clientPanel = new ClientPanel(frame);
                tabPanel.addTab(LABEL_PROJECT_TAB, clientPanel);
                clientPanel.fireProjectCombo(projectText.getText());
            }

        });
        cancelButton.setBounds(300, 595, 130, 20);
        this.add(cancelButton);
    }
}
