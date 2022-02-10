<?php
include('conexion.php');
include('functions.php');
$dia=$_GET['dia'];

//$dia="11-Jan-2021";
//echo $dia;
if($resultset=getSQLResultSet("SELECT latitud, longitud, valor FROM `interpolacion` WHERE dia='$dia'")){

    while($row = $resultset->fetch_array()){
        //echo json_encode($row);         
        $latitud= $row['latitud'];
       
        $longitud= $row['longitud'];
        $valor= $row['valor'];
        
        $array[] = array('lat'=> $latitud, 'lng'=> $longitud, 'count'=> $valor);
        //$arrayLatitud[] = array('latitud'=> $latitud);
        //$arrayLongitud[] = array('longitud'=> $longitud);
        //$arrayValores[] = array('valor'=> $valor);
        
       }
    echo json_encode($array);
}
    ?>