<?php
include 'conexion.php';
include 'functions.php';
if($resultset=getSQLResultSet("SELECT * FROM logros")){

        while($row = $resultset->fetch_array()){
            //echo json_encode($row);         
            $idLogro = $row['idLogro'];
            $dificultad = $row['dificultadCompletar']; 
            $nombre = $row['nombreLogro'];
            $imagen = $row['imagenLogro'];
            $tipo = $row['tipo'];
            // ...y cada variable es asignada a una propiedad y puesta en un array...
            $logros[] = array('idLogro'=> $idLogro, 'dificultadCompletar'=> $dificultad, 'nombreLogro'=> $nombre, 'imagenLogro'=> $imagen, 'tipo'=> $tipo); 
   } // while()
   //
   //
   //
   // ...para luego ser codificada en formato JSON y enviada como respuesta a la petición HTTP.
   echo json_encode($logros);
            
           
        }
    
?>
