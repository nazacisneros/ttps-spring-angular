import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CiudadService, Ciudad } from '../../services/ciudad.service';
import { BarrioService, Barrio } from '../../services/barrio.service';
import { COLORES_MASCOTA, TAMANIOS_MASCOTA, ESTADOS_MASCOTA } from '../../constants/mascota.constants';

@Component({
  selector: 'app-filtro-mascotas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './filtro-mascotas.component.html',
  styleUrl: './filtro-mascotas.component.css'
})
export class FiltroMascotasComponent implements OnInit {

  @Output() filtrosChanged = new EventEmitter<any>();

  filtroForm: FormGroup;
  ciudades: Ciudad[] = [];
  barrios: Barrio[] = [];

  tamanios = TAMANIOS_MASCOTA;
  colores = COLORES_MASCOTA;
  estados = ESTADOS_MASCOTA;

  constructor(
    private fb: FormBuilder,
    private ciudadService: CiudadService,
    private barrioService: BarrioService
  ) {
    this.filtroForm = this.fb.group({
      ciudadId: [''],
      barrioId: [''],
      tamanio: [''],
      color: [''],
      estado: ['']
    });

    this.filtroForm.valueChanges.subscribe(value => {
      this.filtrosChanged.emit(value);
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
        // Resetear seleccion de barrio cuando cambia la ciudad
        this.filtroForm.patchValue({ barrioId: '' });
      });
    } else {
      this.barrios = [];
      this.filtroForm.patchValue({ barrioId: '' });
    }
  }

  limpiarFiltros() {
    this.filtroForm.patchValue({
      ciudadId: '',
      barrioId: '',
      tamanio: '',
      color: '',
      estado: ''
    });
    this.barrios = [];
  }
}
