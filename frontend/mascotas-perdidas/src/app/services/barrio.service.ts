import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Barrio {
  id: number;
  nombre: string;
}

@Injectable({
  providedIn: 'root'
})
export class BarrioService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getBarrios(): Observable<Barrio[]> {
    return this.http.get<Barrio[]>(`${this.baseUrl}/barrios`);
  }

  getBarriosByCiudad(ciudadId: number): Observable<Barrio[]> {
    return this.http.get<Barrio[]>(`${this.baseUrl}/barrios/ciudad/${ciudadId}`);
  }
}
