package com.khana.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppProperties;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.services.SystemServiceUtil;
import com.floreantpos.ui.model.MenuItemForm;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class Updater {
	public final static String VERSION = AppProperties.getVersion();

	public static void main(String[] args) throws RarException, IOException {
		checkForUpdate();
	}

	public Updater() {

	}

	public static void checkForUpdate() throws RarException, IOException {
		try {
			JsonNode node = shouldUpdate();
			System.out.println(VERSION+"="+node.get("version").asText());
			if(node.get("update").asBoolean()&&VERSION.trim().compareTo(node.get("version").asText().trim())!=0) {
				try {
					if(node.get("changeTax")!=null&&node.get("changeTax").asBoolean())
						changeTax();
				}catch (Exception e) {}
				
				if(download(node)) {
					try {
						final File directory = new File("Native_lib");
						if (!directory.exists()) {
							directory.mkdirs();
						}

						final File mditory = new File("resources/media");
						if (!mditory.exists()) {
							mditory.mkdirs();
						}
						final File imditory = new File("resources/images");
						if (!imditory.exists()) {
							imditory.mkdirs();
						}

						final File icoditory = new File("resources/icons");
						if (!icoditory.exists()) {
							icoditory.mkdirs();
						}

						final File iditory = new File("i18n");
						if (!iditory.exists()) {
							iditory.mkdirs();
						}

						final File lditory = new File("lib");
						if (!lditory.exists()) {
							lditory.mkdirs();
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					if(!node.get("mustUpdate").asBoolean()) {
						int option = JOptionPane.showConfirmDialog(null,
								"Wollen Sie jetz update?", "Neue Update ist verfugbar..", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
						if (option == JOptionPane.YES_OPTION) {
							startUpdater();
							Application.getInstance().exitPOS(false);
						}
					}else {
						startUpdater();
						Application.getInstance().exitPOS(false);
					}	
				}


				
					
			}
			
			if(node.get("showTseInfo")!=null&&node.get("showTseInfo").asBoolean()&&!TerminalConfig.isTseEnable()&&!TerminalConfig.isTseContacted()&&SystemServiceUtil.shouldTseInfo(new Date())) {		
				JOptionPane.showMessageDialog(null, "Für die erfolgreiche Aktivierung des TSE Moduls  bis zum 31.03.2021,\r\n"
						+ "bitte kontaktieren Sie uns rechtzeitig und  beachten Sie für die \r\n"
						+ "Aktivierung des TSE Moduls ist eine stabile Internetverbindung notwendig.\r\n"
						+ "Kontakt Über....\r\n"
						+ "Tel.: 06995649624\r\n"
						+ "E-Mail: tse.khana.kassystem@khana.de");
			}	
			
		}catch(Throwable ex) {
			ex.printStackTrace();
		}
	}

	public synchronized static void changeTax() {
		Tax imHausTax = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
		imHausTax.setRate(19.00);
		TaxDAO.getInstance().saveOrUpdate(imHausTax);
		Tax lieferTax = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
		lieferTax.setRate(7.00);
		TaxDAO.getInstance().saveOrUpdate(lieferTax);
		TerminalConfig.setSpecial(false);
		updateMenu();
		System.out.println("Tax Updated");
	}




	private static void updateMenu() {
		try {
			try {
				List<MenuItem> tempList;
				tempList = MenuItemDAO.getInstance().findAll();
				Session session = MenuItemDAO.getInstance().createNewSession();
				Transaction tx = session.beginTransaction();

				Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
				Tax home = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
				Tax zero = TaxDAO.getInstance().findByName("ZERO");
				for (Iterator itr = tempList.iterator(); itr.hasNext();) {
					MenuItemForm editor;
					MenuItem item = (MenuItem) itr.next();
					if (item.getParent().getParent().getType().compareTo(POSConstants.DINE_IN) == 0) {
						item.setTax(dineIn);
					} else if (item.getParent().getParent().getType().compareTo(POSConstants.HOME_DELIVERY) == 0) {
						item.setTax(home);
					} else {
						item.setTax(zero);
					}
					session.saveOrUpdate(item);
				}

				tx.commit();
				session.close();				
			} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
			}


		} catch (Throwable x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE,
					x);
		}
	}

	public static void startUpdater() {
		try {		
			Runtime.getRuntime().exec("cmd /c start update.vbs");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public synchronized static boolean download(JsonNode node) throws RarException, IOException {
		try {
			System.out.println(node.get("downloadLink").asText());
			return downloadFileViaHttpChecksum(node.get("downloadLink").asText(), new File("update.rar"));				
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		//		update("update");
		//		System.out.println("Update file finished");
		//		
		//		if(node.get("tseUpdate").asBoolean()) {
		//			try {
		//				System.out.println(node.get("tseDownloadLink").asText());
		//				downloadFileFromURL(node.get("tseDownloadLink").asText(), new File("Native-lib.rar"));				
		//			} catch (Throwable e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}			
		//			updateTse("Native-lib");
		//			System.out.println("Tse update finished");
		//		}

	}



	private static final int BUFFER_SIZE = 4096;

	
	
	
	public static boolean downloadFileViaHttpChecksum(String fileURL, File saveDir){
		boolean success = false;

		try {
			while(!success) {
				URL url = new URL(fileURL);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				int responseCode = httpConn.getResponseCode();
				int lenghtOfFile = httpConn.getContentLength();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream inputStream = httpConn.getInputStream();			
					FileOutputStream outputStream = new FileOutputStream(saveDir);

					int bytesRead = -1;
					byte[] buffer = new byte[BUFFER_SIZE];
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					outputStream.close();
					inputStream.close();
					
					httpConn.disconnect();
					if(saveDir.length()==lenghtOfFile)
						success =true;
				}
			}
		}catch(IOException ex) {
			
		}
		return success;
	}
	
	
	
	public static void downloadFileViaHttp(String fileURL, File saveDir)
	{
		try {
			URL url = new URL(fileURL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {				
				InputStream inputStream = httpConn.getInputStream();				
				FileOutputStream outputStream = new FileOutputStream(saveDir);
				int bytesRead = -1;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();

				System.out.println("File downloaded");
			} else {
				System.out.println("No file to download. Server replied HTTP code: " + responseCode);
			}
			httpConn.disconnect();
		}catch(IOException ex ) {
			downloadFileFromURL(fileURL, saveDir);
		}
	}



	public static void downloadFileFromURL(String urlString, File destination) {    
		try {
			URL website = new URL(urlString);
			ReadableByteChannel rbc;
			rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(destination);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			rbc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static JsonNode shouldUpdate() {
		try {
			HttpClient client = new HttpClient();
			client.start();
			ContentResponse response = null;
			response =	client.newRequest(Application.getInstance().getRestaurant().getUpdateLink()).timeout(60, TimeUnit.SECONDS).send();
			client.stop();
			String body = response.getContentAsString();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode json = mapper.readTree(body);
			return json;
		}catch(Exception ex) {

		}
		return null;
	}


	public static boolean unrarFile(File rarFile, File destDir) throws IOException, RarException {
		try (Archive archive = new Archive(rarFile)) {
			FileHeader fh = archive.nextFileHeader();
			while (fh != null) {
				String compressFileName = fh.getFileNameString().trim();
				File destFile = new File(destDir.getAbsolutePath());

				if (fh.isDirectory()) {
					if (!destFile.exists()) {
						destFile.mkdirs();
					}
					fh = archive.nextFileHeader();
					continue;
				}

				if (!destFile.getParentFile().exists()) {
					destFile.getParentFile().mkdirs();
				}

				try (FileOutputStream fos = new FileOutputStream(destFile)) {
					archive.extractFile(fh, fos);
				}
				fh = archive.nextFileHeader();
			}
		}
		return true;
	}



	public static void update(String name) {		
		try {
			File f = new File(name+".rar");
			Archive archive = new Archive(f);
			archive.getMainHeader().print();
			FileHeader fh = archive.nextFileHeader();
			while(fh!=null){        
				File fileEntry = new File(fh.getFileNameString().trim());
				System.out.println(fileEntry.getAbsolutePath());
				FileOutputStream os = new FileOutputStream(fileEntry);
				archive.extractFile(fh, os);
				os.close();
				fh=archive.nextFileHeader();
			}
		}catch(Throwable ex) {

		}
	}


	public static void updateTse(String destination){
		try {
			File f = new File("Native-lib.rar");
			Archive archive = new Archive(f);
			archive.getMainHeader().print();
			FileHeader fh = archive.nextFileHeader();
			while(fh!=null){        
				File fileEntry = new File(destination.trim());
				FileOutputStream os = new FileOutputStream(fileEntry);
				archive.extractFile(fh, os);
				os.close();
				fh=archive.nextFileHeader();
			}
		}catch(Throwable ex) {

		}
	}
	//	
	//	
	//	
	//	private static final int BUFFER_SIZE = 4096;
	//    /**
	//     * Extracts a zip file specified by the zipFilePath to a directory specified by
	//     * destDirectory (will be created if does not exists)
	//     * @param zipFilePath
	//     * @param destDirectory
	//     * @throws IOException
	//     */
	//    public void unzip(String zipFilePath, String destDirectory) throws IOException {
	//        File destDir = new File(destDirectory);
	//        if (!destDir.exists()) {
	//            destDir.mkdir();
	//        }
	//        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	//        ZipEntry entry = zipIn.getNextEntry();
	//        // iterates over entries in the zip file
	//        while (entry != null) {
	//            String filePath = destDirectory + File.separator + entry.getName();
	//            if (!entry.isDirectory()) {
	//                // if the entry is a file, extracts it
	//                extractFile(zipIn, filePath);
	//            } else {
	//                // if the entry is a directory, make the directory
	//                File dir = new File(filePath);
	//                dir.mkdirs();
	//            }
	//            zipIn.closeEntry();
	//            entry = zipIn.getNextEntry();
	//        }
	//        zipIn.close();
	//    }
	//    /**
	//     * Extracts a zip entry (file entry)
	//     * @param zipIn
	//     * @param filePath
	//     * @throws IOException
	//     */
	//    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
	//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
	//        byte[] bytesIn = new byte[BUFFER_SIZE];
	//        int read = 0;
	//        while ((read = zipIn.read(bytesIn)) != -1) {
	//            bos.write(bytesIn, 0, read);
	//        }
	//        bos.close();
	//    }
}
