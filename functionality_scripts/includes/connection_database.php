<?php

  // Script contains function that stablishes a connection with the database.
  // -Diego Fabiano
  class DBConnection
{
private
  $connection;
  function
  __construct()
  {
  }

  // connection instance
  function
  connection()
  {
    // Get database credentials
    include_once dirname(__FILE__).'/credentials_database.php';
    $this->connection = new mysqli(DATABASE_HOST, DATABASE_USER,
                                   DATABASE_PASSWORD, DATABASE_NAME);

    if (mysqli_connect_errno()) { // On error.
      echo "Connection to the database failed".mysqli_connect_err();
    }

    return $this->connection;
  }
}
