package be.vdab.welkom.repositories;

import be.vdab.welkom.domain.Taal;
import be.vdab.welkom.exceptions.RepositoryException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component @Qualifier("CSV")
public class CsvTaalRepository implements TaalRepository {

    private final String pad;

    public CsvTaalRepository(@Value("${talenCsvPad}") String pad) {
        this.pad = pad;
    }

    @Override
    public List<Taal> findAll() {
        try (var stream = Files.lines(Path.of(pad))) {
            return stream.map(regel -> {
                var onderdelen = regel.split(",");
                return new Taal(onderdelen[0], onderdelen[1]);
            }).toList();
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            throw new RepositoryException(ex);
        }
    }
}
