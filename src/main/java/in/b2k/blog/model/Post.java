package in.b2k.blog.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "B2K_POST")
@NamedQuery(name = "Post.getByAuthor", query = "SELECT p FROM Post p where p.author = ?1")
@NamedQuery(name = "Post.getWithComment", query = "SELECT p FROM Post p left join fetch p.comments where p.id = :id")
@NamedQuery(name = "Post.findAllWithComment", query = "SELECT p FROM Post p left join fetch p.comments")
@Data @EqualsAndHashCode(callSuper=false)
public class Post extends BaseEntity {

    @Column( name = "AUTHOR", length = 255)
    protected String author;
    @Column( name = "STATUS", length = 255 )
    protected String status;
    @Column( name = "LIKES" )
    protected Integer likes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    protected Set<Comment> comments = new HashSet<>();

    public void addComments(Comment comment){
        comments.add(comment);
        comment.setPost(this);
    }
    public void removeComments(Comment comment){
        comments.remove(comment);
        comment.setPost(null);
    }

    public static Post insert(final Post newJob) {        
        newJob.persist();       
        return newJob;
    }

    public static Optional<Post> findByAuthor(String author){
        return find("#Post.getByAuthor", Map.of("author", author)).firstResultOptional();
    }

    public static Optional<Post> getWithComment(Long id){
        return find("#Post.getWithComment", Map.of("id", id)).firstResultOptional();
    }
}