package com.khana.backup;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.floreantpos.model.dao.RestaurantDAO;
public class FtpBackup {
	public FtpBackup() {
			backupFile();
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}

	private static final int BUFFER_SIZE = 4096;
	public static void backupFile() {
//	public static void main(String[] args) {

		String server = "vwp9989.webpack.hosteurope.de";
		int port = 21;
		String user = "ftp10975332-315701";
		String pass = "ch03ms23";


		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				System.out.println("Operation failed. Server reply code: " + replyCode);
				return;
			}
			boolean success = ftpClient.login(user, pass);
			showServerReply(ftpClient);
			if (!success) {
				System.out.println("Could not login to the server");
				return;
			}        
			// Creates a directory
			SimpleDateFormat format = new SimpleDateFormat("MM"); 
			SimpleDateFormat format1 = new SimpleDateFormat("dd");
			String genBckpDir = "/khana_gmbh/POS/kunden_backup_einzel/"+RestaurantDAO.getRestaurant().getZipCode()+"_"+RestaurantDAO.getRestaurant().getName()+"/"+format.format(new Date());
//			ftpCreateDirectoryTree(ftpClient, genBckpDir);
			String dbDir = genBckpDir+"/"+format1.format(new Date());
			ftpCreateDirectoryTree(ftpClient, dbDir);
			try {
				uploadFile(ftpClient, genBckpDir,dbDir);
			}catch(Exception ex) {
				System.out.println("Oops! Unbale to upload file");
			}

		} catch (Throwable ex) {
			System.out.println("Oops! Something wrong happened");
			ex.printStackTrace();
			
		}finally {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
			}
		}
	}



	private synchronized static void uploadFile(FTPClient ftpClient, String remoteDir, String dbDir) {	
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd");
			
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// APPROACH #1: uploads first file using an InputStream
			File firstLocalFile = new File("C:/Backup/database/"+format.format(new Date())+".zip");

			String firstRemoteFile = dbDir+"/posdb.zip";
			InputStream inputStream = new FileInputStream(firstLocalFile);

			System.out.println("Start uploading first file");
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
			if (done) {
				System.out.println("The first file is uploaded successfully.");
			}

			// APPROACH #2: uploads second file using an OutputStream
			File secondLocalFile = new File("C:/Backup/config/khana.config_bckp.properties");
			String secondRemoteFile = remoteDir+"/khana.config_bckp.properties";
			inputStream = new FileInputStream(secondLocalFile);

			System.out.println("Start uploading second file");
			OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
			byte[] bytesIn = new byte[4096];
			int read = 0;

			while ((read = inputStream.read(bytesIn)) != -1) {
				outputStream.write(bytesIn, 0, read);
			}
			inputStream.close();
			outputStream.close();

			boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				System.out.println("The second file is uploaded successfully.");
			}

		} catch (Throwable ex) {
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}


private synchronized static boolean ftpCreateDirectoryTree( FTPClient client, String dirTree ) throws IOException {

	boolean dirExists = true;

	//tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
	String[] directories = dirTree.split("/");
	for (String dir : directories ) {
		if (!dir.isEmpty() ) {
			if (dirExists) {
				dirExists = client.changeWorkingDirectory(dir);
			}
			if (!dirExists) {
				if (!client.makeDirectory(dir)) {
					throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
				}
				if (!client.changeWorkingDirectory(dir)) {
					throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
				}
			}
		}
	}
	return dirExists;
}

}