package org.dbflute.intro.app.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.definition.DatabaseInfoDef;
import org.dbflute.intro.app.logic.DbFluteClientLogic;
import org.dbflute.intro.app.logic.DbFluteIntroLogic;

/**
 * @author p1us2er0
 */
public class NewClientPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_REQUIRED = "(*)";
    protected static final String LABEL_PROJECT = "DB名";
    private static final String LABEL_CLIENT_CREATE = "作成";
    protected static final String LABEL_CLIENT_CHANGE = "変更";
    private static final String LABEL_CLIENT_CANCEL = "キャンセル";

    protected static final String LABEL_PROJECT_TAB = "DB";

    private static final String DEF_PROJECT = "yourdb";

    private static final String MSG_REQUIRED = "「%1$s」を入力してください。";
    private static final String MSG_INVALID = "%1$s「%2$s」が不正です。";
    private static final String MSG_EXIST_PROJECT = LABEL_PROJECT + "「%1$s」がすでに存在します。";
    private static final String MSG_DUPLICATE_CHEMA_SYNC_CHECK = OptionPanel.LABEL_SCHEMA_SYNC_CHECK + "の名前が重複しています。";
    private static final String MSG_TEST_CONNECTION_ERROR = "DBに接続できませんでした。作成を続けますか。\r\n%1$s";
    private static final String MSG_CLIENT_CREATE_ERROR = "%1$sに失敗しました。";
    private static final String MSG_CLIENT_CREATE_FINISHED = "%1$sしました。";

    private JFrame frame;

    private JTextField projectText;
    protected BasicPanel basicPanel;
    private OptionPanel optionPanel;

    protected JButton clientCreateButton;

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

        this.add(createLabel(LABEL_PROJECT + LABEL_REQUIRED, 10));

        projectText = new JTextField(DEF_PROJECT);
        projectText.setBounds(150, 10, 300, 20);
        projectText.setColumns(10);
        this.add(projectText);

        JTabbedPane tabbedPanel = new JTabbedPane();
        tabbedPanel.setBounds(0, 30, 480, 500);
        add(tabbedPanel);

        basicPanel = new BasicPanel(frame);
        basicPanel.setBounds(0, 30, 480, 270);
        tabbedPanel.addTab("基本", basicPanel);
        tabbedPanel.setSelectedComponent(basicPanel);

        optionPanel = new OptionPanel(frame);
        optionPanel.setBounds(0, 300, 480, 350);
        tabbedPanel.addTab("オプション", optionPanel);

        clientCreateButton = new JButton(new ClientCreateAction(LABEL_CLIENT_CREATE));
        clientCreateButton.setBounds(150, 595, 300, 20);
        // TODO
        //clientCreateButton.setEnabled(!dbFluteIntro.getExistedDBFluteVersionList().isEmpty());
        this.add(clientCreateButton);
    }

    private JLabel createLabel(String label, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(10, y, 150, 20);

        return jLabel;
    }

    private class ClientCreateAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ClientCreateAction(String name) {
            putValue(NAME, name);
        }

        public void actionPerformed(ActionEvent event) {

            ClientBean clientBean = asResult();

            Map<String, String> data = new LinkedHashMap<String, String>();
            data.put(LABEL_PROJECT, clientBean.getProject());
            data.put(BasicPanel.LABEL_DATABASE, clientBean.getDatabase());
            data.put(BasicPanel.LABEL_TARGET_LANGUAGE, clientBean.getTargetLanguage());
            data.put(BasicPanel.LABEL_TARGET_CONTAINER, clientBean.getTargetContainer());
            data.put(BasicPanel.LABEL_PACKAGE_BASE, clientBean.getPackageBase());
            data.put(DatabasePanal.LABEL_URL, clientBean.getDatabaseBean().getUrl());
            DatabaseInfoDef databaseInfoDef = DatabaseInfoDef.codeOf(clientBean.getDatabase());
            if (databaseInfoDef != null && databaseInfoDef.isNeedSchema()) {
                data.put(DatabasePanal.LABEL_SCHEMA, clientBean.getDatabaseBean().getSchema());
            }
            data.put(DatabasePanal.LABEL_USER, clientBean.getDatabaseBean().getUser());
            if (databaseInfoDef != null && databaseInfoDef.isNeedJdbcDriverJar()) {
                data.put(BasicPanel.LABEL_JDBC_DRIVER_JAR_PATH, clientBean.getJdbcDriverJarPath());
            }
            data.put(BasicPanel.LABEL_DBFLUTE_VERSION, clientBean.getDbfluteVersion());

            Map<String, DatabaseBean> schemaSyncCheckMap = new LinkedHashMap<String, DatabaseBean>();
            JTabbedPane schemaSyncCheckTabPanel = optionPanel.schemaSyncCheckTabPanel;
            int schemaSyncCheckCount = schemaSyncCheckTabPanel == null ? 0 : schemaSyncCheckTabPanel.getTabCount();
            for (int i = 0; i < schemaSyncCheckCount; i++) {
                Component tabComponent = schemaSyncCheckTabPanel.getComponent(i);
                if (!(tabComponent instanceof DatabasePanal)) {
                    continue;
                }

                DatabasePanal schemaSyncCheckPage = (DatabasePanal) tabComponent;
                DatabaseBean databaseBean = schemaSyncCheckPage.asResult();

                schemaSyncCheckMap.put(schemaSyncCheckTabPanel.getTitleAt(i), databaseBean);
                data.put(schemaSyncCheckTabPanel.getTitleAt(i) + "." + DatabasePanal.LABEL_USER, databaseBean.getUser());
            }

            for (Entry<String, String> entry : data.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_REQUIRED, entry.getKey()));
                    return;
                }
            }

            final File dbfluteClientDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + clientBean.getProject());
            if (projectText.isEnabled() && dbfluteClientDir.exists()) {
                JOptionPane.showMessageDialog(frame, String.format(MSG_EXIST_PROJECT, clientBean.getProject()));
                return;
            }

            if (clientBean.getJdbcDriverJarPath() != null && !clientBean.getJdbcDriverJarPath().equals("")) {
                final File jdbcDriverJarFile = new File(clientBean.getJdbcDriverJarPath());
                if (!jdbcDriverJarFile.exists() || !jdbcDriverJarFile.isFile()) {
                    JOptionPane.showMessageDialog(frame,
                            String.format(MSG_INVALID, BasicPanel.LABEL_JDBC_DRIVER_JAR_PATH, clientBean.getJdbcDriverJarPath()));
                    return;
                }
            }

            if (schemaSyncCheckCount != schemaSyncCheckMap.size()) {
                JOptionPane.showMessageDialog(frame, MSG_DUPLICATE_CHEMA_SYNC_CHECK);
                return;
            }

            DbFluteClientLogic dbFluteIntroLogic = new DbFluteClientLogic();
            try {
                dbFluteIntroLogic.testConnection(clientBean);
            } catch (RuntimeException e) {
                int result = JOptionPane.showConfirmDialog(frame,
                        String.format(MSG_TEST_CONNECTION_ERROR, e.getMessage()), null, JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try {
                dbFluteIntroLogic.createClient(clientBean, true);
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

    public ClientBean asResult() {

        ClientBean clientBean = basicPanel.asResult();

        final String projectName = projectText.getText();
        clientBean.setProject(projectName);
        clientBean.setOptionBean(optionPanel.asResult());

        return clientBean;
    }


    protected void reflect(ClientBean clientBean) {

        projectText.setText(clientBean.getProject());
        basicPanel.reflect(clientBean);
        optionPanel.reflect(clientBean);

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
