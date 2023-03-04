package edu.poly.springshop.service;

import edu.poly.springshop.domain.Manufacturer;
import edu.poly.springshop.dto.ManufacturerDto;
import edu.poly.springshop.exception.ManufacturerException;
import edu.poly.springshop.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufaturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Manufacturer insertManufacturer(ManufacturerDto dto) {
        List<?> foundList = manufacturerRepository.findByNameContainsIgnoreCase(dto.getName());

        if (foundList.size() > 0) {
            throw new ManufacturerException("Manufacturer name exist");
        }

        Manufacturer entity = new Manufacturer();

        BeanUtils.copyProperties(dto, entity);

        if (dto.getLogoFile() != null) {
            String filename = fileStorageService.storageLogoFile(dto.getLogoFile());

            entity.setLogo(filename);
            dto.setLogoFile(null);
        }

        return manufacturerRepository.save(entity);
    }
}
