
// import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

// @Component({
//   selector: 'app-video',
//   templateUrl: './video-stream.component.html',
//   styleUrls: ['./video-stream.component.css']
// })
// export class VideoStreamComponent implements OnInit {
//   @ViewChild('videoPlayer') videoPlayer!: ElementRef<HTMLVideoElement>;
//   videoUrl: SafeUrl | null = null;
//   videoId: string = '2542c662-a123-4092-86b7-5a7b40ee973c';  

//   constructor(private http: HttpClient, private sanitizer: DomSanitizer) {}

//   ngOnInit(): void {
//     this.loadVideo(this.videoId);  
//   }

//   loadVideo(videoId: string) {
//     const videoStreamUrl = `http://localhost:8080/api/video/stream/range/${videoId}`;
//     console.log("Fetching video from URL:", videoStreamUrl);  
//     this.videoUrl = this.sanitizer.bypassSecurityTrustUrl(videoStreamUrl);
//   }
// }


// import { CommonModule } from '@angular/common';
// import { Component, OnInit } from '@angular/core';
// import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

// interface Video {
//   id: string;
//   title: string;
//   url: SafeUrl | null;
// }

// @Component({
//   selector: 'app-video',
//   imports:[CommonModule],
//   templateUrl: './video-stream.component.html',
//   styleUrls: ['./video-stream.component.css']
// })
// export class VideoStreamComponent implements OnInit {
//   videos: Video[] = [];  // Multiple Videos List
  

//   constructor(private sanitizer: DomSanitizer) {}

//   ngOnInit(): void {
//     this.loadVideos();
//   }

//   loadVideos() {
//     const videoIds = [
//       //  { id: 'b1294023-afef-4eca-9e6e-1f70b1c0de47', title: 'Marvel Video' },
//       { id: 'c4e22e16-33e7-496f-af06-cfab3489dbb9', title: 'Marvel Video' },
//       // { id: 'ac43c9cb-bae7-4112-af61-5faae6f72fd2', title: 'Nature 1' },
//       // { id: 'f0445947-86a4-4c28-b4bc-f00617c29b05', title: 'Nature 2' }
//     ];

//     this.videos = videoIds.map(video => ({
//       ...video,
//       url: this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/stream/range/${video.id}`)
//     }));
//   }

//   // ... spread operator - existing video  copy  url 
// }




import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

interface Video {
  id: string;
  title: string;
  url: SafeUrl | null;
}

@Component({
  selector: 'app-video',
  imports: [CommonModule],
  templateUrl: './video-stream.component.html',
  styleUrls: ['./video-stream.component.css']
})
export class VideoStreamComponent implements OnInit {
  videos: Video[] = [];  // Multiple Videos List
  selectedVideo: Video | null = null; // Video currently selected for streaming

  constructor(private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.loadVideos();
  }

  loadVideos() {
    const videoIds = [
      { id: 'c4e22e16-33e7-496f-af06-cfab3489dbb9', title: 'Marvel Video' },
      { id: 'ac43c9cb-bae7-4112-af61-5faae6f72fd2', title: 'Nature 1' },
      { id: '5662eb7c-e427-4bed-91f8-67fa2d3f06bf', title: ' TMKOC' }
    ];

    this.videos = videoIds.map(video => ({
      ...video,
      url: this.sanitizer.bypassSecurityTrustUrl(`http://localhost:8080/api/video/stream/range/${video.id}`)
    }));
  }

  // Method to handle video selection
  selectVideo(video: Video) {
    this.selectedVideo = video;
  }
}
