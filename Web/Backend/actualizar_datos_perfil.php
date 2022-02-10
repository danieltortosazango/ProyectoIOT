<?php
include 'conexion.php';
$idUsuario=$_POST['idUsuario'];
$contrasenya=$_POST['contrasenya'];
$ciudad=$_POST['Ciudad'];

/*$idUsuario="nachonachete@gmail.com";
$contrasenya=123456789;
$ciudad="La vila Joiosa";*/

$sentencia=$conexion->prepare("UPDATE `usuarios` SET `idUsuario`='$idUsuario',`contrasenya`='$contrasenya',`Ciudad`='$ciudad' WHERE idUsuario= '$idUsuario'");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}
$sentencia->bind_param('sssis',$idUsuario,$nombre,$apellidos,$contrasenya,$ciudad);
$sentencia->execute();

$resultado = $sentencia->get_result();

$sentencia->close();
$conexion->close();
?>
