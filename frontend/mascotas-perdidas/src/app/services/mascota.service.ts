import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class MascotaService {

  private api = 'http://localhost:8080/api/Mascotas';
  private publicacionesApi = 'http://localhost:8080/api/publicaciones';

  constructor(private http: HttpClient) {}

  crearMascota(data: any) {
    return this.http.post(this.api, data);
  }

  crearPublicacionMascota(data: any) {
    const now = new Date();
    const publicacionData = {
      mascota: {
        nombre: data.nombre,
        tamanio: data.tamanio,
        color: data.color,
        descripcion: data.descripcion,
        estado: data.estado
      },
      barrioId: data.barrio,
      coordenadas: {
        latitud: data.latitud,
        longitud: data.longitud
      },
      fecha: now.toISOString().split('T')[0],
      hora: now.toISOString()
    };

    return this.http.post(`${this.publicacionesApi}/con-mascota`, publicacionData);
  }

  obtenerPublicaciones() {
    return this.http.get(this.publicacionesApi);
  }

  obtenerPublicacionesFiltradas(filtros: any) {
    const cleanFiltros = Object.keys(filtros).reduce((acc: any, key) => {
      if (filtros[key] && filtros[key] !== '') {
        acc[key] = filtros[key];
      }
      return acc;
    }, {});

    let params = new URLSearchParams();
    if (cleanFiltros.ciudadId) params.append('ciudadId', cleanFiltros.ciudadId);
    if (cleanFiltros.barrioId) params.append('barrioId', cleanFiltros.barrioId);
    if (cleanFiltros.tamanio) params.append('tamanio', cleanFiltros.tamanio);
    if (cleanFiltros.color) params.append('color', cleanFiltros.color);
    if (cleanFiltros.estado) params.append('estado', cleanFiltros.estado);

    const url = `${this.publicacionesApi}/filtradas?${params.toString()}`;
    return this.http.get(url);
  }

  eliminarMascota(mascotaId: number, usuarioId: number) {
    return this.http.delete(`${this.api}/${mascotaId}/usuario/${usuarioId}`);
  }

  eliminarPublicacion(publicacionId: number, usuarioId: number) {
    return this.http.delete(`${this.publicacionesApi}/${publicacionId}/usuario/${usuarioId}`);
  }

  obtenerPublicacionPorId(publicacionId: number) {
    return this.http.get(`${this.publicacionesApi}/${publicacionId}/detalle`);
  }

  actualizarPublicacion(publicacionId: number, data: any) {
    return this.http.put(`${this.publicacionesApi}/${publicacionId}/actualizar`, data);
  }
}
