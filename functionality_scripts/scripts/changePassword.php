<?php

 // Script that does changes an user password on request. -Diego Fabiano
   // Script Reviewed the 9/7/2017 after incorporating email encryption.

require_once '../includes/DBManipulation.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    if(isset($_POST['email']) and isset($_POST['old_password']) and isset($_POST['new_password'])){ // Making sure that the right method is requested.
        $db = new DBManipulation(); 
        if($db->userLogin($_POST['email'], $_POST['old_password'])){ // We pass email and password to userLogin which will find if there is such matching pair, if there is then we can change the user old password.

          $dbfeedback = $db->updatePassword($_POST['email'], $_POST['new_password']);

          if($dbfeedback == 1){
            $response['error'] = false;
            $response['message'] = "Password was changed successfully, please login";
          }elseif($dbfeedback == 0){
            $response['error'] = true;
            $response['message'] = "Email does not exist, please enter a valid email.";

          }elseif($dbfeedback == 2){ //Unknown error happened while trying to create user in the database.
            $response['error'] = true;
            $response['message'] = "Some error occurred please try again";
        }

        }else{ // If we get here it means that input was a wrong combination.
            $response['error'] = true;
            $response['message'] = "The provided password does not match your email";
        }

    }else{ // If we get here 2nd if statement never ran, which means we are missing one of the fields.
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}
echo json_encode($response);
