<?php
require_once '../includes/DBManipulation.php';
$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['u_email']) and isset($_POST['old_upword']) and isset($_POST['new_upword'])) { // Making sure that the right method is requested.
    $db = new DBManipulation();
    if ($db->userLogin($_POST['u_email'], $_POST['old_upword'])) { // We pass email and password to userLogin which will find if there is such matching pair, if there is then we can change the user old password.

      $db_response = $db->updatePassword($_POST['u_email'], $_POST['new_upword']);

      if ($db_response == 1) {
        $response['error']   = false;
        $response['message'] = "Password was changed successfully, please login";
      }
      elseif($db_response == -1)
      {
        $response['error']   = true;
        $response['message'] = "Email does not exist, please enter a valid email.";
      }
      elseif($db_response == 0)
      {
        $response['error']   = true;
        $response['message'] = "Some error occurred while tryng to access the database, please try again";
      }
    } else {
      $response['error']   = true;
      $response['message'] = "The provided password does not match your email";
    }

  } else { // If we get here 2nd if statement never ran, which means we are missing one of the fields.
    $response['error']   = true;
    $response['message'] = "Required fields are missing";
  }
}
echo json_encode($response);
