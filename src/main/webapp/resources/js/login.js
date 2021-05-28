function KinesrtUser(){
	let form = {
		username : $("#username").val(),
		email : $("#email").val(),
		ageGroup : $("#ageGroup").val(),
		age : $("#age").val(),
		school : $("#school").val()
	}
	
	$.ajax({
		url : 'saveUser' , type : 'POST', dataType : 'json',
		data : JSON.stringify(form),
		contentType: 'application/json',
		success: function(Result){
			if(Result.Result ==true){
				alert('회원가입 성공')
				location.href = 'http://localhost:8080/' 
			}
			
		}
		
		
	})
	
}