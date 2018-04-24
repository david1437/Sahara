<?php
require_once '../includes/DBManipulation.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['st_recid'])) { // Making sure that the right method is requested.
    $db                = new DBManipulation();
    $db_response       = $db->getShippingTypeInfo($_POST['st_recid']);
    $response['error'] = false;
    $response['data']  = $db_response;
  } else {
    $response['error']   = true;
    $response['message'] = "Required fields are missing";
  }
}
echo json_encode($response);
