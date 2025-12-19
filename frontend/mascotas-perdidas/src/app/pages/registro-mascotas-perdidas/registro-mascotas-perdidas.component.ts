import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { MascotaService } from '../../services/mascota.service';
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

  editMode: boolean = false;
  publicacionId: number | null = null;
  mascotaId: number | null = null;

  selectedCoordinates: {lat: number, lng: number} | null = null;
  mapCenterLat: number = -34.6037; // Buenos Aires por defecto
  mapCenterLng: number = -58.3816;
  mapZoom: number = 12;

  tamanios = TAMANIOS_MASCOTA;
  colores = COLORES_MASCOTA;
  estados = ESTADOS_MASCOTA;

  constructor(
    private fb: FormBuilder,
    private mascotaService: MascotaService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.mascotaForm = this.fb.group({
      nombre: ['', Validators.required],
      tamanio: ['', Validators.required],
      color: ['', Validators.required],
      descripcion: ['', Validators.required],
      estado: ['', Validators.required],
      latitud: ['', Validators.required],
      longitud: ['', Validators.required],
    });
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.editMode = true;
        this.publicacionId = +params['id'];
        this.loadPublicacionData(this.publicacionId);
      }
    });
  }

  loadPublicacionData(publicacionId: number) {

    this.mascotaService.obtenerPublicacionPorId(publicacionId).subscribe({
      next: (data: any) => {
        if (!data) {
          alert('Error: No se recibieron datos de la publicación');
          this.router.navigate(['/mascotas-perdidas']);
          return;
        }

        this.mascotaId = data.mascotaId;
        console.log('Mascota ID guardado:', this.mascotaId);

        const formData = {
          nombre: data.nombreMascota,
          tamanio: data.tamanioMascota,
          color: data.colorMascota,
          descripcion: data.descripcionMascota,
          estado: data.estadoMascota,
          latitud: data.latitud,
          longitud: data.longitud
        };

        this.mascotaForm.patchValue(formData);

        if (data.latitud && data.longitud) {
          this.selectedCoordinates = {
            lat: parseFloat(data.latitud),
            lng: parseFloat(data.longitud)
          };

          this.mapCenterLat = parseFloat(data.latitud);
          this.mapCenterLng = parseFloat(data.longitud);
          this.mapZoom = 15;
        } else {
          console.warn('No hay coordenadas en los datos');
        }
      },
      error: (error) => {
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

  onCoordinatesSelected(coordinates: {lat: number, lng: number}) {
    this.selectedCoordinates = coordinates;
    this.mascotaForm.patchValue({
      latitud: coordinates.lat,
      longitud: coordinates.lng
    });
    console.log('Coordenadas seleccionadas:', coordinates);
  }

  cancelar() {
    if (confirm('¿Estás seguro de que deseas cancelar? Se perderán los cambios no guardados.')) {
      this.router.navigate([this.editMode ? '/mascotas-perdidas' : '/']);
    }
  }

  submit() {
    if (this.mascotaForm.invalid) return;

    if (this.editMode && this.publicacionId) {
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
