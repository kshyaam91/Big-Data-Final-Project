<?php
//$con = mysql_connect("localhost","root","student");
$con = mysql_connect("localhost","dk47","dk47");

if (!$con){ die('Could not connect: ' . mysql_error());}


if (!empty($_POST)) {

if (mysql_select_db('appliance', $con)) {
	//mysql_select_db('appliance', $con));
	$usr = $_POST["username"];
	$app = $_POST["appname"];
	$energy = $_POST["energy"];
	$optime = $_POST["optime"];
	$starttime = $_POST["starttime"];
	$deadline = $_POST["deadline"];
	$constraint = $_POST["constraints"];
	
	$q = mysql_query("SELECT EXISTS(SELECT * FROM appdetail WHERE AppName = '$app')");
	//$g =  mysql_query("SELECT EXISTS(SELECT * FROM userlist WHERE email = '$email')");
	
	if(mysql_result($q,0) == 1)
		print("false_user");
	//else if (mysql_result($g,0) ==1)
		//print("false_email");
	else {

	$result = mysql_query("INSERT INTO appdetail (username, AppName, Energy, OperationTime, UserStartTime, UserEndTime, ConstraintType)
		VALUES ('$usr','$app','$energy','$optime','$starttime', '$deadline','$constraint')");

		if($result)
			print("true");
		else
			print("false");
	}
}	
mysql_close();


}

else {

	?>

	        <h1>Login</h1>

	        <form action="addappliance.php" method="post">

	            Username:<br />

	            <input type="text" name="username" placeholder="username" />

	            <br /><br />

	            Appname:<br />

	            <input type="text" name="appname" placeholder="appname" value="" />

	            <br /><br />


energy:<br />

	            <input type="text" name="energy" placeholder="energy" value="" />

	            <br /><br />
optime:<br />

	            <input type="text" name="optime" placeholder="optime" value="" />

	            <br /><br />
starttime:<br />

	            <input type="text" name="starttime" placeholder="starttime" value="" />

	            <br /><br />
deadline:<br />

	            <input type="text" name="deadline" placeholder="deadline" value="" />

	            <br /><br />
constraints:<br />

	            <input type="text" name="constraints" placeholder="constraints" value="" />

	            <br /><br />


	            <input type="submit" value="Login" />

	        </form>

	        <a href="register.php">Register</a>

	    <?php

	}

	 

	?> 


