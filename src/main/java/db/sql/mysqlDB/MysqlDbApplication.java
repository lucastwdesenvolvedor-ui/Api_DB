package db.sql.mysqlDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MysqlDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysqlDbApplication.class, args);
        System.out.println("KEY ENV: " + System.getenv("key"));
	}

}
