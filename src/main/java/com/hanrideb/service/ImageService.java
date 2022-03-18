package com.hanrideb.service;

import com.hanrideb.domain.ImageModel;
import com.hanrideb.repository.ImageModelRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final ImageModelRepository imageModelRepository;

    public ImageService(ImageModelRepository imageModelRepository) {
        this.imageModelRepository = imageModelRepository;
    }

    public ImageModel getByName(String name) {
        return imageModelRepository.findByName(name);
    }

    public ImageModel Save(ImageModel imageModel) {
        UUID uuid = UUID.randomUUID();
        imageModel.setName(uuid.toString());
        return imageModelRepository.save(imageModel);
    }

    public File getByNameFile(String name) throws IOException {
        ImageModel imageModel = imageModelRepository.findByName(name);
        String fileName = "../../resource/" + imageModel.getName() + imageModel.getImgContentType();

        File f = new File(fileName);
        byte[] fileContent = imageModel.getImg();

        Path path = Paths.get(f.getAbsolutePath());
        try {
            Files.write(path, fileContent);
        } catch (IOException ex) {
            System.out.println("dosya oluştuurken hata oluştu");
        }

        return f;
    }
}
