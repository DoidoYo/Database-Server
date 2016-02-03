// Toggle Function
$('.toggle').click(function(){
  // Switches the Icon
  $(this).children('i').toggleClass('fa-pencil');
  // Switches the forms  
  $('.form').animate({
    height: "toggle",
    'padding-top': 'toggle',
    'padding-bottom': 'toggle',
    opacity: "toggle"
  }, "slow");
});

function loginFunction()
{
	var t = true;
	
	$(".l").each(function() {
		if($(this).val() == "") { //gets text value from elements
			alert("Please Fill In Every Textbox");
			t = false;
			return false; //breaks out of each loop
		}
	});
	
	if(t){
		var msg = '';
		var msg = "login?";
		$(".l").each(function() {
			msg+=$(this).attr('name');
			msg+="=";
			msg+=$(this).val();
			msg+="&"
		});
		var s = msg;
		msg = s.slice(0,s.length-1);
		
		$.ajax({
			type:'get',
			url: msg,
			success: function(data) {
				if(data.indexOf("Logging") > -1) {
					//alert("new page");
					$("#init").hide();
					localStorage.setItem('id', $("#signinid").val());
					$("#display").load("load=" + $("#signinid").val());
					//window.location.href = "load=" + $("#signinid").val();
				} else {
					alert(data);
				}
			}
		});
		//$.get("lalala", function(data,status){});
	}
	
	//if(t) {
	//	$("#login").submit();
	//}
	
}

function signupFunction()
{
	var t = true;
	
	$(".s").each(function() {
		if($(this).val() == "") { //gets text value from elements
			alert("Please Fill In Every Textbox");
			t = false;
			return false; //breaks out of each loop
		}
	});
	
	if(t) {
		var msg = "signup?";
		$(".s").each(function() {
			msg+=$(this).attr('name');
			msg+="=";
			msg+=$(this).val();
			msg+="&"
		});
		var s = msg;
		msg = s.slice(0,s.length-1);
		
		$.ajax({
			type:'get',
			url: msg,
			success: function(data) {
				//server response
				if(data.indexOf('ok') > -1) {
					alert("Successfully Signed Up");
					location.reload();
				} else {
					alert(data);
				}
			}
		});
	}
	
}