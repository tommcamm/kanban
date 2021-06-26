package edu.tommraff.kanban.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.tommraff.kanban.domain.Kanban} entity.
 */
public class KanbanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    private String name;

    private LocalDate created_at;

    private LocalDate last_edit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getLast_edit() {
        return last_edit;
    }

    public void setLast_edit(LocalDate last_edit) {
        this.last_edit = last_edit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KanbanDTO)) {
            return false;
        }

        KanbanDTO kanbanDTO = (KanbanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, kanbanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KanbanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", created_at='" + getCreated_at() + "'" +
            ", last_edit='" + getLast_edit() + "'" +
            "}";
    }
}
