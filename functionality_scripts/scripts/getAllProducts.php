<?php
require_once '../includes/DBManipulation.php';
$response = array();

// Making sure that the right method is requested.
if($_SERVER['REQUEST_METHOD']=='GET'){
        $db = new DBManipulation();
        /*
        date_default_timezone_set('EST5EDT');
        $timestamp = date('Y-m-d G:i:s');
        */
        // attempting to create user.
        $db_response = $db->getProducts();
        $response['error'] = false;
        $response['message'] = "Products fetched successfully!";
	$response['products'] = $db_response;
}else{  // If we got here it means we did not pushed the ewsright method from the app.
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}
echo json_encode($response);
