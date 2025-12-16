import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, tap } from 'rxjs';
import { UsuarioPerfil } from '../models/usuario-perfil.model';

export interface UsuarioAuth {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  esAdmin: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'http://localhost:8080/api/auth';
  private userSubject = new BehaviorSubject<UsuarioAuth | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {
    const user = localStorage.getItem('user');
    if (user) {
      this.userSubject.next(JSON.parse(user));
    }
  }

  login(data: any) {
    // CORREGIDO: usar paréntesis () en lugar de backticks
    return this.http.post<any>(`${this.api}/login`, data).pipe(
      tap(res => {
        localStorage.setItem('jwt', res.token);
        localStorage.setItem('user', JSON.stringify({
          id: res.id,
          nombre: res.nombre,
          apellido: res.apellido,
          email: res.email,
          esAdmin: res.esAdmin
        }));
        this.userSubject.next({
          id: res.id,
          nombre: res.nombre,
          apellido: res.apellido,
          email: res.email,
          esAdmin: res.esAdmin
        });
      })
    );
  }

  logout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user');
    this.userSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt');
  }

  getUser(): UsuarioAuth | null {
    return this.userSubject.value;
  }

  register(data: any) {
    const payload = { ...data } as any;
    if (payload.password) {
      payload.contrasenia = payload.password;
      delete payload.password;
    }
    // CORREGIDO: usar paréntesis
    return this.http.post(`${this.api}/registro`, payload);
  }

  getProfile() {
    // CORREGIDO: usar paréntesis
    return this.http.get<UsuarioPerfil>(`${this.api}/me`);
  }

  updateProfile(data: any, id?: number) {
    if (id) {
      // CORREGIDO: usar paréntesis
      return this.http.put(`/api/usuarios/${id}/perfil`, data);
    }
    // CORREGIDO: usar paréntesis
    return this.http.put(`${this.api}/me`, data);
  }
}