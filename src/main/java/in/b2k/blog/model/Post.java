package in.b2k.blog.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "B2K_POST")
@NamedQuery(name = "Post.getByAuthor", query = "SELECT p FROM Post p where p.author = ?1")
@NamedQuery(name = "Post.getWithComment", query = "SELECT p FROM Post p left join fetch p.comments where p.id = :id")
@NamedQuery(name = "Post.findAllWithComment", query = "SELECT p FROM Post p left join fetch p.comments")
@Data @EqualsAndHashCode(callSuper=false)
public class Post extends BaseEntity {

    @Column( name = "AUTHOR" )
    protected String author;
    @Column( name = "STATUS" )
    protected String status;
    @Column( name = "LIKES" )
    protected Integer likes;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected Set<Comment> comments ;//= new HashSet<>();

    public static Optional<PanacheEntityBase> findByAuthor(String author){
        return find("#Post.getByAuthor", Map.of("author", author)).firstResultOptional();
    }

    public static Optional<Post> getWithComment(Long id){
        return find("#Post.getWithComment", Map.of("id", id)).firstResultOptional();
        //return (Post) find("#Post.findAllWithComment").page(Page.ofSize(1)).firstPage().list().get(0);
    }
}