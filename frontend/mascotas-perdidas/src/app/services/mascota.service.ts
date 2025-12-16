import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class MascotaService {

  private api = 'http://localhost:8080/api/Mascotas';

  constructor(private http: HttpClient) {}

  crearMascota(data: any) {
    return this.http.post(this.api, data);
  }
}
