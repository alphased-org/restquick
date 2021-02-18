package com.alphased.restquick.crsw;

import com.alphased.restquick.crsw.exception.LoadErrorCRSWContainerException;
import com.alphased.restquick.crsw.model.WorkerInformation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Instant;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

@SpringBootApplication
@Log4j2
public class CRSWApplication {

    private static WorkerInformation workerInformation;

    public static void main(String[] args) {
        preStartup(args);
        log.info("Application created with -> {}", workerInformation);
        SpringApplication.run(CRSWApplication.class, args);
    }

    private static void preStartup(String[] args) {
        args = new String[2];
        args[0] = "erdem";
        args[1] = "free";
        notEmpty(args, "Start arguments is empty.");
        notNull(args[0], "Cannot be null OwnerID. point -> args[0]");
        notNull(args[1], "Cannot be null WorkerPlan. point -> args[1]");
        workerInformation = WorkerInformation.builder().ownerId(args[0]).createdDate(Instant.now().getEpochSecond()).workerPlan(args[1]).build();
    }

    @Bean
    public CRSWContainer crswContainer() throws LoadErrorCRSWContainerException {
        return new CRSWContainer(workerInformation);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module);
    }

}
