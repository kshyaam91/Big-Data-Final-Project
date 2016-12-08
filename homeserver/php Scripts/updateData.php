<?php
$con = mysql_connect("localhost","dk47","dk47");
//$con = mysql_connect("localhost","root","student");
mysql_select_db("appliance");
 




       
$q=mysql_query("SELECT * FROM appdetail");
       

 while ($row = mysql_fetch_assoc($q)) {

echo $row > newfile.txt; 

}


mysql_close();




?>

	        
