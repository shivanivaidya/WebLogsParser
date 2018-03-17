import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;

import org.apache.commons.net.util.SubnetUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

class WebLogParser {
	// Pattern to check for valid IP addresses and IP CIDR networks.
	Pattern ipv4 = Pattern.compile("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])");
	
	public static void main(String[] args) throws IOException {
		WebLogParser parser = new WebLogParser();
		String fileName = "https://s3.amazonaws.com/syseng-challenge/public_access.log.txt";
		ArrayList<String> logs = parser.getLogs(args, fileName);
		parser.printLogs(logs);
	}
	
	// Prints each log in the ArrayList of logs.
	public void printLogs(ArrayList<String> logs) {
		for(String log : logs) {
			System.out.println(log);
		}
	}
	
	// Returns true if the input parameter is a valid IP address or IP CIDR network.
	public boolean isValidIpOrSubnet(String ip) {
		Matcher matcher = ipv4.matcher(ip);
		return matcher.find();
	}
	
	// Prints the correct usage of the web_helper utility.
	public void printUsage() {
		System.out.println("\nUsage: ./weblog_helper --ip <IP>" + "\n" + "IP: A valid source IP address or IP CIDR network\n");
	}
	
	/**Calls the appropriate method depending on whether the input to the --ip switch is 
	   - a valid IP address
	   - a valid IP CIDR network.
	   It calls the method to print usage of the web_helper utility if the inputs are invalid. **/ 
	public ArrayList<String> getLogs(String[] args, String fileName) throws IOException{
		ArrayList<String> logs = new ArrayList<String>();
		if(args.length == 2 && args[0].equals("--ip")) {
			if(isValidIpOrSubnet(args[1])) {
				if(args[1].contains("/")) {
					logs = getSubnetLogs(args[1], fileName);
				}
				else {
					logs = getSourceIpLogs(args[1], fileName);
				}
			}
			else {
				printUsage();
			}
		}
		else {
			printUsage();
		}
		return logs;
	}
	
	// Returns the ArrayList of logs containing the log lines from the log file corresponding to the sourceIp.	
	public ArrayList<String> getSourceIpLogs(String sourceIp, String fileName) throws IOException {
		URL logFile = new URL(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(logFile.openStream()));
		ArrayList<String> sourceIpLogs = new ArrayList<String>();
		
		String line; 
		while((line = br.readLine()) != null) {
			if(line.trim().startsWith(sourceIp)) {
				sourceIpLogs.add(line);
			}
		}
		br.close();
		return sourceIpLogs;
	}
	
	// Returns the ArrayList of logs containing the log lines from the log file corresponding to IP addresses in the given subnet.
	public ArrayList<String> getSubnetLogs(String subnet, String fileName) throws IOException {
		SubnetUtils utils = new SubnetUtils(subnet);
		
		URL logFile = new URL(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(logFile.openStream()));
		
		ArrayList<String> subnetLogs = new ArrayList<String>();
		
		String line; 
		while((line = br.readLine()) != null) {
			Matcher matcher = ipv4.matcher(line);
			if(matcher.find()) {
				if(utils.getInfo().isInRange(matcher.group())) {
					subnetLogs.add(line);
				}
			}
		}
		br.close();
		return subnetLogs;
	}
}