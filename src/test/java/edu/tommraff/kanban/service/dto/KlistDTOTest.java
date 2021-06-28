package edu.tommraff.kanban.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import edu.tommraff.kanban.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KlistDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KlistDTO.class);
        KlistDTO klistDTO1 = new KlistDTO();
        klistDTO1.setId(1L);
        KlistDTO klistDTO2 = new KlistDTO();
        assertThat(klistDTO1).isNotEqualTo(klistDTO2);
        klistDTO2.setId(klistDTO1.getId());
        assertThat(klistDTO1).isEqualTo(klistDTO2);
        klistDTO2.setId(2L);
        assertThat(klistDTO1).isNotEqualTo(klistDTO2);
        klistDTO1.setId(null);
        assertThat(klistDTO1).isNotEqualTo(klistDTO2);
    }
}
