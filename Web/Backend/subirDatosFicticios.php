<?php
include 'conexion.php';

$arrayLatitud = [];
$arrayLongitud = [];
$arrayValores = [];

ejecutarComandoSQL("INSERT INTO `lecturas`(`dia`, `hora`, `ubicacion`, `valor`, `idMagnitud`, `idUSuario`) VALUES ("12/01/2021", "12:00", $ubicacion, $valor, "SO2")");

?>
