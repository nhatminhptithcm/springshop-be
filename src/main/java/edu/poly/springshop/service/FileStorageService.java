package edu.poly.springshop.service;

import edu.poly.springshop.config.FileStorageProperties;
import edu.poly.springshop.exception.FileNotFoundException;
import edu.poly.springshop.exception.FileStorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileLogoStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileLogoStorageLocation = Paths.get(fileStorageProperties.getUploadLogoDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileLogoStorageLocation);
        } catch (Exception ex) {
            throw new FileNotFoundException("Could not create the directory where the uploaded files will be stored", ex);
        }
    }

    public String storageLogoFile(MultipartFile file) {
        return storeFile(fileLogoStorageLocation, file);
    }

    private String storeFile(Path location, MultipartFile file) {
        UUID uuid = UUID.randomUUID();

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uuid.toString() + "." + ext;

        try {
            if (filename.contains("")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }

            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (Exception ex) {
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", ex);
        }
    }

    public Resource loadLogoFileAsResource(String filename) {
        return loadFileAsResource(fileLogoStorageLocation, filename);
    }

    private Resource loadFileAsResource(Path location, String filename) {
        try {
            Path filePath = location.resolve(filename).normalize();

            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + filename);
            }
        } catch (Exception exception) {
            throw new FileNotFoundException("File not found " + filename, exception);
        }
    }

    public void deleteLogoFile(String filename) {
        deleteFile(fileLogoStorageLocation, filename);
    }

    private void deleteFile(Path location, String filename) {
        try {
            Path filePath = location.resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found " + filename);
            }
            Files.delete(filePath);
        } catch (Exception ex) {
            throw new FileNotFoundException("File not found " + filename, ex);
        }
    }
}
