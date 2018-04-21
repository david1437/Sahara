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
          // If user is already in the database, then we don't want to override if this happens we return 0.
            if($this->doesUserExist($u_email)){
                return -1;
            }else{
                $password = $this->USF_encrypt($u_pword);
                $statement = $this->connection->prepare("INSERT INTO users (u_email, u_pword, ut_recid) VALUES (?, ?, 1);"); // If the user doesn't exist we will try to create it.
                $statement->bind_param("ss",$u_email,$password);
                if($statement->execute()){ // If statement executed we need a way to know that it did, therefore we return 1.
                    return 1;
                }else{
                    return 0; // Also a way to know it did not, therefore we return 2.
                }
            }
        }
        // update user profile
        public function updateUserProfile(&$parameters, &$u_email){
          // If user is NOT in the database, then we can't change anything
            if($this->doesUserExist($u_email) == 0){
              return -1;
            }else{
              // need to get parameters
              foreach ($parameters as $key => $value)
              {
                $attribute = $key;
                $attribute_value = $value;
                $statement = $this->connection->prepare("UPDATE users SET ? = ? WHERE u_email = ?"); // If the user doesn't exist we will try to create it.
                $statement->bind_param("sss",$attribute,$attribute_value,$u_email);
                // in case query fails
                if(!$statement->execute())
                {
                  return 0;
                }
              }
              // If we got here all queries were successfull.
              return 1;
            }
        }

 		// userLogin function searches for matching parameters (email, password) in the database.
         public function userLogin(&$u_email, &$u_pword){
            $password = $this->USF_encrypt($u_pword);

            $statement = $this->connection->prepare("SELECT u_recid FROM users WHERE u_email = ? AND u_pword = ?");
            $statement->bind_param("ss", $u_email,$password);
            $statement->execute();
            $statement->store_result();
            return $statement->num_rows > 0;
        }

 		// getUserByEmail finds a particular email for the user that we are looking for and returns all the information related to that email.
        public function getUserByEmail(&$u_email){
            $statement = $this->connection->prepare("SELECT * FROM users WHERE u_email = ?");
            $statement->bind_param("s",$u_email);
            $statement->execute();
            return $statement->get_result()->fetch_assoc();
        }
        private function getu_recid(&$u_email){
          $statement = $this->connection->prepare("SELECT u_recid FROM users WHERE u_email = ?");
          $statement->bind_param("s",$response);
          $statement->execute();
          return $statement->get_result()->fetch_assoc();
        }
 		// doesUserExist checks if an user is already in the database by email.
        private function doesUserExist(&$email){
            $statement = $this->connection->prepare("SELECT u_recid FROM users WHERE u_email = ?");
            $statement->bind_param("s", $email);
            $statement->execute();
            $statement->store_result();
            return $statement->num_rows > 0;
        }

        public function updatePassword(&$u_email, &$u_pword){
          if(!($this->doesUserExist($u_email))) { // If user is not in the database, then we return 0.
            return -1;
          } else {
            $password = $this->USF_encrypt($u_pword);
            $statement = $this->connection->prepare("UPDATE users SET u_pword = ? WHERE u_email = ?");
            $statement->bind_param("ss", $password, $u_email);
            if($statement->execute()){ // If statement executed we need a way to know that it did, therefore we return 1.
                 return 1;
            }else{
                return 0;
            }
          }
        }
        public function getShoppingCart(&$u_email)
        {
          // If user is not in the database, then we return 0.
          if(!($this->doesUserExist($u_email))) {
            return -1;
          } else {
            $urecid = getu_recid($u_email);
            $statement = $this->connection->prepare("SELECT * FROM shopping_cart WHERE u_recid = ?");
            $statement->bind_param("s", $urecid);
            $statement->execute();
            return $statement->get_result()->fetch_assoc();
          }
        }
        public function getShoppingHistory(&$u_email)
        {
          // If user is not in the database, then we return 0.
          if(!($this->doesUserExist($u_email))) {
            return -1;
          } else {
            $urecid = getu_recid($u_email);
            $statement = $this->connection->prepare("SELECT * FROM purchase_history WHERE u_recid = ?");
            $statement->bind_param("s", $urecid);
            $statement->execute();
            return $statement->get_result()->fetch_assoc();
          }
        }
        public function getShippingTypeInfo(&$st_recid)
        {
          $statement = $this->connection->prepare("SELECT * FROM shipping_type WHERE st_recid = ?");
          $statement->bind_param("s", $st_recid);
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
