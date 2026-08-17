package com.example.school_management_system.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class PersistenceService {

    private final Path saveFile;
    private final ObjectMapper mapper;

    public PersistenceService(Path saveFile) {
        this.saveFile = saveFile;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(SchoolData data) throws IOException {
        Path parent = saveFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        mapper.writeValue(saveFile.toFile(), data);
    }
    public Optional<SchoolData> load() throws IOException {
        if (!Files.exists(saveFile)) {
            return Optional.empty();
        }
        return Optional.of(mapper.readValue(saveFile.toFile(), SchoolData.class));
    }

    public boolean saveFileExists() {
        return Files.exists(saveFile);
    }
}
