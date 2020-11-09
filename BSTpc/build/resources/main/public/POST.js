function upload() {
  //Toggle the overlay ("Congratulations, study uploaded.")
  document.getElementById("overlay").style.display = "block";

  //Fill in missing Javascript Object fields in order to send it to the server without error.
  var jsonFromForm = JSON.stringify($('form').serializeObject());
  const jsonToUpload = JSON.parse(jsonFromForm);
  var keywordString = document.getElementById("keywords").value;
  var keywordArray = keywordString.split(",");
  jsonToUpload.keywords = keywordArray;
  jsonToUpload.author = username;
  jsonToUpload.author_id = "1234";
  jsonToUpload.images = document.getElementById("outputImg").innerHTML;
  jsonToUpload.template = document.getElementById("user_json_document").value;
  jsonToUpload.institution = "SUNY Oswego Test Center";
  jsonToUpload.costInCredits = 20;
  jsonToUpload.num_stimuli = Number(jsonToUpload.num_stimuli);
  jsonToUpload.num_trials = Number(jsonToUpload.num_trials);
  jsonToUpload.num_responses = Number(jsonToUpload.num_responses);
  jsonToUpload.randomize = Boolean(jsonToUpload.randomize);
  jsonToUpload.duration = Number(jsonToUpload.duration);
  console.log(jsonToUpload);

  //Send the JSON to the database
    $.ajax({
      url: 'http://pi.cs.oswego.edu:12100/upload',
      headers: { 'token': token },
      type: 'POST',
      data: JSON.stringify(jsonToUpload),
      contentType: 'application/json; charset=utf-8',
      dataType: 'json',
      success: function(data) {
        console.log("Study uploaded.");
      }
    });
}