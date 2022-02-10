<?php
ini_set("mail.log", "/tmp/mail.log");
ini_set("mail.add_x_header", TRUE);

$to = "david95950@gmail.com";
$subject = $_POST['firstname']." - ".$_POST['country'];
$txt = $_POST['subject'];
$headers = array("From: ".$_POST['correo'],
    "Reply-To: ".$to,
    "X-Mailer: PHP/" . PHP_VERSION
);
$headers = implode("\r\n", $headers);
echo mail($to,$subject,$txt,$headers)?"Enviado.\n":"Lo sentimos, no se pudo enviar.\n";
?>  
