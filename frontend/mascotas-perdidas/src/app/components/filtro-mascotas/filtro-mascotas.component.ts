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
    console.log('=== Iniciando carga de barrios ===');
    console.log('Estado inicial - barrios.length:', this.barrios.length);

    this.barrioService.getBarrios().subscribe({
      next: (barrios) => {
        console.log('✅ Respuesta recibida del backend');
        console.log('Datos raw:', JSON.stringify(barrios));
        console.log('Cantidad de barrios:', barrios?.length || 0);
        console.log('Es array?:', Array.isArray(barrios));

        if (!barrios) {
          console.error('❌ Barrios es null o undefined');
          this.barrios = [];
          return;
        }

        if (!Array.isArray(barrios)) {
          console.error('❌ Barrios NO es un array');
          this.barrios = [];
          return;
        }

        if (barrios.length === 0) {
          console.warn('⚠️ El array de barrios está vacío');
          this.barrios = [];
          return;
        }

        console.log('Primer barrio completo:', barrios[0]);
        console.log('  - id:', barrios[0].id, '(tipo:', typeof barrios[0].id, ')');
        console.log('  - nombre:', barrios[0].nombre, '(tipo:', typeof barrios[0].nombre, ')');

        // Asignar los barrios
        this.barrios = [...barrios]; // Crear nueva referencia

        console.log('✅ Barrios asignados a la propiedad');
        console.log('this.barrios.length:', this.barrios.length);
        console.log('Contenido completo:', this.barrios);

        // Forzar detección de cambios
        this.cdr.detectChanges();
        console.log('✅ Detección de cambios forzada');

        // Verificar después de un momento
        setTimeout(() => {
          console.log('=== Verificación post-asignación ===');
          console.log('barrios en template:', this.barrios.length);
        }, 100);
      },
      error: (error) => {
        console.error('❌ ERROR cargando barrios');
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

  // TrackBy function para mejorar el rendimiento del *ngFor
  trackByBarrioId(index: number, barrio: Barrio): number {
    return barrio.id;
  }
}
