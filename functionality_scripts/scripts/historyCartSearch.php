<?php
require_once '../includes/DBManipulation.php';
$response = array();

// Making sure that the right method is requested.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['query']) && isset($_POST['u_email']) && isset($_POST['sort'])) {
    $db = new DBManipulation();

    $db_response          = $db->searchHistoryCart($_POST['u_email'], $_POST['query'], $_POST['sort']);
    $response['error']    = false;
    $response['message']  = "Products fetched successfully!";
    $response['products'] = $db_response;
  } else {
    $response['error']   = true;
    $response['message'] = "String must be passed!";
  }
} else { // If we got here it means we did not pushed the ewsright method from the app.
  $response['error']   = true;
  $response['message'] = "Invalid Request";
}
echo json_encode($response);
