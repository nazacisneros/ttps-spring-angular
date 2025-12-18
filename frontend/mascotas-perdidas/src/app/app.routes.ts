import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { RegistroMascotasPerdidasComponent } from './pages/registro-mascotas-perdidas/registro-mascotas-perdidas.component';
import { MascotasPerdidasComponent } from './pages/mascotas-perdidas/mascotas-perdidas.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { AuthGuard } from './services/auth.guard';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'mascotas-perdidas', component: MascotasPerdidasComponent },
    { path: 'perfil', component: RegistroComponent, canActivate: [AuthGuard] },
    { path: 'mascotas/nueva', component: RegistroMascotasPerdidasComponent, canActivate: [AuthGuard] },
    { path: '**', component: PageNotFoundComponent },
  ];
