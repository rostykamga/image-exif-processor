package org.rostykamga.imageexifprocessor;

import com.drew.metadata.MetadataException;

import javax.swing.*;

public class MainClass {

    public static void main(String[] args) throws MetadataException {

        SwingUtilities.invokeLater(() -> {
            MainUI ui = new MainUI();
            ui.setTitle("Image-exif-extractor");
            ui.setVisible(true);
        });
    }
}
