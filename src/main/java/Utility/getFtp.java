package Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class getFtp {
	
	
	public static boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) {  
		
	    //getFtp.downFile("155.35.75.161", 21, "test","111111", "D:\\VM",  "Oolong.jar","C:\\Jsystem_workspace\\Oolong\\");
		boolean success = false;  
	    FTPClient ftp = new FTPClient();  
	    try {  
	        int reply;  
	        ftp.connect(url, port);  
	        //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
	        ftp.login(username, password);//登录  
	        reply = ftp.getReplyCode();  
	        if (!FTPReply.isPositiveCompletion(reply)) {  
	            ftp.disconnect();  
	            return success;  
	        }  
	        ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录  
	        FTPFile[] fs = ftp.listFiles();  
	        for(FTPFile ff:fs){  
	            if(ff.getName().equals(fileName)){  
	                File localFile = new File(localPath+"/"+ff.getName());  
	                  
	                OutputStream is = new FileOutputStream(localFile);   
	                ftp.retrieveFile(ff.getName(), is);  
	                is.close();  
	            }  
	        }  
	          
	        ftp.logout();  
	        success = true;  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (ftp.isConnected()) {  
	            try {  
	                ftp.disconnect();  
	            } catch (IOException ioe) {  
	            }  
	        }
	    }  
	    System.out.println("Updated recent jar from Server!");
	    return success;

}
	public static boolean uploadFile(String url, int port, String username,String password, String path, String filename, InputStream input) {
		
		FTPClient ftpClient = new FTPClient();  
		String encoding = System.getProperty("file.encoding");
		
        boolean result = false;
 
        try {
            int reply;
           
            ftpClient.connect(url);
            ftpClient.login(username, password);
            ftpClient.setControlEncoding(encoding);
         
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("failed");
                ftpClient.disconnect();
                return result;
            }
 
         
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                //result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
                //ftpClient.storeFileStream(remotePath+"/"+filename);
            	ftpClient.storeFile(filename, input);
                System.out.println("ok, done! uploaded successfully");
                
                if (result) {
                    System.out.println("ok, done! uploaded successfully");
                }else {
                	 //System.out.println("No!");
                }
            }else {
            	System.out.println("No!path");
            }
           // input.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        		return result;
    }
 
}