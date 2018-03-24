<?php

 // Script that does the validation of an user. -Diego Fabiano

require_once '../includes/DBManipulation.php';

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

    if(isset($_POST['email']) and isset($_POST['password']) and isset($_POST['token'])){ // Making sure that the right method is requested.
        $db = new DBManipulation();
        if($db->userLogin($_POST['email'], $_POST['password'])){ // We pass email and password to userLogin which will find if there is such matching pair.

            $user = $db->getUserByEmail($_POST['email']); // If there was a matching pair we will call getUserByEmail, that way we can get all the information about that user.
            $response['error'] = false;
            $response['id'] = $user['id'];
            $response['email'] = $db->USF_decrypt($user['email']);
            $response['password'] = $user['password'];

             if((strcmp($_POST['token'],"null")) != 0){
                if((strcmp($_POST['token'],$user['token_id'])) == 0){
                       $response['error'] = false;
                       $response['message'] = "The token was not updated on user login";
                } else {
                      $db->refreshToken($user['email'],$_POST['token']);
                      $response['error'] = false;
                      $response['message'] = "The token has been updated on user login";

            }
        }

        }else{ // If we get here it means that input was a wrong combination.
            $response['error'] = true;
            $response['message'] = "Invalid email or password";
        }

    }else{ // If we get here 2nd if statement never ran, which means we are missing one of the fields.
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}
echo json_encode($response);
