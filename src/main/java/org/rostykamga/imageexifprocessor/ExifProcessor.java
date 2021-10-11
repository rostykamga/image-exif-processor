package org.rostykamga.imageexifprocessor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import org.rostykamga.imageexifprocessor.model.ExifData;
import org.rostykamga.imageexifprocessor.utils.ImageUtils;
import org.rostykamga.imageexifprocessor.utils.Parser;
import org.rostykamga.imageexifprocessor.utils.StringUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;

public class ExifProcessor {

    BufferedImage extractThumbnail(Metadata metadata)  {

        try {
            ExifThumbnailDirectory thubnail = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);

            if (thubnail == null)
                return null;

            byte[] data = (byte[]) thubnail.getObject(ImageUtils.TAG_THUMBNAIL_DATA);

            return ImageUtils.byteArrayToImage(data);
        }
        catch (Exception ex){
            return null;
        }
    }

    public BufferedImage extractThumbnail( String file){

        try {
            return extractThumbnail(JpegMetadataReader.readMetadata(new File(file), Collections.singletonList(ImageUtils.CUSTOM_READER)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    ExifData extractExif(Metadata metadata) {

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
