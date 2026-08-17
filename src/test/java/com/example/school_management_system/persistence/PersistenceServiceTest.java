package com.example.school_management_system.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceServiceTest {

    @Test
    void loadReturnsEmptyWhenFileDoesNotExist(@TempDir Path tempDir) throws IOException {
        PersistenceService service = new PersistenceService(tempDir.resolve("nonexistent.json"));

        Optional<SchoolData> loaded = service.load();

        assertTrue(loaded.isEmpty());
    }

    @Test
    void savedDataCanBeLoadedBackUnchanged(@TempDir Path tempDir) throws IOException {
        PersistenceService service = new PersistenceService(tempDir.resolve("test-data.json"));

        SchoolData original = new SchoolData(
                "Test School",
                List.of(
                        new SchoolData.StudentData("S1", "Alice", "alice@example.com"),
                        new SchoolData.StudentData("S2", "Bob", "bob@example.com")
                ),
                List.of(
                        new SchoolData.CourseData("C1", "Math", 5)
                ),
                List.of(
                        new SchoolData.Enrollment("S1", "C1"),
                        new SchoolData.Enrollment("S2", "C1")
                )
        );

        service.save(original);
        Optional<SchoolData> loaded = service.load();

        assertTrue(loaded.isPresent());
        assertEquals(original, loaded.get());
    }

    @Test
    void saveFileExistsReflectsReality(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("data.json");
        PersistenceService service = new PersistenceService(file);

        assertFalse(service.saveFileExists(), "Should not exist before save");

        SchoolData empty = new SchoolData("Empty", List.of(), List.of(), List.of());
        service.save(empty);

        assertTrue(service.saveFileExists(), "Should exist after save");
    }
}
