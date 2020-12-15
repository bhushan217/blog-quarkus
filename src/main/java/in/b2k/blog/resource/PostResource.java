package in.b2k.blog.resource;

import java.util.List;
import java.util.Optional;

import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import lombok.extern.log4j.Log4j2;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import in.b2k.blog.model.Post;
import io.quarkus.panache.common.Sort;

@Path("posts")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @GET
    public List<Post> get() {
        return Post.listAll(Sort.by("author"));
    }

    @GET
    @Path("{id}")
    public Post getSingle(@PathParam Long id) {
        Optional<Post> entity = Post.getWithComment(id);//findById(id);//
        if (entity.isEmpty()) {
            throw new WebApplicationException(String.format("Post with id of {} does not exist.",id), 404);
        }
        return entity.get();
    }

    @POST
    @Transactional
    public Response create(Post post) {
        if (post.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        post.persist();
        return Response.ok(post).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Post update(@PathParam Long id, Post post) {
        if (post.getAuthor() == null) {
            throw new WebApplicationException("Post Name was not set on request.", 422);
        }

        Post entity = Post.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Post with id of " + id + " does not exist.", 404);
        }

        entity.setAuthor(post.getAuthor());

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Post entity = Post.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Post with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }
}
