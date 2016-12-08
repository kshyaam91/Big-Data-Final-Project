<?php
//$con=mysql_connect("localhost","root","12020210749343");
$con=mysql_connect("localhost","dk47","dk47");
//$con = mysql_connect("localhost","root","student");
mysql_select_db("appliance");

$usr = $_POST["username"];




		//$q=mysql_query("SELECT * FROM appliance_data WHERE appname = '$appname'");
		$q=mysql_query("SELECT * FROM appdetail WHERE username='$usr'");
		echo("Querry Fired...\n");
		echo("value...\n");

$handle = fopen('/home/deekshith/tmp/newtimestamp1'.'.txt','w+');
echo("file opened...\n");

//mysql_fetch_assoc is the key here, don't use mysql_fetch_array as it creates double results
while($row = mysql_fetch_assoc($q)) {
  fputs($handle, join(',', $row)."\n");
}

fclose($handle);

mysql_close();
//----------------------wait here till o/p is not available------------------//

$outfile = '/home/deekshith/tmp/hadoop-output.txt';
unlink($outfile);

while (!file_exists($outfile)) 
	{
		sleep(2);
	}

sleep(4);

/*---------------------script to update the db---------------------------------*/
$handle = fopen("/home/deekshith/tmp/hadoop-output.txt", "r");

echo("file opened succesfully....");

$conn = mysql_connect("localhost","dk47","dk47"); 

mysql_select_db("appliance");

while (($data = fgetcsv($handle, ",")) !== FALSE)
{
echo("fetching record....");
$sql = "UPDATE appliance.appdetail SET PreViewCost='$data[7]',PreViewStartTime='$data[5]',PreViewEndTime='$data[6]' WHERE AppName='$data[0]'";  
mysql_query($sql,$conn) or die(mysql_error());
if (mysql_query($sql)){
//echo "$data[12] \n";
print("positive");
}
}

?>


