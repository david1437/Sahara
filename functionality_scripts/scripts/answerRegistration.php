<?php
  // Script that does the registration of an user answer from the app. -Diego Fabiano
  // Script Reviewed the 9/7/2017 after incorporating email encryption. 

require_once '../includes/DBManipulation.php';

$response = array(); // Initialize array to display the response.

if($_SERVER['REQUEST_METHOD']=='POST'){ // If statement makes sure that the App is calling the right method.
    if( isset($_POST['message_id']) and isset($_POST['question']) and isset($_POST['answer']) ) { // If we received email, question, and answer then  we may proceed.
        $db = new DBManipulation();

        $dbfeedback2 = $db ->getResponseValueId($_POST['answer']);
        date_default_timezone_set('EST5EDT');
        $timestamp = date('Y-m-d G:i:s');
        $dbfeedback = $db->importUserAnswer( $_POST['message_id'], $dbfeedback2['id'], $timestamp); // If we got 3 parameters we will attempt to create a new user.

        if($dbfeedback == 1){ // importUserAnswer returns 1 on success.
            $response['error'] = false;
            $response['message'] = "Answer was stored successfully";
            $response['id'] = $dbfeedback2['id'];
        }elseif($dbfeedback == 2){ //Unknown error happened while trying to create answer in the database. importUserAnswer returns 2 when error occured.
            $response['error'] = true;
            $response['message'] = "Some error occurred while trying to update the response";
        }
    }

    else{  // If we got here it means that we did not get all 3 parameters.
    $response['error'] = true;
    $response['message'] = "Missing parameters";
    }
}else { // If we got here it means we did not pushed the right method from the app.
     $response['error'] = true;
    $response['message'] = "Invalid method was requested by the app";
}

echo json_encode($response);
