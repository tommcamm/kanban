package edu.tommraff.kanban.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Kanban.
 */
@Entity
@Table(name = "kanban")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Kanban implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column(name = "last_edit")
    private LocalDate last_edit;

    @ManyToOne(optional = false)
    @NotNull
    private User userkanban;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kanban id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Kanban name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreated_at() {
        return this.created_at;
    }

    public Kanban created_at(LocalDate created_at) {
        this.created_at = created_at;
        return this;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getLast_edit() {
        return this.last_edit;
    }

    public Kanban last_edit(LocalDate last_edit) {
        this.last_edit = last_edit;
        return this;
    }

    public void setLast_edit(LocalDate last_edit) {
        this.last_edit = last_edit;
    }

    public User getUserkanban() {
        return this.userkanban;
    }

    public Kanban userkanban(User user) {
        this.setUserkanban(user);
        return this;
    }

    public void setUserkanban(User user) {
        this.userkanban = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kanban)) {
            return false;
        }
        return id != null && id.equals(((Kanban) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Kanban{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", created_at='" + getCreated_at() + "'" +
            ", last_edit='" + getLast_edit() + "'" +
            "}";
    }
}
