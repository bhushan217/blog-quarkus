package in.b2k.blog.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data  @EqualsAndHashCode(callSuper=false)
public abstract class BaseEntity extends PanacheEntityBase {
    
    /**
     * The auto-generated ID field. This field is set by Hibernate ORM when this entity
     * is persisted.
     *
     * @see #persist()
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    protected Long id;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "<" + id + ">";
    }
    
    @Column( name = "created_by" )
    protected Long createdBy;
    @Column( name = "updated_by" )
    protected Long updatedBy;

    @Column( name = "created_tsp" )
    protected LocalDateTime createdAt;
    @Column( name = "updated_tsp" )
    protected LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
