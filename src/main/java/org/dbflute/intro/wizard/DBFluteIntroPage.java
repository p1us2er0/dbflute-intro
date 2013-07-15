package org.dbflute.intro.wizard;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dbflute.emecha.eclipse.plugin.core.exception.EmPluginException;
import org.dbflute.emecha.eclipse.plugin.core.meta.website.EmMetaFromWebSite;
import org.dbflute.intro.util.SwingUtil;
import org.dbflute.intro.util.SwingUtil.ProgressBarDialog;

/**
 * @author ecode
 * @author jflute
 * @author shin1988
 */
public class DBFluteIntroPage {

    private static final String LABEL_TITLE = "DBFlute Intro";
    private static final String LABEL_SETTING = "設定";
    private static final String LABEL_HELP = "ヘルプ";
    private static final String LABEL_VERSION = "バージョン";
    private static final String LABEL_PROXY_HOST = "ホスト";
    private static final String LABEL_PROXY_PORT = "ポート";
    private static final String LABEL_DOWNLOAD = "ダウンロード";
    private static final String LABEL_PROXY_SETTING = "プロキシ設定";
    private static final String LABEL_SET = "設定";

    private static final String MSG_DOWNLOAD = "DBFluteのバージョンを入力してください。\n最新バージョン : %1$s\n最新スナップショットバージョン : %2$s";
    private static final String MSG_NETWORK_ERROR = "ネットワークエラー。 プロキシを設定してください。";
    private static final String MSG_CANCELED_DOWNLOAD = "ダウンロードをキャンセルしました。";
    private static final String MSG_DOWNLOAD_ERROR = "ダウンロードエラー";
    private static final String MSG_DOWNLOADING = "ダウンロード中。";

    private JFrame frame;
    private NewClientPanel newClientPanel;
    private JTabbedPane tabPanel;

    private JDialog dialog;
    private JTextField proxyHostText;
    private JTextField proxyPortText;

    private final Action downloadAction = new DownloadAction();
    private final Action proxySettingsViewAction = new ProxySettingsViewAction();
    private final Action proxySettingsAction = new ProxySettingsAction();

    private final DBFluteIntro dbFluteIntro = new DBFluteIntro();

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
                    throw new RuntimeException(e);
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

        dbFluteIntro.loadProxy();

        frame = new JFrame(LABEL_TITLE);
        frame.setBounds(100, 100, 500, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout(0, 0));
        frame.setLocationRelativeTo(null);

        tabPanel = new JTabbedPane();
        frame.getContentPane().add(tabPanel, "name_5009361789717");

        if (!dbFluteIntro.getProjectList().isEmpty()) {
            tabPanel.addTab(NewClientPanel.LABEL_PROJECT_TAB, new ClientPanel(frame));
        }

        newClientPanel = new NewClientPanel(frame);
        tabPanel.addTab("+", newClientPanel);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu setting = new JMenu(LABEL_SETTING);
        menuBar.add(setting);

        JMenuItem downloadMenuItem = new JMenuItem(downloadAction);
        setting.add(downloadMenuItem);

        JMenuItem proxySettingsMenuItem = new JMenuItem(proxySettingsViewAction);
        setting.add(proxySettingsMenuItem);

        JMenu help = new JMenu(LABEL_HELP);
        menuBar.add(help);

        JMenuItem versionMenuItem = new JMenuItem(LABEL_VERSION + ": " + dbFluteIntro.getVersion());
        help.add(versionMenuItem);

        SwingUtil.updateLookAndFeel(frame);

        if (dbFluteIntro.getExistedDBFluteVersionList().isEmpty()) {
            downloadAction.actionPerformed(null);
        }
    }

    private class DownloadAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public DownloadAction() {
            putValue(NAME, LABEL_DOWNLOAD);
        }

        public void actionPerformed(ActionEvent event) {

            EmMetaFromWebSite site = null;

            try {
                site = dbFluteIntro.getEmMetaFromWebSite();
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(frame, MSG_NETWORK_ERROR);
                return;
            }

            String message = String.format(MSG_DOWNLOAD, site.getLatestVersionDBFlute(),
                    site.getLatestSnapshotVersionDBFlute());
            final String dbfluteVersion = JOptionPane.showInputDialog(frame, message, site.getLatestVersionDBFlute());

            if (dbfluteVersion == null) {
                JOptionPane.showMessageDialog(frame, MSG_CANCELED_DOWNLOAD);
                return;
            }

            ProgressBarDialog progressBarDialog = new ProgressBarDialog(frame) {

                @Override
                public void execute() {
                    try {
                        dbFluteIntro.downloadDBFlute(dbfluteVersion);

                    } catch (EmPluginException e) {
                        JOptionPane.showMessageDialog(frame, MSG_DOWNLOAD_ERROR);
                        return;
                    }

                    newClientPanel.fireVersionInfoDBFlute();
                }
            };

            progressBarDialog.start("Download", MSG_DOWNLOADING);
        }
    }

    private class ProxySettingsViewAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ProxySettingsViewAction() {
            putValue(NAME, LABEL_PROXY_SETTING);
        }

        public void actionPerformed(ActionEvent event) {

            Properties properties = dbFluteIntro.getProperties();
            String proxyHost = properties.getProperty("proxyHost");
            String proxyPort = properties.getProperty("proxyPort");

            dialog = new JDialog(frame, LABEL_PROXY_SETTING, true);
            dialog.setBounds(100, 100, 200, 150);
            dialog.getContentPane().setLayout(new CardLayout(0, 0));
            dialog.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            dialog.getContentPane().add(panel, "name_5009361789718");
            panel.setLayout(null);

            JLabel proxyHostLabel = new JLabel(LABEL_PROXY_HOST);
            proxyHostLabel.setBounds(10, 10, 80, 20);
            panel.add(proxyHostLabel);

            proxyHostText = new JTextField();
            proxyHostText.setBounds(80, 10, 100, 20);
            proxyHostText.setColumns(10);
            proxyHostText.setText(proxyHost);
            panel.add(proxyHostText);

            JLabel proxyPortLabel = new JLabel(LABEL_PROXY_PORT);
            proxyPortLabel.setBounds(10, 35, 80, 20);
            panel.add(proxyPortLabel);

            proxyPortText = new JTextField();
            proxyPortText.setBounds(80, 35, 100, 20);
            proxyPortText.setColumns(10);
            proxyPortText.setText(proxyPort);
            panel.add(proxyPortText);

            JButton proxySettingsButton = new JButton(proxySettingsAction);
            proxySettingsButton.setBounds(80, 60, 70, 20);
            panel.add(proxySettingsButton);

            dialog.setVisible(true);
        }
    }

    private class ProxySettingsAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ProxySettingsAction() {
            putValue(NAME, LABEL_SET);
        }

        public void actionPerformed(ActionEvent event) {

            Properties properties = dbFluteIntro.getProperties();
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

            dbFluteIntro.loadProxy();

            dialog.setVisible(false);
        }
    }
}
