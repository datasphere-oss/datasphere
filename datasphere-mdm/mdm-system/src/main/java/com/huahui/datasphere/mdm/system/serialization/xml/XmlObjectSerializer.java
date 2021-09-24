package com.huahui.datasphere.mdm.system.serialization.xml;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * Serialization of objects.
 * Singleton storing a customized serializer
 *
 * @author theseusyang
 * @since 29.07.2020
 **/
public class XmlObjectSerializer {
    /**
     * Data deserialization error text template
     */
    private static final String DESERIALIZATION_ERROR_MESSAGE = "Object deserialization error '%s': %s\n%s";
    /**
     * Data serialization error text template
     */
    private static final String SERIALIZATION_ERROR_MESSAGE = "Object serialization error '%s': %s";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final String ZONED_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final int DATE_FORMAT_LENGTH = 10;

    private static final int DATE_TIME_FORMAT_LENGTH = 23;

    /**
     * UI LDT formatter - 3 places for millis.
     */
    private static final DateTimeFormatter UD_ISO_LOCAL_DATE_TIME = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .appendFraction(ChronoField.MILLI_OF_SECOND, 3, 3, true)
            .toFormatter();

    /**
     * Lazy initialized object instance
     */
    private static final XmlObjectSerializer INSTANCE;

    static {

        JacksonXmlModule module = new JacksonXmlModule();
        module.addSerializer(XMLGregorianCalendar.class, new XMLCalendarSerializer());
        module.addDeserializer(XMLGregorianCalendar.class, new XMLCalendarDeserializer());
        module.setDefaultUseWrapper(false);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(UD_ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.registerModule(javaTimeModule);

        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        xmlMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        xmlMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        xmlMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        INSTANCE = new XmlObjectSerializer(xmlMapper);
    }

    private final XmlMapper xmlMapper;

    private XmlObjectSerializer(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    /**
     * Deserialize object from xml string
     *
     * @param javaClass target type
     * @param xmlString xml value
     * @param <T> generic
     * @return Deserialized object
     */
    public <T> T fromXmlString(Class<T> javaClass, String xmlString) {
        Objects.requireNonNull(javaClass, "JavaClass is required");
        Objects.requireNonNull(xmlString, "XML string is required");
        try {
            return xmlMapper.readValue(xmlString, javaClass);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format(DESERIALIZATION_ERROR_MESSAGE, javaClass, ex.getLocalizedMessage(), xmlString), ex);
        }
    }

    /**
     * Deserialize object from xml string
     *
     * @param javaClass target type
     * @param inputStream data stream containing data in xml format
     * @param <T> generic
     * @throws IOException read data error
     */
    public <T> T fromXmlInputStream(Class<T> javaClass, InputStream inputStream) throws IOException {
        Objects.requireNonNull(javaClass, "JavaClass is required");
        Objects.requireNonNull(inputStream, "Input stream is required");
        return xmlMapper.readValue(inputStream, javaClass);
    }

    /**
     * Deserialize object from xml string
     *
     * @param javaClass target type
     * @param file file containing data in xml format
     * @param <T> generic
     * @throws IOException read data error
     */
    public <T> T fromXmlInputFile(Class<T> javaClass, File file) throws IOException {
        Objects.requireNonNull(javaClass, "JavaClass is required");
        Objects.requireNonNull(file, "File is required");
        return xmlMapper.readValue(file, javaClass);
    }


    /**
     * Serialize object to xml format
     *
     * @param value source object
     * @param prettyPrint use pretty print
     * @return xml value
     */
    public String toXmlString(Object value, boolean prettyPrint) {
        Objects.requireNonNull(value, "Value is required");
        String xml;
        try {
            if (prettyPrint) {
                ObjectWriter writer = xmlMapper.writerWithDefaultPrettyPrinter();
                xml = writer.writeValueAsString(value);
            } else {
                xml = xmlMapper.writeValueAsString(value);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format(SERIALIZATION_ERROR_MESSAGE, value, ex.getLocalizedMessage()), ex);
        }
        return xml;
    }

    public static XmlObjectSerializer getInstance() {
        return INSTANCE;
    }

    /**
     * Abstract datetime deserializer that supports multiple formats
     *
     * @param <T> value type
     */
    private abstract static class AbstractDateTimeDeserializer<T> extends StdScalarDeserializer<T> {

        private static final long serialVersionUID = 1L;

        protected AbstractDateTimeDeserializer(Class<T> vc) {
            super(vc);
        }

        protected abstract T parseWithFormat(String value, String format);

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String strValue = p.getValueAsString();
            int length = strValue.length();
            if (length < DATE_FORMAT_LENGTH || 'T' != strValue.charAt(DATE_FORMAT_LENGTH)) {
                return parseWithFormat(strValue.substring(0, DATE_FORMAT_LENGTH), DATE_FORMAT);
            } else if (length < DATE_TIME_FORMAT_LENGTH || '+' != strValue.charAt(DATE_TIME_FORMAT_LENGTH)) {
                return parseWithFormat(strValue.substring(0, DATE_TIME_FORMAT_LENGTH), DATETIME_FORMAT);
            } else {
                return parseWithFormat(strValue, ZONED_DATETIME_FORMAT);
            }
        }
    }

    private static class XMLCalendarDeserializer extends AbstractDateTimeDeserializer<XMLGregorianCalendar> {

        private static final long serialVersionUID = 1L;

        private final Map<String, SimpleDateFormat> formatters = new HashMap<>();

        private transient DatatypeFactory datatypeFactory;

        protected XMLCalendarDeserializer() {
            super(XMLGregorianCalendar.class);
        }

        @Override
        protected XMLGregorianCalendar parseWithFormat(String value, String format) {
            var formatter = formatters.computeIfAbsent(format, SimpleDateFormat::new);
            XMLGregorianCalendar result;
            try {
                var date = formatter.parse(value);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(date);
                if (datatypeFactory == null) {
                    datatypeFactory = DatatypeFactory.newInstance();
                }
                result = datatypeFactory.newXMLGregorianCalendar(cal);
            } catch (Exception e) {
                throw new IllegalArgumentException("Illegal date time value: " + value);
            }
            return result;
        }
    }

    private static class XMLCalendarSerializer extends StdSerializer<XMLGregorianCalendar> {

        private static final long serialVersionUID = 1L;

        public XMLCalendarSerializer() {
            super(XMLGregorianCalendar.class);
        }

        @Override
        public void serialize(XMLGregorianCalendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            DateFormat df = provider.getConfig().getDateFormat();
            if (df.getCalendar() == null) {
                jgen.writeString(value.toString());
            } else {
                SimpleDateFormat dateFormat = (SimpleDateFormat) df;
                Date date = value.toGregorianCalendar().getTime();
                dateFormat.setTimeZone(TimeZone.getTimeZone(value.getTimeZone(value.getTimezone()).getDisplayName()));
                jgen.writeString(dateFormat.format(date));
            }
        }
    }
}
