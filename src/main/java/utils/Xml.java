package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Xml {
    public static Object deserialize(Class clazz, String path) throws IOException {
        XStream xStream = new XStream(new StaxDriver());
        xStream.processAnnotations(clazz);
        return xStream.fromXML(new String(Files.readAllBytes(Paths.get(path)), "UTF-8"));
    }

    public static Object getParsedEntity(Class entityClass, String source, String... omitFields) {
        XStream xStream = new XStream(new StaxDriver());
        for (String omitField : omitFields) {
            xStream.omitField(entityClass, omitField);
        }
        xStream.processAnnotations(entityClass);
        return xStream.fromXML(source);
    }

    public static void serialize(Object object, String path) throws IOException {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(object.getClass());
        Writer writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-16");
        writer.write("<?xml version=\"1.0\" encoding =\"UTF-8\"?>\n");
        xStream.toXML(object, writer);
    }

    public static String serialize(Object object) {
        XStream xStream = new XStream(
                new DomDriver() {
                    public HierarchicalStreamWriter createWriter(Writer out) {
                        return new SimpleWriter(out);
                    }
                });
        xStream.processAnnotations(object.getClass());
        return xStream.toXML(object);
    }

    public static class SimpleWriter extends PrettyPrintWriter {
        public SimpleWriter(Writer writer) {
            super(writer);
        }

        protected void writeText(QuickWriter writer, String text) {
            writer.write(text);
        }
    }
}

