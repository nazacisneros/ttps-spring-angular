import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MascotaService } from '../../services/mascota.service';
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

  constructor(private mascotaService: MascotaService) {}

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
}
