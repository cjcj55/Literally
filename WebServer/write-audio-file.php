<?php
require_once 'connect.php';

// Set the upload directory
$uploadDir = 'uploads/';

// Get the user_id parameter from the function argument
$user_id = $_POST['user_id'];

// Get the uploaded file information
$uploadedFile = $_FILES['audio'];

// Generate a unique file name for the uploaded file
$fileName = bin2hex(random_bytes(10)) . '.mp3';

// Generate the full file path
$filePath = $uploadDir . $fileName;

// Move the uploaded file to the upload directory with the generated file name
if (move_uploaded_file($uploadedFile['tmp_name'], $filePath)) {
    // Insert the file path into the database if the file was uploaded successfully
    $dbUploadDir = '/var/www/' . $filePath;
    $sql = "INSERT INTO audio_clips (user_id, filepath) VALUES ('$user_id', '$dbUploadDir')";
    $result = mysqli_query($conn, $sql);

    if (!$result) {
        echo "MySQL Error: " . mysqli_error($conn);
    }
    
    if ($result) {
        echo "File uploaded and saved to database.";
    } else {
        echo "Error saving file to database.";
    }
} else {
    // Return an error message if the file could not be uploaded
    echo "Error uploading file.";
}
?>
