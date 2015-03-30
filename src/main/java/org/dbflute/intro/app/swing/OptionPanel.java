package org.dbflute.intro.app.swing;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.dbflute.intro.app.bean.ClientBean;
import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.bean.OptionBean;

/**
 * @author p1us2er0
 */
public class OptionPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String LABEL_OPTION = "オプション";
    private static final String LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS = "DBコメントを別名基準で利用";
    private static final String LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF = "差分判定でカラム定義順をチェック";
    private static final String LABEL_IS_CHECK_DB_COMMENT_DIFF = "差分判定でDBコメントをチェック";
    private static final String LABEL_IS_CHECK_PROCEDURE_DIFF = "差分判定でプロシージャをチェック";
    private static final String LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN = "プロシージャ自動生成";
    protected static final String LABEL_SCHEMA_SYNC_CHECK = "他のDB環境";

    private static final String MSG_SCHEMA_SYNC_CHECK_ENV = "DB環境を入力してください。";

    private JFrame frame;

    private Map<String, JCheckBox> optionMap = new LinkedHashMap<String, JCheckBox>();
    private JTextField aliasDelimiterInDbCommentText;

    protected JTabbedPane schemaSyncCheckTabPanel;

    /**
     * Create the application.
     */
    public OptionPanel(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    public void initialize() {

        this.setLayout(null);

        this.add(createLabel(LABEL_OPTION, 10));
        this.add(createOption(LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS, 10));
        this.add(createOption(LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF, 35));
        this.add(createOption(LABEL_IS_CHECK_DB_COMMENT_DIFF, 60));
        this.add(createOption(LABEL_IS_CHECK_PROCEDURE_DIFF, 85));
        this.add(createOption(LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN, 110));
        this.add(createLabel(LABEL_SCHEMA_SYNC_CHECK, 140));

        aliasDelimiterInDbCommentText = new JTextField();
        aliasDelimiterInDbCommentText.setBounds(400, 10, 40, 20);
        this.add(aliasDelimiterInDbCommentText);

        JButton schemaSyncCheckAddButton = new JButton(new SchemaSyncCheckAddAction());
        schemaSyncCheckAddButton.setBounds(150, 140, 40, 20);
        this.add(schemaSyncCheckAddButton);

        JButton schemaSyncCheckRemoveButton = new JButton(new SchemaSyncCheckRemoveAction());
        schemaSyncCheckRemoveButton.setBounds(190, 140, 40, 20);
        this.add(schemaSyncCheckRemoveButton);
    }

    private JLabel createLabel(String label, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(10, y, 150, 20);

        return jLabel;
    }

    private JCheckBox createOption(String label, int y) {
        JCheckBox checkBox = new JCheckBox(label, true);
        checkBox.setBounds(150, y, 250, 20);
        optionMap.put(label, checkBox);

        return checkBox;
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
                    schemaSyncCheckTabPanel.setBounds(0, 165, 450, 160);
                    add(schemaSyncCheckTabPanel);
                }

                DatabasePanal schemaSyncCheckPanal = new DatabasePanal(false);
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

    public OptionBean asResult() {

        final OptionBean optionBean = new OptionBean();
        optionBean.setDbCommentOnAliasBasis(optionMap.get(LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS).isSelected());
        optionBean.setAliasDelimiterInDbComment(aliasDelimiterInDbCommentText.getText());
        optionBean.setCheckColumnDefOrderDiff(optionMap.get(LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF).isSelected());
        optionBean.setCheckDbCommentDiff(optionMap.get(LABEL_IS_CHECK_DB_COMMENT_DIFF).isSelected());
        optionBean.setCheckProcedureDiff(optionMap.get(LABEL_IS_CHECK_PROCEDURE_DIFF).isSelected());
        optionBean.setGenerateProcedureParameterBean(optionMap.get(LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN)
                .isSelected());

        return optionBean;
    }

    protected void reflect(ClientBean clientBean, Map<String, DatabaseBean> envDatabaseBeanMap) {

        OptionBean optionBean = clientBean.getOptionBean();

        optionMap.get(LABEL_IS_DB_COMMENT_ON_ALIAS_BASIS).setSelected(optionBean.isDbCommentOnAliasBasis());
        optionMap.get(LABEL_IS_CHECK_COLUMN_DEF_ORDER_DIFF).setSelected(optionBean.isCheckColumnDefOrderDiff());
        optionMap.get(LABEL_IS_CHECK_DB_COMMENT_DIFF).setSelected(optionBean.isCheckDbCommentDiff());
        optionMap.get(LABEL_IS_CHECK_PROCEDURE_DIFF).setSelected(optionBean.isCheckProcedureDiff());
        optionMap.get(LABEL_IS_GENERATE_PROCEDURE_PARAMETER_BEAN).setSelected(
                optionBean.isGenerateProcedureParameterBean());
        aliasDelimiterInDbCommentText.setText(optionBean.getAliasDelimiterInDbComment());

        if (schemaSyncCheckTabPanel == null && !envDatabaseBeanMap.isEmpty()) {
            schemaSyncCheckTabPanel = new JTabbedPane();
            schemaSyncCheckTabPanel.setBounds(0, 360, 480, 160);
            add(schemaSyncCheckTabPanel);
        }

        for (Entry<String, DatabaseBean> entry : envDatabaseBeanMap.entrySet()) {

            DatabasePanal schemaSyncCheckPanal = new DatabasePanal(false);
            schemaSyncCheckPanal.reflect(entry.getValue());
            schemaSyncCheckTabPanel.addTab(entry.getKey(), schemaSyncCheckPanal);
            schemaSyncCheckTabPanel.setVisible(schemaSyncCheckTabPanel.getTabCount() != 0);
        }
    }
}
