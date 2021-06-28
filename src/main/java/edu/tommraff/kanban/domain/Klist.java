package edu.tommraff.kanban.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Klist.
 */
@Entity
@Table(name = "klist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Klist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "title", length = 40, nullable = false)
    private String title;

    @Column(name = "jhi_order")
    private Integer order;

    @ManyToOne(optional = false)
    @NotNull
    private Kanban kanbanlist;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Klist id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Klist title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Klist order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Kanban getKanbanlist() {
        return this.kanbanlist;
    }

    public Klist kanbanlist(Kanban kanban) {
        this.setKanbanlist(kanban);
        return this;
    }

    public void setKanbanlist(Kanban kanban) {
        this.kanbanlist = kanban;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Klist)) {
            return false;
        }
        return id != null && id.equals(((Klist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Klist{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
