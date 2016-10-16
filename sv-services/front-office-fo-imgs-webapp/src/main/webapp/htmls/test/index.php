<?php
require_once 'facebook.php';

$appapikey = 'd89664a0c8d57967fe148bd4a2ffbaf7';
$appsecret = 'ef59e661bd95215d640437b0d74cb9ec';
$facebook = new Facebook($appapikey, $appsecret);
$user = $facebook->require_login();

$appcallbackurl = '[Callback URL]';

//catch the exception that gets thrown if the cookie has an invalid session_key in it
try {
  if (!$facebook->api_client->users_isAppAdded()) {
    $facebook->redirect($facebook->get_add_url());
  }
} catch (Exception $ex) {
  //this will clear cookies for your application and redirect them to a login prompt
  $facebook->set_user(null, null);
  $facebook->redirect($appcallbackurl);
}
?>

<h1>
  Hola Mundo!
</h1>

