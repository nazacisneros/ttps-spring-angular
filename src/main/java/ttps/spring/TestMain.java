//package ttps.spring;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import ttps.spring.config.PersistenceConfig;
//import ttps.spring.repository.MascotaRepository;
//import ttps.spring.repository.UsuarioRepository;
//import ttps.spring.repository.AvistamientoRepository;
//import ttps.spring.repository.BarrioRepository;
//import ttps.spring.repository.CiudadRepository;
//import ttps.spring.repository.CoordenadaRepository;
//import ttps.spring.repository.PublicacionRepository;
//import ttps.spring.repository.PuntajeRepository;
//import ttps.spring.repository.RankingRepository;
//
//public class TestMain {
//    public static void main(String[] args) {
//
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//        ctx.register(PersistenceConfig.class);
//        ctx.refresh();
//
//        try {
//            // ====== Usuario =======
//            System.out.println("=== Test UsuarioRepository ===");
//            UsuarioRepository usuarioRepo = ctx.getBean(UsuarioRepository.class);
//            System.out.println("✓ Repositorio Usuario cargado: " + usuarioRepo);
//            System.out.println("✓ Cantidad de usuarios: " + usuarioRepo.count());
//
//            // ====== Mascota =======
//            System.out.println("\n=== Test MascotaRepository ===");
//            MascotaRepository mascotaRepo = ctx.getBean(MascotaRepository.class);
//            System.out.println("✓ Repositorio Mascota cargado: " + mascotaRepo);
//            System.out.println("✓ Cantidad de mascotas: " + mascotaRepo.count());
//
//            // ====== Avistamiento =======
//            System.out.println("\n=== Test AvistamientoRepository ===");
//            AvistamientoRepository avistRepo = ctx.getBean(AvistamientoRepository.class);
//            System.out.println("✓ Repositorio Avistamiento cargado: " + avistRepo);
//            System.out.println("✓ Cantidad de avistamientos: " + avistRepo.count());
//
//            // ====== Barrio =======
//            System.out.println("\n=== Test BarrioRepository ===");
//            BarrioRepository barrioRepo = ctx.getBean(BarrioRepository.class);
//            System.out.println("✓ Repositorio Barrio cargado: " + barrioRepo);
//            System.out.println("✓ Cantidad de barrios: " + barrioRepo.count());
//
//            // ====== Ciudad =======
//            System.out.println("\n=== Test CiudadRepository ===");
//            CiudadRepository ciudadRepo = ctx.getBean(CiudadRepository.class);
//            System.out.println("✓ Repositorio Ciudad cargado: " + ciudadRepo);
//            System.out.println("✓ Cantidad de ciudades: " + ciudadRepo.count());
//
//            // ====== Coordenada =======
//            System.out.println("\n=== Test CoordenadaRepository ===");
//            CoordenadaRepository coordRepo = ctx.getBean(CoordenadaRepository.class);
//            System.out.println("✓ Repositorio Coordenada cargado: " + coordRepo);
//            System.out.println("✓ Cantidad de coordenadas: " + coordRepo.count());
//
//            // ====== Publicacion =======
//            System.out.println("\n=== Test PublicacionRepository ===");
//            PublicacionRepository publiRepo = ctx.getBean(PublicacionRepository.class);
//            System.out.println("✓ Repositorio Publicacion cargado: " + publiRepo);
//            System.out.println("✓ Cantidad de Publicaciones: " + publiRepo.count());
//
//            // ====== Puntaje =======
//            System.out.println("\n=== Test PuntajeRepository ===");
//            PuntajeRepository puntRepo = ctx.getBean(PuntajeRepository.class);
//            System.out.println("✓ Repositorio Puntaje cargado: " + puntRepo);
//            System.out.println("✓ Cantidad de Puntajes: " + puntRepo.count());
//
//            // ====== Ranking =======
//            System.out.println("\n=== Test RankingRepository ===");
//            RankingRepository rankRepo = ctx.getBean(RankingRepository.class);
//            System.out.println("✓ Repositorio Ranking cargado: " + rankRepo);
//            System.out.println("✓ Cantidad de Rankings: " + rankRepo.count());
//
//        } catch (Exception e) {
//            System.err.println("✗ Error durante la prueba: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            ctx.close();
//        }
//    }
//}
