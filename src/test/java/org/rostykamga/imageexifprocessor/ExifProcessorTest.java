package org.rostykamga.imageexifprocessor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rostykamga.imageexifprocessor.model.ExifData;
import org.rostykamga.imageexifprocessor.utils.Parser;
import org.rostykamga.imageexifprocessor.utils.StringUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExifProcessorTest {

    private static Metadata mockedMetadata;
    private static FileSystemDirectory mockedFSDirectory;
    private static ExifSubIFDDirectory mockedExifSubIfDirectory;
    private static ExifIFD0Directory mockedExifD0Directory;
    private static ExifData exifData;

    /**
     * Setup the mocks to be used, and the static object.
     */
    @BeforeAll
    static void setup(){

        // Create our mocks
        mockedMetadata = mock(Metadata.class);
        mockedFSDirectory = mock(FileSystemDirectory.class);
        mockedExifSubIfDirectory = mock(ExifSubIFDDirectory.class);
        mockedExifD0Directory = mock(ExifIFD0Directory.class);

        //Mock methods
        when(mockedFSDirectory.getDescription(FileSystemDirectory.TAG_FILE_SIZE)).thenReturn("12345 bytes");

        when(mockedExifSubIfDirectory.getDescription(ExifDirectoryBase.TAG_EXIF_IMAGE_WIDTH)).thenReturn("1920 pixels");
        when(mockedExifSubIfDirectory.getDescription(ExifDirectoryBase.TAG_EXIF_IMAGE_HEIGHT)).thenReturn("1080 pixels");

        when(mockedExifD0Directory.getDescription(ExifIFD0Directory.TAG_RESOLUTION_UNIT)).thenReturn("Inch");
        when(mockedExifD0Directory.getDescription(ExifIFD0Directory.TAG_COPYRIGHT)).thenReturn("2021");
        when(mockedExifD0Directory.getDescription(ExifIFD0Directory.TAG_MAKE)).thenReturn("Sony");
        when(mockedExifD0Directory.getDescription(ExifIFD0Directory.TAG_MODEL)).thenReturn("E10");
        when(mockedExifD0Directory.getDescription(ExifDirectoryBase.TAG_X_RESOLUTION)).thenReturn("72 pixels");
        when(mockedExifD0Directory.getDescription(ExifDirectoryBase.TAG_Y_RESOLUTION)).thenReturn("62 pixels");
        when(mockedExifD0Directory.getDescription(ExifDirectoryBase.TAG_DATETIME)).thenReturn("2021:10:11 15:26:08");

        when(mockedMetadata.getFirstDirectoryOfType(FileSystemDirectory.class)).thenReturn(mockedFSDirectory);
        when(mockedMetadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class)).thenReturn(mockedExifSubIfDirectory);
        when(mockedMetadata.getFirstDirectoryOfType(ExifIFD0Directory.class)).thenReturn(mockedExifD0Directory);

        exifData = new ExifData();
        exifData.setDatetime(new Date(1633962368000L));
        exifData.setModel("E10");
        exifData.setxResolution(72.0);
        exifData.setMake("Sony");
        exifData.setCopyright("2021");
        exifData.setResolutionUnit("Inch");
        exifData.setImageWidth(1920);
        exifData.setImageHeight(1080);
        exifData.setImageLength(12345);
        exifData.setyResolution(62.0);

    }

    @Test
    void extractThumbnail() {
    }

    @Test
    void extractExif_test() {

        // given
        ExifData expected = exifData;

        //when
        ExifData actual = new ExifProcessor().extractExif(mockedMetadata);

        //then
        assertEquals(expected, actual);
    }
}