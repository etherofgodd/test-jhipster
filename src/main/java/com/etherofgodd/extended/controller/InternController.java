package com.etherofgodd.extended.controller;

import com.etherofgodd.domain.Intern;
import com.etherofgodd.extended.config.ApplicationUrl;
import com.etherofgodd.extended.service.impl.ExtendedInternServiceImpl;
import com.etherofgodd.service.dto.InternDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApplicationUrl.BASE_CONTEXT_URL)
public class InternController {
    private final ExtendedInternServiceImpl internService;

    @Autowired
    public InternController(ExtendedInternServiceImpl internService) {
        this.internService = internService;
    }

    @GetMapping(ApplicationUrl.INTERS_ALL)
    public ResponseEntity<List<Intern>> getAllInterns() {
        List<Intern> interns = internService.findAllInterns();
        return new ResponseEntity<>(interns, HttpStatus.OK);
    }

    @GetMapping(ApplicationUrl.GET_INTERNS_BY_ID)
    public ResponseEntity<List<InternDTO>> getInternsByInternId(@PathVariable String internId) {
        List<InternDTO> interns = internService.getInternsByInternId(internId);
        return new ResponseEntity<>(interns, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createIntern(@RequestBody @Valid InternDTO internDTO) {
        internService.createIntern(internDTO);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }


}
