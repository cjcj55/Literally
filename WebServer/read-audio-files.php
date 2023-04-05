<?php
require_once 'connect.php';

// Set the upload directory
$uploadDir = 'uploads/';

// Get the user_id parameter from the function argument
$user_id = $_POST['user_id'];

// Query the database for audio files with the given user_id
$sql = "SELECT * FROM audio_clips WHERE user_id='$user_id'";
$result = mysqli_query($conn, $sql);

// Create an array to store the audio files
$audioFiles = array();

// Loop through the audio files in the database and add them to the array
while ($row = mysqli_fetch_assoc($result)) {
    $fileName = $row['filename'];
    $filePath = $row['filepath'];
    $fullPath = $uploadDir . $filePath;
    $audioData = base64_encode(file_get_contents($fullPath));
    $audioFile = array(
        'fileName' => $fileName,
        'audioData' => $audioData
    );
    array_push($audioFiles, $audioFile);
}

// Send the array of audio files as JSON
header('Content-Type: application/json');
echo json_encode($audioFiles);
?>
