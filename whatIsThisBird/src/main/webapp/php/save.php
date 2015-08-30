<?php

if($_SERVER['REQUEST_METHOD'] == "POST") {

	$img = str_replace('data:image/'.$_POST['type'].';base64,', '', $_POST['string']);
	$img = str_replace(' ', '+', $img);

	// logic to determine file name
	$filename = 'test';

	file_put_contents($filename . '.' . $_POST['type'], base64_decode($img));

}