package edu.tommraff.kanban.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.tommraff.kanban.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KanbanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KanbanDTO.class);
        KanbanDTO kanbanDTO1 = new KanbanDTO();
        kanbanDTO1.setId(1L);
        KanbanDTO kanbanDTO2 = new KanbanDTO();
        assertThat(kanbanDTO1).isNotEqualTo(kanbanDTO2);
        kanbanDTO2.setId(kanbanDTO1.getId());
        assertThat(kanbanDTO1).isEqualTo(kanbanDTO2);
        kanbanDTO2.setId(2L);
        assertThat(kanbanDTO1).isNotEqualTo(kanbanDTO2);
        kanbanDTO1.setId(null);
        assertThat(kanbanDTO1).isNotEqualTo(kanbanDTO2);
    }
}
