<?php
  // Script that updates information of an user. -Diego Fabiano

require_once '../includes/DBManipulation.php';


$response = array();

// Making sure that the right method is requested.
if($_SERVER['REQUEST_METHOD']=='POST'){
    if(isset($_POST['u_email'])) {
        $db = new DBManipulation();
        /*
        date_default_timezone_set('EST5EDT');
        $timestamp = date('Y-m-d G:i:s');
        */
        // attempting to create user.
        $db_response = $db->updateUserProfile($_POST['u_email'],$_POST['parameters']);
        if($db_response == 1){
          $response['error'] = false;
          $response['message'] = "User: " . $_POST['e_email'] . " updated successfully ";
        } else if($db_response == -1){
          $response['error'] = true;
          $response['message'] = "User is not in the database, please register the user";
        } else {
          $response['error'] = true;
          $response['message'] = "Some error occurred while tryng to access the database, please try again";
        }
    }else{
        //we don't have enough parameters
        $response['error'] = true;
        $response['message'] = "Make sure to provide email";
    }
}else{  // If we got here it means we did not pushed the ewsright method from the app.
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
echo json_encode($response);
