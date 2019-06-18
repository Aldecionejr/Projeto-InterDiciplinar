<?php
$servidor = '';
$usuario = '';
$senha = '';
$banco = '';

$mysqli = mysqli_connect($servidor, $usuario, $senha, $banco);

if (mysqli_connect_errno()) {die('Não foi possível conectar-se ao banco de dados: '. mysqli_connect_error());
exit();
}



?>