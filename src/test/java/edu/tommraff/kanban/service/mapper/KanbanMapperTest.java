package edu.tommraff.kanban.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KanbanMapperTest {

    private KanbanMapper kanbanMapper;

    @BeforeEach
    public void setUp() {
        kanbanMapper = new KanbanMapperImpl();
    }
}
