<?php
include 'conexion.php';


//Sentencia sql para consultar la distancia recorridos en 1 dia
$sentencia=$conexion->prepare("SELECT valor FROM `lecturasestacion` ORDER BY `lecturasestacion`.`dia` DESC");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
?>
