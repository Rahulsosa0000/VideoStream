import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VideoserviceService } from '../../service/videoservice.service';

@Component({
  selector: 'app-videoupload',
  imports: [CommonModule,FormsModule],
  templateUrl: './videoupload.component.html',
  styleUrl: './videoupload.component.css'
})
export class VideouploadComponent {

  selectedFile: File | null = null;
  title: string = '';
  description: string = '';
  uploadMessage: string = '';

  constructor(private videoService: VideoserviceService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadVideo() {
    if (!this.selectedFile || !this.title.trim()) {
      this.uploadMessage = "Please select a file and enter a title.";
      return;
    }

    this.videoService.uploadVideo(this.selectedFile, this.title, this.description).subscribe({
      next: (response) => {
        this.uploadMessage = "Video uploaded successfully!";
        console.log("Uploaded video:", response);
      },
      error: (err) => {
        this.uploadMessage = "Upload failed!";
        console.error("Upload error:", err);
      }
    });
  }
  
}
