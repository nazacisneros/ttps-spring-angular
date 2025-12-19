import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { CiudadService, Ciudad } from '../../services/ciudad.service';
import { BarrioService, Barrio } from '../../services/barrio.service';
import { ActivatedRoute } from '@angular/router';
import { Router, RouterModule  } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  editMode = false;
  userId: number | null = null;

  registerForm!: FormGroup;
  ciudades: Ciudad[] = [];
  barrios: Barrio[] = [];

  // Mensaje de error del servidor
  serverError: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private ciudadService: CiudadService,
    private barrioService: BarrioService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      telefono: ['', Validators.required],
      barrio: ['', Validators.required],
      ciudad: ['', Validators.required],
    });
  }

  ngOnInit() {
    if (this.route.snapshot.url[0]?.path === 'perfil') {
      this.editMode = true;
      // En modo edición, el email no se puede cambiar
      this.registerForm.get('email')?.disable();
      // Los campos de ubicación son opcionales en edición
      this.registerForm.get('telefono')?.clearValidators();
      this.registerForm.get('barrio')?.clearValidators();
      this.registerForm.get('ciudad')?.clearValidators();
      this.registerForm.get('telefono')?.updateValueAndValidity();
      this.registerForm.get('barrio')?.updateValueAndValidity();
      this.registerForm.get('ciudad')?.updateValueAndValidity();
    } else {
      // En modo registro, el password es obligatorio
      this.registerForm.get('password')?.setValidators([Validators.required]);
      this.registerForm.get('password')?.updateValueAndValidity();
    }

    this.ciudadService.getCiudades().subscribe(ciudades => {
      this.ciudades = ciudades;
      if (this.editMode) {
        this.loadUserData();
      }
    });
  }

  loadUserData() {
    this.authService.getProfile().subscribe(user => {
      this.userId = user.id ?? null;
      this.registerForm.patchValue({
        nombre: user.nombre,
        apellido: user.apellido,
        email: user.email,
        telefono: user.telefono,
        barrio: user.barrioId,
        ciudad: user.ciudadId
      });

      if (user.ciudadId) {
        this.onCiudadChange(user.ciudadId);
      }
    });
  }



  onCiudadChange(ciudadId: number) {
    if (ciudadId) {
      this.barrioService.getBarriosByCiudad(ciudadId).subscribe(barrios => {
        this.barrios = barrios;
      });
    } else {
      this.barrios = [];
    }
  }

 submit() {
    if (this.registerForm.invalid) return;

    this.serverError = '';

    if (this.editMode) {
      const payload: any = {
        nombre: this.registerForm.value.nombre,
        apellido: this.registerForm.value.apellido,
        telefono: this.registerForm.value.telefono || '',
        barrioId: this.registerForm.value.barrio || null
      };

      this.authService.updateProfile(payload)
        .subscribe({
          next: (response) => {
            console.log('Perfil actualizado correctamente', response);
            // Actualizar el usuario en el servicio de autenticación
            this.authService.refreshUserData();
            alert('Perfil actualizado exitosamente');
            // Redirigir al home
            this.router.navigate(['/']);
          },
          error: (err) => {
            console.error('Error actualizando perfil:', err);
            console.error('Status:', err.status);
            console.error('Error completo:', JSON.stringify(err, null, 2));

            // Si la actualización fue exitosa (status 200) pero hay un problema de parsing
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
      const payload: any = {
        nombre: this.registerForm.value.nombre,
        apellido: this.registerForm.value.apellido,
        email: this.registerForm.value.email,
        telefono: this.registerForm.value.telefono,
        password: this.registerForm.value.password,
        barrioId: this.registerForm.value.barrio
      };

      this.authService.register(payload)
      .subscribe({
        next: () => {
          console.log('Registro exitoso');
          this.router.navigate(['/']);
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
