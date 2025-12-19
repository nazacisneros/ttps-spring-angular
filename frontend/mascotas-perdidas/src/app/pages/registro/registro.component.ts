import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { MapaMascotasComponent } from '../../components/mapa-mascotas/mapa-mascotas.component';
import { ActivatedRoute } from '@angular/router';
import { Router, RouterModule  } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, MapaMascotasComponent],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  editMode = false;
  userId: number | null = null;

  registerForm!: FormGroup;

  // Coordenadas del mapa
  selectedCoordinates: {lat: number, lng: number} | null = null;
  mapCenterLat: number = -34.6037; // Buenos Aires por defecto
  mapCenterLng: number = -58.3816;
  mapZoom: number = 12;

  // Mensaje de error del servidor
  serverError: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      telefono: ['', Validators.required],
      latitud: ['', Validators.required],
      longitud: ['', Validators.required],
    });
  }

  ngOnInit() {
    if (this.route.snapshot.url[0]?.path === 'perfil') {
      this.editMode = true;
      this.registerForm.get('email')?.disable();
      this.registerForm.get('telefono')?.clearValidators();
      this.registerForm.get('latitud')?.clearValidators();
      this.registerForm.get('longitud')?.clearValidators();
      this.registerForm.get('telefono')?.updateValueAndValidity();
      this.registerForm.get('latitud')?.updateValueAndValidity();
      this.registerForm.get('longitud')?.updateValueAndValidity();


      this.loadUserData();
    } else {

      this.registerForm.get('password')?.setValidators([Validators.required]);
      this.registerForm.get('password')?.updateValueAndValidity();
    }
  }

  loadUserData() {
    this.authService.getProfile().subscribe(user => {
      this.userId = user.id ?? null;
      this.registerForm.patchValue({
        nombre: user.nombre,
        apellido: user.apellido,
        email: user.email,
        telefono: user.telefono
      });

    });
  }

  onCoordinatesSelected(coordinates: {lat: number, lng: number}) {
    this.selectedCoordinates = coordinates;
    this.registerForm.patchValue({
      latitud: coordinates.lat,
      longitud: coordinates.lng
    });
    console.log('Coordenadas seleccionadas:', coordinates);
  }

  cancelar() {
    if (confirm('¿Estás seguro de que deseas cancelar? Se perderán los cambios no guardados.')) {
      this.router.navigate([this.editMode ? '/' : '/login']);
    }
  }

 submit() {
    if (this.registerForm.invalid) return;

    this.serverError = '';

    if (this.editMode) {
      const payload: any = {
        nombre: this.registerForm.value.nombre,
        apellido: this.registerForm.value.apellido,
        telefono: this.registerForm.value.telefono || ''
      };

      if (this.selectedCoordinates) {
        payload.latitud = this.selectedCoordinates.lat;
        payload.longitud = this.selectedCoordinates.lng;
      }

      this.authService.updateProfile(payload)
        .subscribe({
          next: (response) => {
            console.log('Perfil actualizado correctamente', response);
            this.authService.refreshUserData();
            alert('Perfil actualizado exitosamente');
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Error actualizando perfil:', err);
            console.error('Status:', err.status);
            console.error('Error completo:', JSON.stringify(err, null, 2));

            if (err.status === 200 || err.status === 0) {
              console.log('Actualización exitosa a pesar del error de parsing');
              this.authService.refreshUserData();
              alert('Perfil actualizado exitosamente');
              this.router.navigate(['/']);
              return;
            }

            if (err.error && typeof err.error === 'string') {
              this.serverError = err.error;
            } else if (err.error && err.error.message) {
              this.serverError = err.error.message;
            } else {
              this.serverError = 'No se pudo actualizar el perfil. Intenta nuevamente.';
            }
          }
        });
    } else {
      // Modo registro
      const payload: any = {
        nombre: this.registerForm.value.nombre,
        apellido: this.registerForm.value.apellido,
        email: this.registerForm.value.email,
        telefono: this.registerForm.value.telefono,
        password: this.registerForm.value.password,
        latitud: this.registerForm.value.latitud,
        longitud: this.registerForm.value.longitud
      };

      this.authService.register(payload)
      .subscribe({
        next: (response: any) => {
          console.log('Registro exitoso:', response);
          alert(response.mensaje || 'Usuario registrado exitosamente. Por favor inicia sesión.');
          this.router.navigate(['/login']);
        },
        error: (error: any) => {
          console.error('Error en registro:', error);

          if (error.error && typeof error.error === 'object' && error.error.message) {
            this.serverError = error.error.message;
          } else if (error.error && typeof error.error === 'string') {
            this.serverError = error.error;
          } else if (error.status === 403 || error.status === 400) {
            this.serverError = 'Error de validación: Verifica tus datos e intenta nuevamente.';
          } else {
            this.serverError = 'Error al registrar usuario. Inténtalo de nuevo.';
          }
        }
      });
    }
  }

}
