package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

   // final static int

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        Alias expected = repository.findById();
        assertNotNull(expected);
        assertEquals(, expected.getAliasId());
        assertEquals(, expected.getName());
        assertEquals(, expected.getPersona());
    }

    @Test
    void shouldAdd() {
        Alias alias = new Alias();
        alias.setName("Bond");
        alias.setPersona("007");
        Alias actual = repository.add(alias);
        alias.setAliasId();

        assertNotNull(actual);
        assertEquals(alias, actual);
    }

    @Test
    void shouldUpdate() {
        Alias alias = new Alias();
        alias.setAliasId();
        assertTrue(repository.update(alias));
        assertEquals(alias, repository.findById());
        alias.setAliasId(20);
        assertFalse(repository.update(alias));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));
    }
}