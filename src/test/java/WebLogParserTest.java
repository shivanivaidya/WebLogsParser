import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import java.net.URL;

import java.io.IOException;
import java.io.File;

public class WebLogParserTest {
	String SRC_IP_1 = "93.79.202.178";
	String FILENAME_1 = new File("src/test/resources/Test1.txt").toURI().toString();
	
	String SUBNET_1 = "180.76.15.0/24";
	String FILENAME_2 = new File("src/test/resources/Test2.txt").toURI().toString();
	
	ArrayList<String> EMPTY = new ArrayList<String>();
	
	ArrayList<String> SRC_IP_LOGS_1 = new ArrayList<String>() {{
    	add("93.79.202.178 - - [02/Jun/2015:17:05:41 -0700] ");
    	add("93.79.202.178 - - [02/Jun/2015:17:05:43 -0700] ");
    	add("93.79.202.178 - - [02/Jun/2015:17:05:44 -0700] ");
    	add("93.79.202.178 - - [02/Jun/2015:17:05:44 -0700] ");
	}};
	
	ArrayList<String> SUBNET_LOGS_1 = new ArrayList<String>() {{
    	add("180.76.15.20 - - [02/Jun/2015:17:23:05 -0700] ");
    	add("180.76.15.15 - - [02/Jun/2015:17:35:23 -0700] ");
    	add("180.76.15.146 - - [02/Jun/2015:17:50:23 -0700] ");
    	add("180.76.15.27 - - [02/Jun/2015:18:05:23 -0700] ");
    	add("180.76.15.138 - - [02/Jun/2015:18:20:23 -0700] ");
    	add("180.76.15.152 - - [02/Jun/2015:18:32:17 -0700] ");
	}};
	String INVALID_IP1 = "256.256.256.256";
	
	String[] VALID_ARGS1 = {"--ip", SRC_IP_1};
	String[] VALID_ARGS2 = {"--ip", SUBNET_1};
	String[] INVALID_ARGS1 = {"--ip"};
	String[] INVALID_ARGS2 = {"--ip", INVALID_IP1};
	
	@Test
	public void testGetSourceIpLogs() throws IOException {
		WebLogParser parser = new WebLogParser();
		ArrayList<String> actualSourceIpLogs = parser.getSourceIpLogs(SRC_IP_1, FILENAME_1);
		assertEquals(SRC_IP_LOGS_1, actualSourceIpLogs);
	}
	
	@Test
	public void testGetSourceIpLogs_NoEntriesFound() throws IOException {
		WebLogParser parser = new WebLogParser();
		ArrayList<String> actualSourceIpLogs = parser.getSourceIpLogs(SRC_IP_1, FILENAME_2);
		assertEquals(EMPTY, actualSourceIpLogs);
	}
	
	@Test
	public void testGetSubnetLogs() throws IOException {
		WebLogParser parser = new WebLogParser();
		ArrayList<String> actualSubnetLogs = parser.getSubnetLogs(SUBNET_1, FILENAME_2);
		assertEquals(SUBNET_LOGS_1, actualSubnetLogs);
	}
	
	@Test
	public void testGetSubnetLogs_NoEntriesFound() throws IOException {
		WebLogParser parser = new WebLogParser();
		ArrayList<String> actualSubnetLogs = parser.getSubnetLogs(SUBNET_1, FILENAME_1);
		assertEquals(EMPTY, actualSubnetLogs);
	}
	
	@Test
	public void testGetLogs_WithValidSourceIp() throws IOException {
		WebLogParser parser = new WebLogParser();
		ArrayList<String> actualSourceIpLogs = parser.getLogs(VALID_ARGS1, FILENAME_1);
		assertEquals(SRC_IP_LOGS_1, actualSourceIpLogs);
	}
	
	@Test
	public void testGetLogs_WithValidSubnet() throws IOException {
		WebLogParser parser = new WebLogParser();
		ArrayList<String> actualSubnetLogs = parser.getLogs(VALID_ARGS2, FILENAME_2);
		assertEquals(SUBNET_LOGS_1, actualSubnetLogs);
	}
}