import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private api = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(data: any) {
    // El backend espera `contrasenia` en lugar de `password`.
    const payload = { ...data } as any;
    if (payload.password) {
      payload.contrasenia = payload.password;
      delete payload.password;
    }
    return this.http.post(`${this.api}/registro`, payload);
  }

  getProfile() {
    return this.http.get<any>(`${this.api}/me`);
  }

  // Si se pasa `id` usará el endpoint de Usuario (PUT /api/usuarios/{id}/perfil),
  // si no, intenta el endpoint /api/auth/me (cuando exista sesión autenticada).
  updateProfile(data: any, id?: number) {
    if (id) {
      return this.http.put(`/api/usuarios/${id}/perfil`, data);
    }
    return this.http.put(`${this.api}/me`, data);
  }
}

