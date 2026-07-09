package nl.group9.hypotheek;

import io.camunda.zeebe.client.ZeebeClient;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@ApplicationScoped
public class ProcessDeployManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDeployManager.class);

    ZeebeClient zeebeClient;

    @Inject
    public ProcessDeployManager(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    void deployProcess(@Observes @Priority(1) StartupEvent startupEvent) {
        InputStream resource = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("proces/hypotheek-aanvraag.bpmn");

        if (resource == null) {
            throw new RuntimeException("BPMN niet gevonden op classpath");
        }

        LOGGER.info("====Hypotheekaanvraag proces deploy starten...====");

        zeebeClient.newDeployResourceCommand()
                .addResourceStream(resource, "hypotheek-aanvraag.bpmn")
                .send()
                .join();

        LOGGER.info("====Hypotheekaanvraag proces gedeployed====");
    }
}
