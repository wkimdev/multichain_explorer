package kr.doublechain.basic.explorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExplorerApplication.class, args);
		
//		ApplicationContext context = SpringApplication.run(ExplorerApplication.class, args);
//		UpdateBlockListener starter = context.getBean(UpdateBlockListener.class);
//		try {
//			starter.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
