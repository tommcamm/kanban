package edu.tommraff.kanban.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link edu.tommraff.kanban.domain.Klist} entity. This class is used
 * in {@link edu.tommraff.kanban.web.rest.KlistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /klists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KlistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private IntegerFilter order;

    private LongFilter kanbanlistId;

    public KlistCriteria() {}

    public KlistCriteria(KlistCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.kanbanlistId = other.kanbanlistId == null ? null : other.kanbanlistId.copy();
    }

    @Override
    public KlistCriteria copy() {
        return new KlistCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public LongFilter getKanbanlistId() {
        return kanbanlistId;
    }

    public LongFilter kanbanlistId() {
        if (kanbanlistId == null) {
            kanbanlistId = new LongFilter();
        }
        return kanbanlistId;
    }

    public void setKanbanlistId(LongFilter kanbanlistId) {
        this.kanbanlistId = kanbanlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KlistCriteria that = (KlistCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(order, that.order) &&
            Objects.equals(kanbanlistId, that.kanbanlistId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, order, kanbanlistId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KlistCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (kanbanlistId != null ? "kanbanlistId=" + kanbanlistId + ", " : "") +
            "}";
    }
}
