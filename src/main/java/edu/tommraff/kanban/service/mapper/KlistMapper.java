package edu.tommraff.kanban.service.mapper;

import edu.tommraff.kanban.domain.*;
import edu.tommraff.kanban.service.dto.KlistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Klist} and its DTO {@link KlistDTO}.
 */
@Mapper(componentModel = "spring", uses = { KanbanMapper.class })
public interface KlistMapper extends EntityMapper<KlistDTO, Klist> {
    @Mapping(target = "kanbanlist", source = "kanbanlist", qualifiedByName = "id")
    KlistDTO toDto(Klist s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KlistDTO toDtoId(Klist klist);
}
