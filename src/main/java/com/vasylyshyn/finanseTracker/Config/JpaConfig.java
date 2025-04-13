package com.vasylyshyn.finanseTracker.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.vasylyshyn.finanseTracker.Repositorys")
public class JpaConfig {
}
