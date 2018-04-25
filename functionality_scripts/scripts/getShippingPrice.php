<?php
require_once '../includes/DBManipulation.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['u_email'])) { // Making sure that the right method is requested.
    $db                = new DBManipulation();
    $db_response       = $db->getShippingPrice($_POST['u_email']);
    if($db_response == -1)
    {
    	$response['error']   = true;
    	$response['message'] = "User not in the database";
    } else if($db_response == 0){
    	$response['error']   = true;
    	$response['message'] = "Problem while accesing the database";
    } else {
    	$response['error'] = false;
    	$response['data']  = $db_response;
    }
  } else {
    $response['error']   = true;
    $response['message'] = "Required fields are missing";
  }
}
echo json_encode($response);
