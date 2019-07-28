package com.wlpds.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wlpds.service.Constants;
import com.wlpds.service.SendingNotification;
import com.wlpds.service.VisuallyRecognisedData;


@org.springframework.stereotype.Controller
public class Controller {
	public static final Logger logger = LoggerFactory.getLogger(Controller.class);
	public static String uploadDirectory = System.getProperty("user.dir")+"/uploads"; 
	public static boolean sendNotification = true;

	@RequestMapping("/")
	public String UploadPage(Model model) {
		return "uploadview";
	}

	@RequestMapping("/upload")
	public String Upload(Model model, @RequestParam("file") MultipartFile file) throws Exception{
		Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());

		if (Files.notExists(fileNameAndPath.getParent())) {
			Files.createDirectory(fileNameAndPath.getParent());
		}

		if (file.isEmpty()) {
			throw new Exception("FILE IS EMPTY");
		}

		try {
			//Files.createFile(fileNameAndPath);
			Files.write(fileNameAndPath, file.getBytes());
		}catch(IOException e) {
			logger.error(e.getMessage());
			throw e;
		}

		this.callServices(fileNameAndPath.toString());
		
		if (sendNotification == false) {
			//		model.addAttribute("message","Successfully uploaded files " + fileNameAndPath.toString());
			model.addAttribute("message","Everything is under control....");
			return "uploadstatusview";
		}else {
			return "uploadmessagesentview";
		}
	}

	private void callServices(String path) throws Exception {
		VisuallyRecognisedData vdr = new VisuallyRecognisedData();
		vdr.getVisuallyRecognisedData_Java(path);

		if(!vdr.getTextMessage().equals(Constants.Blank)) {
			SendingNotification sn = new SendingNotification();
			sn.sendTextNotification(vdr.getTextMessage());
			sendNotification = true;
		}else {
			sendNotification= false;
		}
	}
}