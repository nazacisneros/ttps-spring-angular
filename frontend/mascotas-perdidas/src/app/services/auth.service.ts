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
  private baseUrl = 'http://localhost:8080/api';
  private api = `${this.baseUrl}/auth`;
  private userSubject = new BehaviorSubject<UsuarioAuth | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {
    const user = localStorage.getItem('user');
    if (user) {
      this.userSubject.next(JSON.parse(user));
    }
  }

  login(data: any) {
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
    return this.http.post(`${this.api}/registro`, payload);
  }

  getProfile() {
    return this.http.get<UsuarioPerfil>(`${this.api}/me`);
  }

  updateProfile(data: any, id?: number) {
    return this.http.put(`${this.api}/me`, data);
  }

  refreshUserData() {
    this.getProfile().subscribe({
      next: (profile) => {
        const updatedUser: UsuarioAuth = {
          id: profile.id,
          nombre: profile.nombre,
          apellido: profile.apellido,
          email: profile.email,
          esAdmin: profile.esAdmin
        };
        localStorage.setItem('user', JSON.stringify(updatedUser));
        this.userSubject.next(updatedUser);
      },
      error: (err) => {
        console.error('Error al refrescar datos del usuario:', err);
      }
    });
  }
}
