<?php
  // Script that updates information of an user. -Diego Fabiano

require_once '../includes/DBManipulation.php';


$response = array();

// Making sure that the right method is requested.
if($_SERVER['REQUEST_METHOD']=='POST'){
    if(isset($_POST['u_email']) && isset($_POST['p_recid']) && isset($_POST['pr_recid'])) {
        $db = new DBManipulation();
        /*
        date_default_timezone_set('EST5EDT');
        $timestamp = date('Y-m-d G:i:s');
        */
        // attempting to create user.
        $db_response = $db->addToCart($_POST['u_email'], $_POST['p_recid'], $_POST['pr_recid']);
        if($db_response == 1){
          $response['error'] = false;
          $response['message'] = "Item added to shopping cart!";
        } else if($db_response == -1){
          $response['error'] = true;
          $response['message'] = "Item couldnt be added!";
        } else {
          $response['error'] = true;
          $response['message'] = "Some error occurred while tryng to access the database, please try again";
        }
    } else {
	  $response['error'] = true;
          $response['message'] = "Make sure to send an email, p_recid, and pr_recid";
    }
}else{  // If we got here it means we did not pushed the ewsright method from the app.
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
echo json_encode($response);
