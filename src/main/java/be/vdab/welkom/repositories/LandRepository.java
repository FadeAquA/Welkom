package be.vdab.welkom.repositories;

import be.vdab.welkom.domain.Land;
import be.vdab.welkom.exceptions.RepositoryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class LandRepository {

    private final String pad;

    public LandRepository(@Value("${landenCsvPad}") String pad) {
        this.pad = pad;
    }

    public List<Land> findAll() {
        try (var stream = Files.lines(Path.of(pad))) {
            return stream.map(regel -> {
                var onderdelen = regel.split(",");
                return new Land(onderdelen[0], onderdelen[1], Integer.parseInt(onderdelen[2]));
            }).toList();
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            throw new RepositoryException(ex);
        }
    }


}
