import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { RegistroMascotasPerdidasComponent } from './pages/registro-mascotas-perdidas/registro-mascotas-perdidas.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'perfil', component: RegistroComponent },
    { path: 'mascotas/nueva', component: RegistroMascotasPerdidasComponent },   
    { path: '**', component: PageNotFoundComponent }, 
  ];
