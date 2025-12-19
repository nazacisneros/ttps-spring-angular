import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

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
  selector: 'app-mapa-mascotas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mapa-mascotas.component.html',
  styleUrl: './mapa-mascotas.component.css'
})

export class MapaMascotasComponent implements OnInit, OnDestroy, OnChanges {

  @Input() mode: 'view' | 'select' = 'view'; // 'view' para listado, 'select' for registration
  @Input() centerLat: number = -34.6037; // Buenos Aires
  @Input() centerLng: number = -58.3816;
  @Input() zoom: number = 12;
  @Input() publicaciones: Publicacion[] = [];
  @Input() selectedPublicacion?: Publicacion;

  @Output() coordinatesSelected = new EventEmitter<{lat: number, lng: number}>();

  public mapId: string;
  private map: any;
  private markers: any[] = [];
  private selectedMarker: any;

  constructor() {
    this.mapId = 'map-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
  }

  ngOnInit() {
    setTimeout(() => {
      this.initializeMap();
      if (this.selectedPublicacion) {
        setTimeout(() => this.focusOnPublicacion(this.selectedPublicacion!), 100);
      }
    }, 300);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.map) {
      if (changes['centerLat'] || changes['centerLng'] || changes['zoom']) {
        this.map.setView([this.centerLat, this.centerLng], this.zoom);
      }

      if (changes['publicaciones']) {
        this.updatePublicacionMarkers();
      }

      if (changes['selectedPublicacion'] && this.selectedPublicacion) {
        this.focusOnPublicacion(this.selectedPublicacion);
      }
    }
  }

  ngOnDestroy() {
    if (this.map) {
      try {
        this.map.remove();
      } catch (e) {
      }
      this.map = null;
    }
  }

  private initializeMap() {
    try {
      const mapElement = document.getElementById(this.mapId);
      if (!mapElement) {
        console.warn('Map container not found for ID:', this.mapId);
        return;
      }

      mapElement.innerHTML = '';

      this.map = L.map(this.mapId, {
        center: [this.centerLat, this.centerLng],
        zoom: this.zoom,
        zoomControl: true,
        scrollWheelZoom: true,
        doubleClickZoom: true,
        dragging: true,
        attributionControl: true
      });

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19,
        minZoom: 3
      }).addTo(this.map);

      if (this.mode === 'select') {

        this.map.on('click', (e: any) => {
          const { lat, lng } = e.latlng;
          this.setSelectedMarker(lat, lng);
          this.coordinatesSelected.emit({ lat, lng });
        });
      } else {

        this.updatePublicacionMarkers();
      }

      setTimeout(() => {
        if (this.map) {
          this.map.invalidateSize();
        }
      }, 200);

    } catch (error) {
      console.error('Error initializing map:', error);
    }
  }

  private setSelectedMarker(lat: number, lng: number) {
    if (this.selectedMarker && this.map) {
      this.map.removeLayer(this.selectedMarker);
    }

    if (this.map) {
      this.selectedMarker = L.marker([lat, lng]).addTo(this.map);
      this.map.setView([lat, lng], this.map.getZoom());
    }
  }

  private updatePublicacionMarkers() {
    if (!this.map || this.mode !== 'view') return;

    this.markers.forEach(marker => {
      if (this.map) {
        this.map.removeLayer(marker);
      }
    });
    this.markers = [];

    this.publicaciones.forEach(publicacion => {
      const lat = parseFloat(publicacion.latitud);
      const lng = parseFloat(publicacion.longitud);

      if (!isNaN(lat) && !isNaN(lng)) {
        const iconHtml = publicacion.estadoMascota.toLowerCase() === 'perdida'
          ? '<div style="background-color: #dc3545; width: 20px; height: 20px; border-radius: 50%; border: 2px solid white; box-shadow: 0 2px 5px rgba(0,0,0,0.3);"></div>'
          : '<div style="background-color: #28a745; width: 20px; height: 20px; border-radius: 50%; border: 2px solid white; box-shadow: 0 2px 5px rgba(0,0,0,0.3);"></div>';

        const customIcon = L.divIcon({
          className: 'publication-marker',
          html: iconHtml,
          iconSize: [20, 20],
          iconAnchor: [10, 10]
        });

        const marker = L.marker([lat, lng], { icon: customIcon }).addTo(this.map);

        const popupContent = `
          <div style="font-family: Arial, sans-serif; max-width: 200px;">
            <h4 style="margin: 0 0 8px 0; color: #333;">${publicacion.nombreMascota}</h4>
            <p style="margin: 4px 0; font-size: 14px;"><strong>Tamaño:</strong> ${publicacion.tamanioMascota}</p>
            <p style="margin: 4px 0; font-size: 14px;"><strong>Color:</strong> ${publicacion.colorMascota}</p>
            <p style="margin: 4px 0; font-size: 14px;"><strong>Estado:</strong> ${publicacion.estadoMascota}</p>
            <p style="margin: 4px 0; font-size: 12px; color: #666;">${publicacion.descripcionMascota}</p>
          </div>
        `;
        marker.bindPopup(popupContent);

        this.markers.push(marker);
      }
    });
  }

  private focusOnPublicacion(publicacion: Publicacion) {
    if (!this.map) return;

    const lat = parseFloat(publicacion.latitud);
    const lng = parseFloat(publicacion.longitud);

    if (!isNaN(lat) && !isNaN(lng)) {
      this.map.setView([lat, lng], 15);
    }
  }

  setCoordinates(lat: number, lng: number) {
    if (this.map && this.mode === 'select') {
      this.setSelectedMarker(lat, lng);
      this.coordinatesSelected.emit({ lat, lng });
    }
  }

  focusOnPublication(publicacion: Publicacion) {
    this.focusOnPublicacion(publicacion);
  }
}
