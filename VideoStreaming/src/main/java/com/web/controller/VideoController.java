package com.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.model.Video;
import com.web.msg.CustomMessage;
import com.web.service.VideoService;

@RestController
@RequestMapping("/api/video")
public class VideoController {
	
	 @Value("${files.video}")  
	    private String DIR;
	 
		public static final int CHUNK_SIZE = 1024 * 1024;


//
//	@Autowired
	private VideoService videoService;

		
		
	@CrossOrigin("*")
	@PostMapping("/upload")
	public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
			@RequestParam("description") String description) {
		try {
			Video video = new Video();
			video.setTitle(title);
			video.setDescription(description);
			video.setVideoId(UUID.randomUUID().toString());

			Video savedVideo = videoService.save(video, file);

			if (savedVideo != null) {
				return ResponseEntity.ok(savedVideo);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new CustomMessage("Video is not uploaded", false));
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new CustomMessage("Something went wrong: " + e.getMessage(), false));
		}
	}

	@GetMapping("/stream/{videoId}")
	public ResponseEntity<Resource> stream(@PathVariable String videoId) {

		Video video = videoService.get(videoId);
		String contentType = video.getContentType();
		String filePath = video.getFilePath();

		Resource resource = new FileSystemResource(filePath);

		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

	}

	@GetMapping("/getAll")
	public List<Video> getAll() {
		return videoService.getAll();

	}

	
	// Stream Videos in Chunks
	@SuppressWarnings("resource")
	@GetMapping("/stream/range/{videoId}")
	public ResponseEntity<Resource> streamVideoRange(@PathVariable("videoId") String videoId,
			@RequestHeader(value = "Range", required = false) String range   //  range set in header

	) {
		System.out.println(range);
		Video video = videoService.get(videoId);
		Path path = Paths.get(DIR, video.getFilePath()); 
		Resource resource = new FileSystemResource(path);   
		//new FileSystemResource(path): Loads the file as a resource.
		String contentType = video.getContentType();
		
		
		if (contentType == null) {
			contentType = "application/octet-stream";  
		}	

		// File length
		long fileLength = path.toFile().length(); 

		if (range == null) {  // if range is null  then response full video  
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

		}

		long rangeStart;
		long rangeEnd;
		String[] ranges = range.replace("bytes=", "").split("-"); //Removes "bytes=" from the header and splits it into an array.

		rangeStart = Long.parseLong(ranges[0]);  // range  start  from  0

		rangeEnd = rangeStart + CHUNK_SIZE;   // range-end =  0 + 1mb;
		if (rangeEnd >= fileLength) {    
			rangeEnd = fileLength - 1;
		}
		
		 //ranges[0] = "1048576" (Start)
	     // ranges[1] = "2097152" (End)

		System.out.println("Start Range:- " + rangeStart);
		System.out.println("Start End:- " + rangeEnd);
		InputStream inputStream;
		try {

			inputStream = Files.newInputStream(path);  // read files in chunks 
			long skip = inputStream.skip(rangeStart);
			System.out.println("skip:-" + skip);  // Skips to rangeStart position.

			long contentLength = rangeEnd - rangeStart + 1;
			System.out.println("Content Length:- "+contentLength);

			byte[] data = new byte[(int) contentLength];  //Reads only the required bytes into the data array.
														 //  only  chunks  data read 
			int read = inputStream.read(data, 0, data.length);   // read chunks
			System.out.println("read no of bytes:- " + read);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
			//Content-Range: Tells the browser which bytes are sent (206 Partial Content).
//			headers.add("Cache-Control", "no-cache , no-store, must-revalidate");
//		    headers.add("Pragma", "no-cache"); //Cache-Control, Pragma, Expires: Prevents caching.
//			headers.add("Expires", "0");  // cache  expire  set
			headers.add("X-Content-Type-Options", "nosniff");
			headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");

			headers.setContentLength(contentLength);
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers)
					.contentType(MediaType.parseMediaType(contentType)).body(new ByteArrayResource(data));
            //Sends the chunk as a byte array (ByteArrayResource(data)).
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	  @Autowired
//	    private VideoCache videoCache;
//	
//	
//	 @GetMapping("/stream/range/{videoId}")
//	    public ResponseEntity<Resource> streamVideoRange(@PathVariable("videoId") String videoId,
//	                                                     @RequestHeader(value = "Range", required = false) String range) {
//	        System.out.println("Range Requested: " + range);
//	        
//	        Video video = videoService.get(videoId);
//	        Path path = Paths.get(DIR, video.getFilePath());
//
//	        String contentType = video.getContentType();
//	        if (contentType == null) {
//	            contentType = "application/octet-stream";
//	        }
//
//	        long fileLength = path.toFile().length();
//	        
//	        // **Full Video Serve**
//	        if (range == null) {
//	            Resource resource = new FileSystemResource(path);
//	            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
//	        }
//
//	        long rangeStart;
//	        long rangeEnd;
//	        String[] ranges = range.replace("bytes=", "").split("-");
//	        rangeStart = Long.parseLong(ranges[0]);
//
//	        rangeEnd = rangeStart + CHUNK_SIZE;
//	        if (rangeEnd >= fileLength) {
//	            rangeEnd = fileLength - 1;
//	        }
//
//	        // **Cache Key**
//	        String cacheKey = videoId + ":" + rangeStart + "-" + rangeEnd;
//
//	        // **Check if Chunk is Cached**
//	        if (videoCache.isCached(cacheKey)) {
//	            System.out.println("Serving from Cache");
//	            return serveCachedChunk(cacheKey, fileLength, rangeStart, rangeEnd);
//	        }
//
//	        try (InputStream inputStream = Files.newInputStream(path)) {
//	            inputStream.skip(rangeStart);
//	            long contentLength = rangeEnd - rangeStart + 1;
//
//	            byte[] data = new byte[(int) contentLength];
//	            inputStream.read(data, 0, data.length);
//
//	            // **Add Chunk to Cache**
//	            videoCache.addToCache(cacheKey, data);
//
//	            HttpHeaders headers = new HttpHeaders();
//	            headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
//	            headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
//	            headers.setContentLength(contentLength);
//
//	            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//	                    .headers(headers)
//	                    .contentType(MediaType.parseMediaType(contentType))
//	                    .body(new ByteArrayResource(data));
//	        } catch (IOException e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	        }
//	    }
//
//	    private ResponseEntity<Resource> serveCachedChunk(String cacheKey, long fileLength, long rangeStart, long rangeEnd) {
//	        byte[] data = videoCache.getFromCache(cacheKey);
//
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
//	        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
//	        headers.setContentLength(data.length);
//
//	        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//	                .headers(headers)
//	                .contentType(MediaType.parseMediaType("video/mp4"))
//	                .body(new ByteArrayResource(data));
//	    }
//	
      
}
