package edu.poly.springshop.controller;

import edu.poly.springshop.domain.Manufacturer;
import edu.poly.springshop.dto.ManufacturerDto;
import edu.poly.springshop.service.FileStorageService;
import edu.poly.springshop.service.ManufaturerService;
import edu.poly.springshop.service.MapValidationErrorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {
    @Autowired
    private ManufaturerService manufaturerService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createManufacturer(@Valid @ModelAttribute ManufacturerDto dto,
                                                BindingResult result) {
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapvalidationFields(result);

        if (responseEntity != null) {
            return responseEntity;
        }

        Manufacturer entity = manufaturerService.insertManufacturer(dto);

        dto.setId(entity.getId());
        dto.setLogo(entity.getLogo());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}
