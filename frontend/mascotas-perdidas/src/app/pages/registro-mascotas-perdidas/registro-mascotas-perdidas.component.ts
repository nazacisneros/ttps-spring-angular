import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
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

  // Modo edición
  editMode: boolean = false;
  publicacionId: number | null = null;
  mascotaId: number | null = null;

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
    private router: Router,
    private route: ActivatedRoute
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

    // Detectar si estamos en modo edición
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.editMode = true;
        this.publicacionId = +params['id'];
        this.loadPublicacionData(this.publicacionId);
      }
    });
  }

  loadCiudades() {
    this.ciudadService.getCiudades().subscribe(ciudades => {
      this.ciudades = ciudades;
    });
  }

  loadPublicacionData(publicacionId: number) {
    console.log('Cargando publicación con ID:', publicacionId);

    this.mascotaService.obtenerPublicacionPorId(publicacionId).subscribe({
      next: (data: any) => {
        console.log('Datos de publicación recibidos del backend:', data);
        console.log('Tipo de data:', typeof data);
        console.log('Keys de data:', data ? Object.keys(data) : 'data es null/undefined');

        // Validar que tengamos los datos necesarios
        if (!data) {
          console.error('No se recibieron datos de la publicación');
          alert('Error: No se recibieron datos de la publicación');
          this.router.navigate(['/mascotas-perdidas']);
          return;
        }

        // Guardar IDs
        this.mascotaId = data.mascotaId;
        console.log('Mascota ID guardado:', this.mascotaId);

        // Cargar barrios de la ciudad
        if (data.ciudadId) {
          console.log('Cargando barrios de ciudad ID:', data.ciudadId);
          this.barrioService.getBarriosByCiudad(data.ciudadId).subscribe({
            next: (barrios) => {
              console.log('Barrios cargados:', barrios);
              this.barrios = barrios;
              this.showMap = true;
            },
            error: (err) => {
              console.error('Error cargando barrios:', err);
            }
          });
        } else {
          console.warn('No hay ciudadId en los datos');
        }

        // Rellenar el formulario
        const formData = {
          nombre: data.nombreMascota,
          tamanio: data.tamanioMascota,
          color: data.colorMascota,
          descripcion: data.descripcionMascota,
          estado: data.estadoMascota,
          ciudad: data.ciudadId,
          barrio: data.barrioId,
          latitud: data.latitud,
          longitud: data.longitud
        };

        console.log('Datos para rellenar formulario:', formData);
        this.mascotaForm.patchValue(formData);

        // Configurar coordenadas del mapa
        if (data.latitud && data.longitud) {
          this.selectedCoordinates = {
            lat: parseFloat(data.latitud),
            lng: parseFloat(data.longitud)
          };

          this.mapCenterLat = parseFloat(data.latitud);
          this.mapCenterLng = parseFloat(data.longitud);
          this.mapZoom = 15;
          console.log('Coordenadas configuradas:', this.selectedCoordinates);
        } else {
          console.warn('No hay coordenadas en los datos');
        }
      },
      error: (error) => {
        console.error('Error completo cargando datos de publicación:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error body:', error.error);

        let errorMsg = 'Error al cargar los datos de la mascota';
        if (error.status === 404) {
          errorMsg = 'Publicación no encontrada';
        } else if (error.status === 403) {
          errorMsg = 'No tienes permisos para ver esta publicación';
        } else if (error.error && typeof error.error === 'string') {
          errorMsg = error.error;
        }

        alert(errorMsg);
        this.router.navigate(['/mascotas-perdidas']);
      }
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

    if (this.editMode && this.publicacionId) {
      // Modo edición - actualizar publicación existente
      const updateData = {
        mascota: {
          nombre: this.mascotaForm.value.nombre,
          tamanio: this.mascotaForm.value.tamanio,
          color: this.mascotaForm.value.color,
          descripcion: this.mascotaForm.value.descripcion,
          estado: this.mascotaForm.value.estado
        },
        barrioId: this.mascotaForm.value.barrio,
        coordenadas: {
          latitud: this.mascotaForm.value.latitud,
          longitud: this.mascotaForm.value.longitud
        }
      };

      this.mascotaService.actualizarPublicacion(this.publicacionId, updateData)
        .subscribe({
          next: () => {
            alert('Publicación actualizada correctamente');
            this.router.navigate(['/mascotas-perdidas']);
          },
          error: err => {
            console.error('Error al actualizar la publicación:', err);
            if (err.error && typeof err.error === 'string') {
              alert('Error: ' + err.error);
            } else {
              alert('Error al actualizar la publicación. Verifica que todos los campos estén completos.');
            }
          }
        });
    } else {
      // Modo creación - crear nueva publicación
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

}
