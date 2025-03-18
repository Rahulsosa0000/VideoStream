package com.web.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.web.model.Video;

public interface VideoService {

	Video save(Video video, MultipartFile file);
	
	Video get(String videoId);
	
	List<Video> getAll();
		
}

