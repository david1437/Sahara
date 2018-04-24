<?php
      require_once '../includes/DBManipulation.php';
$response = array();

// Making sure that the right method is requested.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['pr_name']) and isset($_POST['pr_pword'])) {
    $db = new DBManipulation();

    $db_response = $db->createProducer($_POST['pr_name'], $_POST['pr_pword']);
    if ($db_response == 1) {
      $response['error']   = false;
      $response['message'] = "Producer: ".$_POST['pr_name']." created successfully ";
    } else if ($db_response == -1) {
      $response['error']   = true;
      $response['message'] = "Producer is already in the database, please login";
    } else {
      $response['error']   = true;
      $response['message'] = "Some error occurred while tryng to access the database, please try again";
    }
  } else {
    //we don't have enough parameters
    $response['error']   = true;
    $response['message'] = "Make sure to enter both email and password";
  }
} else { // If we got here it means we did not pushed the ewsright method from the app.
  $response['error']   = true;
  $response['message'] = "Invalid Request";
}
echo json_encode($response);
