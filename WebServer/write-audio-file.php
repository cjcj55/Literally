<?php
require_once 'connect.php';

$userId = $_POST['user_id'];
$targetDir = "uploads/";
$extension = pathinfo($_FILES['audio_file']['name'], PATHINFO_EXTENSION);
$fileName = bin2hex(random_bytes(8)) . ".$extension";
$targetFile = $targetDir . $fileName;
$uploadOk = 1;
$audioFileType = strtolower($extension);

// Define the upload directory and make sure it exists
if (!file_exists($targetDir)) {
    mkdir($targetDir, 0777, true);
}

// Check if file already exists
if (file_exists($targetFile)) {
    echo "Sorry, file already exists.";
    $uploadOk = 0;
}

// Allow certain file formats
if($audioFileType != "mp3" && $audioFileType != "wav") {
    echo "Sorry, only MP3 and WAV files are allowed.";
    $uploadOk = 0;
}

// Check if $uploadOk is set to 0 by an error
if ($uploadOk == 0) {
    echo "Sorry, your file was not uploaded.";
// if everything is ok, try to upload file
} else {
    if (move_uploaded_file($_FILES["audio_file"]["tmp_name"], $targetFile)) {
        // Save relative path of file to database
        $filePath = $targetDir . $fileName;
        $stmt = $conn->prepare("INSERT INTO audio_clips (user_id, time_said, filepath) VALUES (?, ?, ?)");
        $stmt->bind_param("is", $userId, NOW(), $filePath);
        $stmt->execute();
        $stmt->close();
        echo "The file has been uploaded as " . $fileName;
    } else {
        if ($_FILES["audio_file"]["error"] > 0) {
            echo "Sorry, there was an error uploading your file: " . $_FILES["audio_file"]["error"];
            $uploadOk = 0;
        }        
    }
}

$conn->close();
?>
