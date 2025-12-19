import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MascotaService } from '../../services/mascota.service';
import { AuthService } from '../../services/auth.service';
import { FiltroMascotasComponent } from '../../components/filtro-mascotas/filtro-mascotas.component';
import { MapaMascotasComponent } from '../../components/mapa-mascotas/mapa-mascotas.component';

interface Publicacion {
  id: number;
  fecha: any;
  hora: any;
  estado: string;
  nombreMascota: string;
  tamanioMascota: string;
  colorMascota: string;
  descripcionMascota: string;
  estadoMascota: string;
  latitud: string;
  longitud: string;
  mascotaId: number;
  usuarioId: number;
}

@Component({
  selector: 'app-mascotas-perdidas',
  standalone: true,
  imports: [CommonModule, FiltroMascotasComponent, MapaMascotasComponent],
  templateUrl: './mascotas-perdidas.component.html',
  styleUrl: './mascotas-perdidas.component.css'
})
export class MascotasPerdidasComponent implements OnInit {

  publicaciones: Publicacion[] = [];
  filteredPublicaciones: Publicacion[] = [];
  selectedPublicacion?: Publicacion;
  viewMode: 'list' | 'map' = 'list';
  isLoading = false;

  // Propiedades del Mapa
  mapCenterLat: number = -34.6037;
  mapCenterLng: number = -58.3816;
  mapZoom: number = 12;

  constructor(
    private mascotaService: MascotaService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadPublicaciones();
  }

  loadPublicaciones() {
    this.isLoading = true;
    this.mascotaService.obtenerPublicacionesFiltradas({}).subscribe({
      next: (data: any) => {
        this.publicaciones = data;
        this.filteredPublicaciones = [...this.publicaciones];
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error cargando publicaciones:', error);
        this.isLoading = false;
      }
    });
  }

  onFiltrosChanged(filtros: any) {
    this.isLoading = true;

    const cleanFiltros = Object.keys(filtros).reduce((acc: any, key) => {
      if (filtros[key] && filtros[key] !== '') {
        acc[key] = filtros[key];
      }
      return acc;
    }, {});

    if (Object.keys(cleanFiltros).length === 0) {
      this.filteredPublicaciones = [...this.publicaciones];
      this.isLoading = false;
    } else {
      this.mascotaService.obtenerPublicacionesFiltradas(cleanFiltros).subscribe({
        next: (data: any) => {
          this.filteredPublicaciones = data;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error filtrando publicaciones:', error);
          this.isLoading = false;
        }
      });
    }
  }

  toggleViewMode(mode: 'list' | 'map') {
    this.viewMode = mode;
  }

  focusOnPublication(publicacion: Publicacion) {
    this.viewMode = 'map';
    this.selectedPublicacion = publicacion;
  }

  formatDate(dateArray: number[]): string {
    if (!dateArray || dateArray.length < 3) return '';
    const [year, month, day] = dateArray;
    return `${day.toString().padStart(2, '0')}/${month.toString().padStart(2, '0')}/${year}`;
  }

  formatTime(timeArray: number[]): string {
    if (!timeArray || timeArray.length < 3) return '';
    const [hour, minute, second] = timeArray;
    return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
  }

  getMarkerCoordinates(publicacion: Publicacion): { lat: number, lng: number } {
    return {
      lat: parseFloat(publicacion.latitud),
      lng: parseFloat(publicacion.longitud)
    };
  }

  isOwner(publicacion: Publicacion): boolean {
    const user = this.authService.getUser();
    return user !== null && user.id === publicacion.usuarioId;
  }

  editarMascota(publicacion: Publicacion): void {
    this.router.navigate(['/mascotas/editar', publicacion.id]);
  }

  eliminarMascota(publicacion: Publicacion): void {
    if (!confirm(`¿Estás seguro de que deseas eliminar la publicación de ${publicacion.nombreMascota}?`)) {
      return;
    }

    const user = this.authService.getUser();
    if (!user) {
      alert('Debes estar autenticado para eliminar una mascota');
      return;
    }

    this.mascotaService.eliminarPublicacion(publicacion.id, user.id).subscribe({
      next: () => {
        alert('Publicación eliminada exitosamente');
        this.loadPublicaciones();
      },
      error: (error) => {
        console.error('Error eliminando publicación:', error);

        if (error.status === 403) {
          alert('No tienes permisos para eliminar esta publicación. Verifica que seas el publicador.');
        } else if (error.status === 401) {
          alert('Tu sesión ha expirado. Por favor, inicia sesión nuevamente.');
        } else if (error.status === 404) {
          alert('La publicación no fue encontrada.');
        } else {
          alert(`Error al eliminar la publicación: ${error.message || 'Error desconocido'}`);
        }
      }
    });
  }
}
