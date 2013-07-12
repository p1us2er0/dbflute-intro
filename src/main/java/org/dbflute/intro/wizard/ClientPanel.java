package org.dbflute.intro.wizard;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.dbflute.intro.util.SwingUtil;
import org.dbflute.intro.util.SwingUtil.ProgressBarDialog;

/**
 * @author ecode
 */
public class ClientPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String LABEL_SCHEMA_HTML;
    private static final String LABEL_HISTORY_HTML;
    private static final String LABEL_SYNC_CHECK_HTML;
    private static final String LABEL_DOC = "ドキュメント生成";
    private static final String LABEL_SYNC_CHECK = "スキーマの差分チェック";
    private static final String LABEL_REPLACE_SCHEMA = "DBスキーマ(再)構築";
    private static final String LABEL_PROJECT_TAB = "DB";
    private static final String LABEL_ENV = "環境名";

    private static final String MSG_EXECUTE = "実行中...";
    private static final String MSG_FINISHED_SUCCESS = "正常終了。";
    private static final String MSG_FINISHED_ERROR = "異常終了。ログファイル=%1$s";
    private static final String MSG_NOT_FOUND_ENV = NewClientPanel.LABEL_SCHEMA_SYNC_CHECK + "が見つかりません。";
    private static final String MSG_CANCELED = "キャンセルしました。";
    private static final String MSG_INVALID_URL = "URLが不正です。";
    private static final String MSG_NOT_FOUND_URL = "「%1$s」が見つかりません。";
    private static final String MSG_REPLACE_SCHEMA = "DBスキーマを(再)構築します。";

    private JFrame frame;

    private JComboBox projectCombo;

    static {
        final File docDir = new File(DBFluteIntro.BASIC_DIR_PATH, "/dbflute_${project}/output/doc/");

        LABEL_SCHEMA_HTML = "<a href='" + new File(docDir, "schema-${project}.html").toURI()
                + "'>テーブル定義を開く</a> (SchemaHTML)";
        LABEL_HISTORY_HTML = "<a href='" + new File(docDir, "history-${project}.html").toURI()
                + "'>DB変更履歴を開く</a> (HistoryHTML)";
        LABEL_SYNC_CHECK_HTML = "<a href='" + new File(docDir, "sync-check-result_${env}.html").toURI()
                + "'>差分チェック結果を開く</a> (SyncCheckHTML)";
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

        projectCombo = new JComboBox();
        projectCombo.setBounds(150, 10, 300, 20);
        this.add(projectCombo);

        this.add(createHTMLLink(LABEL_SCHEMA_HTML, 35));
        this.add(createHTMLLink(LABEL_HISTORY_HTML, 60));
        this.add(createHTMLLink(LABEL_SYNC_CHECK_HTML, 85));

        Map<String, List<ProcessBuilder>> map = new LinkedHashMap<String, List<ProcessBuilder>>();
        map.put(LABEL_DOC, DBFluteIntro.getJdbcDocCommondList());
        map.put(LABEL_SYNC_CHECK, DBFluteIntro.getSchemaSyncCheckCommondList());
        map.put(LABEL_REPLACE_SCHEMA, DBFluteIntro.getReplaceSchemaCommondList());

        int y = 0;
        for (Entry<String, List<ProcessBuilder>> entry : map.entrySet()) {
            JButton jdbcDocButton = new JButton(new TaskAction(entry.getKey(), entry.getValue()));
            jdbcDocButton.setBounds(10, 150 + y, 200, 20);
            this.add(jdbcDocButton);

            y += 30;
        }

        fireProjectCombo(null);
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

            final Map<String, Integer> resultMap = new LinkedHashMap<String, Integer>();
            for (final ProcessBuilder processBuilder : taskList) {

                processBuilder.directory(new File(DBFluteIntro.BASIC_DIR_PATH, "dbflute_" + project));

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
                        int result = DBFluteIntro.executeCommond(processBuilder);

                        resultMap.put(processBuilder.command().toString(), result);
                    }
                };

                progressBarDialog.start(getValue(NAME).toString(), MSG_EXECUTE);

                if (Collections.frequency(resultMap.values(), 0) != resultMap.size()) {
                    break;
                }
            }

            String message = MSG_FINISHED_SUCCESS;
            if (Collections.frequency(resultMap.values(), 0) != resultMap.size()) {
                String logPath = DBFluteIntro.INI_FILE_PATH + "/dbflute_" + project + "/log/dbflute.log";
                message = String.format(MSG_FINISHED_ERROR, logPath);
            }

            JOptionPane.showMessageDialog(frame, message);
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

                String project = projectCombo.getSelectedItem().toString();

                if (url == null) {
                    JOptionPane.showMessageDialog(frame, MSG_INVALID_URL);
                    return;
                }

                String decodePath = null;
                try {
                    decodePath = URLDecoder.decode(url.getFile(), System.getProperty("file.encoding"));
                } catch (UnsupportedEncodingException e1) {
                    JOptionPane.showMessageDialog(frame, String.format(MSG_NOT_FOUND_URL, url.getFile()));
                    return;
                }

                String env = null;
                if (decodePath.contains("sync-check-result_${env}.html")) {
                    env = showSchemaSyncCheckEnvDialog(project);
                    if (env == null) {
                        return;
                    }
                }

                String path = decodePath.replaceAll("\\$\\{project\\}", projectCombo.getSelectedItem().toString());
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
        DBFluteIntro dbFluteIntro = new DBFluteIntro();
        for (String projectTemp : dbFluteIntro.getProjectList()) {
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

        SwingUtil.updateLookAndFeel(frame);
    }
}
