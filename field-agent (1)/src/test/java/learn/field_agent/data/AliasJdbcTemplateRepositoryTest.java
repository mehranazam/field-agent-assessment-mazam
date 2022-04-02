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
        Alias expected = repository.findById(1);
        assertNotNull(expected);
        assertEquals(1, expected.getAliasId());
        assertEquals("Hazel", expected.getName());
        assertEquals("Blackhawk", expected.getPersona());
    }

    @Test
    void shouldAdd() {
        Alias alias = new Alias();
        alias.setName("Urban");
        alias.setPersona("Legend");
        alias.setAgentId(3);
        Alias actual = repository.add(alias);
        alias.setAliasId(6);

        assertNotNull(actual);
        assertEquals(alias, actual);
    }

    @Test
    void shouldUpdate() {
        Alias alias = new Alias();
        alias.setAliasId(1);
        alias.setName("Alex");
        alias.setPersona("Bladerunner");

        assertTrue(repository.update(alias));
//        assertEquals(alias, repository.findById(3));
        alias.setAliasId(20);
        assertFalse(repository.update(alias));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));
    }

    @Test
    void shouldGetAliasesByAgentId(){
        throw new UnsupportedOperationException();
    }
}