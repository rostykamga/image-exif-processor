package org.rostykamga.imageexifprocessor.utils;

import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.lang.annotations.NotNull;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.ExifThumbnailDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageUtils {

    public static int TAG_THUMBNAIL_DATA = 0x10000;


    public static ExifReader CUSTOM_READER = new ExifReader() {
        @Override
        public void readJpegSegments(@NotNull final Iterable<byte[]> segments, @NotNull final Metadata metadata, @NotNull final JpegSegmentType segmentType) {
            super.readJpegSegments(segments, metadata, segmentType);

            for (byte[] segmentBytes : segments) {
                // Filter any segments containing unexpected preambles
                if (!startsWithJpegExifPreamble(segmentBytes)) {
                    continue;
                }

                // Extract the thumbnail
                try {
                    ExifThumbnailDirectory tnDirectory = metadata.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
                    if (tnDirectory != null && tnDirectory.containsTag(ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET)) {
                        int offset = tnDirectory.getInt(ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET);
                        int length = tnDirectory.getInt(ExifThumbnailDirectory.TAG_THUMBNAIL_LENGTH);

                        byte[] tnData = new byte[length];
                        System.arraycopy(segmentBytes, JPEG_SEGMENT_PREAMBLE.length() + offset, tnData, 0, length);
                        tnDirectory.setObject(TAG_THUMBNAIL_DATA, tnData);
                    }
                } catch (MetadataException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static BufferedImage byteArrayToImage(byte[] array) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(array);

        return ImageIO.read(bais);
    }
}
