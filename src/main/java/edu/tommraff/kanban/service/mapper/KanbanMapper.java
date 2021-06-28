package edu.tommraff.kanban.service.mapper;

import edu.tommraff.kanban.domain.*;
import edu.tommraff.kanban.service.dto.KanbanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Kanban} and its DTO {@link KanbanDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface KanbanMapper extends EntityMapper<KanbanDTO, Kanban> {
    @Mapping(target = "userkanban", source = "userkanban", qualifiedByName = "login")
    KanbanDTO toDto(Kanban s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KanbanDTO toDtoId(Kanban kanban);
}
