< ? php

  // Script that does the validation of an user. -Diego Fabiano

  require_once '../includes/DBManipulation.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (isset($_POST['u_email']) and isset($_POST['u_pword'])) {
    $db = new DBManipulation();
    // We pass email and password to userLogin which will find if there is such matching pair.
    if ($db->userLogin($_POST['u_email'], $_POST['u_pword'])) {
      // If there was a matching pair, get all needed information, this might be needed to be added upon for more data.
      $user                  = $db->getUserByEmail($_POST['u_email']);
      $response['error']     = false;
      $response['user_data'] = $user;
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
