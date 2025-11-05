package ttps.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ttps.spring.config.AppConfig;
import ttps.spring.entity.*;
import ttps.spring.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // ======== Repositorios ========
        UsuarioRepository usuarioRepo = ctx.getBean(UsuarioRepository.class);
        CiudadRepository ciudadRepo = ctx.getBean(CiudadRepository.class);
        BarrioRepository barrioRepo = ctx.getBean(BarrioRepository.class);
        CoordenadaRepository coordenadaRepo = ctx.getBean(CoordenadaRepository.class);
        MascotaRepository mascotaRepo = ctx.getBean(MascotaRepository.class);
        PublicacionRepository publicacionRepo = ctx.getBean(PublicacionRepository.class);
        RankingRepository rankingRepo = ctx.getBean(RankingRepository.class);
        PuntajeRepository puntajeRepo = ctx.getBean(PuntajeRepository.class);
        AvistamientoRepository avistamientoRepo = ctx.getBean(AvistamientoRepository.class);
        MedallaRepository medallaRepo = ctx.getBean(MedallaRepository.class);

        // ======== ENTIDADES BÁSICAS ========
        Coordenada coord = new Coordenada("-34.9214", "-57.9544");

        Ciudad ciudad = new Ciudad("La Plata", coord);

        Barrio barrio = new Barrio("Centro", ciudad);
        barrioRepo.guardar(barrio);  // Luego el barrio, que referencia la ciudad

        Usuario usuario = new Usuario();
        usuario.setNombre("Naza");
        usuario.setEmail("naza@test.com");
        usuario.setContrasenia("1234");

        Mascota mascota = new Mascota("Toby", "Mediano", "Marrón", "Labrador amistoso", "Perdido");
        mascotaRepo.guardar(mascota);  // Se persiste la mascota

        Usuario usuario2 = new Usuario();
        usuario.setNombre("Nazaa");
        usuario.setEmail("nazaa@test.com");
        usuario.setContrasenia("12345");
        usuarioRepo.guardar(usuario2);
        Coordenada coord2 = new Coordenada("-34.9216", "-57.9546");
        Publicacion publicacion = new Publicacion(
                LocalDate.now(),
                LocalDateTime.now(),
                coord2,
                "ACTIVA",
                mascota,
                usuario2
        );
        publicacionRepo.guardar(publicacion);
        Ranking ranking = new Ranking("Ranking General", 0);
        Medalla medalla = new Medalla("Rescatista");
        medallaRepo.guardar(medalla);
        Puntaje puntaje = new Puntaje(100, ranking, usuario);
        puntajeRepo.guardar(puntaje);
        Usuario usuario3 = new Usuario();
        usuario.setNombre("Nazaa");
        usuario.setEmail("nazaa@test.com");
        usuario.setContrasenia("12345");
        usuarioRepo.guardar(usuario3);
        Coordenada coord3 = new Coordenada("-34.9211", "-57.9541");
        Avistamiento avistamiento = new Avistamiento(
                LocalDate.now(),
                LocalDateTime.now(),
                coord3,
                mascota,
                usuario3
        );
        avistamiento.setComentario("Vista en Parque Saavedra");
        avistamientoRepo.guardar(avistamiento);
        ctx.close();
    }
}