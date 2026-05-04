package ma.ghizlane.activitepratique2;

import ma.ghizlane.activitepratique2.entities.Product;
import ma.ghizlane.activitepratique2.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Activitepratique2Application {

	public static void main(String[] args) {
		SpringApplication.run(Activitepratique2Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ProductRepository productRepository){
		return args->{
			productRepository.save(new Product(null,"Computer",9000,3));
			productRepository.save(new Product(null,"Printer",1200,2));
			productRepository.save(new Product(null,"SmartPhone",5000,10));

			productRepository.findAll().forEach(p->{
				System.out.println(p.getName());
			});
		};
	}
}
