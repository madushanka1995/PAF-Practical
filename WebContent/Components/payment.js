$(document).ready(function()
{
if ($("#alertSuccess").text().trim() == "")
 {
 $("#alertSuccess").hide();
 }
 $("#alertError").hide();
});




//SAVE ============================================
$(document).on("click", "#btnSave", function(event)
{
// Clear alerts---------------------
 $("#alertSuccess").text("");
 $("#alertSuccess").hide();
 $("#alertError").text("");
 $("#alertError").hide();
// Form validation-------------------
var status = validatePaymentForm();
if (status != true)
 {
 $("#alertError").text(status);
 $("#alertError").show();
 return;
 }

var type = ($("#hidpaymentIdSave").val() == "") ? "POST" : "PUT";


$.ajax(
		{
		 url : "PaymentAPI",
		 type : type,
		 data : $("#formPayment").serialize(),
		 dataType : "text",
		 complete : function(response, status)
		 {
		 onPaymentSaveComplete(response.responseText, status);
		 }
		});

});
function onPaymentSaveComplete(response, status)
{
if (status == "success")
 {
	var resultSet = JSON.parse(response);
	if (resultSet.status.trim() == "success")
	{
		$("#alertSuccess").text("Successfully saved.");
		$("#alertSuccess").show();
		
		$("#divPaymentGrid").html(resultSet.data);
	} else if (resultSet.status.trim() == "error")
	{
		$("#alertError").text(resultSet.data);
		$("#alertError").show();
	}
 	} else if (status == "error")
 	{
 		$("#alertError").text("Error while saving.");
 		$("#alertError").show();
 	} else
 	{
 		$("#alertError").text("Unknown error while saving..");
 		$("#alertError").show();
 	}
		$("#hidpaymentIdSave").val("");
		$("#formPayment")[0].reset();
}

//UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
 $("#hidpaymentIdSave").val($(this).closest("tr").find('#hidpaymentIdSave').val());
 $("#productName").val($(this).closest("tr").find('td:eq(0)').text());
 $("#paymentDate").val($(this).closest("tr").find('td:eq(1)').text());
 $("#amount").val($(this).closest("tr").find('td:eq(2)').text());
 $("#productId").val($(this).closest("tr").find('td:eq(3)').text());
 $("#phone").val($(this).closest("tr").find('td:eq(4)').text());
 

});


$(document).on("click", ".btnRemove", function(event)
		{
		 $.ajax(
		 {
		 url : "PaymentAPI",
		 type : "DELETE",
		 data : "paymentId=" + $(this).data("itemid"),
		 dataType : "text",
		 complete : function(response, status)
		 {
		 onPaymentDeleteComplete(response.responseText, status);
		 }
		 });
		});

function onPaymentDeleteComplete(response, status)
{
if (status == "success")
 {
 var resultSet = JSON.parse(response);
 if (resultSet.status.trim() == "success")
 {
 $("#alertSuccess").text("Successfully deleted.");
 $("#alertSuccess").show();
 $("#divPaymentGrid").html(resultSet.data);
 } else if (resultSet.status.trim() == "error")
 {
 $("#alertError").text(resultSet.data);
 $("#alertError").show();
 }
 } else if (status == "error")
 {
 $("#alertError").text("Error while deleting.");
 $("#alertError").show();
 } else
 {
 $("#alertError").text("Unknown error while deleting..");
 $("#alertError").show();
 }
}

//CLIENTMODEL=========================================================================
function validatePaymentForm()
{
	
//doctor Name
if ($("#productName").val().trim() == "")
{
return "Insert product Name.";
}

var letterReg1 = /^[a-zA-Z ]+$/;
var tmpfName =  $("#productName").val().trim();
if(!tmpfName.match(letterReg1)){
	return "First Letter must have alphabet charaters only...!";
}
//specialization
if ($("#paymentDate").val().trim() == "")
{
return "Insert paymentDate.";
}

if ($("#amount").val().trim() == "")
{
return "Insert amount.";
}

if ($("#productId").val().trim() == "")
{
return "Insert productId.";
}

//phone
if ($("#phone").val().trim() == "") {
	return "Insert phone.";
}
var contactReg = /^\d{10}$/;
var tmpPhone =  $("#phone").val().trim();
if(!tmpPhone.match(contactReg)){
	return "Insert a valid Phone Number...!";
}

return true;
}


