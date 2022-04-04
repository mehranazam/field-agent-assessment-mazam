package learn.field_agent.domain;


import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Register the service with the Spring DI context.
public class SecurityClearanceService {

    private final SecurityClearanceRepository repository;


    // A concrete SecurityClearanceRepository will be injected here.
    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    public List<SecurityClearance> findAll(){
        return repository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId){
        return repository.findById(securityClearanceId);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance){
        Result<SecurityClearance> result = validate(securityClearance);
        if(result.getType() != ResultType.SUCCESS){
            return result;
        }

        SecurityClearance sc = repository.add(securityClearance);
        result.setPayload(sc);
        return result;
    }


    public Result<SecurityClearance> update(SecurityClearance securityClearance){
        Result<SecurityClearance> result = validate(securityClearance);
        if(result.getType() != ResultType.SUCCESS){
            return result;
        }

        if(!repository.update(securityClearance)){
            result.addMessage("security clearance id " + securityClearance.getSecurityClearanceId() + " not found", ResultType.NOT_FOUND);
        }
        return result;
    }


    public boolean deleteById(int securityClearanceId) {
        return repository.deleteById(securityClearanceId);
    }


    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();

        if(securityClearance == null){
            result.addMessage("security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if(securityClearance.getName() == null || securityClearance.getName().trim().length() == 0){
            result.addMessage("name is required", ResultType.INVALID);
        }

        return result;
    }
}
