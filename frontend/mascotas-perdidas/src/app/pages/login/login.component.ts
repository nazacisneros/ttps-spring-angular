import { Component } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule  } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm!: FormGroup;
  loginError: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      contrasenia: ['', Validators.required]
    });
  }

  submit() {
    if (this.loginForm.invalid) return;

    this.loginError = '';

    this.authService.login(this.loginForm.value)
      .subscribe({
        next: () => {
          console.log('Login OK');
          this.router.navigate(['/']);
        },
        error: (error: any) => {
          console.error('Error en login:', error);
          if (error.error && typeof error.error === 'string') {
            this.loginError = error.error;
          } else if (error.message) {
            this.loginError = error.message;
          } else {
            this.loginError = 'Email o contraseña incorrectos';
          }
        }
      });
  }

}
