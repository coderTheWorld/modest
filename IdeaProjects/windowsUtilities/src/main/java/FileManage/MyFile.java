package FileManage;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

public class MyFile extends File {
    MyFile(String s) {
        super(s);
    }

    private synchronized boolean move() throws IOException {
        if (this.exists()) {
            if (!this.isDirectory()) {

                Logger logger = Logger.getLogger("FileManage");


                Date date = new Date(this.lastModified());
                System.out.printf("%tF%n", date);
                String dateString = String.format("%tF", date);
                File des = new File(this.getParent() + '\\' + dateString);
                if (des.exists()) {
                    if (!des.isDirectory()) {
                        logger.severe("The same name file exists,can not create dir!");
                        System.out.println("The same name file exists,can not create dir!");
                        return false;
                    }
                } else {
                    try {
                        if (des.mkdir()) {
                            System.out.println(des.getName() + "Dir Successfully created!");
                            logger.info(des.getName() + "Dir Successfully created!");
                        } else {
                            System.out.println(des.getName() + "Dir created failed!");
                            logger.severe(des.getName() + "Dir created failed!");
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                try {
                    if (this.renameTo(new File(des.getAbsolutePath() + '\\' + this.getName()))) {
                        System.out.println(this.getName() + "File move successfully!");
                        logger.info(this.getName() + "File move successfully!");
                        return true;
                    } else {
                        System.out.println(this.getName() + "File move failed!");
                        logger.severe(this.getName() + "File move failed!");
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                System.out.println("Skip directory!");
                return true;
            }
        } else {
            System.out.println("File does not exist!");
            return true;
        }
    }


    public boolean rerange(FileFilter fileFilter) {
        File[] fileList = this.listFiles(fileFilter);
        for (File f : fileList) {
            MyFile myFile = new MyFile(f.getAbsolutePath());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (myFile.move()) {
                    System.out.println("succeed!");
                } else {
                    System.out.println("failed!");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean rename(FileFilter fileFilter) {
        File[] fileList = this.listFiles(fileFilter);

        int i = 0;
        for (File f : fileList) {
            MyFile myFile = new MyFile(f.getAbsolutePath());

            if (myFile.rename(String.valueOf(f.hashCode()))) {
                System.out.println("succeed!");
            } else {
                System.out.println("failed!");
                return false;
            }
        }

        fileList = this.listFiles(fileFilter);
        Arrays.sort(fileList, new IMGComparatorByCreateDateTime());
        i = 0;
        for (File f : fileList) {
            MyFile myFile = new MyFile(f.getAbsolutePath());

            if (myFile.rename(f.getParentFile().getName() + "_" + i++)) {
                System.out.println("succeed!");
            } else {
                System.out.println("failed!");
                return false;
            }
        }
        return true;
    }

    class FileComparatorByLastModified implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return (f1.lastModified() - f2.lastModified()) > 0 ? 1 : ((f1.lastModified() - f2.lastModified() < 0) ? -1 : 0);
        }
    }

    class IMGComparatorByCreateDateTime implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return IMG.getIMGOrignalDate(f1).compareTo(IMG.getIMGOrignalDate(f2));
        }
    }

    private synchronized boolean rename(String filename) {
        if (this.exists()) {
            if (!this.isDirectory()) {

                Logger logger = Logger.getLogger("FileManage");

                System.out.println(this.getParent() + '\\' + this.getName());
                System.out.println(this.getParent() + '\\' + this.getName().replaceAll(".+\\.", filename + "\\."));
                String des = this.getParent() + '\\' + this.getName().replaceAll(".+\\.", filename + "\\.");


                try {
                    if (this.renameTo(new File(des))) {
                        System.out.println(this.getName() + "File rename successfully!");
                        logger.info(this.getName() + "File rename successfully!");
                        return true;
                    } else {
                        System.out.println(this.getName() + "File rename failed!");
                        logger.severe(this.getName() + "File rename failed!");
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                System.out.println("Skip directory!");
                return true;
            }
        } else {
            System.out.println("File does not exist!");
            return true;
        }
    }
}
