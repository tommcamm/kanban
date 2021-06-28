package edu.tommraff.kanban.domain;

import static org.assertj.core.api.Assertions.assertThat;

import edu.tommraff.kanban.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KlistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Klist.class);
        Klist klist1 = new Klist();
        klist1.setId(1L);
        Klist klist2 = new Klist();
        klist2.setId(klist1.getId());
        assertThat(klist1).isEqualTo(klist2);
        klist2.setId(2L);
        assertThat(klist1).isNotEqualTo(klist2);
        klist1.setId(null);
        assertThat(klist1).isNotEqualTo(klist2);
    }
}
