<?php

  // Script that does the validation of an user. -Diego Fabiano

  require_once '../includes/DBManipulation.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['pr_name']) and isset($_POST['pr_pword'])) {
    $db = new DBManipulation();
    // We pass email and password to userLogin which will find if there is such matching pair.
    if ($db->producerLogin($_POST['pr_name'], $_POST['pr_pword'])) {
      $producer                  = $db->getProducerByEmail($_POST['pr_name']);
      $response['error']     = false;
      $response['message'] = "Log in Successfull!";
      $response['producer_data'] = $producer;
    } else {
      // If we get here it means that input was a wrong combination.
      $response['error']   = true;
      $response['message'] = "Invalid email or password";
    }
  } else {
    $response['error']   = true;
    $response['message'] = "Required fields are missing";
  }
}
echo json_encode($response);
