package com.hacker.rank.november.real.data.challenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * The CBSE Class 12 examination, is taken by Indian high school students at the end of K-12 school education. The scores or 
 * grades in this examination form the basis of their entry to the College or University system, for an undergraduate program. 
 * At the K-12 level, students appear for examination in five subjects. These five subjects generally include one language; 
 * three elective subjects oriented towards Science, Commerce or Humanities; and any elective of their choice as a fifth subject.
 * <br/>
 * <li/>The Challenge
 *
 * <br/>This challenge is based on real school data of the CBSE Class 12 examination conducted in the year 2013. You are given 
 * the grades obtained by students with specific but popular combinations of subjects (and all these students had opted for 
 * Mathematics). Their grades in four subjects are known to you. However their grade in Mathematics (i.e, the fifth subject) is hidden.
 *
 * The records provided to you are the grades obtained by students who had opted for the following combinations of subjects or courses 
 * and obtained a passing grade in each subject.
 *
 * <li/>English, Physics, Chemistry, Mathematics, Computer Science    
 * <li/>English, Physics, Chemistry, Mathematics, Physical Education    
 * <li/>English, Physics, Chemistry, Mathematics, Economics    
 * <li/>English, Physics, Chemistry, Mathematics, Biology  
 * <li/>English, Economics, Accountancy, Mathematics, Business Studies  
 * 
 * <br/>
 * The grades of students in four subjects (other than Mathematics) are provided to you. Can you predict what grade they had obtained in Mathematics?
 *
 * To help you build a prediction engine, we will provide you with a training file, containing the grade points obtained by students with the above 
 * subject combinations, in all five subjects.
 * 
 * @author Vivek Singh
 * @version 1
 * 
 */
public class PredictMissingGradeChallenge {

	//~ static variable declaration // ================================================================================
	private static final String fileName = "training.json";
	
	//~ main method starts here
	public static void main(String...strings) throws IOException, JSONParseException {
		// open the file
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
		
		// get the number of entries from the file. Represent the number of data available in the file using which a record has to be maintained
		int dataCount = Integer.parseInt(bufferedReader.readLine());
		
	    // iterate from the next line
		while (dataCount-- > 0) {
			Map<Subject, Integer> mappedParser = JSONParser.parseLine(bufferedReader.readLine());
			
		}
		
		bufferedReader.close();
	}
}

/**
 * JSONParser class will take a string object and extract the record from that string object and returns the map containing the data
 * 
 * @author Vivek
 */
class JSONParser {
	/**
	 * regex definition for a line
	 */
	private static final String REGEX = "[{}\"]";
	
	/**
	 * static parseLine method will take the string object and parse the object and returns a meaningful data in a map 
	 */
	public static Map<Subject, Integer> parseLine (String record) throws JSONParseException {
		// holds the Subject and its corresponding marks
		Map<Subject, Integer> subjectMarksMapper = new HashMap<Subject, Integer>();
		
		record = record.replaceAll(REGEX, " ").trim(); // replace all regex char
		
		String[] entries = record.split(","); // split the string
		
		// iterate
		for (String entry : entries) {
			String[] combinations = entry.split(":");
			
			Subject subject = Subject.getSubjectEnum(combinations[0].trim());
			int marks = Integer.parseInt(combinations[1].trim());
			
			subjectMarksMapper.put(subject, marks);
		}
		return subjectMarksMapper;
	}
}

/**
 * JSONParseException class will record the exception caught while parsing the line from the file 
 * 
 * @author Vivek
 */
@SuppressWarnings("serial")
class JSONParseException extends Exception {
	/**
	 * default serial_id
	 */
	private static final long serialVersionUID = 6445207844786171116L;
	
	/**
	 * exception message
	 */
	private String message;
	
	/**
	 * default constructor
	 */
	public JSONParseException(String message) {
		super(message);
		this.message = message;
	}
	
	/**
	 * returns the message
	 */
	public String getMessage() {
		return this.message;
	}
}
/**
 * Subject enum enumerates the subjects
 * 
 * @author Vivek
 */
enum Subject {
	/**
	 * enum constants
	 */
	ENGLISH ("English"), PHYSICS ("Physics"), CHEMISTRY ("Chemistry"), MATHEMATICS ("Mathematics"), COMPUTER_SCIENCE ("Computer Science"), 
	PHYSICAL_EDUCATION ("Physical Education"), ECONOMICS ("Economics"), BIOLOGY ("Biology"), ACCOUNTANCY ("Accountancy"), BUSINESS_STUDIES ("Business Studies");
	
	/**
	 * string that represents an enum constants
	 */
	private String subject;
	
	/**
	 * default constructor
	 */
	private Subject (String subject) {
		this.subject = subject;
	}
	
	/**
	 * returns the enum constants against its string value
	 */
	public static Subject getSubjectEnum (String subject) {
		if (subject == null) return null;
		
		for (Subject subject2 : Subject.values()) {
			if (subject2.getSubject().equals(subject.trim())) {
				return subject2;
			}
		}
		return null;
	}
	
	/**
	 * returns the string value
	 */
	public String getSubject() {
		return this.subject;
	}
}

/**
 * PlotDataGraph will plot the data coming from the file and create a mapper to make prediction
 * 
 * @author Vivek
 */
class PlotDataGraph {
	
}