<?php 
require "conn.php";

$id=$_GET["id"];
$nome=$_GET["nome"];
$usuario=$_GET["usuario"];
$senha=$_GET["senha"];
$sql="INSERT INTO crud (id,nome,usuario,senha) VALUES ('$id','$nome','$usuario','$senha')";

if (mysqli_query($mysqli, $sql)) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . mysqli_error($mysqli);
}
?>

