package com.example.kafka.producer;

import com.example.core.entity.Patient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

@Path("/quotes")
public class QuotesResource {

    @Inject
    @Channel("quote-requests")
    Emitter<String> quoteRequestEmitter;

    @Channel("quotes")
    Multi<Uni<List<Patient>>> quotes;

    @RolesAllowed({"doctor", "staff"})
    @POST
    @Path("/request")
    @Produces(MediaType.TEXT_PLAIN)
    public String createRequest(@QueryParam("value") String term) {
        quoteRequestEmitter.send((term));
        return term;
    }

    @RolesAllowed({"doctor", "staff"})
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Uni<List<Patient>>> stream() {
        return quotes.log();
    }
}
