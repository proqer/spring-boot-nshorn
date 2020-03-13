package com.pragmasoft.study.controllers.rest;

import com.pragmasoft.study.model.ScriptModel;
import com.pragmasoft.study.services.NashornScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/scripts")
public class ScriptsController {

    private NashornScriptService nashornScriptService;

    //TODO add scriptDTO, list member script dto
    //TODO SONAR,
    //TODO interrupted exc
    //TODO is thread interrupted
    //TODO expl to result
    //TODO custom exception
    //TODO use one script engine with different context
    //TODO compile script before eval
    //TODO event mechanism
    //TODO check java 9 flow api
    //TODO to js file

    @Autowired
    public ScriptsController(NashornScriptService nashornScriptService) {
        this.nashornScriptService = nashornScriptService;
    }

    /**
     * Asynchronously execute given script
     *
     * @param code to be executed
     * @return response entity with created script model
     */
    @PostMapping
    public ResponseEntity<ScriptModel> addScript(@RequestBody String code) {
        ScriptModel createdScriptModel = nashornScriptService.addScript(code);
        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(ScriptsController.class).getScriptById(createdScriptModel.getId())).build();
        return ResponseEntity
                .accepted()
                .location(uriComponents.encode().toUri())
                .body(createdScriptModel);
    }

    /**
     * Get information about all scripts
     *
     * @return response entity with all existing scripts
     */
    @GetMapping
    public ResponseEntity<Iterable<ScriptModel>> getAllScripts() {
        return ResponseEntity
                .ok(nashornScriptService.getAllScripts());
    }

    /**
     * Get information about script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return script model by given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScriptModel> getScriptById(@PathVariable("id") String id) {
        return ResponseEntity
                .of(nashornScriptService.getScriptById(id));
    }

    /**
     * Stop and delete script by given id, returns 204 NO_CONTENT
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") String id) {
        if (!nashornScriptService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get status of script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text status
     */
    @GetMapping("/{id}/status")
    public String getScriptStatus(@PathVariable("id") String id) {
        return nashornScriptService.getScriptStatusById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Update status of script with given id. Stop execution of script
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     */
    @PostMapping("/{id}/stop")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stopScriptExecution(@PathVariable("id") String id) {
        if (!nashornScriptService.stopScriptExecutionById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get code of script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text code
     */
    @GetMapping("/{id}/code")
    public String getScriptCode(@PathVariable("id") String id) {
        return nashornScriptService.getScriptCodeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Get result of script with given id
     * If script with given id doesn't exist returns 404 NON_FOUND
     *
     * @param id of script
     * @return plane text result
     */
    @GetMapping("/{id}/result")
    public String getScriptResult(@PathVariable("id") String id) {
        return nashornScriptService.getScriptResultById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
