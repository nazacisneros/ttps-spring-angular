export interface UsuarioPerfil {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  barrioId?: number;
  barrioNombre?: string;
  ciudadId?: number;
  ciudadNombre?: string;
}
