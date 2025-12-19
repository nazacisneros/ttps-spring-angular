import { Component, Output, EventEmitter, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
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
  barrios: Barrio[] = [];

  tamanios = TAMANIOS_MASCOTA;
  colores = COLORES_MASCOTA;
  estados = ESTADOS_MASCOTA;

  constructor(
    private fb: FormBuilder,
    private barrioService: BarrioService,
    private cdr: ChangeDetectorRef
  ) {
    this.filtroForm = this.fb.group({
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
    this.loadBarrios();
  }

  loadBarrios() {

    this.barrioService.getBarrios().subscribe({
      next: (barrios) => {
        if (!barrios) {
          this.barrios = [];
          return;
        }

        if (!Array.isArray(barrios)) {
          this.barrios = [];
          return;
        }

        if (barrios.length === 0) {
          this.barrios = [];
          return;
        }

        this.barrios = [...barrios]; 

        this.cdr.detectChanges();

        setTimeout(() => {
        }, 100);
      },
      error: (error) => {
        console.error('Status:', error.status);
        console.error('Message:', error.message);
        console.error('Error completo:', error);
        this.barrios = [];
      }
    });
  }

  limpiarFiltros() {
    this.filtroForm.patchValue({
      barrioId: '',
      tamanio: '',
      color: '',
      estado: ''
    });
  }

  trackByBarrioId(index: number, barrio: Barrio): number {
    return barrio.id;
  }
}
