package org.rostykamga.imageexifprocessor;

import com.drew.metadata.MetadataException;

public class MainClass {

    public static void main(String[] args) throws MetadataException {
        ExifProcessor processor = new ExifProcessor();

        System.out.println(processor.extractThumbnail("Test pictures/Canon_40D_photoshop_import.jpg"));
    }
}
