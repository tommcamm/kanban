package edu.tommraff.kanban.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KlistMapperTest {

    private KlistMapper klistMapper;

    @BeforeEach
    public void setUp() {
        klistMapper = new KlistMapperImpl();
    }
}
