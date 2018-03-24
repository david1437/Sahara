<?php

 // Script defines basic database manipulations necessery to run the app. -Diego Fabiano

    class DBManipulation{ // Connects with the database.

        private $connection;

        function __construct(){
            require_once dirname(__FILE__).'/connection_database.php'; // Calls connection script
            $db = new DBConnection(); // New database connection
            $this->connection = $db->connection();

        }

        // create a new user in the database.
        public function createUser(&$u_email, &$u_pword){
            if($this->doesUserExist($u_email)){ // If user is already in the database, then we don't want to override if this happens we return 0.
                return -1;
            }else{
                $password = $this->USF_encrypt($u_pword);
                $statement = $this->connection->prepare("INSERT INTO user (u_email, u_pword, ut_recid) VALUES (?, ?, 0);"); // If the user doesn't exist we will try to create it.
                $statement->bind_param("ss",$u_email,$password);
                if($statement->execute()){ // If statement executed we need a way to know that it did, therefore we return 1.
                    return 1;
                }else{
                    return 0; // Also a way to know it did not, therefore we return 2.
                }
            }
        }

        public function getQuestionId($user_id, $generated_time){
          $statement = $this->connection->prepare("SELECT * FROM question WHERE user_id = ? AND generated_time = ?");
          $statement->bind_param("ss",$user_id,$generated_time);
          $statement->execute();
          return $statement->get_result()->fetch_assoc();
        }

 		// userLogin function searches for matching parameters (email, password) in the database.
         public function userLogin(&$email, &$pass){
            $password = md5($pass);
            $email_secure = $this->USF_encrypt($email);
            $statement = $this->connection->prepare("SELECT id FROM user WHERE email = ? AND password = ?");
            $statement->bind_param("ss", $email_secure,$password);
            $statement->execute();
            $statement->store_result();
            return $statement->num_rows > 0;
        }

 		// getUserByEmail finds a particular email for the user that we are looking for and returns all the information related to that email.
        public function getUserByEmail(&$email){
            $email_secure = $this->USF_encrypt($email);
            $statement = $this->connection->prepare("SELECT * FROM user WHERE email = ?");
            $statement->bind_param("s",$email_secure);
            $statement->execute();
            return $statement->get_result()->fetch_assoc();
        }

        // getUserByUserId finds a particular id for the user that we are looking for and returns all the information related to that user id.
        public function getUserByUserId(&$id){
            $statement = $this->connection->prepare("SELECT * FROM user WHERE id = ?");
            $statement->bind_param("s",$id);
            $statement->execute();
            return $statement->get_result()->fetch_assoc();
        }

       public function updateNotificationStatus(&$question_id){
            $statement = $this->connection->prepare("UPDATE question SET  needs_notification = (0) WHERE id = (?);");
            $statement->bind_param("i", $question_id);
            if($statement->execute()){ // If statement executed we need a way to know that it did, therefore we return 1.
                    return 1;
                }else{
                    return 2; // Also a way to know it did not, therefore we return 2.
                }
        }

 		// doesUserExist checks if an user is already in the database by email.
        private function doesUserExist(&$email){
            $statement = $this->connection->prepare("SELECT id FROM user WHERE email = ?");
            $statement->bind_param("s", $email);
            $statement->execute();
            $statement->store_result();
            return $statement->num_rows > 0;
        }

        // If we want to send a specific push, then we need a specific token and we use email as identifier... This function returns the token related to that user.
        public function getToken(&$email){
        	$statement = $this->connection->prepare("SELECT token_id FROM user WHERE email = ?");
        	$statement->bind_param("s", $email);
        	$statement->execute();
        	$response = $statement->get_result()->fetch_assoc();
        	return array($response['token_id']);
        }

        // Getting all the information from all users enrolled in the app. This function is not yet used and was made for future functionalities that the App will potentially have.
 		   public function getAllUsers(){
            $statement = $this->connection->prepare("SELECT * FROM user");
            $statement->execute();
            $response = $statement->get_result();
            return $response;
        }

        // getResponseValueId gets the id of an specific response in the response_value table
        public function getResponseValueId(&$response){
            $statement = $this->connection->prepare("SELECT id FROM response_value WHERE value = (?)");
            $statement->bind_param("s",$response);
            $statement->execute();
            return $statement->get_result()->fetch_assoc();
        }


        public function USF_decrypt(&$string) {
            $output = false;
            $encrypt_method = "AES-256-CBC";
            $secret_key = KEY;
            $secret_iv = IV;
            $second_secret_key = md5(md5(KEY_SECONDARY));
            $key = hash('sha256', $secret_key);
            // iv - encrypt method AES-256-CBC expects 16 bytes.
            $iv = substr(hash('sha256', $secret_iv), 0, 16);

            $output = str_replace($second_secret_key, '', $string);
            $output = openssl_decrypt(base64_decode(base64_decode($output)), $encrypt_method, $key, 0, $iv);

            return $output;
          }

        public function USF_encrypt(&$string){
          $output = false;
          $encrypt_method = "AES-256-CBC";
          $secret_key = KEY;
          $secret_iv = IV;
          $second_secret_key = md5(md5(KEY_SECONDARY));
          $key = hash('sha256', $secret_key);
          // iv - encrypt method AES-256-CBC expects 16 bytes.
          $iv = substr(hash('sha256', $secret_iv), 0, 16);

          $output = openssl_encrypt($string, $encrypt_method, $key, 0, $iv);
          $output = base64_encode(base64_encode($output));
          $position = strlen($output)/2;
          $output = substr($output, 0, $position) . $second_secret_key . substr($output, $position);
          return $output;
        }
}
