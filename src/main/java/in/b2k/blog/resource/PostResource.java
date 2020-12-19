package in.b2k.blog.resource;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import in.b2k.blog.model.Comment;
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
        Optional<Post> entity = Post.find("id", id).firstResultOptional();
        if(entity.isEmpty()){
            throw new WebApplicationException(String.format("Post with id of {} does not exist.",id), 404);
        }
        return entity.get();
    }

    @GET
    @Path("{id}/comment")
    public List<Comment> getComment(@PathParam Long id) {
        List<Comment> entity = Comment.listAll(Sort.by("author"));
        if(entity.isEmpty()){
            throw new WebApplicationException(String.format("Post with id of {} does not exist.",id), 404);
        }
        return entity;
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
