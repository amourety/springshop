function search() {
    let query = document.getElementById("q").value;
    $.ajax({
        type: 'GET',
        url: '/search',
        data: {
            q: query
        }
    }).done(function (data) {
        let tableHtml = "";
        tableHtml += '<div class = "row">';
        let availableTags = [];
        for(let i = 0; i < data.length;i++){
            availableTags[i] = data[i].name;
            tableHtml +='<div class="col-md-4 col-sm-6 col-lg-4">';
            tableHtml +='<div class="item">';
            tableHtml +='<img class="img-responsive" src="' + data[i].img + '">';
            tableHtml += '<div class="item-dtls">';
            tableHtml += '<h4><a style="font-size: 14px; color: #000000; font-weight: bold;">' + data[i].name + '</a><a style="font-size: 12px; color: #adabab;"> Rating:' + data[i].rating + '</a></h4>';
            tableHtml += '<span class="price lblue" id = "special">' + data[i].price + '$</span>';

            tableHtml += '</div>';
            tableHtml += '<div class="ecom bg-black">';
            tableHtml +='<a class="btn" href="" onclick="doBuying(' + data[i].id + ',' + data[i].price + '); return false;"> Add to cart</a>';
            tableHtml +='<a class="btn" href="/products/' + data[i].id + '"> Info</a>';
            tableHtml += '</div>';
            tableHtml += '</div>';
            tableHtml += '</div>';
        }
        tableHtml += '</div>';

        $("#catalog").html(tableHtml);
        $("#q").autocomplete({
            source: availableTags
        });
    }).fail(function () {
        alert('ALL BAD')
    });


}

function getUserOrders() {
    $.ajax({
        type: 'POST',
        url: '/orders',
        data_type: 'json'
    }).done(function (data) {
        let tableHtml = "";
        tableHtml += '<h3> Orders: ' + data.length + '</h3>';
        tableHtml += "<ul class = \"list-group\">";
        for(let i = 0; i < data.length; i++) {
            tableHtml += '<li class=\"list-group-item\">';
            tableHtml += '<div class="alert alert-light" role="alert">';
            tableHtml += '<h5>' + (i + 1) + '. ' + '</h5>';
            tableHtml += '<ul class="list-group">';
            for (let j = 0; j < data[i].products.length; j ++) {
                tableHtml += '<li class="list-group-item">' + data[i].products[j].name + " " + data[i].products[j].price + " " + data[i].products[j].amount + '</li>';
            }
            tableHtml += '</ul>';
            tableHtml += '<br>';
            tableHtml += '<button type="button" class="btn btn-danger"' + ' onclick = "deleteUserOrder(' + data[i].order_id + '); return false;"> Delete </button>';
            tableHtml += "</div>";
            tableHtml += "<h3 style='color:greenyellow;'>" + data[i].track + '</h3>';
            tableHtml += "</li>"
        }


        tableHtml += "</ul>";
        if(data.length === 0) {
            $('#amountOfOrders').html("");
        } else {
            $('#amountOfOrders').html(data.length);
        }
        $("#orders").html(tableHtml);
    }).fail(function (jqXHR, exception) {
        let msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        alert(msg)
    });

}




function getData() {
    $.ajax({
        type: 'GET',
        url: '/main.json',
        data_type: 'json'
    }).done(function (data) {
        $("#dobutton").show();
        getUserOrders();
        $(document.body).on({
            "shown.bs.dropdown": function(){ this.closable = true; },
            "hide.bs.dropdown": function(){ return this.closable; },
            "click": function(e){ this.closable = !$(e.target).closest(".dropdown-menu").length; },
        },".dropdown.keepopen");

        let tableHtml = "";
        let sum = 0;
        let amount = 0;
        if (data.length === 0) {
            tableHtml += '<a class="dropdown-item" href="#">Your cart is empty</a>';
            $("#amountOfCart").html('');
            $("#dobutton").hide();
        } else {
            for (let i = 0; i < data.length; i++) {
                sum = sum + +data[i].price * +data[i].amount;
                amount = amount + +data[i].amount;
            }
            for (let i = 0; i < data.length; i++) {
                tableHtml += '<a class="dropdown-item" href="#">'
                    + data[i].name + ' '+
                    data[i].price + '$ '+
                    data[i].amount + ' '+
                    '<img src="/resources/images/delete.png" onclick="deleteProduct(' + data[i].id + '); return false;"/>';
                tableHtml += '</a>';
            }
        }
        if (data.length === 0) {
        } else {
            $("#amountOfCart").html(amount);
            $("#sum").html('Count: ' + sum + "$")
        }
        $("#cart").html(tableHtml);
        $("#cart2").html(tableHtml);
    }).fail(function (jqXHR, exception) {
        let msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        alert(msg)
    });
    $.ajax({
        type: 'GET',
        url: '/answers.json',
        data_type: 'json'
    }).done(function (data) {
        if(data.length === 0){
            $('#answersCount').html("");
        } else {
            $('#answersCount').html(data.length);
        }
    }).fail(function (jqXHR, exception) {
        let msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        alert(msg)
    });
    $("#q").keypress(function(event){
        if(event.keyCode == 13){
            event.preventDefault();

        }
    });
}