import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;


public class IMGMetadataTest {

    public static void main(String[] args) {

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return false;
                }
                return true;
            }
        };

        File f = new File("F:\\OpenSource\\TEST\\IMG\\");
        File[] flist = f.listFiles(fileFilter);
        for (File img : flist) {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(img);
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        System.out.println(tag);
                    }
                }
                ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                Date date = null;
                if (exifDirectory != null && exifDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                    date = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                } else {
                    date = new Date(img.lastModified());
                }
                System.out.println("HEY:" + date);

            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

