<?php
$con = mysql_connect("localhost","dk47","dk47");
//$con = mysql_connect("localhost","root","student");
mysql_select_db("appliance");

if (!empty($_POST)) {
 
$usr = $_POST["username"];

        //$q=mysql_query("SELECT * FROM appliance_data WHERE appname = '$appname'");
        $q=mysql_query("SELECT `AppName`, `ScheduledStartTime`, `ScheduledEndTime`, `Cost`, `PreViewCost`, `PreViewStartTime`, `PreViewEndTime` FROM `appdetail` WHERE `username`='$usr'");



        while($e = mysql_fetch_assoc($q))
            $output[]=$e;     
 
echo json_encode($output);


mysql_close();


}
else {

	?>

	        <h1>Login</h1>

	        <form action="getlist.php" method="post">

	            Username:<br />

	            <input type="text" name="username" placeholder="username" />

	            <br /><br />

	           


	            <input type="submit" value="Login" />

	        </form>

	        <a href="register.php">Register</a>

	    <?php

	}

	 

	?> 

