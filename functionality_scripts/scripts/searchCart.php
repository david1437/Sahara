<?php
require_once '../includes/DBManipulation.php';
$response = array();

// Making sure that the right method is requested.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['query']) && isset($_POST['u_email'])) {
    $db = new DBManipulation();
    /*
        	date_default_timezone_set('EST5EDT');
        	$timestamp = date('Y-m-d G:i:s');
        	*/
    // attempting to create user.
    $db_response          = $db->searchCart($_POST['u_email'], $_POST['query']);
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
