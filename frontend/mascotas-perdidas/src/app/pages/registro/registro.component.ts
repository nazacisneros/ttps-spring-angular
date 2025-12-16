import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
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
      barrio: ['', Validators.required],
      ciudad: ['', Validators.required],
      cbu: ['', Validators.required],
    });
  }

  ngOnInit() {
    if (this.route.snapshot.url[0]?.path === 'perfil') {
      this.editMode = true;
      this.loadUserData();
    }
  }

  loadUserData() {
    this.authService.getProfile().subscribe(user => {
      this.userId = user.id ?? null;
      this.registerForm.patchValue({
        nombre: user.nombre,
        apellido: user.apellido,
        email: user.email,
        telefono: user.telefono,
        barrio: user.barrio,
        ciudad: user.ciudad,
        cbu: user.cbu
      });
    });
  }

 submit() {
    if (this.registerForm.invalid) return;

    if (this.editMode) {
      const payload: any = {
        nombre: this.registerForm.value.nombre,
        apellido: this.registerForm.value.apellido,
        telefono: this.registerForm.value.telefono,
        barrio: this.registerForm.value.barrio
      };
      this.authService.updateProfile(payload, this.userId ?? undefined)
        .subscribe(() => console.log('Perfil actualizado'));
    } else {
      const payload: any = {
        nombre: this.registerForm.value.nombre,
        apellido: this.registerForm.value.apellido,
        email: this.registerForm.value.email,
        telefono: this.registerForm.value.telefono,
        password: this.registerForm.value.password
      };

      this.authService.register(payload)
      .subscribe({
        next: () => {
          console.log('Registro exitoso');
          this.router.navigate(['/']);
        },
        error: () => {
          alert('Credenciales incorrectas');
        }
      });
    }
  }

}
