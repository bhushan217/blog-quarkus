package in.b2k.blog.model;

import javax.persistence.*;

@Entity
@Table(name = "B2K_COMMENT")
@NamedQuery(name = "Comment.getByAuthor", query = "SELECT c FROM Comment c where c.author = :author")
public class Comment extends BaseEntity {

    @Column( name = "AUTHOR" )
    protected String author;

    @Column( name = "CONTENT_TXT" )
    protected String contentText;

    @Column( name = "LIKES" )
    protected Integer likes;

	@JoinColumn(name="POST_ID", referencedColumnName="ID", foreignKey = @ForeignKey(name = "B2K_POST_COMMENT"))
    @ManyToOne(fetch = FetchType.LAZY)
    protected Post post;
}
