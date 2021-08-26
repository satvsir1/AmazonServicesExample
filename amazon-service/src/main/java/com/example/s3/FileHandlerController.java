package com.example.s3;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileHandlerController {

	@Autowired
	private AmazonS3ClientService amazonS3ClientService;

	@PostMapping
	public Map<String, String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
		String preSignedUrl = this.amazonS3ClientService.uploadFileToS3Bucket(file, true);

		Map<String, String> response = new HashMap<>();
		response.put("message", "file [" + file.getOriginalFilename() + "] uploading request submitted successfully.");
		response.put("PreSignedUrl", preSignedUrl);
		return response;
	}

	@DeleteMapping
	public Map<String, String> deleteFile(@RequestParam("file_name") String fileName) {
		this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);

		Map<String, String> response = new HashMap<>();
		response.put("message", "file [" + fileName + "] removing request submitted successfully.");

		return response;
	}
}
