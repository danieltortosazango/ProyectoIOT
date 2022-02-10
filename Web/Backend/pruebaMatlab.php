<?php
     $command = "matlab -nojvm -nosplash -nodesktop -wait -r \"x = interpolar();\" 2>&1";
     //$output=exec($command);
     //$output;
     //exec($command, $output, $retval);
     //print_r($output)
         
         
         
    function exec_callback($command, $callback){
    $array = array();
    exec($command, $array, $ret);
        echo("Estoy fuera");
    if(!empty($array)){
        echo("estoy dentro");
        foreach ($array as $line){
            echo($line);
            
        }
    }
}

// example to use
function print_lines($line){
    echo "> $line\n";
}

exec_callback("dir", 'print_lines');
?>