
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;


public class JAXBContextWithLegacyFactory {
    private static JAXBContext tmp;

        public static class JAXBContextImpl extends JAXBContext {
        public final Class<?> creator;
        JAXBContextImpl(Class<?> creator) {
            this.creator = creator;
        }

        @Override
        public Unmarshaller createUnmarshaller() throws JAXBException {
            return tmp.createUnmarshaller();
        }

        @Override
        public Marshaller createMarshaller() throws JAXBException {
            return tmp.createMarshaller();
        }

        @Override
        public Validator createValidator() throws JAXBException {
            return tmp.createValidator();
        }
    }

    public static abstract class FactoryBase {
        public static JAXBContext createContext(Class<?>[] classesToBeBound,
                Map<String, ?> properties) throws JAXBException {
            return new JAXBContextImpl(FactoryBase.class);
        }

        public static JAXBContext createContext(String contextPath,
                ClassLoader classLoader, Map<String, ?> properties)
                throws JAXBException {
            return new JAXBContextImpl(FactoryBase.class);
        }
    }

    public static class Factory1 extends FactoryBase {}

    public static class Factory2 extends FactoryBase {
        public static JAXBContext createContext(Class<?>[] classesToBeBound,
                Map<String, ?> properties) throws JAXBException {
            return new JAXBContextImpl(Factory2.class);
        }

        public static JAXBContext createContext(String contextPath,
                ClassLoader classLoader, Map<String, ?> properties)
                throws JAXBException {
            return new JAXBContextImpl(Factory2.class);
        }
    }

    
    
    public static void main(String[] args) throws JAXBException {
        System.out.println("\nWithout security manager\n");
        test(FactoryBase.class, FactoryBase.class);
        test(Factory1.class, FactoryBase.class);
        test(Factory2.class, Factory2.class);

        System.out.println("\nWith security manager\n");
        Policy.setPolicy(new Policy() {
            @Override
            public boolean implies(ProtectionDomain domain, Permission permission) {
                return true; 
            }
        });
        System.setSecurityManager(new SecurityManager());
        test(FactoryBase.class, FactoryBase.class);
        test(Factory1.class, FactoryBase.class);
        test(Factory2.class, Factory2.class);
    }

    public static void test(Class<?> factoryClass, Class<?> creatorClass) throws JAXBException {
        System.clearProperty(JAXBContext.JAXB_CONTEXT_FACTORY);
        System.out.println("** Testing  with Factory Class: " + factoryClass.getName());
        System.out.println(JAXBContext.JAXB_CONTEXT_FACTORY + " = "
                + System.getProperty(JAXBContext.JAXB_CONTEXT_FACTORY, ""));
        System.out.println("Calling "
                + "JAXBContext.newInstance(JAXBContextWithLegacyFactory.class)");
        tmp = JAXBContext.newInstance(JAXBContextWithLegacyFactory.class);
        System.setProperty(JAXBContext.JAXB_CONTEXT_FACTORY,
                factoryClass.getName());
        System.out.println(JAXBContext.JAXB_CONTEXT_FACTORY + " = "
                + System.getProperty(JAXBContext.JAXB_CONTEXT_FACTORY));
        System.out.println("Calling "
                + "JAXBContext.newInstance(JAXBContextWithLegacyFactory.class)");
        JAXBContext ctxt = JAXBContext.newInstance(JAXBContextWithLegacyFactory.class);
        System.out.println("Successfully loaded JAXBcontext: " +
                System.identityHashCode(ctxt) + "@" + ctxt.getClass().getName());
        if (ctxt.getClass() != JAXBContextImpl.class) {
            throw new RuntimeException("Wrong JAXBContext class"
                + "\n\texpected: "
                + System.identityHashCode(tmp) + "@" + JAXBContextImpl.class.getName()
                + "\n\tactual:   "
                + System.identityHashCode(ctxt) + "@" + ctxt.getClass().getName());
        }
        if (((JAXBContextImpl)ctxt).creator != creatorClass) {
            throw new RuntimeException("Wrong Factory class"
                + "\n\texpected: "
                + System.identityHashCode(tmp) + "@" + creatorClass.getName()
                + "\n\tactual:   "
                + System.identityHashCode(ctxt) + "@" + ((JAXBContextImpl)ctxt).creator.getName());
        }
    }
}
