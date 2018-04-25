<?php
require_once '../includes/DBManipulation.php';
$response = array();

// Making sure that the right method is requested.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  $db = new DBManipulation();

  $db_response          = $db->getCategories();
  $response['error']    = false;
  $response['message']  = "Products fetched successfully!";
  $response['data'] = $db_response;
} else { 
  $response['error']   = true;
  $response['message'] = "Invalid Request";
}
echo json_encode($response);
