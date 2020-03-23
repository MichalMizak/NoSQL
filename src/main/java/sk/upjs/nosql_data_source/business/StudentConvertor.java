package sk.upjs.nosql_data_source.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import de.undercouch.bson4jackson.BsonFactory;
import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.entity.Student;
import sk.upjs.nosql_data_source.entity.StudijnyProgram;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;

public class StudentConvertor {

	private StudentDao dao = DaoFactory.INSTANCE.getStudentDao();
	private List<Student> students = dao.getAll();
	private List<SimpleStudent> simpleStudents = dao.getSimpleStudents();

	public String getJSONFromObject(Object o, boolean format) {
		ObjectMapper mapper = new ObjectMapper();
		if (format)
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getBSONFromObject(Object o) {
		ObjectMapper mapper = new ObjectMapper(new BsonFactory());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			mapper.writeValue(outputStream, o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

	public String getXMLFromObject(Object o, boolean format) {
		XmlMapper xmlMapper = new XmlMapper();
		if (format)
			xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		try {
			return xmlMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getYAMLFromObject(Object o, boolean format) {
		YAMLMapper mapper = new YAMLMapper();
		if (format)
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getCSVFromObject(Object o, boolean withHeader) {
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = mapper.schemaFor(o.getClass());

		if (withHeader) {
			schema = schema.withHeader();
		}
		try {
			return mapper.writer(schema).writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getStudentsJSON(boolean format) {
		return students.stream().map(s -> getJSONFromObject(s, format)).collect(Collectors.toList());
	}

	private List<byte[]> getStudentsBSON() {
		return students.stream().map(this::getBSONFromObject).collect(Collectors.toList());
	}

	private List<String> getStudentsXML(boolean format) {
		return students.stream().map(s -> getXMLFromObject(s, format)).collect(Collectors.toList());
	}

	private List<String> getStudentsYAML(boolean format) {
		return students.stream().map(s -> getYAMLFromObject(s, format)).collect(Collectors.toList());
	}

	private String getSimpleStudentsCSV(boolean withHeader) {
		StringBuilder result = new StringBuilder();
		simpleStudents.forEach(sp -> result.append(getCSVFromObject(sp, withHeader)));
		return result.toString();
	}

	private String getStudyProgramCSV(boolean withHeader) {
		List<StudijnyProgram> studijneProgramy = students.stream()
				.flatMap(s -> s.getStudium().stream().map(studium -> studium.getStudijnyProgram()))
				.collect(Collectors.toList());
		StringBuilder result = new StringBuilder();
		studijneProgramy.forEach(sp -> result.append(getCSVFromObject(sp, withHeader)));
		return result.toString();
	}

	public static void main(String[] args) throws JsonProcessingException {
		StudentConvertor sc = new StudentConvertor();
		System.out.println(sc.getSimpleStudentsCSV(true));

		/*
		 * System.out.println("BSON:"); StudentDao dao =
		 * DaoFactory.INSTANCE.getStudentDao(); List<Student> students = dao.getAll();
		 */

		// System.out.println(sc.getBSONFromObject(students));

	}

}
