package kr.doublechain.basic.explorer.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Created by 전성국 on 2018-05-23.
 */
@Component
public class HealthCheck implements HealthIndicator {

    @Override
    public Health health() {

        boolean isOk = check();

        if(!isOk) {
            return Health.down().withDetail("Error Code", 10000).build();
        }

        return Health.up().build();

    }

    public boolean check() {
        return true;
    }
}
