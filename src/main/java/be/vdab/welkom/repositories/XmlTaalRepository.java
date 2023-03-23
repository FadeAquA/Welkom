package be.vdab.welkom.repositories;

import be.vdab.welkom.domain.Taal;
import be.vdab.welkom.exceptions.RepositoryException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component @Qualifier("XML")
public class XmlTaalRepository implements TaalRepository {

    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private final String pad;

    public XmlTaalRepository(@Value("${talenXmlPad}") String pad) {
        this.pad = pad;
    }

    @Override
    public List<Taal> findAll() {
        List<Taal> talen = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(pad))) {
            var reader = factory.createXMLStreamReader(bufferedReader);
            while (reader.hasNext()) {
                reader.next();
                if (reader.isStartElement() && "taal".equals(reader.getLocalName())) {
                    var taalCode = reader.getAttributeValue(0);
                    var taalNaam = reader.getAttributeValue(1);
                    talen.add(new Taal(taalCode, taalNaam));
                }
            }
            return talen;
        } catch (IOException | XMLStreamException ex) {
            throw new RepositoryException(ex);
        }
    }
}
