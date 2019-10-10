package com.whatsappsim.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatsappsim.dto.MessageDTO.FileDTO;
import com.whatsappsim.mongodb.model.Message;
import com.whatsappsim.mongodb.repository.MessagesRepository;

@Service
public class FileService {

	@Autowired
	private MessagesRepository messagesRepository;
	
	/**
	 * Recover by a messageId the path to an uploaded image and transforms to Base 64  
	 * @param messageId
	 */
	public FileDTO getImage(String messageId) throws IOException{
		FileDTO returnImage = new FileDTO();
        
		Message message = messagesRepository.getFileByMessageIdAndType(messageId, "images");
		FileDTO image = message.getImages().get(0);
        returnImage.setName(image.getName());
        String fileReal = image.getReal();
		String mimetype = fileReal.substring(fileReal.lastIndexOf(".")+1);
		
		returnImage.setReal("data:"+imageExtensionToMimeType(mimetype)+";base64," + 
				new String(Base64.getEncoder().encode(loadFileAsBytesArray(fileReal))));
		return returnImage;
	}

	/**
	 * Recover by a messageId the path to an uploaded video and transforms to Base 64  
	 * @param messageId
	 */
	public FileDTO getVideo(String messageId) throws IOException {
		FileDTO returnVideo = new FileDTO();
		
		Message message = messagesRepository.getFileByMessageIdAndType(messageId, "videos");
	    String fileName = message.getVideos().get(0).getName();
		String fileReal = message.getVideos().get(0).getReal();
		String mimetype = fileName.substring(fileName.lastIndexOf(".")+1);
		returnVideo.setName(fileName);
		returnVideo.setReal("data:"+videoExtensionToMimeType(mimetype)+";base64," 
				+ new String(Base64.getEncoder().encode(loadFileAsBytesArray(fileReal))));
		
		return returnVideo;
	}
	
	/**
	 * Save a file to a given path
	 * @param filePath
	 * @param data
	 */
	public void saveFile(String filePath, String data) throws IOException {
		data = data.substring(data.indexOf(",")+1);
    	byte[] imageDataBytes = Base64.getDecoder().decode(data);
        FileOutputStream imageOutFile = new FileOutputStream(filePath);
        
        imageOutFile.write(imageDataBytes);
        imageOutFile.flush();
        imageOutFile.close();
	}
	
	private static String imageExtensionToMimeType(String extension){
		String mimeType = "";
		switch (extension) { 
			case "gif": mimeType = "image/gif"; break;
			case "jpeg": mimeType = "image/jpeg"; break;
			case "jpg": mimeType = "image/jpeg"; break;
			case "png": mimeType = "image/png"; break;
			case "bmp": mimeType = "image/bmp"; break;		
		}
		return mimeType;
	}
	
	private static String videoExtensionToMimeType(String extension){
		String mimeType = "";
		switch (extension) { 
			case "flv": mimeType = "video/x-flv"; break;
			case "mp4": mimeType = "video/mp4"; break;
			case "m3u8": mimeType = "application/x-mpegURL"; break;
			case "ts": mimeType = "video/MP2T"; break;
			case "3gp": mimeType = "video/3gpp"; break;
			case "mov": mimeType = "video/quicktime"; break;
			case "avi": mimeType = "video/x-msvideo"; break;
			case "wmv": mimeType = "video/x-ms-wmv"; break;
		}
		return mimeType;
	}
	
	private static byte[] loadFileAsBytesArray(String fileName) throws IOException {
		File file = new File(fileName);
	    InputStream is = new FileInputStream(file);

	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	    	is.close();
	    	throw new IOException("File is too long "+file.getName());
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	    	is.close();
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    is.close();
	    return bytes;
	}
	
}
