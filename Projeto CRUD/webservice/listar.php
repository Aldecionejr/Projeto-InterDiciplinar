<?php
require "conn.php";

$sql = "SELECT * FROM crud";

$res = mysqli_query($mysqli, $sql);

while ($row = mysqli_fetch_assoc($res)) {
    $arrayUsuario[] = array(
                         "id"  => $row['id'],
                         "nome" => $row['nome'],
						 "usuario" => $row['usuario'],
						 "senha" => $row['senha']
        );
}

    echo json_encode($arrayUsuario);

?>