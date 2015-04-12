package org.dbflute.intro.app.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.dbflute.intro.app.logic.DbFluteClientLogic;
import org.dbflute.intro.app.logic.DbFluteEngineLogic;
import org.dbflute.intro.app.logic.DbFluteIntroLogic;
import org.dbflute.intro.mylasta.util.SwingUtil;
import org.dbflute.intro.mylasta.util.SwingUtil.ProgressBarDialog;

/**
 * @author p1us2er0
 * @author jflute
 * @author shin1988
 */
public class DbfluteIntroFrame {

    private static final String LABEL_TITLE = "DBFlute Intro";
    protected static final String LABEL_SETTING = "設定";
    private static final String LABEL_USE_SYSTEM_PROXIES = "システムのプロキシ設定利用";
    private static final String LABEL_PROXY_HOST = "ホスト";
    private static final String LABEL_PROXY_PORT = "ポート";
    protected static final String LABEL_DOWNLOAD = "ダウンロード";
    private static final String LABEL_PROXY_SETTING = "プロキシ設定";
    private static final String LABEL_SET = "設定";
    private static final String LABEL_HELP = "ヘルプ";
    private static final String LABEL_VERSION = "バージョン";
    private static final String LABEL_UPGRADE = "バージョン更新";

    private static final String MSG_DOWNLOAD = "DBFluteのバージョンを入力してください。\n最新バージョン : %1$s\n最新スナップショットバージョン : %2$s";
    private static final String MSG_NETWORK_ERROR = "ネットワークエラー。 プロキシを設定してください。";
    private static final String MSG_USE_SYSTEM_PROXIES_ATTENTION = "※チェックした場合は、アプリの再起動が必要";
    private static final String MSG_CANCELED_DOWNLOAD = "ダウンロードをキャンセルしました。";
    private static final String MSG_DOWNLOAD_ERROR = "ダウンロードエラー";
    private static final String MSG_DOWNLOADING = "ダウンロード中。";

    protected DbFluteIntroLogic dbFluteIntroLogic = new DbFluteIntroLogic();
    protected DbFluteEngineLogic dbFluteEngineLogic = new DbFluteEngineLogic();
    protected DbFluteClientLogic dbFluteClientLogic = new DbFluteClientLogic();

    private JFrame frame;
    JTabbedPane tabPanel;

    private JDialog dialog;
    private JCheckBox useSystemProxiesCheckBox;
    private JTextField proxyHostText;
    private JTextField proxyPortText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        DbfluteIntroFrame dbfluteIntroFrame = new DbfluteIntroFrame();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    dbfluteIntroFrame.initialize();
                    dbfluteIntroFrame.frame.setVisible(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        dbFluteIntroLogic.loadProxy();

        frame = new JFrame(LABEL_TITLE);
        frame.setBounds(100, 100, 500, 725);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout(0, 0));
        frame.setLocationRelativeTo(null);
        SwingUtil.updateLookAndFeel(frame);

        tabPanel = new JTabbedPane();
        frame.getContentPane().add(tabPanel, "name_5009361789717");

        if (!dbFluteClientLogic.getProjectList().isEmpty()) {
            ClientPanel clientPanel = new ClientPanel(frame);
            tabPanel.addTab(NewClientPanel.LABEL_PROJECT_TAB, clientPanel);
        }

        NewClientPanel newClientPanel = new NewClientPanel(frame);
        tabPanel.addTab(" + ", newClientPanel);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu setting = new JMenu(LABEL_SETTING);
        menuBar.add(setting);

        JMenuItem downloadMenuItem = new JMenuItem(new DownloadAction());
        setting.add(downloadMenuItem);

        JMenuItem proxySettingsMenuItem = new JMenuItem(new ProxySettingsViewAction());
        setting.add(proxySettingsMenuItem);

        JMenu help = new JMenu(LABEL_HELP);
        menuBar.add(help);

        JMenuItem versionMenuItem = new JMenuItem(LABEL_VERSION + ": " + dbFluteIntroLogic.getVersion());
        help.add(versionMenuItem);

        JMenuItem upgradeMenuItem = new JMenuItem(new UpgradeAction());
        help.add(upgradeMenuItem);

        if (dbFluteEngineLogic.getExistedVersionList().isEmpty()) {
            downloadDBFlute();
        }
    }

    private class DownloadAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public DownloadAction() {
            putValue(NAME, LABEL_DOWNLOAD);
        }

        public void actionPerformed(ActionEvent event) {
            downloadDBFlute();
        }
    }

    private class ProxySettingsViewAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ProxySettingsViewAction() {
            putValue(NAME, LABEL_PROXY_SETTING);
        }

        public void actionPerformed(ActionEvent event) {
            viewProxySettings();
        }
    }

    private class ProxySettingsAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ProxySettingsAction() {
            putValue(NAME, LABEL_SET);
        }

        public void actionPerformed(ActionEvent event) {

            Properties properties = dbFluteIntroLogic.getProperties();
            properties.put("proxyHost", proxyHostText.getText());
            properties.put("proxyPort", proxyPortText.getText());
            properties.put("java.net.useSystemProxies", String.valueOf(useSystemProxiesCheckBox.isSelected()));

            FileOutputStream stream = null;
            try {
                stream = FileUtils.openOutputStream(new File(DbFluteIntroLogic.INI_FILE_PATH));
                properties.store(stream, "Proxy Setting");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(stream);
            }

            dbFluteIntroLogic.loadProxy();

            dialog.setVisible(false);

            if (dbFluteEngineLogic.getExistedVersionList().isEmpty()) {
                downloadDBFlute();
            }
        }
    }

    private class UpgradeAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public UpgradeAction() {
            putValue(NAME, LABEL_UPGRADE);
        }

        public void actionPerformed(ActionEvent event) {

            boolean result = dbFluteIntroLogic.upgrade();
            if (!result) {
                JOptionPane.showMessageDialog(frame, MSG_DOWNLOAD_ERROR);
                return;
            }

            JOptionPane.showMessageDialog(frame, "再起動してください。");
            // int reboot = JOptionPane.showConfirmDialog(frame, "再起動しますか。", null, JOptionPane.OK_CANCEL_OPTION);
            // if (reboot == JOptionPane.CANCEL_OPTION) {
            //     return;
            // }
            //
            // System.exit(0);
        }
    }

    private void downloadDBFlute() {

        Properties publicProperties = null;

        try {
            publicProperties = dbFluteEngineLogic.getPublicProperties();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(frame, MSG_NETWORK_ERROR);
            viewProxySettings();
            return;
        }

        String message = String.format(MSG_DOWNLOAD, publicProperties.getProperty("dbflute.latest.release.version"),
                publicProperties.getProperty("dbflute.latest.snapshot.version"));
        final String dbfluteVersion = JOptionPane.showInputDialog(frame, message, publicProperties.getProperty("dbflute.latest.release.version"));

        if (dbfluteVersion == null) {
            JOptionPane.showMessageDialog(frame, MSG_CANCELED_DOWNLOAD);
            return;
        }

        ProgressBarDialog progressBarDialog = new ProgressBarDialog(frame) {

            @Override
            public void execute() {
                try {
                    dbFluteEngineLogic.download(dbfluteVersion);
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(frame, MSG_DOWNLOAD_ERROR);
                    return;
                }

                for (Component component : tabPanel.getComponents()) {
                    if (component instanceof NewClientPanel) {
                        ((NewClientPanel) component).basicPanel.fireVersionInfoDBFlute();
                    }
                }
            }
        };

        progressBarDialog.start("Download", MSG_DOWNLOADING);
    }

    private void viewProxySettings() {

        Properties properties = dbFluteIntroLogic.getProperties();
        boolean javaNetUseSystemProxies = Boolean.parseBoolean(properties.getProperty("java.net.useSystemProxies"));
        String proxyHost = properties.getProperty("proxyHost");
        String proxyPort = properties.getProperty("proxyPort");

        dialog = new JDialog(frame, LABEL_PROXY_SETTING, true);
        dialog.setBounds(100, 100, 250, 180);
        dialog.getContentPane().setLayout(new CardLayout(0, 0));
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        dialog.getContentPane().add(panel, "name_5009361789718");
        panel.setLayout(null);

        useSystemProxiesCheckBox = new JCheckBox(LABEL_USE_SYSTEM_PROXIES + "(※)", javaNetUseSystemProxies);
        useSystemProxiesCheckBox.setBounds(10, 10, 180, 20);
        panel.add(useSystemProxiesCheckBox);

        useSystemProxiesCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireUseSystemProxiesCheckBox();
            }
        });

        JLabel useSystemProxiesAttentionLabel = new JLabel(MSG_USE_SYSTEM_PROXIES_ATTENTION);
        useSystemProxiesAttentionLabel.setBounds(10, 35, 230, 20);
        panel.add(useSystemProxiesAttentionLabel);

        JLabel proxyHostLabel = new JLabel(LABEL_PROXY_HOST);
        proxyHostLabel.setBounds(10, 60, 80, 20);
        panel.add(proxyHostLabel);

        proxyHostText = new JTextField();
        proxyHostText.setBounds(80, 60, 100, 20);
        proxyHostText.setColumns(10);
        proxyHostText.setText(proxyHost);
        panel.add(proxyHostText);

        JLabel proxyPortLabel = new JLabel(LABEL_PROXY_PORT);
        proxyPortLabel.setBounds(10, 85, 80, 20);
        panel.add(proxyPortLabel);

        proxyPortText = new JTextField();
        proxyPortText.setBounds(80, 85, 100, 20);
        proxyPortText.setColumns(10);
        proxyPortText.setText(proxyPort);
        panel.add(proxyPortText);

        JButton proxySettingsButton = new JButton(new ProxySettingsAction());
        proxySettingsButton.setBounds(80, 110, 70, 20);
        panel.add(proxySettingsButton);

        fireUseSystemProxiesCheckBox();

        dialog.setVisible(true);
    }

    private void fireUseSystemProxiesCheckBox() {
        boolean selected = useSystemProxiesCheckBox.isSelected();
        proxyHostText.setEditable(!selected);
        proxyPortText.setEditable(!selected);
    }
}
