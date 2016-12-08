<?php
$con=mysql_connect("localhost","dk47","dk47");

mysql_select_db("appliance");

$usr = $_POST["username"];

$q=mysql_query("SELECT `Energy`, `ScheduledStartTime`, `ScheduledEndTime` FROM appdetail WHERE `username`='$usr'");
echo("Fetched querry....");

$handle = fopen('/home/hduser/tmp/newtimestamp2'.'.txt','w+');

while($row = mysql_fetch_assoc($q)) {
  fputs($handle, join(',', $row)."\n");
}

fclose($handle);
mysql_close();
?>
