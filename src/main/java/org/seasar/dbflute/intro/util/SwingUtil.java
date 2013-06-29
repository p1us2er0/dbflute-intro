package org.seasar.dbflute.intro.util;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

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
}
