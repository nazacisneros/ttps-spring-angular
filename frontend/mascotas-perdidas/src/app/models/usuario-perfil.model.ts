export interface UsuarioPerfil {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  esAdmin: boolean;
  barrioId?: number;
  barrioNombre?: string;
  ciudadId?: number;
  ciudadNombre?: string;
}
