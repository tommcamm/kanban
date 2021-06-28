package edu.tommraff.kanban.service.mapper;

import edu.tommraff.kanban.domain.*;
import edu.tommraff.kanban.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { KlistMapper.class })
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "klisttask", source = "klisttask", qualifiedByName = "id")
    TaskDTO toDto(Task s);
}
