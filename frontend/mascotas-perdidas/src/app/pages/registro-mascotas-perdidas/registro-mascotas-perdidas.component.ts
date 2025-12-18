import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MascotaService } from '../../services/mascota.service';
import { CiudadService, Ciudad } from '../../services/ciudad.service';
import { BarrioService, Barrio } from '../../services/barrio.service';
import { MapaMascotasComponent } from '../../components/mapa-mascotas/mapa-mascotas.component';
import { COLORES_MASCOTA, TAMANIOS_MASCOTA, ESTADOS_MASCOTA } from '../../constants/mascota.constants';

@Component({
  selector: 'app-registro-mascotas-perdidas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MapaMascotasComponent],
  templateUrl: './registro-mascotas-perdidas.component.html',
  styleUrls: ['./registro-mascotas-perdidas.component.css']
})
export class RegistroMascotasPerdidasComponent implements OnInit {

  mascotaForm: FormGroup;
  ciudades: Ciudad[] = [];
  barrios: Barrio[] = [];

  // Coordenadas
  selectedCoordinates: {lat: number, lng: number} | null = null;
  mapCenterLat: number = -34.6037; // Buenos Aires 
  mapCenterLng: number = -58.3816;
  mapZoom: number = 12;
  showMap: boolean = false;

  tamanios = TAMANIOS_MASCOTA;
  colores = COLORES_MASCOTA;
  estados = ESTADOS_MASCOTA;

  constructor(
    private fb: FormBuilder,
    private mascotaService: MascotaService,
    private ciudadService: CiudadService,
    private barrioService: BarrioService,
    private router: Router
  ) {
    this.mascotaForm = this.fb.group({
      nombre: ['', Validators.required],
      tamanio: ['', Validators.required],
      color: ['', Validators.required],
      descripcion: ['', Validators.required],
      estado: ['', Validators.required],
      ciudad: ['', Validators.required],
      barrio: ['', Validators.required],
      latitud: ['', Validators.required],
      longitud: ['', Validators.required],
    });
  }

  ngOnInit() {
    this.loadCiudades();
  }

  loadCiudades() {
    this.ciudadService.getCiudades().subscribe(ciudades => {
      this.ciudades = ciudades;
    });
  }

  onCiudadChange(ciudadId: number) {
    if (ciudadId) {
      this.barrioService.getBarriosByCiudad(ciudadId).subscribe(barrios => {
        this.barrios = barrios;
        // Espera a que esten seleccionados ciudad y barrio para mostrar el mapa
        this.checkShowMap();
      });

      // Cambia el foco del mapa dependiendo de la ciudad seleccionada
      const selectedCiudad = this.ciudades.find(c => c.id === ciudadId);
      if (selectedCiudad) {
        // por ahora usa coordenadas default, falta implementar la api de ciudades
        this.updateMapCenter(ciudadId);
      }
    } else {
      this.barrios = [];
      this.showMap = false;
    }
  }

  onBarrioChange() {
    this.checkShowMap();
  }

  private checkShowMap() {
    const ciudadSeleccionada = this.mascotaForm.get('ciudad')?.value;
    const barrioSeleccionado = this.mascotaForm.get('barrio')?.value;
    this.showMap = !!(ciudadSeleccionada && barrioSeleccionado);
  }

  onCoordinatesSelected(coordinates: {lat: number, lng: number}) {
    this.selectedCoordinates = coordinates;
    this.mascotaForm.patchValue({
      latitud: coordinates.lat,
      longitud: coordinates.lng
    });
  }

  private updateMapCenter(ciudadId: number) {
    // coordenadas de ejemplo para ciudades
    const cityCoordinates: {[key: number]: {lat: number, lng: number}} = {
      1: { lat: -34.6037, lng: -58.3816 }, // Buenos Aires
      2: { lat: -31.4167, lng: -64.1833 }  // Córdoba
    };

    const coords = cityCoordinates[ciudadId];
    if (coords) {
      this.mapCenterLat = coords.lat;
      this.mapCenterLng = coords.lng;
      this.mapZoom = 13; 
    }
  }

  submit() {
    if (this.mascotaForm.invalid) return;

    this.mascotaService.crearPublicacionMascota(this.mascotaForm.value)
      .subscribe({
        next: () => {
          alert('Mascota registrada correctamente con ubicación');
          this.router.navigate(['/']);
        },
        error: err => {
          console.error('Error al registrar la mascota:', err);
          if (err.error && typeof err.error === 'string') {
            alert('Error: ' + err.error);
          } else {
            alert('Error al registrar la mascota. Verifica que todos los campos estén completos.');
          }
        }
      });
  }

}
