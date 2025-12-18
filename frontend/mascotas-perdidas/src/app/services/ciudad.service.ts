import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Ciudad {
  id: number;
  nombre: string;
}

@Injectable({
  providedIn: 'root'
})
export class CiudadService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getCiudades(): Observable<Ciudad[]> {
    return this.http.get<Ciudad[]>(`${this.baseUrl}/ciudades`);
  }
}
