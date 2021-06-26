package edu.tommraff.kanban.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.tommraff.kanban.domain.Kanban} entity. This class is used
 * in {@link edu.tommraff.kanban.web.rest.KanbanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /kanbans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KanbanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter created_at;

    private LocalDateFilter last_edit;

    public KanbanCriteria() {}

    public KanbanCriteria(KanbanCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.created_at = other.created_at == null ? null : other.created_at.copy();
        this.last_edit = other.last_edit == null ? null : other.last_edit.copy();
    }

    @Override
    public KanbanCriteria copy() {
        return new KanbanCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getCreated_at() {
        return created_at;
    }

    public LocalDateFilter created_at() {
        if (created_at == null) {
            created_at = new LocalDateFilter();
        }
        return created_at;
    }

    public void setCreated_at(LocalDateFilter created_at) {
        this.created_at = created_at;
    }

    public LocalDateFilter getLast_edit() {
        return last_edit;
    }

    public LocalDateFilter last_edit() {
        if (last_edit == null) {
            last_edit = new LocalDateFilter();
        }
        return last_edit;
    }

    public void setLast_edit(LocalDateFilter last_edit) {
        this.last_edit = last_edit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KanbanCriteria that = (KanbanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(created_at, that.created_at) &&
            Objects.equals(last_edit, that.last_edit)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, created_at, last_edit);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KanbanCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (created_at != null ? "created_at=" + created_at + ", " : "") +
            (last_edit != null ? "last_edit=" + last_edit + ", " : "") +
            "}";
    }
}
