# Get IP's and ATTEMPTS number made during '2017-01-01.13:00:00' and '2017-01-01.14:00:00' and been these attempts more or equals to 100
SELECT counter.ip,counter.attempts FROM (SELECT ip,count(ip) AS attempts FROM requests WHERE date BETWEEN '2017-01-01.13:00:00' AND DATE_ADD('2017-01-01.13:00:00', INTERVAL 1 HOUR) GROUP BY ip) counter WHERE counter.attempts >= 200;

# Get requests from ip 192.168.102.136
SELECT * FROM requests WHERE ip='192.168.102.136';
