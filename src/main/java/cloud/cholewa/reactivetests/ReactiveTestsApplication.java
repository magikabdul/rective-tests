package cloud.cholewa.reactivetests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ReactiveTestsApplication {

//    static {
//        BlockHound.install();
//    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveTestsApplication.class, args);
    }

}
