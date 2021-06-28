package edu.tommraff.kanban.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link edu.tommraff.kanban.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 40)
    private String title;

    private Integer order;

    private KlistDTO klisttask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public KlistDTO getKlisttask() {
        return klisttask;
    }

    public void setKlisttask(KlistDTO klisttask) {
        this.klisttask = klisttask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", order=" + getOrder() +
            ", klisttask=" + getKlisttask() +
            "}";
    }
}
