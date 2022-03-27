package learn.field_agent.controllers;


import learn.field_agent.domain.Result;
import learn.field_agent.domain.ResultType;
import learn.field_agent.domain.SecurityClearanceService;
import learn.field_agent.models.SecurityClearance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                         // 1. Spring DI and MVC
@RequestMapping("/security_clearances") // 2. Base URL
public class SecurityClearanceController {

    private final SecurityClearanceService service;

    // 3. Auto-inject SecurityClearanceService
    public SecurityClearanceController(SecurityClearanceService service) {
        this.service = service;
    }

    @GetMapping
    public List<SecurityClearance> findAll(){
        return service.findAll();
    }

    @GetMapping("/{securityClearanceId}")
    public ResponseEntity<SecurityClearance> findById(@PathVariable int securityClearanceId){
        SecurityClearance securityClearance = service.findById(securityClearanceId);
        if(securityClearance == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(securityClearance, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SecurityClearance> add(@RequestBody SecurityClearance securityClearance){
        Result<SecurityClearance> result = service.add(securityClearance);
        if (result.getType() == ResultType.INVALID) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
    }

    @PutMapping("/{securityClearanceId}")
    public ResponseEntity<Void> update(@PathVariable int securityClearanceId, @RequestBody SecurityClearance securityClearance){

        // id conflict. stop immediately.
        if(securityClearanceId != securityClearance.getSecurityClearanceId()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 4. ResultType -> HttpStatus
        Result<SecurityClearance> result = service.update(securityClearance);
        if (result.getType() == ResultType.INVALID) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (result.getType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{securityClearanceId}")
    public ResponseEntity<Void> delete(@PathVariable int securityClearanceId){
        if(service.deleteById(securityClearanceId)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
