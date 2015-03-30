package org.dbflute.intro.mylasta.util;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

public class SwingUtil {

    public static void updateLookAndFeel(Component component) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
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

            JLabel messageLabel = new JLabel(message);
            messageLabel.setBounds(10, 10, 200, 20);
            panel.add(messageLabel);

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

    public static class JTextAreaStream extends OutputStream {

        private JTextArea textArea;
        private ByteArrayOutputStream out;

        public JTextAreaStream(JTextArea area) {
            textArea = area;
            out = new ByteArrayOutputStream();
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
        }

        @Override
        public void flush() throws IOException {

            // TODO この処理を入れると、ストリームの内容が抜け落ちてしまうため、暫定でコメントアウト
            // SwingUtilities.invokeLater(new Runnable() {
            //     public void run() {
            textArea.append(out.toString());
            textArea.setCaretPosition(textArea.getText().length());
            out.reset();
            //     }
            // });
        }
    }

    public static final DocumentFilter UPPER_DOCUMENT_FILTER = new DocumentFilter() {
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            super.insertString(fb, offset, string.toUpperCase(), attr);
        }

        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null) {
                string = string.toUpperCase();
            }
            super.replace(fb, offset, length, string, attr);
        }
    };

    public static class ReflectionCaretListener implements CaretListener {

        private JTextComponent textComponent;
        private String oldValue = "";

        public ReflectionCaretListener(JTextComponent textComponent) {
            super();
            this.textComponent = textComponent;
        }

        @Override
        public void caretUpdate(CaretEvent event) {

            String sourceText = ((JTextField) event.getSource()).getText();
            String distText = this.textComponent.getText();

            if (oldValue.equals(distText)) {
                this.textComponent.setText(sourceText);
            }

            oldValue = sourceText;
        }
    };
}
