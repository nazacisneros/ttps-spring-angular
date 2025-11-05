package ttps.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ttps.spring.config.PersistenceConfig;
import ttps.spring.model.MiPrimerEntity;

import javax.sql.DataSource;


public class SpringTestMain {
	public static void main(String[] args) {
		 // Create a new AnnotationConfigApplicationContext
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        // Register the configuration class (PersistenceConfig in this case)
        ctx.register(PersistenceConfig.class);
        // Refresh the context to initialize Spring beans
        ctx.refresh();
		DataSource dataSource = ctx.getBean(DataSource.class);
        System.out.println("DataSource cargado: " + dataSource);
        MiPrimerEntity e = ctx.getBean("prueba", MiPrimerEntity.class);
		System.out.println("Bean cargado: "+e.hola());
        // Don't forget to close the context to free main.resources
        ctx.close();
	}
}