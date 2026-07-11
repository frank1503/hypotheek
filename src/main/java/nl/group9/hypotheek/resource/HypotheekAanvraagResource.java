package nl.group9.hypotheek.resource;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/hypotheekaanvraag")
public class HypotheekAanvraagResource {

    ZeebeClient zeebeClient;

    @Inject
    public HypotheekAanvraagResource(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @POST
    public void startHypotheekAanvraag() {
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("hypotheek-aanvraag")
                .latestVersion()
                .send()
                .join();
    }
}
