<?php
  // Script that updates information of an user. -Diego Fabiano
  require_once '../includes/DBManipulation.php';

$response = array();
  function addProduct(&$pr_name, &$p_price, &$c_category, &$p_name, &$quantity)

// Making sure that the right method is requested.
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['pr_name']) && isset($_POST['p_price']) && isset($_POST['c_category']) && isset($_POST['p_name']) && isset($_POST['quantity'])) {
    $db = new DBManipulation();

    $db_response = $db->addProduct($_POST['pr_name'], $_POST['p_price'], $_POST['c_category'], $_POST['p_name'], $_POST['quantity']);

    if ($db_response == 1) {
      $response['error']   = false;
      $response['message'] = "Product has been added";
    } else if ($db_response == -1) {
      $response['error']   = true;
      $response['message'] = "Problem while adding product";
    }
  } else {
    $response['error']   = true;
    $response['message'] = "Make sure to send an email, p_recid, and pr_recid";
  }
} else { // If we got here it means we did not pushed the ewsright method from the app.
  $response['error']   = true;
  $response['message'] = "Invalid Request";
}
echo json_encode($response);
