<?php
require_once '../includes/DBManipulation.php';
$response = array();

// Making sure that the right method is requested.
if($_SERVER['REQUEST_METHOD']=='POST'){
  if(isset($_POST['u_email'])) {
        $db = new DBManipulation();
        // attempting to create user.
        $db_response = $db->buyCart($_POST['u_email']);
        $response['error'] = false;
        $response['message'] = "Cart bought succesfully!";
  } else {
    $response['error'] = true;
    $response['message'] = "Missing email";
  }
}else{  // If we got here it means we did not pushed the ewsright method from the app.
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
echo json_encode($response);