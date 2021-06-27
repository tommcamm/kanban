package edu.tommraff.kanban.service.mapper;

import edu.tommraff.kanban.domain.*;
import edu.tommraff.kanban.service.dto.KanbanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Kanban} and its DTO {@link KanbanDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KanbanMapper extends EntityMapper<KanbanDTO, Kanban> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KanbanDTO toDtoId(Kanban kanban);
}
