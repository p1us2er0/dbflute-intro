package org.dbflute.intro.util;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SwingUtil {

    public static void updateLookAndFeel(Component component) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            //
        } catch (InstantiationException e) {
            //
        } catch (IllegalAccessException e) {
            //
        } catch (UnsupportedLookAndFeelException e) {
            //
        }
        SwingUtilities.updateComponentTreeUI(component);
    }

    public static class FileTransferHandler extends TransferHandler {

        private static final long serialVersionUID = 1L;

        private JTextField textField;

        public FileTransferHandler(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public boolean importData(TransferSupport support) {
            try {
                if (canImport(support)) {

                    @SuppressWarnings("unchecked")
                    List<Object> transferData = (List<Object>) support.getTransferable().getTransferData(
                            DataFlavor.javaFileListFlavor);

                    for (Object obj : transferData) {
                        if (obj instanceof File) {
                            File file = (File) obj;

                            textField.setText(file.getAbsolutePath());
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return false;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }
    }

    public static abstract class ProgressBarDialog {

        private JFrame frame;

        public ProgressBarDialog(JFrame frame) {
            this.frame = frame;
        }

        public void start(String title, String message) {
            final JDialog dialog = new JDialog(frame, title, true);
            dialog.setBounds(100, 100, 200, 100);
            dialog.getContentPane().setLayout(new CardLayout(0, 0));
            dialog.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            // TODO
            dialog.getContentPane().add(panel, "panel");
            panel.setLayout(null);

            JLabel downloadMessageLabel = new JLabel(message);
            downloadMessageLabel.setBounds(10, 10, 200, 20);
            panel.add(downloadMessageLabel);

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setBounds(10, 35, 180, 20);
            progressBar.setValue(0);
            progressBar.setIndeterminate(true);
            panel.add(progressBar);

            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        execute();
                    } finally {
                        dialog.setVisible(false);
                    }
                }
            };
            thread.start();

            dialog.setVisible(true);
        }

        public abstract void execute();
    }
}
