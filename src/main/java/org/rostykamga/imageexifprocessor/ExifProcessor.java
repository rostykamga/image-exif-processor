package org.rostykamga.imageexifprocessor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.*;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import org.rostykamga.imageexifprocessor.model.ExifData;
import org.rostykamga.imageexifprocessor.utils.Parser;
import org.rostykamga.imageexifprocessor.utils.StringUtils;

import java.io.File;
import java.io.IOException;

public class ExifProcessor {


    public void extractThumbnail(String imageFile){

//        File jpegFile = new File(imageFile);
//        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
//        Directory thubnail = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
    }

    public ExifData extractExif(Metadata metadata) {

        Directory fileDir = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
        Directory imgSubIfDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Directory ifd0Dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        if(fileDir == null && imgSubIfDir == null && ifd0Dir == null)
            return null;

        ExifData data = new ExifData();

        // Set the image length if available
        if(fileDir != null){
            long fileSize = Parser.parseToLongOrDefault(
                    StringUtils.takeNthChunk(0, fileDir.getDescription(FileSystemDirectory.TAG_FILE_SIZE)),
                    0
            );

            data.setImageLength(fileSize);
        }

        //Set the image width, and height
        if(imgSubIfDir != null){
            int imageWidth = Parser.parseToIntOrDefault(
                    StringUtils.takeNthChunk(0, imgSubIfDir.getDescription(ExifDirectoryBase.TAG_EXIF_IMAGE_WIDTH)),
                    0
            );
            int imageHeight = Parser.parseToIntOrDefault(
                    StringUtils.takeNthChunk(0, imgSubIfDir.getDescription(ExifDirectoryBase.TAG_EXIF_IMAGE_HEIGHT)),
                    0);

            data.setImageHeight(imageHeight);
            data.setImageWidth(imageWidth);
        }

        if(ifd0Dir != null){
            data.setResolutionUnit(ifd0Dir.getDescription(ExifIFD0Directory.TAG_RESOLUTION_UNIT));

            data.setCopyright(ifd0Dir.getDescription(ExifIFD0Directory.TAG_COPYRIGHT));

            data.setMake(ifd0Dir.getDescription(ExifIFD0Directory.TAG_MAKE));

            data.setModel(ifd0Dir.getDescription(ExifIFD0Directory.TAG_MODEL));

            data.setxResolution(Parser.parseToDoubleOrDefault(
                    StringUtils.takeNthChunk(0, ifd0Dir.getDescription(ExifDirectoryBase.TAG_X_RESOLUTION)),
                    0.0));

            data.setyResolution(Parser.parseToDoubleOrDefault(
                    StringUtils.takeNthChunk(0, ifd0Dir.getDescription(ExifDirectoryBase.TAG_Y_RESOLUTION)),
                    0.0));

            data.setDatetime(Parser.parseToDateOrDefault(ifd0Dir.getDescription(ExifIFD0Directory.TAG_DATETIME), null));
        }
        return data;

    }

    /**
     * Extracts the Primary camera and image exif data into a POJO
     * @param imageFile
     * @return the extracted image exif data if found, or null otherwise.
     */
    public ExifData extractExif(String imageFile) {
        File jpegFile = new File(imageFile);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

            return extractExif(metadata);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
