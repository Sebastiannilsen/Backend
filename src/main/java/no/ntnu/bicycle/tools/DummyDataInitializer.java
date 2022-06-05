package no.ntnu.bicycle.tools;

import lombok.SneakyThrows;
import no.ntnu.bicycle.model.*;
import no.ntnu.bicycle.repository.BicycleRepository;
import no.ntnu.bicycle.repository.CustomerRepository;
import no.ntnu.bicycle.repository.OrderRepository;
import no.ntnu.bicycle.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * This class initializes example data
 */
@Component
public class DummyDataInitializer implements ApplicationListener<ApplicationReadyEvent>{

    private CustomerRepository customerRepository;


    private OrderRepository orderRepository;


    private ProductRepository productRepository;

    private BicycleRepository bicycleRepository;


    /**
     * Constructor for dummy data initializer
     * @param customerRepository CustomerRepository
     * @param orderRepository OrderRepository
     * @param productRepository ProductRepository
     * @param bicycleRepository BicycleRepository
     */
    public DummyDataInitializer(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository, BicycleRepository bicycleRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.bicycleRepository = bicycleRepository;
    }

    private final Logger logger = Logger.getLogger("DummyInit");

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (isDataImported()){
            logger.info("Data already exists");
        }else {

            logger.info("Importing test data...");
            Customer sebastian = new Customer("Sebastian", "Nilsen", "sebasn@stud.ntnu.no", "2001-04-19", 94198166, "$2a$12$QjPXqckLsFqDDRxrEfboC.0WYcUSP5wMhuOftGkcnpA9vI1sUOiWa", Role.ROLE_ADMIN);
            Customer anne = new Customer("Anne", "Ruud", "anneruud@mail.no", "1994-06-04", 94198782, "$2a$12$QjPXqckLsFqDDRxrEfboC.0WYcUSP5wMhuOftGkcnpA9vI1sUOiWa", Role.ROLE_USER);

            BillingAndShippingAddress adresse1 = new BillingAndShippingAddress("Sebastian", "Nilsen", "Fiolveien 1b", "Norway", "1395", "Hvalstad");

            sebastian.setAddress(adresse1);

            customerRepository.saveAll(List.of(sebastian, anne));

            Product blueHelmet = new Product("Blue helmet", "blue", "blue-helmet.png", "Save my Brain er en rimelig hjelm som også har godkjenning CE 1078. Hjelmen tilpasses enkelt til justeringsskruen i nakken og passer dermed til flere forskjellige barn eller for barnets utvikling.", 199);
            Product whiteHelmet = new Product("White helmet", "white", "white-helmet.png", "Save my Brain er en rimelig hjelm som også har godkjenning CE 1078. Hjelmen tilpasses enkelt til justeringsskruen i nakken og passer dermed til flere forskjellige barn eller for barnets utvikling.", 199);
            Product blueHelmet1 = new Product("Blue helmet", "blue", "blue-helmet.png", "Save my Brain er en rimelig hjelm som også har godkjenning CE 1078. Hjelmen tilpasses enkelt til justeringsskruen i nakken og passer dermed til flere forskjellige barn eller for barnets utvikling.", 199);
            Product whiteHelmet1 = new Product("White helmet", "white", "white-helmet.png", "Save my Brain er en rimelig hjelm som også har godkjenning CE 1078. Hjelmen tilpasses enkelt til justeringsskruen i nakken og passer dermed til flere forskjellige barn eller for barnets utvikling.", 199);
            Product blueHelmet2 = new Product("Blue helmet", "blue", "blue-helmet.png", "Save my Brain er en rimelig hjelm som også har godkjenning CE 1078. Hjelmen tilpasses enkelt til justeringsskruen i nakken og passer dermed til flere forskjellige barn eller for barnets utvikling.", 199);
            Product whiteHelmet2 = new Product("White helmet", "white", "white-helmet.png", "Save my Brain er en rimelig hjelm som også har godkjenning CE 1078. Hjelmen tilpasses enkelt til justeringsskruen i nakken og passer dermed til flere forskjellige barn eller for barnets utvikling.", 199);


            productRepository.saveAll(List.of(blueHelmet, whiteHelmet, blueHelmet1, whiteHelmet1, blueHelmet2, whiteHelmet2));


            sebastian.addProductToShoppingCart(blueHelmet);
            sebastian.addProductToShoppingCart(whiteHelmet);

            anne.addProductToShoppingCart(blueHelmet2);

            customerRepository.saveAll(List.of(sebastian, anne));

            CustomerOrder order1 = new CustomerOrder(sebastian);
            CustomerOrder order2 = new CustomerOrder(anne);

            orderRepository.saveAll(List.of(order1, order2));

            Bicycle bicycle = new Bicycle("Brown", "62.47433372997846, 6.164537350482638", 1);
            bicycle.setStatusToAvailable();
            Bicycle bicycle1 = new Bicycle("Green", "62.47309499833774, 6.153352263095387", 1);
            bicycle1.setStatusToAvailable();

            bicycleRepository.saveAll(List.of(bicycle, bicycle1));
        }
    }

    /**
     * Checking if data is imported
     * @return true if data is imported, false if it's not imported
     */
    private boolean isDataImported() {
        return (customerRepository.count() > 0);
    }
}
