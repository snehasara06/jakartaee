package com.kgisl.jakartaee;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/greetings")
public class GreetingResource {

    @Inject
    private GreetingRepository greetingRepository;

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String findOne(@PathParam("name") String name) {

        return greetingRepository.findByNameIgnoreCase(name)
                .map(Greeting::getMessage)
                .orElse(name + " not found");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Greeting> findAll() {

        return greetingRepository.findAll()
                .toList();
    }

    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGreeting(Greeting greeting) {

        Greeting saved = greetingRepository.save(greeting);
        return Response.ok("Created greeting: " + greeting.getId()).build();
    }

    @PUT
@Consumes(MediaType.APPLICATION_JSON)
@Path("/{id}") // Include the ID of the greeting to be updated in the URL
public Response updateGreeting(@PathParam("id") Long id, Greeting updatedGreeting) {
    Optional<Greeting> existingGreetingOptional = greetingRepository.findById(id);

    if (existingGreetingOptional.isEmpty()) {
        return Response.status(Response.Status.NOT_FOUND).entity("Greeting with ID " + id + " not found").build();
    }

    Greeting existingGreeting = existingGreetingOptional.get();
    existingGreeting.setMessage(updatedGreeting.getMessage());
existingGreeting.setName(updatedGreeting.getName());
    // Save the updated greeting
    greetingRepository.save(existingGreeting);

    return Response.ok("Updated greeting with ID: " + id).build();
}

@DELETE
@Path("/{id}") // Include the ID of the greeting to be deleted in the URL
public Response deleteGreeting(@PathParam("id") Long id) {
    Optional<Greeting> existingGreetingOptional = greetingRepository.findById(id);

    if (existingGreetingOptional.isEmpty()) {
        return Response.status(Response.Status.NOT_FOUND).entity("Greeting with ID " + id + " not found").build();
    }

    Greeting existingGreeting = existingGreetingOptional.get();

    // Delete the greeting
    greetingRepository.delete(existingGreeting);

    return Response.ok("Deleted greeting with ID: " + id).build();
}

}