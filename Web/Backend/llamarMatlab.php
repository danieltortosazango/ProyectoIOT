<?php 
// Get current working directory
// magicSquare.m exists in this directory

//El arxivo .m debe estar en la misma carpeta que este php
$pwd = getcwd();
echo $pwd;
//$versionMatlab='R2018a';
// Set command. Please use -r option and remember to add exit in the last 
$latitud = array(38.996622, 38.9976738, 39.000028, 39.0029207);
$longitud = array(-0.166156, -0.1639319, -0.160959, -0.188148);
$valor = array(1,3,5,3);
$latitud2 = [38.996622, 38.9976738, 39.000028, 39.0029207];
$longitud2= [-0.166156, -0.1639319, -0.160959, -0.188148];
$valor2 = [1,3,5,3];
$array = serialize($latitud);
$array2 = serialize($longitud);
$array3 = serialize($valor);
//Hay que cambiar la version de matlab por la vuestra
//Cambiar "magicSquare" por el nombre de la funcion interpolación
//$cmd = 'C:\Program Files\MATLAB\R2018a\bin\matlab -automation -sd ' . $pwd . ' -r interpolar(1,2,3)';
//$cmd= "matlab -nojvm -nodesktop -nodisplay -r interpolar("array","array2","array3")";
//$cmd= "matlab -nojvm -nodesktop -nodisplay -r interpolar('".$array."','".$array2."','".$array3."')";
$cmd= "matlab -nojvm -nodesktop -nodisplay -r cogerBaseDeDatos('14/01/2021')";
// exec
$res = exec($cmd, $output, $return);
//var_dump($res, $output, $return);
//$last_line = exec($cmd, $output, $retval);
//passthru($cmd, $return)
//var_dump($last_line, $output, $retval);
//echo var_dump($last_line, $output, $retval);
//echo $cmd;
//echo $last_line;
//$lista= array($output);
//echo $return;
//echo $retval;
//echo $retval;
/*foreach ($output as $line) {
    echo "$line\n";
}*/
/*if ($retval == 1 || $retval== 0){
  // Read from CSV file which MATLAB has created
    
  //Archivo con los valores devueltos de matlab
  $lines = file('result.csv');
  echo '<p>Results:<br>';
  foreach($lines as $line)
  {
    echo $line.'<br>';
  }
  echo '</p>';
} else {
  // When command failed
  echo '<p>Failed</p>';
}*/
?>

