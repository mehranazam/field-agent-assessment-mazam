package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll(){
        List<SecurityClearance> all = repository.findAll();

        assertNotNull(all);
        assertTrue(all.size() > 0);


    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(20);
        assertEquals(null, actual);
    }

    @Test
    void shouldAdd(){
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Lee");
        SecurityClearance actual = repository.add(securityClearance);
        securityClearance.setSecurityClearanceId(3);

        assertNotNull(actual);
        assertEquals(securityClearance, actual);
    }

    @Test
    void shouldUpdate(){
        SecurityClearance securityClearance = new SecurityClearance();
//        securityClearance.setSecurityClearanceId(2);
//        assertEquals(securityClearance, repository.findById(2));
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName("test");
        assertTrue(repository.update(securityClearance));

        securityClearance.setSecurityClearanceId(20);
        assertFalse(repository.update(securityClearance));
    }

    @Test
    void shouldDelete(){
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(2));
    }
}