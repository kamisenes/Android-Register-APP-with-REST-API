<?php
//kayit ol
require_once("db.php");

$name = $_POST['name'];
$surname = $_POST['surname'];
$email = $_POST['email'];
$password = $_POST['password'];



$sql = "SELECT * FROM users WHERE users.email = :email";
$checkLogin = $db->prepare($sql);
$checkLogin->bindParam(":email", $email);
$checkLogin->execute();

$fetch = $checkLogin->fetch();
//at least 1 row
if ($fetch) {
    //hata
    $response['error'] = "Email is already used";
    echo json_encode($response);
    exit;
 }
 else {

    $sql = "INSERT INTO 
    users (email, password, name,surname)
    values (?, ?, ?,?";

    $insert = $db->prepare($sql);
    $insExec = $insert->execute([$email, $password, $name,$surname]);

    if ($insExec) { 
        $response['succes'] = true;
        $response['message'] = "Register successful";
        echo json_encode($response);
        exit;
    }
    else {
        //hata
        $response['error'] = "System error";
        echo json_encode($response);
        exit;
    }

 }
