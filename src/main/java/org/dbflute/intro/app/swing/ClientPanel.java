package org.dbflute.intro.app.swing;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.logic.DbFluteClientLogic;
import org.dbflute.intro.app.logic.DbFluteIntroLogic;
import org.dbflute.intro.app.logic.DbFluteTaskLogic;
import org.dbflute.intro.mylasta.util.SwingUtil.JTextAreaStream;
import org.dbflute.intro.mylasta.util.SwingUtil.ProgressBarDialog;

/**
 * @author p1us2er0
 */
public class ClientPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String LABEL_SCHEMA_HTML;
    private static final String LABEL_LOAD_DATA_REVERSE_HTML;
    private static final String LABEL_HISTORY_HTML;
    private static final String LABEL_SYNC_CHECK_HTML;
    private static final String LABEL_REMOVE = "削除";
    private static final String LABEL_DOC = "ドキュメント生成";
    private static final String LABEL_LOAD_DATA_REVERSE = "データ抽出";
    private static final String LABEL_SYNC_CHECK = "スキーマの差分チェック";
    private static final String LABEL_REPLACE_SCHEMA = "DBスキーマ(再)構築 (ReplaceSchema)";
    private static final String LABEL_PROJECT_TAB = "DB";
    private static final String LABEL_ENV = "環境名";
    private static final String LABEL_CLEAR = "クリア";

    private static final String MSG_EXECUTE = "実行中...";
    private static final String MSG_FINISHED_SUCCESS = "正常終了。";
    private static final String MSG_FINISHED_ERROR = "異常終了。ログファイル=%1$s";
    private static final String MSG_NOT_FOUND_ENV = OptionPanel.LABEL_SCHEMA_SYNC_CHECK + "が見つかりません。";
    private static final String MSG_CANCELED = "キャンセルしました。";
    private static final String MSG_INVALID_URL = "URLが不正です。";
    private static final String MSG_NOT_FOUND_URL = "「%1$s」\r\nが見つかりません。";
    private static final String MSG_REMOVE = "「%1$s」の設定を削除します。";
    private static final String MSG_REMOVE_SUCCESS = "「%1$s」の設定を削除しました。";
    private static final String MSG_REMOVE_ERROR = "「%1$s」の設定を削除できませんでした。";
    private static final String MSG_REPLACE_SCHEMA = "DBスキーマを(再)構築します。";

    private JFrame frame;

    private JComboBox<String> projectCombo;
    private JTextArea consoleArea;

    // TODO
    private DbFluteClientLogic dbFluteClientLogic = new DbFluteClientLogic();
    private DbFluteTaskLogic dbFluteTaskLogic = new DbFluteTaskLogic();

    static {
        final File docDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_${project}/output/doc/");

        LABEL_SCHEMA_HTML = "<a href='" + new File(docDir, "schema-${project}.html").toURI()
                + "'>テーブル定義を開く</a> (SchemaHTML)";
        LABEL_LOAD_DATA_REVERSE_HTML = "<a href='" + new File(docDir, "data").toURI() + "'>抽出データを開く</a>";
        LABEL_HISTORY_HTML = "<a href='" + new File(docDir, "history-${project}.html").toURI()
                + "'>DB変更履歴を開く</a> (HistoryHTML)";
        LABEL_SYNC_CHECK_HTML = "<a href='" + new File(docDir, "sync-check-result_${env}.html").toURI()
                + "'>差分チェック結果を開く</a> (SchemaSyncCheck)";
    }

    /**
     * Create the application.
     */
    public ClientPanel(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    public void initialize() {

        this.setLayout(null);

        JLabel projectLabel = new JLabel(NewClientPanel.LABEL_PROJECT + NewClientPanel.LABEL_REQUIRED);
        projectLabel.setBounds(10, 10, 150, 20);
        this.add(projectLabel);

        projectCombo = new JComboBox<String>();
        projectCombo.setBounds(150, 10, 190, 20);
        fireProjectCombo(null);
        this.add(projectCombo);

        projectCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireProjectCombo((String) projectCombo.getSelectedItem());
            }
        });

        JButton changeButton = new JButton(NewClientPanel.LABEL_CLIENT_CHANGE);
        changeButton.setBounds(350, 10, 60, 20);
        this.add(changeButton);

        changeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String project = (String) projectCombo.getSelectedItem();

                JTabbedPane tabPanel = (JTabbedPane) getParent();
                tabPanel.removeAll();
                NewClientPanel newClientPanel = new NewClientPanel(frame);
                tabPanel.insertTab(LABEL_PROJECT_TAB, null, newClientPanel, null, 0);
                tabPanel.setSelectedComponent(newClientPanel);

                ClientBean clientBean = dbFluteClientLogic.convClientBeanFromDfprop(project);
                Map<String, DatabaseBean> databaseBeanMap = dbFluteClientLogic.convDatabaseBeanMapFromDfprop(project);

                newClientPanel.reflect(clientBean, databaseBeanMap);
            }
        });

        JButton removeButton = new JButton(LABEL_REMOVE);
        removeButton.setBounds(410, 10, 60, 20);
        this.add(removeButton);

        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String project = (String) projectCombo.getSelectedItem();

                String msg = String.format(MSG_REMOVE, project);
                int result = JOptionPane.showConfirmDialog(frame, msg, null, JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }

                boolean delete = dbFluteClientLogic.deleteClient((String) projectCombo.getSelectedItem());
                msg = String.format(delete ? MSG_REMOVE_SUCCESS : MSG_REMOVE_ERROR, project);
                JOptionPane.showMessageDialog(frame, msg);
                projectCombo.actionPerformed(null);
            }
        });

        this.add(createHTMLLink(LABEL_SCHEMA_HTML, 55));
        this.add(createHTMLLink(LABEL_HISTORY_HTML, 80));

        JButton button = new JButton(new TaskAction(LABEL_DOC, dbFluteTaskLogic.getDocCommandList()));
        button.setBounds(10, 105, 250, 20);
        this.add(button);

        this.add(createHTMLLink(LABEL_LOAD_DATA_REVERSE_HTML, 150));

        button = new JButton(new TaskAction(LABEL_LOAD_DATA_REVERSE, dbFluteTaskLogic.getLoadDataReverseCommandList()));
        button.setBounds(10, 175, 250, 20);
        this.add(button);

        this.add(createHTMLLink(LABEL_SYNC_CHECK_HTML, 220));

        button = new JButton(new TaskAction(LABEL_SYNC_CHECK, dbFluteTaskLogic.getSchemaSyncCheckCommandList()));
        button.setBounds(10, 245, 250, 20);
        this.add(button);

        button = new JButton(new TaskAction(LABEL_REPLACE_SCHEMA, dbFluteTaskLogic.getReplaceSchemaCommandList()));
        button.setBounds(10, 300, 250, 20);
        this.add(button);

        JButton consoleAreaClearButton = new JButton(LABEL_CLEAR);
        consoleAreaClearButton.setBounds(405, 330, 70, 20);
        consoleAreaClearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consoleArea.setText("");
            }
        });
        this.add(consoleAreaClearButton);

        consoleArea = new JTextArea();
        consoleArea.setEditable(false);

        Font font = consoleArea.getFont();
        consoleArea.setFont(new Font(font.getName(), font.getStyle(), font.getSize() - 3));
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setBounds(2, 350, 475, 212);
        this.add(scrollPane);
    }

    private JEditorPane createHTMLLink(String message, int y) {

        JEditorPane link = new JEditorPane("text/html", message);
        link.setBounds(10, y, 300, 20);
        link.setEditable(false);
        link.setOpaque(false);
        link.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        link.setFont(new JLabel().getFont());
        link.addHyperlinkListener(new HyperlinkHandler());

        return link;
    }

    private class TaskAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private List<ProcessBuilder> taskList;

        public TaskAction(String taskName, List<ProcessBuilder> taskList) {
            putValue(NAME, taskName);
            this.taskList = taskList;
        }

        public void actionPerformed(ActionEvent event) {

            String project = projectCombo.getSelectedItem().toString();

            String env = null;
            if (getValue(NAME).toString().equals(LABEL_SYNC_CHECK)) {
                env = showSchemaSyncCheckEnvDialog(project);
                if (env == null) {
                    return;
                }
            }

            if (getValue(NAME).toString().equals(LABEL_REPLACE_SCHEMA)) {
                int result = JOptionPane.showConfirmDialog(frame, MSG_REPLACE_SCHEMA, LABEL_REPLACE_SCHEMA,
                        JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            consoleArea.setText("");

            final Map<String, Integer> resultMap = new LinkedHashMap<String, Integer>();
            for (final ProcessBuilder processBuilder : taskList) {

                processBuilder.directory(new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project));

                Map<String, String> environment = processBuilder.environment();
                environment.put("pause_at_end", "n");
                if (getValue(NAME).toString().equals(LABEL_SYNC_CHECK)) {
                    environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + env);
                } else if (getValue(NAME).toString().equals(LABEL_REPLACE_SCHEMA)) {
                    environment.put("answer", "y");
                }

                ProgressBarDialog progressBarDialog = new ProgressBarDialog(frame) {

                    @Override
                    public void execute() {

                        JTextAreaStream textAreaStream = null;

                        try {
                            textAreaStream = new JTextAreaStream(consoleArea);
                            int result = dbFluteTaskLogic.executeCommand(processBuilder, textAreaStream);
                            resultMap.put(processBuilder.command().toString(), result);
                        } finally {
                            IOUtils.closeQuietly(textAreaStream);
                        }
                    }
                };

                progressBarDialog.start(getValue(NAME).toString(), MSG_EXECUTE);

                if (Collections.frequency(resultMap.values(), 0) != resultMap.size()) {
                    break;
                }
            }

            String message = MSG_FINISHED_SUCCESS;
            if (Collections.frequency(resultMap.values(), 0) != resultMap.size()) {
                try {
                    String logPath = DbFluteIntroLogic.BASE_DIR_PATH + "dbflute_" + project + "/log/dbflute.log";
                    logPath = new File(logPath).getCanonicalPath();
                    message = String.format(MSG_FINISHED_ERROR, logPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            JOptionPane.showMessageDialog(frame, message);
        }
    }

    private String showSchemaSyncCheckEnvDialog(String project) {

        List<String> envList = dbFluteClientLogic.getEnvList(project);

        if (envList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, MSG_NOT_FOUND_ENV);
            return null;
        }

        Object obj = JOptionPane.showInputDialog(frame, LABEL_ENV, LABEL_SYNC_CHECK, JOptionPane.QUESTION_MESSAGE,
                null, envList.toArray(), null);
        if (obj == null) {
            JOptionPane.showMessageDialog(frame, MSG_CANCELED);
            return null;
        }

        return obj.toString();
    }

    private class HyperlinkHandler implements HyperlinkListener {

        @Override
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == EventType.ACTIVATED) {

                URL url = event.getURL();

                if (url == null) {
                    JOptionPane.showMessageDialog(frame, MSG_INVALID_URL);
                    return;
                }

                String dp1us2er0Path = null;
                try {
                    dp1us2er0Path = URLDecoder.decode(url.getFile(), System.getProperty("file.encoding"));
                } catch (UnsupportedEncodingException e1) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_NOT_FOUND_URL, url.getFile()));
                    return;
                }

                String project = projectCombo.getSelectedItem().toString();
                String env = null;
                if (dp1us2er0Path.contains("sync-check-result_${env}.html")) {
                    env = showSchemaSyncCheckEnvDialog(project);
                    if (env == null) {
                        return;
                    }
                }

                String path = dp1us2er0Path.replaceAll("\\$\\{project\\}", projectCombo.getSelectedItem().toString());
                path = path.replaceAll("\\$\\{env\\}", env);
                File file = new File(path);

                try {
                    file = file.getCanonicalFile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_NOT_FOUND_URL, file.getAbsolutePath()));
                    return;
                }

                if (!file.exists()) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_NOT_FOUND_URL, file.getAbsolutePath()));
                    return;
                }

                try {
                    file = new File(file.getCanonicalPath());

                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, e);
                    return;
                }
            }
        }
    }

    protected void fireProjectCombo(String project) {

        projectCombo.removeAllItems();
        for (String projectTemp : dbFluteClientLogic.getProjectList()) {
            projectCombo.addItem(projectTemp);
        }

        if (project != null && !project.equals("")) {
            projectCombo.setSelectedItem(project);
        }

        JTabbedPane tabPanel = (JTabbedPane) this.getParent();
        if (tabPanel != null) {
            tabPanel.remove(this);
            if (projectCombo.getItemCount() != 0) {
                tabPanel.insertTab(LABEL_PROJECT_TAB, null, this, null, 0);
                tabPanel.setSelectedComponent(this);
            }
        }

        String selectedProject = (String) projectCombo.getSelectedItem();
        boolean selected = selectedProject != null && !selectedProject.equals("");

        for (Component component : this.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (LABEL_REPLACE_SCHEMA.equals(button.getText())) {
                    button.setVisible(selected && dbFluteClientLogic.existReplaceSchemaFile(selectedProject));
                }
            }
        }
    }
}
