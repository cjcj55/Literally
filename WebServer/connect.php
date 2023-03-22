<?php

//ini_set('display_errors', 1);
//error_reporting(E_ALL);

$dbName = "Literally";
$u = "root";
$p = "literally123";

if ($conn = mysqli_connect("ip-172-31-14-70.us-east-2.compute.internal", $u, $p, $dbName)) {
    echo "Connection established to ${dbName}";
}

?>