package ttps.spring.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ttps.spring.entity.Barrio;
import ttps.spring.entity.Ciudad;
import ttps.spring.entity.Coordenada;
import ttps.spring.repository.BarrioRepository;
import ttps.spring.repository.CiudadRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    // Seeds mock para pruebas

    private final CiudadRepository ciudadRepository;
    private final BarrioRepository barrioRepository;

    public DataInitializer(CiudadRepository ciudadRepository, BarrioRepository barrioRepository) {
        this.ciudadRepository = ciudadRepository;
        this.barrioRepository = barrioRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (ciudadRepository.count() == 0) {
            Coordenada coordBsAs = new Coordenada("-58.3816", "-34.6037");
            Coordenada coordCordoba = new Coordenada("-64.1888", "-31.4201");

            Ciudad bsAs = new Ciudad("Buenos Aires", coordBsAs);
            Ciudad cordoba = new Ciudad("Córdoba", coordCordoba);

            Ciudad savedBsAs = ciudadRepository.save(bsAs);
            Ciudad savedCordoba = ciudadRepository.save(cordoba);

            Barrio palermo = new Barrio("Palermo", savedBsAs);
            Barrio recoleta = new Barrio("Recoleta", savedBsAs);
            Barrio nuevaCordoba = new Barrio("Nueva Córdoba", savedCordoba);
            Barrio guemes = new Barrio("Güemes", savedCordoba);

            barrioRepository.save(palermo);
            barrioRepository.save(recoleta);
            barrioRepository.save(nuevaCordoba);
            barrioRepository.save(guemes);
        }
    }
}
