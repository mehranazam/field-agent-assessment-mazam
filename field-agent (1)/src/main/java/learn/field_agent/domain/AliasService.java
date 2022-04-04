package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {

    private final AliasRepository repository;
    private final AgentRepository agentRepository;

    public AliasService(AliasRepository repository, AgentRepository agentRepository) {
        this.repository = repository;
        this.agentRepository = agentRepository;
    }

    public Alias findById(int aliasId){
        return repository.findById(aliasId);
    }

    public Result<Alias> add(Alias alias){
        Result<Alias> result = validate(alias);
        if(!result.isSuccess()){
            return result;
        }

        if(alias.getAliasId() != 0){
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }



        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
    }


    public Result<Alias> update(Alias alias){
        Result<Alias> result = validate(alias);

        if(!result.isSuccess()){
            return result;
        }

        if(alias.getAliasId() != 0){
            result.addMessage("aliasId cannot be set for `update` operation", ResultType.INVALID);
            return result;
        }

       if(!repository.update(alias)){
           String msg = String.format("aliasId: %s, not found", alias.getAliasId());
           result.addMessage(msg, ResultType.NOT_FOUND);
       }
       repository.update(alias);
       result.setPayload(alias);

       return result;
    }

    public boolean deleteById(int aliasId){
        return repository.deleteById(aliasId);
    }



    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();

        if(alias == null){
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if(Validations.isNullOrBlank(alias.getName())){
            result.addMessage("name is required", ResultType.INVALID);
            return result;
        }


        List<Alias> matchingName = repository.getAliasesByName(alias.getName());
        if(matchingName.size() > 0){
            if(Validations.isNullOrBlank(alias.getPersona())){
                result.addMessage("Cannot add alias with duplicate name without persona", ResultType.INVALID);
                return result;
            }

            boolean anyAlreadyHavePersona = matchingName.stream().anyMatch(
                    a -> alias.getPersona().equals(a.getPersona())
            );
            if(anyAlreadyHavePersona){
                result.addMessage("Aliases with duplicate names must have unique personas.",
                        ResultType.INVALID);
                return result;
            }
        }

        Agent matchingAgent = agentRepository.findById(alias.getAgentId());

        if( matchingAgent == null ){
            result.addMessage( "Invalid agent id.", ResultType.INVALID );
            return result;
        }

        return result;
    }
}
