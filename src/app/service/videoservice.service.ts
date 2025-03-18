import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VideoserviceService {

  private baseUrl = 'http://localhost:8080/api/video'; 

  constructor(private http: HttpClient) {}

  getVideoChunk(videoId: string, start: number, end: number): Observable<HttpResponse<Blob>> {
    const headers = new HttpHeaders({
      'Range': `bytes=${start}-${end}`
    });

    return this.http.get(`${this.baseUrl}stream/range/${videoId}`, { 
      headers, 
      responseType: 'blob',
      observe: 'response' 
    });
  }

  uploadVideo(file: File, title: string, description: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', title);
    formData.append('description', description);

    return this.http.post(`${this.baseUrl}/upload`, formData);
  }

}
