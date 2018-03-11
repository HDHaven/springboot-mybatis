package com.haven.framework.springboot.mybatis.upload.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	@GetMapping("/in")
	public String in() {
		logger.debug("进来了");
		return "upload";
	}
	
	@PostMapping("/test")
	public @ResponseBody String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String contentType = file.getContentType();
		String fileName = file.getOriginalFilename();
		logger.info("Content-Type: "+ contentType +", fileName: "+ fileName +", size: "+ file.getSize());
		String filePath = "D:/upload";
		File newFile = new File(filePath);
		if(!newFile.exists()) newFile.mkdirs();
		try {
			logger.debug("准备保存文件到目录："+ filePath +"/"+ fileName);
			FileOutputStream fos = new FileOutputStream(filePath +"/"+ fileName);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
			logger.debug("成功保存上传的文件");
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			return "File Not Found";
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return "File Upload Failure";
		}
		return "OK";
	}
	
}
