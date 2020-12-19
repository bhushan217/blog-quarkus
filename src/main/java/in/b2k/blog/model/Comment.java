package in.b2k.blog.model;

import java.util.Map;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "B2K_COMMENT")
@NamedQuery(name = "Comment.findByPost", query = "SELECT c FROM Comment c where c.post.id = :id")
@Data @EqualsAndHashCode(callSuper=false)
public class Comment extends BaseEntity {

    @Column( name = "AUTHOR", length = 255 )
    protected String author;

    @Column( name = "CONTENT_TXT", length = 255 )
    protected String contentText;

    @Column( name = "LIKES" )
    protected Integer likes;

	@JoinColumn(name="POST_ID", foreignKey = @ForeignKey(name = "B2K_POST_COMMENT"))
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected Post post;

    
    public static Optional<Comment> findByPost(Long id){
        return find("#Comment.findByPost", Map.of("id", id)).firstResultOptional();
    }
}
