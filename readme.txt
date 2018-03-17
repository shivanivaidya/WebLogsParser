This application reads the log file available at https://s3.amazonaws.com/syseng-challenge/public_access.log.txt 
uses the command line script called weblog_helper to return some specific log lines as follows:

1) To return all log lines that correspond to a given source IP address:

$ ./weblog_helper --ip 178.93.28.59

(--ip <IP>) restricts the output to the given IP address.

2) To return all log lines that correspond to a given IP CIDR network ( e.g. 180.76.15.0/24):

(--ip <IP>) restricts the output to the given subnet.

$ ./weblog_helper --ip 180.76.15.0/24