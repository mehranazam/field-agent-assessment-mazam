package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgentService {

    private final AgentRepository repository;

    private final AliasRepository aliasRepository;

    public AgentService(AgentRepository repository, AliasRepository aliasRepository) {
        this.repository = repository;
        this.aliasRepository = aliasRepository;
    }

    public List<Agent> findAll() {
        return repository.findAll();
    }

    public Agent findById(int agentId) {
        return hydrate(repository.findById(agentId));
    }



    public Agent hydrate(Agent toHydrate){
        List<Alias> agentAliases = aliasRepository.getAliasesByAgentId(toHydrate.getAgentId());
        toHydrate.setAliases(agentAliases);

        return toHydrate;
    }



    public Result<Agent> add(Agent agent) {
        Result<Agent> result = validate(agent);
        if (!result.isSuccess()) {
            return result;
        }

        if (agent.getAgentId() != 0) {
            result.addMessage("agentId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        agent = repository.add(agent);
        result.setPayload(agent);
        return result;
    }

    public Result<Agent> update(Agent agent) {
        Result<Agent> result = validate(agent);
        if (!result.isSuccess()) {
            return result;
        }

        if (agent.getAgentId() <= 0) {
            result.addMessage("agentId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(agent)) {
            String msg = String.format("agentId: %s, not found", agent.getAgentId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int agentId) {
        return repository.deleteById(agentId);
    }

    private Result<Agent> validate(Agent agent) {
        Result<Agent> result = new Result<>();
        if (agent == null) {
            result.addMessage("agent cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(agent.getFirstName())) {
            result.addMessage("firstName is required", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(agent.getLastName())) {
            result.addMessage("lastName is required", ResultType.INVALID);
        }

        if (agent.getDob() != null && agent.getDob().isAfter(LocalDate.now().minusYears(12))) {
            result.addMessage("agents younger than 12 are not allowed", ResultType.INVALID);
        }

        if (agent.getHeightInInches() < 36 || agent.getHeightInInches() > 96) {
            result.addMessage("height must be between 36 and 96 inches", ResultType.INVALID);
        }

        return result;
    }



}
