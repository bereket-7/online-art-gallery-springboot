package com.project.oag.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.FileUploadUtil;
import com.project.oag.entity.Event;
import com.project.oag.service.EventService;


@RestController
@RequestMapping("/event")
@CrossOrigin("http://localhost:8080/")
public class EventController {
	 @Autowired
	 private EventService eventService;
	 
	// @Value("${uploadDir}")
	 private String path = "src/main/resources/static/img/event-images/";
	
	 @PostMapping("/upload")
	 public ResponseEntity<Event> fileUpload(@ModelAttribute("event") Event event,@RequestParam("image") MultipartFile image) throws IOException
     {
		 //String uploadDir = "images/" + savedUser.getId();
		  String filename = StringUtils.cleanPath(image.getOriginalFilename());
		 //String filename = null;
		//try {
			event.setEventphoto(filename);
			eventService.uploadEvent(event);
			FileUploadUtil.uploadFile(path, filename, image);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return new ResponseEntity<>(new Event(filename,"Image is not uploaded"),HttpStatus.OK);
		//}	 
		 return new ResponseEntity<>(new Event(filename,"Image is successfully uploaded"),HttpStatus.OK);	
	}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 /*
		public String uploadEvent(String path,MultipartFile file) throws IOException{
			
			 String filename = file.getOriginalFilename();
			 String filePath = path + File.separator + filename;
			 
			 File f = new File(path);
			 
			 if(!f.exists()) {
				 f.mkdir();
			 }
			 
	         //Path filePath = uploadPath.resolve(fileName);
	         //Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			 
			Files.copy(file.getInputStream(), Paths.get(filePath));
			
			//return eventRepository.save();
			 
			 return filename;
			
		}*/
}
