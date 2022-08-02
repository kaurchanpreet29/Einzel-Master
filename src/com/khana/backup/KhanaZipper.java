package com.khana.backup;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import com.floreantpos.config.TerminalConfig;
public class KhanaZipper {

//    public static void main(String[] args) {
//
//        Path source = Paths.get("database/derby-single/posdb");
//       
//        if (!Files.isDirectory(source)) {
//            System.out.println("Please provide a folder.");
//            return;
//        }
//
//        try {
//
//        	KhanaZipper.zipFolder(source);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Done");
//
//    } 
	
    public static boolean zipMyFolder() {   	
    	Path source = Paths.get("database/derby-single/posdb");
    	 if(TerminalConfig.isNoGUI()||TerminalConfig.isKhanaServer())
    		 source = Paths.get("database/derby-server/posdb");

         if (!Files.isDirectory(source)) {
        	 int option = JOptionPane.showConfirmDialog(null,
 					"Möchten Sie ein Backup von gestern erhalten?", "Datenbank ist nicht verfügbar!", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
 			if (option == JOptionPane.YES_OPTION) {
 				 if(!unzipFile(source.toString()))
 				 JOptionPane.showMessageDialog(null, "Kontaktiern Sie Kassensystem Support!!!");
 			}else {
 				return false;
 			}       	
            
         }

         if(TerminalConfig.isKhanaClient())
        	 return false;
         
         try {

         	zipFolder(source);

         } catch (IOException e) {
             e.printStackTrace();
         }
         return true;
        
    }
    
    
    
    public static boolean unzipFile(String destination) {
    	File directory = new File("C:/Backup/Database");
    	if (!directory.exists()) {
			directory.mkdirs();
		}
    	SimpleDateFormat format = new SimpleDateFormat("dd");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DAY_OF_MONTH, -1);
    	
        String zipFileName = directory.toString()+"/" +format.format(calendar.getTime())+ ".zip";
        
        if(!new File(zipFileName).exists())      	
        	return false;
        File destDir = new File(destination);       
       
        byte[] buffer = new byte[1024];
        try {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
        	File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }
                
                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
        zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        return true;
        }catch(Exception ex) {
        	  
        }
        return true; 
        
    }    
   
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
     
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
     
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
     
        return destFile;
    }

    // zip a directory, including sub files and sub directories
    public static void zipFolder(Path source) throws IOException {

        // get folder name as zip file name
    	
    	File directory = new File("C:/Backup/Database");
    	if (!directory.exists()) {
			directory.mkdirs();
		}
    	SimpleDateFormat format = new SimpleDateFormat("dd");
    	
    	
        String zipFileName = directory.toString()+"/" +format.format(new Date())+ ".zip";

        try (
                ZipOutputStream zos = new ZipOutputStream(
                        new FileOutputStream(zipFileName))
        ) {

            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file,
                    BasicFileAttributes attributes) {

                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    try (FileInputStream fis = new FileInputStream(file.toFile())) {

                        Path targetFile = source.relativize(file);
                        zos.putNextEntry(new ZipEntry(targetFile.toString()));

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }

                        // if large file, throws out of memory
                        //byte[] bytes = Files.readAllBytes(file);
                        //zos.write(bytes, 0, bytes.length);

                        zos.closeEntry();

//                        System.out.printf("Zip file : %s%n", file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    System.err.printf("Unable to zip : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });

        }

    }

}