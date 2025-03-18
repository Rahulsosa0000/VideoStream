import { Component } from '@angular/core';
import { VideoStreamComponent } from "./components/video-stream/video-stream.component";
import { VideouploadComponent } from "./components/videoupload/videoupload.component";

@Component({
  selector: 'app-root',
  imports: [VideoStreamComponent, VideouploadComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'videoStreaming';
}
