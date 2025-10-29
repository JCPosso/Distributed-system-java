window.onload=function(){
    var mb = document.getElementById("myform");
    getData();
    mb.addEventListener("submit", (e)=>{
        e.preventDefault();
        const formData= new FormData(e.currentTarget);
        postData(formData);
        $("#myform")[0].reset();
        return false;
        });
    function getData(){
        $.ajax({
            url: "/app/getmessages",
            type: 'GET',
            success:renderList
                });
    }

    function renderList(data) {
        $("#demo").empty();
        str ="";
        num =0;
        data.map(message=>{
        num+=1
        str+="<tr>" + "<th>" + num + "</th>" +
            "<th>" + message.user + "</th>" +
             "<th>" + message.message + "</th>" +
            "<th>" + message.date + "</th> " +
            "</tr>"
        });
        document.getElementById("demo").innerHTML = str;
    }

async function postData(formData){
       const nuser = formData.get("data-name");
       const newMessage = formData.get("data-message");
                jQuery.ajax({
                        url: "/app/addmessage",
                        type: "POST",
                        data: JSON.stringify({
                                    'user' : nuser,
                                    'message': newMessage
                                    })
                    }).then( e => {
                        getData();
                    });
                return true;
            };

}