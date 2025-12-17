//package ttps.spring.config;
//
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//    // Configuración raíz (p. ej. JPA / DataSource)
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class[] { PersistenceConfig.class, AppConfig.class };
//    }
//
//    // Configuración del servlet (Spring MVC)
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[] { WebConfig.class };
//    }
//
//    // Mapear DispatcherServlet
//    @Override
//    protected String[] getServletMappings() {
//        return new String[] { "/" };
//    }
//}