package org.dbflute.intro.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dbflute.intro.ClientDto;
import org.dbflute.intro.DBFluteIntro;
import org.dbflute.intro.DatabaseDto;
import org.dbflute.intro.definition.DatabaseInfoDef;
import org.dbflute.intro.util.SwingUtil;

/**
 * @author ecode
 */
public class BasicPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_DATABASE = "DBMS";
    protected static final String LABEL_TARGET_LANGUAGE = "言語";
    protected static final String LABEL_TARGET_CONTAINER = "コンテナ";
    protected static final String LABEL_PACKAGE_BASE = "パッケージベース";
    protected static final String LABEL_JDBC_DRIVER_JAR_PATH = "JDBCドライバのパス";
    private static final String LABEL_FILE_CHOOSE = "ファイル選択";
    protected static final String LABEL_DBFLUTE_VERSION = "DBFluteバージョン";
    protected static final String LABEL_SYSTEM_USER_DATABASE = "システムユーザ ※DBスキーマ(再)構築 (ReplaceSchema)の初回で利用";

    private static final String MSG_DBFLUTE_VERSION = "DBFluteモジュールをダウンロードして下さい。(" + DBFluteIntroPage.LABEL_SETTING
            + " → " + DBFluteIntroPage.LABEL_DOWNLOAD;

    private JFrame frame;

    private JLabel jdbcDriverJarPathLabel;

    private JComboBox databaseCombo;
    private JComboBox targetLanguageCombo;
    private JComboBox targetContainerCombo;
    private JTextField packageBaseText;
    private DatabasePanal databasePanal;
    private DatabasePanal systemUserDatabasePanal;
    private JTextField jdbcDriverJarPathText;
    protected JComboBox dbfluteVersionCombo;

    /**
     * Create the application.
     */
    public BasicPanel(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    public void initialize() {

        this.setLayout(null);

        this.add(createLabel(LABEL_DATABASE + NewClientPanel.LABEL_REQUIRED, 10));
        this.add(createLabel(LABEL_TARGET_LANGUAGE + NewClientPanel.LABEL_REQUIRED, 35));
        this.add(createLabel(LABEL_TARGET_CONTAINER + NewClientPanel.LABEL_REQUIRED, 60));
        this.add(createLabel(LABEL_PACKAGE_BASE + NewClientPanel.LABEL_REQUIRED, 85));
        jdbcDriverJarPathLabel = createLabel(LABEL_JDBC_DRIVER_JAR_PATH, 215);
        this.add(jdbcDriverJarPathLabel);
        this.add(createLabel(LABEL_DBFLUTE_VERSION + NewClientPanel.LABEL_REQUIRED, 240));
        this.add(createLabel(LABEL_SYSTEM_USER_DATABASE, 290, 500));

        databaseCombo = new JComboBox(DatabaseInfoDef.values());
        databaseCombo.setBounds(150, 10, 300, 20);
        databaseCombo.setSelectedIndex(-1); // means no selection
        this.add(databaseCombo);

        databaseCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseInfoDef databaseInfoDef = (DatabaseInfoDef) databaseCombo.getSelectedItem();
                databasePanal.fireDatabaseUrlText(databaseInfoDef);
                databasePanal.fireDatabaseSchemaLabel(databaseInfoDef);
                databasePanal.fireDatabaseSchemaText(databaseInfoDef);
                fireJdbcDriverJarPathLabel(databaseInfoDef);
                fireJdbcDriverJarPathText(databaseInfoDef);
            }
        });

        targetLanguageCombo = new JComboBox(new String[] {"java", "csharp", "scala"});
        targetLanguageCombo.setBounds(150, 35, 300, 20);
        targetLanguageCombo.setSelectedIndex(-1); // means no selection
        this.add(targetLanguageCombo);

        targetContainerCombo = new JComboBox(new String[] {"seasar", "spring", "guice", "cdi"});
        targetContainerCombo.setBounds(150, 60, 300, 20);
        targetContainerCombo.setSelectedIndex(-1); // means no selection
        this.add(targetContainerCombo);

        packageBaseText = new JTextField();
        packageBaseText.setBounds(150, 85, 300, 20);
        packageBaseText.setColumns(10);
        this.add(packageBaseText);

        databasePanal = new DatabasePanal(true);
        databasePanal.setBounds(0, 100, 480, 110);
        this.add(databasePanal);

        jdbcDriverJarPathText = new JTextField();
        jdbcDriverJarPathText.setBounds(150, 215, 210, 20);
        jdbcDriverJarPathText.setColumns(10);
        this.add(jdbcDriverJarPathText);

        JButton jdbcDriverJarPathButton = new JButton(new JdbcDriverJarPathAction());
        jdbcDriverJarPathButton.setBounds(360, 215, 90, 20);
        this.add(jdbcDriverJarPathButton);

        dbfluteVersionCombo = new JComboBox();
        dbfluteVersionCombo.setBounds(150, 240, 300, 20);
        this.add(dbfluteVersionCombo);

        systemUserDatabasePanal = new DatabasePanal(false);
        systemUserDatabasePanal.setBounds(0, 305, 480, 110);
        this.add(systemUserDatabasePanal);

        fireVersionInfoDBFlute();
    }

    private JLabel createLabel(String label, int y) {
        return createLabel(label, y, 150);
    }

    private JLabel createLabel(String label, int y, int width) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(10, y, width, 20);

        return jLabel;
    }

    private class JdbcDriverJarPathAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public JdbcDriverJarPathAction() {
            putValue(NAME, LABEL_FILE_CHOOSE);
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

    private void fireJdbcDriverJarPathLabel(DatabaseInfoDef databaseInfoDef) {
        boolean required = databaseInfoDef != null && databaseInfoDef.needJdbcDriverJar();
        jdbcDriverJarPathLabel.setText(LABEL_JDBC_DRIVER_JAR_PATH + (required ? NewClientPanel.LABEL_REQUIRED : ""));
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

    protected void fireVersionInfoDBFlute() {

        dbfluteVersionCombo.removeAllItems();
        DBFluteIntro dbFluteIntro = new DBFluteIntro();
        List<String> existedDBFluteVersionList = dbFluteIntro.getExistedDBFluteVersionList();
        for (String version : existedDBFluteVersionList) {
            dbfluteVersionCombo.addItem(version);
        }

        dbfluteVersionCombo.setSelectedIndex(dbfluteVersionCombo.getItemCount() - 1);
        dbfluteVersionCombo.setToolTipText(MSG_DBFLUTE_VERSION);
    }

    public ClientDto asResult() {

        DatabaseInfoDef databaseInfo = (DatabaseInfoDef) databaseCombo.getSelectedItem();
        final String database = databaseInfo != null ? databaseInfo.databaseName() : null;
        final String targetLanguage = (String) targetLanguageCombo.getSelectedItem();
        final String targetContainer = (String) targetContainerCombo.getSelectedItem();
        final String packageBase = packageBaseText.getText();
        final String databaseInfoDriver = databaseInfo != null ? databaseInfo.driverName() : null;
        final DatabaseDto databaseDto = databasePanal.asResult();
        final DatabaseDto systemUserDatabaseDto = systemUserDatabasePanal.asResult();
        final String jdbcDriverJarPath = jdbcDriverJarPathText.getText();
        final String versionInfoDBFlute = dbfluteVersionCombo.getSelectedItem().toString();

        final ClientDto clientDto = new ClientDto();
        clientDto.setDatabase(database);
        clientDto.setTargetLanguage(targetLanguage);
        clientDto.setTargetContainer(targetContainer);
        clientDto.setPackageBase(packageBase);
        clientDto.setJdbcDriver(databaseInfoDriver);
        clientDto.setDatabaseDto(databaseDto);
        clientDto.setSystemUserDatabaseDto(systemUserDatabaseDto);
        clientDto.setJdbcDriverJarPath(jdbcDriverJarPath);
        clientDto.setDbfluteVersion(versionInfoDBFlute);

        return clientDto;
    }

    protected void reflect(ClientDto clientDto, Map<String, DatabaseDto> envDatabaseDtoMap) {

        databaseCombo.setSelectedItem(DatabaseInfoDef.codeOf(clientDto.getDatabase()));
        targetLanguageCombo.setSelectedItem(clientDto.getTargetLanguage());
        targetContainerCombo.setSelectedItem(clientDto.getTargetContainer());
        packageBaseText.setText(clientDto.getPackageBase());
        databasePanal.reflect(clientDto.getDatabaseDto());
        systemUserDatabasePanal.reflect(clientDto.getSystemUserDatabaseDto());
        jdbcDriverJarPathText.setText(clientDto.getJdbcDriverJarPath());
        dbfluteVersionCombo.setSelectedItem(clientDto.getDbfluteVersion());
    }
}
