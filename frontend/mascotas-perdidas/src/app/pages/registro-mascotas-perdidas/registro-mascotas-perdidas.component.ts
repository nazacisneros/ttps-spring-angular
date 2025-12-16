import { Component } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MascotaService } from '../../services/mascota.service';

@Component({
  selector: 'app-registro-mascotas-perdidas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './registro-mascotas-perdidas.component.html',
  styleUrls: ['./registro-mascotas-perdidas.component.css']
})
export class RegistroMascotasPerdidasComponent {

  mascotaForm: FormGroup;

  tamanios = ['Chico', 'Mediano', 'Grande'];
  estados = ['Perdida', 'Encontrada', 'En adopción'];

  constructor(
    private fb: FormBuilder,
    private mascotaService: MascotaService,
    private router: Router
  ) {
    this.mascotaForm = this.fb.group({
      nombre: ['', Validators.required],
      tamanio: ['', Validators.required],
      color: ['', Validators.required],
      descripcion: ['', Validators.required],
      estado: ['', Validators.required],
    });
  }

  submit() {
    if (this.mascotaForm.invalid) return;

    this.mascotaService.crearMascota(this.mascotaForm.value)
      .subscribe({
        next: () => {
          alert('Mascota registrada correctamente');
          this.router.navigate(['/']);
        },
        error: err => {
          console.error(err);
          alert('Error al registrar la mascota');
        }
      });
  }

}
