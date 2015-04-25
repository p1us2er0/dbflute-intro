package org.dbflute.intro.app.swing;

import java.util.EnumSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import org.dbflute.intro.app.bean.DatabaseBean;
import org.dbflute.intro.app.definition.DatabaseInfoDef;
import org.dbflute.intro.mylasta.util.SwingUtil;
import org.dbflute.intro.mylasta.util.SwingUtil.ReflectionCaretListener;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DatabasePanal extends JPanel {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_URL = "URL";
    protected static final String LABEL_SCHEMA = "スキーマ";
    protected static final String LABEL_USER = "ユーザ";
    protected static final String LABEL_PASSWORD = "パスワード";

    private JLabel databaseSchemaLabel;

    private JTextField databaseUrlText;
    private JTextField databaseSchemaText;
    private JTextField databaseUserText;
    private JPasswordField databasePasswordText;

    private ReflectionCaretListener userReflectionCaretListener;

    /**
     * Create the application.
     */
    public DatabasePanal(boolean main) {
        initialize(main);
    }

    /**
     * Initialize the contents of the frame.
     */
    protected void initialize(boolean main) {

        this.setLayout(null);

        this.add(createLabel(LABEL_URL + (main ? NewClientPanel.LABEL_REQUIRED : ""), 10));
        databaseSchemaLabel = createLabel(LABEL_SCHEMA, 35);
        this.add(databaseSchemaLabel);
        this.add(createLabel(LABEL_USER + (main ? NewClientPanel.LABEL_REQUIRED : ""), 60));
        this.add(createLabel(LABEL_PASSWORD, 85));

        int width = main ? 300 : 260;
        databaseUrlText = new JTextField();
        databaseUrlText.setBounds(150, 10, width, 20);
        databaseUrlText.setColumns(10);
        this.add(databaseUrlText);

        databaseSchemaText = new JTextField();
        databaseSchemaText.setBounds(150, 35, width, 20);
        databaseSchemaText.setColumns(10);
        this.add(databaseSchemaText);

        databaseUserText = new JTextField();
        databaseUserText.setBounds(150, 60, width, 20);
        databaseUserText.setColumns(10);
        this.add(databaseUserText);

        databasePasswordText = new JPasswordField();
        databasePasswordText.setBounds(150, 85, width, 20);
        databasePasswordText.setColumns(10);
        this.add(databasePasswordText);
    }

    private JLabel createLabel(String label, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(10, y, 150, 20);

        return jLabel;
    }

    public DatabaseBean asResult() {

        final String databaseUrl = databaseUrlText.getText();
        final String databaseSchema = databaseSchemaText.getText();
        final String databaseUser = databaseUserText.getText();
        final String databasePassword = new String(databasePasswordText.getPassword());

        final DatabaseBean databaseBean = new DatabaseBean();
        databaseBean.setUrl(databaseUrl);
        databaseBean.setSchema(databaseSchema);
        databaseBean.setUser(databaseUser);
        databaseBean.setPassword(databasePassword);

        return databaseBean;
    }

    protected void reflect(DatabaseBean databaseBean) {

        databaseUrlText.setText(databaseBean.getUrl());
        databaseSchemaText.setText(databaseBean.getSchema());
        databaseUserText.setText(databaseBean.getUser());
        databasePasswordText.setText(databaseBean.getPassword());
    }

    protected void fireDatabaseUrlText(DatabaseInfoDef databaseDef) {
        databaseUrlText.setText(databaseDef.getUrlTemplate());
    }

    protected void fireDatabaseSchemaLabel(DatabaseInfoDef databaseDef) {
        boolean required = databaseDef != null && databaseDef.isNeedSchema();
        databaseSchemaLabel.setText(LABEL_SCHEMA + (required ? NewClientPanel.LABEL_REQUIRED : ""));
    }

    protected void fireDatabaseSchemaText(DatabaseInfoDef databaseDef) {
        if (databaseDef == null) {
            return;
        }

        if (EnumSet.of(DatabaseInfoDef.Oracle, DatabaseInfoDef.DB2).contains(databaseDef)) {
            ((AbstractDocument) databaseSchemaText.getDocument()).setDocumentFilter(SwingUtil.UPPER_DOCUMENT_FILTER);
        } else {
            ((AbstractDocument) databaseSchemaText.getDocument()).setDocumentFilter(null);
        }

        // Oracleは、ユーザとスキーマが一致することが多いため、入力補助する
        databaseSchemaText.removeCaretListener(userReflectionCaretListener);
        if (DatabaseInfoDef.Oracle == databaseDef) {
            if (userReflectionCaretListener == null) {
                userReflectionCaretListener = new ReflectionCaretListener(databaseUserText);
            }
            databaseSchemaText.addCaretListener(userReflectionCaretListener);
        }

        databaseSchemaText.setText(databaseDef.getDefultSchema());
    }
}
